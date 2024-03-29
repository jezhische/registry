package com.tagsoft.registry.service;



import com.tagsoft.registry.model.Customer;
import com.tagsoft.registry.model.Role;

import java.util.Set;

public interface CustomerService {

    Customer findByLogin(String login);

    Customer findById(long id);

    Customer save(Customer customer);

    Customer update(Customer customer);

    void deleteByLogin(String login);

    Set<Customer> findAllByRolesContainingOrderByRoles(Role role);

    Customer deleteRole(Customer customer, String role);
}
