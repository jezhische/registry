package com.tagsoft.registry.inmemorydbTest;

import com.tagsoft.registry.constants.RoleEnum;
import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.model.Customer;
import com.tagsoft.registry.model.Role;
import com.tagsoft.registry.model.canada.CanadaContact;
import com.tagsoft.registry.model.us.USContact;
import com.tagsoft.registry.service.ContactService;
import com.tagsoft.registry.service.CustomerService;
import com.tagsoft.registry.service.RoleService;
import com.tagsoft.registry.testConfig.BaseH2ConnectingTest;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class CustomerServiceTest extends BaseH2ConnectingTest {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ContactService contactService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private Customer customer;
    private Contact uSContact;
    private Contact canadaContact;

    @Before
    public void setUp() throws Exception {
        customer = Customer.builder()
                .login("tt2") // iamthefirst
                .password("password")
                .build();
        uSContact = USContact.builder()
                .customer(customer)
                .country("USA")
                .email("us@test.com")
                .name("ivan")
                .lastName("Kindofjunior")
                .states(new HashSet<>(new ArrayList<>(Arrays.asList("Nevada", "Alaska"))))
                .build();
        canadaContact = CanadaContact.builder()
                .customer(customer)
                .country("Canada")
                .email("canada@test.com")
                .name("John")
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
    public void saveWithoutContact() {
        roleService.save(Role.builder().role(RoleEnum.CUSTOMER.toString()).build());
        Customer saved = customerService.save(customer);
        Customer byId = customerService.findById(saved.getId());
        assertEquals(saved, byId);
        assertThat(customerService.findByLogin(customer.getLogin()).isEnabled(), Matchers.comparesEqualTo(true));
        assertThat(customerService.findByLogin(customer.getLogin()).getRoles().stream().findFirst().get().getRole(),
                Matchers.is("CUSTOMER"));
    }

}
