package com.tagsoft.registry.repository.us;

import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.model.us.USContact;
import com.tagsoft.registry.repository.ContactBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface USContactRepository extends ContactBaseRepository<USContact> {
    Optional<USContact> findById(Long id);

    <S extends USContact> S saveAndFlush(S entity);

    void deleteById(Long id);
}
