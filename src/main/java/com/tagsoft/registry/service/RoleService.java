package com.tagsoft.registry.service;


import com.tagsoft.registry.model.Role;
import com.tagsoft.registry.repository.RoleRepository;

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
