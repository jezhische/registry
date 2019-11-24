package com.tagsoft.registry.validationTest;

import com.tagsoft.registry.model.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EqualsAndHashCodeTest {

    @Test
    void customerEqualsTest() {
        Customer build1 = Customer.builder().id(25L).build();
        Customer build2 = Customer.builder().id(25L).build();
        assertEquals(build1, build2);
    }

    @Test
    void customerHashCodeTest() {
        Customer build1 = Customer.builder().id(25L).login("u").build();
        Customer build2 = Customer.builder().id(25L).login("u").build();
        assertEquals(build1, build2);
        assertEquals(build1.hashCode(), build2.hashCode());
    }
}
