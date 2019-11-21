package com.tagsoft.registry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


/**
 * Customer details for {@link Customer}
 * <p>NB: the following relations exists but didn't be reflected in this entity to avoid the massive data loading:</p>
 * <p>OneToMany {@link }Address</p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contacts")
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

    /**
     * NB: the {@link @ElementCollection} annotation creates new table 'contact_states' in db by Hibernate
     */
    @ElementCollection
    private List<String> states;
}
