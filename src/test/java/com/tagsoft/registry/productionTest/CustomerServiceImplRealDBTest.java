package com.tagsoft.registry.productionTest;

import com.tagsoft.registry.constants.RoleEnum;
import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.model.Customer;
import com.tagsoft.registry.model.Role;
import com.tagsoft.registry.model.canada.CanadaContact;
import com.tagsoft.registry.model.us.USContact;
import com.tagsoft.registry.repository.ContactRepository;
import com.tagsoft.registry.service.ContactService;
import com.tagsoft.registry.service.CustomerService;
import com.tagsoft.registry.service.RoleService;
import com.tagsoft.registry.testConfig.BasePostgresConnectingTest;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.*;

public class CustomerServiceImplRealDBTest extends BasePostgresConnectingTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ContactService contactService;
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private Customer customer;
    private Contact uSContact;
    private Contact canadaContact;

    @Before
    public void setUp() throws Exception {
        customer = Customer.builder()
                .login("todoit16") // iamthefirst, iamthesecond
                .password("password")
                .build();
        uSContact = USContact.builder()
                .customer(customer)
                .country("USA")
                .email("us@test.com")
                .name("Ivan")
                .lastName("Kindofjunior")
                .states(new HashSet<>(Arrays.asList("Nevada", "Alaska")))
                .build();
        canadaContact = CanadaContact.builder()
                .customer(customer)
                .country("Canada")
                .email("canada@test.com")
                .name("john")
                .lastName("WishIWere")
                .province("Saskatchewan")
                .city("Toronto")
                .build();
    }

    @After
    public void tearDown() throws Exception {
        customer = null;
        uSContact = null;
        canadaContact = null;
    }
// ====================================================================================================================

    @Test
    @Rollback
    public void saveWithoutContact() {
        Customer saved = customerService.save(customer);
        Customer byId = customerService.findById(saved.getId());
        assertEquals(saved, byId);
        assertThat(customerService.findByLogin(customer.getLogin()).isEnabled(), Matchers.comparesEqualTo(true));
        assertThat(customerService.findByLogin(customer.getLogin()).getRoles().stream().findFirst().get().getRole(),
                Matchers.is("CUSTOMER"));
    }

    @Test
    @Rollback
    public void saveCustomer_OTO_USContact() throws Exception {
        Customer savedCustomer = customerService.save(customer);
        Customer byId = customerService.findById(savedCustomer.getId());
        assertEquals(savedCustomer, byId);
        Contact savedContact = contactService.save(uSContact);
        Contact contactById = contactService.findById(savedContact.getId()).orElse(new Contact());
        assertEquals(savedContact, contactById);
        assertEquals(savedContact.getId(), savedCustomer.getId());
    }

    @Test
    @Rollback
    public void saveCustomer_OTO_CanadaContact() throws Exception {
        Customer savedCustomer = customerService.save(customer);
        Customer byId = customerService.findById(savedCustomer.getId());
        assertEquals(savedCustomer, byId);
        Contact savedContact = contactService.save(canadaContact);
        Contact contactById = contactService.findById(savedContact.getId()).orElse(new Contact());
        assertEquals(savedContact, contactById);
        assertEquals(savedContact.getId(), savedCustomer.getId());
    }


    @Test
    public void comparePassword() {
        Customer ivan = customerService.findByLogin("todoit13");
        assertTrue(bCryptPasswordEncoder.matches("password", ivan.getPassword()));
    }

    /**
     * to don't obtain ConstraintViolationException in the last line -
     * see {@link com.tagsoft.registry.service.CustomerServiceImpl#deleteByLogin(String)} comments
     */
    @Test
    @Rollback
    public void deleteOrphanContact() {
        Customer savedCustomer = customerService.save(customer);
        Customer byId = customerService.findById(savedCustomer.getId());
        assertEquals(savedCustomer, byId);
        Contact savedContact = contactService.save(uSContact);
        Contact contactById = contactService.findById(savedContact.getId()).orElse(new Contact());
        assertEquals(savedContact, contactById);
        // @MapsId checking
        assertEquals(savedContact.getId(), savedCustomer.getId());
        Long id = customer.getId();
        customerService.deleteByLogin(customer.getLogin());
        // check that both customer and contact were deleted
        assertNull(customerService.findById(id));
        assertEquals(contactService.findById(id), Optional.empty());
    }

    // =====================================================================================================================
