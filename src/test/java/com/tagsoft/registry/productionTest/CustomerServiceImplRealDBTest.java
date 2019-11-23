package com.tagsoft.registry.productionTest;

import com.tagsoft.registry.constants.RoleEnum;
import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.model.customer.Customer;
import com.tagsoft.registry.model.customer.Role;
import com.tagsoft.registry.service.ContactService;
import com.tagsoft.registry.service.customer.CustomerService;
import com.tagsoft.registry.service.customer.RoleService;
import com.tagsoft.registry.testConfig.BasePostgresConnectingTest;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

public class CustomerServiceImplRealDBTest extends BasePostgresConnectingTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ContactService contactService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private Customer customer;
    private Contact contact;

    @Before
    public void setUp() throws Exception {
        customer = Customer.builder()
                .login("todoit12") // iamthefirst, iamthesecond
                .password("password")
                .country("USA")
                .build();
        contact = Contact.builder()
                .customer(customer)
                .email("example@test.com")
                .name("ivan")
                .lastName("Kindofjunior")
                .states(new ArrayList<>(Arrays.asList("Nevada", "Alaska")))
                .build();
    }

    @After
    public void tearDown() throws Exception {
        customer = null;
        contact = null;
    }
// ====================================================================================================================

    @Test
//    @Rollback
    public void save() {
        Customer saved = customerService.save(customer);
        Customer byId = customerService.findById(saved.getId());
        assertEquals(saved, byId);
        assertThat(customerService.findByLogin(customer.getLogin()).isEnabled(), Matchers.comparesEqualTo(true));
        assertThat(customerService.findByLogin(customer.getLogin()).getRoles().stream().findFirst().get().getRole(),
                Matchers.is("CUSTOMER"));
    }

    @Test
    @Rollback
    public void saveCustomerOTOContact() throws Exception {
        Customer savedCustomer = customerService.save(customer);
        Customer byId = customerService.findById(savedCustomer.getId());
        assertEquals(savedCustomer, byId);
        Contact savedContact = contactService.save(contact);
        Contact contactById = contactService.findById(savedContact.getId()).orElse(new Contact());
        assertEquals(savedContact, contactById);
        assertEquals(savedContact.getId(), savedCustomer.getId());
    }

    @Test
    @Rollback
    public void deleteOrphanContact() {
        Customer savedCustomer = customerService.save(customer);
        Customer byId = customerService.findById(savedCustomer.getId());
        assertEquals(savedCustomer, byId);
        Contact savedContact = contactService.save(contact);
        Contact contactById = contactService.findById(savedContact.getId()).orElse(new Contact());
        assertEquals(savedContact, contactById);
        assertEquals(savedContact.getId(), savedCustomer.getId());
        Long id = customer.getId();
        customerService.deleteByLogin(customer.getLogin());
        assertNull(customerService.findById(id));
        assertEquals(contactService.findById(id), Optional.empty());
    }

    // =====================================================================================================================
// =============================================================================================================== bunch


    @Rollback
    @Test(expected = DataIntegrityViolationException.class)
    public void loginUniqueConstraint() {
        customerService.save(customer);
    }

    @Test
    @Rollback
    public void addRole() {
        customerService.findByLogin(customer.getLogin()).addRoles(roleService.findByRole(RoleEnum.CUSTOMER.toString()));
        assertTrue(customerService.findByLogin(customer.getLogin()).getRoles().contains(
                roleService.findByRole(RoleEnum.CUSTOMER.toString())
        ));
    }

    @Test
    @Rollback
    public void findByLogin() {
        Customer byLogin = customerService.findByLogin(customer.getLogin());
        assertEquals(customer.getLogin(), byLogin.getLogin());
        System.out.println("************************************" + byLogin);
    }

    @Test
    @Rollback
    public void findById() {
        Customer byLogin = customerService.findByLogin(customer.getLogin());
        Customer byId = customerService.findById(byLogin.getId());
        assertEquals(customer.getLogin(), byId.getLogin());
    }

    @Test
    @Rollback
    public void putWithEnabledFalse() {
        Customer byLogin = customerService.findByLogin(customer.getLogin());
        byLogin.setEnabled(false);
        customerService.update(byLogin);
        assertFalse(byLogin.isEnabled());
    }
// =========================================================================================================== end bunch
// =====================================================================================================================



    @Test
    @Rollback
    public void deleteByLogin() {
        assertNotNull(customerService.findByLogin(customer.getLogin()));
        Set<Customer> customers = customerService.findAllByRolesContainingOrderByRoles(roleService.findByRole(RoleEnum.CUSTOMER.toString()));
        assertTrue(customers.contains(customerService.findByLogin(customer.getLogin())));
        customerService.deleteByLogin(customer.getLogin());
        assertNull(customerService.findByLogin(customer.getLogin()));
        customers = customerService.findAllByRolesContainingOrderByRoles(roleService.findByRole(RoleEnum.CUSTOMER.toString()));
        customers.addAll(customerService.findAllByRolesContainingOrderByRoles(roleService.findByRole(RoleEnum.ADMIN.toString())));
        assertTrue(!customers.contains(customerService.findByLogin(customer.getLogin())));
    }

// =====================================================================================================================
// ======================================================================================================== special

    @Test
    @Rollback
    public void deleteCertainCustomer() {
        String certainLogin = "jezhische";
        assertNotNull(customerService.findByLogin(certainLogin));
        customerService.deleteByLogin(certainLogin);
        assertNull(customerService.findByLogin(certainLogin));
    }

    @Test
    @Rollback
    public void putCertainCustomerEnabledTrue() {
        Customer jezhische = customerService.findByLogin("jezhische");
        jezhische.setEnabled(true);
        customerService.update(jezhische);
        assertTrue(customerService.findByLogin(jezhische.getLogin()).isEnabled());
    }

    @Test
    public void comparePassword() {
        Customer jezhische = customerService.findByLogin("jezhische");
        System.out.println("**********************************************" +
                bCryptPasswordEncoder.matches("password", jezhische.getPassword()));
    }

    @Test
    @Rollback
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

    @Test
    @Rollback
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

