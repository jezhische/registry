package com.tagsoft.registry.model;

import com.tagsoft.registry.model.canada.CanadaContact;
import com.tagsoft.registry.model.us.USContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;


/**
 * Customer details for {@link Customer}
 * <p>See <a href="https://vladmihalcea.com/the-best-way-to-use-entity-inheritance-with-jpa-and-hibernate/">
 *     vladmihalcea: The best way to use entity inheritance with JPA and Hibernate</a></p>
 *     <p>See <a href="https://habr.com/ru/post/337488/">Наследование в Hibernate: выбор стратегии </a></p>
 *  {@link InheritanceType#JOINED} means that subclasses {@link USContact} and {@link CanadaContact} tables Primary Key
 *  is also a Foreign Key to the base class {@code Contact} Primary Key. With this strategy type a table is created
 *  for each class in the hierarchy to store only the local attributes of that class.
 *  <br>
 *  But here I use more simple strategy {@link InheritanceType#SINGLE_TABLE}, that means one table
 *  for whole classes hierarchy. For every single entry exists additional column with delimiter (discriminator) - that is
 *  special value to differ the returned java-type.
 *  <p>Simple example: <a href=https://blog.netgloo.com/2014/12/18/handling-entities-inheritance-with-spring-data-jpa/>
 *  HANDLING ENTITIES INHERITANCE WITH SPRING DATA JPA</a></p>
 *  @see MappedSuperclass @MappedSuperclass annotation to create only us_contacts and canada_contacts tables without
 *  contacts table.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contacts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // by default
@DiscriminatorColumn(name = "contact_type")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    // @MapsId annotation points out, that the id column serves as both Primary Key and FK
    @MapsId // simply it means that contact.id == customer.id
    private Customer customer;

    @Column(name = "name")
    String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column
    @NotEmpty(message = "*Please select your country")
    private String country;
}
