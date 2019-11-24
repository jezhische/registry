package com.tagsoft.registry.service.us;

import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.model.us.USContact;

import java.util.Optional;

public interface USContactService {
    Optional<USContact> findById(Long id);
    void deleteById(Long id);
    Contact save(USContact contact);
    Contact update(USContact contact);
}
