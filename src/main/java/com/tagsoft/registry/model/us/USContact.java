package com.tagsoft.registry.model.us;

import com.tagsoft.registry.model.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * subclass {@code USContact} table Primary Key is also a Foreign Key to the base class {@link Contact} Primary Key
 */
@EqualsAndHashCode(callSuper = true)
@Data
//@Builder
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "us_contacts")
@DiscriminatorValue("US")
public class USContact extends Contact {

    /**
     * NB: the {@link ElementCollection} annotation creates new table 'contact_states' in db by Hibernate
     */
    @ElementCollection
//    @NotEmpty
//    @Size(min = 3)
    private Set<String> states;
}
