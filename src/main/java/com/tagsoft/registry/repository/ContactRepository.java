package com.tagsoft.registry.repository;

import com.tagsoft.registry.model.Contact;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends ContactBaseRepository<Contact> {
    Optional<Contact> findById(Long id);

    <S extends Contact> S saveAndFlush(S entity);

    void deleteById(Long id);
}
