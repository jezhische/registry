package com.tagsoft.registry.service.canada;

import com.tagsoft.registry.model.canada.CanadaContact;
import com.tagsoft.registry.repository.canada.CanadaContactRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service("canadaContactService")
@Transactional
public class CanadaContactServiceImpl implements CanadaContactService {
    private CanadaContactRepository repository;

    public CanadaContactServiceImpl(CanadaContactRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<CanadaContact> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public CanadaContact save(CanadaContact contact) {
        return repository.saveAndFlush(contact);
    }

    @Override
    public CanadaContact update(CanadaContact contact) {
        return repository.saveAndFlush(contact);
    }
}
