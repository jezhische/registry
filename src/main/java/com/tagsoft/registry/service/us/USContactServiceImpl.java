package com.tagsoft.registry.service.us;

import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.model.us.USContact;
import com.tagsoft.registry.repository.us.USContactRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service("uSContactService")
@Transactional
public class USContactServiceImpl implements USContactService {
    private USContactRepository repository;

    public USContactServiceImpl(USContactRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<USContact> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Contact save(USContact contact) {
        return repository.saveAndFlush(contact);
    }

    @Override
    public Contact update(USContact contact) {
        return repository.saveAndFlush(contact);
    }
}
