package com.tagsoft.registry.service;

import com.tagsoft.registry.model.Role;
import com.tagsoft.registry.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.saveAndFlush(role);
    }

    /**
     * The method takes String parameter, not {@link Role}, as the {@link RoleRepository#findByRole(String)} method
     * expects String as default JpaRepository method
     * @param role
     * @return
     */
    @Override
    public Role findByRole(String role) {
        return roleRepository.findByRole(role);
    }
}
