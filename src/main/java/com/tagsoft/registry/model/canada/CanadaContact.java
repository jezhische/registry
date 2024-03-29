package com.tagsoft.registry.model.canada;

import com.tagsoft.registry.model.Contact;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * subclass {@code CanadaContact} table Primary Key is also a Foreign Key to the base class {@link Contact} Primary Key
 */
@EqualsAndHashCode(callSuper = true)
@Data
//@Builder
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "canada_contacts")
@DiscriminatorValue("CA")
public class CanadaContact extends Contact {

    @Column(name = "province")
    @NotEmpty
    private String province;

    @Column(name = "city")
    @NotEmpty
    private String city;
}
