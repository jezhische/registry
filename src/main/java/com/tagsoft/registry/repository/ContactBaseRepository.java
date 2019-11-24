package com.tagsoft.registry.repository;

import com.tagsoft.registry.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ContactBaseRepository<T extends Contact> extends JpaRepository<T, Long> {

// I can use here (optional) Spring Expression Language (SpEL). Then the value of #{#entityName} will be the entity type T
    @Query("select c from #{#entityName} as c where c.id = ?1 ")
    Optional<T> findById(Long id);

    <S extends T> S saveAndFlush(S entity);

    void deleteById(Long id);
}
