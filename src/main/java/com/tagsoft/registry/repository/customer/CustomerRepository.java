package com.tagsoft.registry.repository.customer;

import com.tagsoft.registry.model.customer.Customer;
import com.tagsoft.registry.model.customer.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByLogin(@NotEmpty(message = "*Please provide your login") String login);

    Customer findById(long id);

    void deleteByLogin(String login);


//    @Query(value = "select c.id, c.enabled, c.login, c.password, r.role from customers c " +
//            "inner join customer_role cr on c.id = cr.customer_id " +
//            "inner join roles r on cr.role_id = r.id where r.role = ?1")
//    Optional<Customer> findByRole(String role);

    // Set<Customer> for distinct values to avoid repetitions
    Set<Customer> findAllByRolesContainingOrderByRoles(Role role);
}
