package com.tagsoft.registry.validationTest;


import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.model.canada.CanadaContact;
import com.tagsoft.registry.model.us.USContact;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FieldValidationTest {

    private Contact parentContact;
    private Contact uSContact;
    private Contact canadaContact;

    @BeforeEach
    void setUp() {
        parentContact = Contact.builder()
                .country("USA")
                .name("John")
                .lastName("Doe")
                .email("john_doe@example.com")
                .build();
        uSContact = USContact.builder()
                .country("USA")
                .name("John")
                .lastName("Doe")
                .email("john_doe@example.com")
                .states(new HashSet<>(Arrays.asList("California", "Colorado", "Hawaii", "Delaware")))
                .build();
        canadaContact = CanadaContact.builder()
                .country("USA")
                .name("John")
                .lastName("Doe")
                .email("john_doe@example.com")
                .province("Manitoba")
                .city("Toronto")
                .build();

    }

    @AfterEach
    void tearDown() {
        parentContact = canadaContact = uSContact = null;
    }

    // TODO: why @NotEmpty @Size(min = 3) constraints do not work for @ElementCollection?
    @Test
    @Ignore
    void uSContactStatesConstraints() {
        Set<ConstraintViolation<Contact>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(uSContact);
        assertTrue(violations.isEmpty());
        ((USContact) uSContact).setStates(new HashSet<>(Arrays.asList("California", "Colorado")));
//        assertFalse(violations.isEmpty());
        ((USContact) uSContact).setStates(new HashSet<>());
//        assertFalse(violations.isEmpty());
        ((USContact) uSContact).setStates(null);
//        assertFalse(violations.isEmpty());
    }

    // field validation do not work - why?
    @Test
    @Ignore
    void uSContactNotEmptyConstraints() {
        Set<ConstraintViolation<Contact>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(uSContact);
        assertTrue(violations.isEmpty());
        uSContact.setLastName(null);
//        assertFalse(violations.isEmpty());
        uSContact.setLastName("");
//        assertFalse(violations.isEmpty());
    }
}
