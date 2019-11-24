package com.tagsoft.registry.productionTest.repository;

import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.model.Customer;
import com.tagsoft.registry.model.canada.CanadaContact;
import com.tagsoft.registry.model.us.USContact;
import com.tagsoft.registry.repository.ContactRepository;
import com.tagsoft.registry.repository.CustomerRepository;
import com.tagsoft.registry.service.CustomerService;
import com.tagsoft.registry.testConfig.BasePostgresConnectingTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class ContactRepositoryTest extends BasePostgresConnectingTest {
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    CustomerRepository customerRepository;
    private Contact uSContact;
    private Contact canadaContact;
    private Customer fromDB;

    @Before
    public void setUp() throws Exception {
//        customer = Customer.builder()
//                .login("todoit15") // iamthefirst, iamthesecond
//                .password("password")
//                .build();
        uSContact = USContact.builder()
//                .customer(customer)
                .country("USA")
                .email("us@test.com")
                .name("Ivan")
                .lastName("Kindofjunior")
                .states(new HashSet<>(new ArrayList<>(Arrays.asList("Nevada", "Alaska"))))
                .build();
        canadaContact = CanadaContact.builder()
//                .customer(customer)
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
//        customer = null;
        uSContact = null;
        canadaContact = null;
    }

    @Test
    @Rollback
    public void delete() {
        fromDB = customerRepository.findAll().get(0);
        Contact savedContact = contactRepository.findById(fromDB.getId()).get();
        assertNotNull(savedContact);
        contactRepository.delete(savedContact);
        fromDB = customerRepository.findAll().get(1);
        savedContact = contactRepository.findById(fromDB.getId()).get();
        assertNotNull(savedContact);
        contactRepository.delete(savedContact);
    }

}
