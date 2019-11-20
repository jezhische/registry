package com.tagsoft.registry.repository;

import com.tagsoft.registry.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdderssRepository extends JpaRepository<Address, Long> {
}
