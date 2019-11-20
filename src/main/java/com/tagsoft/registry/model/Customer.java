package com.tagsoft.registry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents customer as "user" with the required attributes: login, password and enabled.
 * <p>All the customer details are contained in the {@link Contact} class, that relies with {@code Customer}
 * by the common id, i.e. includes @interface {@link MapsId} Customer customer (in this case it means that @MapsId
 * provides the mapping for a simple primary key of the parent entity Customer)</p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers",
uniqueConstraints = @UniqueConstraint(columnNames = "login"))
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "login")
    @NotEmpty(message = "*Please provide your login")
    private String login;

    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;

    @Column
    private boolean enabled;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY) // LAZY by default
    @JoinTable(name = "customer_role",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    // contact id will be the same as customer id, so it doesn't need to add "contact" column

    public void setRoles(Role... roles) {
        this.roles  = new HashSet<>();
        this.roles.addAll(Arrays.asList(roles));
    }

    public void addRoles(Role... roles) {
        if (this.roles == null) this.roles = new HashSet<>();
        // check "if (!this.roles.contains(r))" is unnecessary because of Set features
        this.roles.addAll(Arrays.asList(roles));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        return this.id == null ? ((Customer) o).id == null : this.id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

}
