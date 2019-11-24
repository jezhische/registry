package com.tagsoft.registry.service;

import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service("contactService")
@Transactional
public class ContactServiceImpl implements ContactService {

    private ContactRepository repository;

    @Autowired
    public ContactServiceImpl(ContactRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Contact> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
}

    @Override
    public Contact save(Contact contact) {
        return repository.saveAndFlush(contact);
    }

    @Override
    public Contact update(Contact contact) {
        return repository.saveAndFlush(contact);
    }
}
