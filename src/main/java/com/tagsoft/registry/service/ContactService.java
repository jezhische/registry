package com.tagsoft.registry.service;

import com.tagsoft.registry.model.Contact;

import java.util.Optional;

public interface ContactService {
    Optional<Contact> findById(Long id);
    void deleteById(Long id);
    Contact save(Contact contact);
    Contact update(Contact contact);
}
