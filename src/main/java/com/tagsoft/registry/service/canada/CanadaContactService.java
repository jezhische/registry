package com.tagsoft.registry.service.canada;

import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.model.canada.CanadaContact;

import java.util.Optional;

public interface CanadaContactService {
    Optional<CanadaContact> findById(Long id);
    void deleteById(Long id);
    CanadaContact save(CanadaContact contact);
    CanadaContact update(CanadaContact contact);
}
