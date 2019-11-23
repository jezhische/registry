package com.tagsoft.registry.repository.customer;

import com.tagsoft.registry.model.customer.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
    Role findById(long id);
}