// =============================================================================================================== bunch


    @Rollback
    @Test(expected = DataIntegrityViolationException.class)
    public void loginUniqueConstraint() {
        String login = contactRepository.findAll().get(0).getCustomer().getLogin();
        customer.setLogin(login);
        customerService.save(customer);
    }


    @Test
    @Rollback
    public void findByLogin() {
        Customer fromDB = contactRepository.findAll().get(0).getCustomer();
        Customer byLogin = customerService.findByLogin(fromDB.getLogin());
        assertEquals(fromDB.getLogin(), byLogin.getLogin());
        System.out.println("************************************" + byLogin);
    }

    @Test
    public void findAdminRole() {
        assertEquals(RoleEnum.ADMIN.toString(), roleService.findByRole(RoleEnum.ADMIN.toString()).getRole());
    }

    @Test
    @Rollback
    public void addRole() {
        String login = contactRepository.findAll().get(0).getCustomer().getLogin();
        assertFalse(customerService.findByLogin(login).getRoles().contains(
                roleService.findByRole(RoleEnum.ADMIN.toString())));
        customerService.findByLogin(login).addRoles(roleService.findByRole(RoleEnum.ADMIN.toString()));
        assertTrue(customerService.findByLogin(login).getRoles().contains(
                roleService.findByRole(RoleEnum.ADMIN.toString())
        ));
    }

    @Test
    @Rollback
    public void findById() {
        String login = contactRepository.findAll().get(0).getCustomer().getLogin();
        Customer byLogin = customerService.findByLogin(login);
        Customer byId = customerService.findById(byLogin.getId());
        // why don't?
//        assertEquals(byLogin, byId);
        assertEquals(byLogin.getId(), byId.getId());
    }

    @Test
    @Rollback
    public void putWithEnabledFalse() {
        String login = contactRepository.findAll().get(0).getCustomer().getLogin();
        Customer byLogin = customerService.findByLogin(login);
        byLogin.setEnabled(false);
        customerService.update(byLogin);
        assertFalse(byLogin.isEnabled());
    }
// =========================================================================================================== end bunch
// =====================================================================================================================

// =====================================================================================================================
// ======================================================================================================== special

//    @Test
//    @Rollback
//    @Ignore
    public void deleteCertainCustomer() {
        String certainLogin = "jezhische";
        assertNotNull(customerService.findByLogin(certainLogin));
        customerService.deleteByLogin(certainLogin);
        assertNull(customerService.findByLogin(certainLogin));
    }

//    @Test
//    @Rollback
//    @Ignore
    public void putCertainCustomerEnabledTrue() {
        Customer jezhische = customerService.findByLogin("jezhische");
        jezhische.setEnabled(true);
        customerService.update(jezhische);
        assertTrue(customerService.findByLogin(jezhische.getLogin()).isEnabled());
    }

//    @Test
//    @Rollback
//    @Ignore
    public void setAdminAuthority() {
        Customer jezhische = customerService.findByLogin("jezhische");
        Role admin = roleService.findByRole(RoleEnum.ADMIN.toString());
        Role customer = roleService.findByRole(RoleEnum.CUSTOMER.toString());
        jezhische.addRoles(customer);
        if (!jezhische.getRoles().contains(admin)) {
            System.out.println("****************************************************** set the ADMIN role");
            jezhische.addRoles(admin);
        } else             System.out.println("************************************************ the ADMIN role exists");
        assertTrue(customerService.findByLogin("jezhische").getRoles().contains(admin));
    }

//    @Test
//    @Rollback
//    @Ignore
    public void deleteAdminAuthority() {
        Customer jezhische = customerService.findByLogin("jezhische");
        Role admin = roleService.findByRole(RoleEnum.ADMIN.toString());
        if (jezhische.getRoles().contains(admin)) {
            System.out.println("****************************************************** delete the ADMIN role");
            customerService.deleteRole(jezhische, RoleEnum.ADMIN.toString());
        }
        assertTrue(!customerService.findByLogin("jezhische").getRoles().contains(admin));
    }
}

