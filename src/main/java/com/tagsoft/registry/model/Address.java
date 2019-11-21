package com.tagsoft.registry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name = "addresses")
public class Address {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id")
    private Long id;

//    @Column(name = "city")
    private String city;

//    @Column(name = "street")
    private String street;

//    @Column(name = "house")
    private int house;

//    @Column(name = "apartment")
    private int apartment;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "contact_id")
    private Contact contact;
}
