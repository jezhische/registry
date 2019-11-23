package com.tagsoft.registry.service.customer;


import com.tagsoft.registry.model.customer.Role;
import com.tagsoft.registry.repository.customer.RoleRepository;
public interface RoleService {

    Role save(Role role);

    /**
     * The method takes String parameter, not {@link Role}, as the {@link RoleRepository#findByRole(String)} method
     * expects String as default JpaRepository method
     * @param role
     * @return
     */
    Role findByRole(String role);
}
