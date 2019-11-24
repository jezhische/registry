package com.tagsoft.registry.repository.canada;

import com.tagsoft.registry.model.canada.CanadaContact;
import com.tagsoft.registry.model.us.USContact;
import com.tagsoft.registry.repository.ContactBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CanadaContactRepository extends ContactBaseRepository<CanadaContact> {
    Optional<CanadaContact> findById(Long id);

    <S extends CanadaContact> S saveAndFlush(S entity);

    void deleteById(Long id);
}
