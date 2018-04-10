package com.allcode.coupit.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;
import javax.persistence.FetchType;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name="id")
    private Long id;

    @Column(name="first_name")
    @NotBlank
    private String firstName;

    @Column(name="last_name")
    @NotBlank
    private String lastName;

    @Column(name="email")
    @NotBlank
    @Email
    private String email;

    @JsonIgnore
    @Column(name="password")
    @NotBlank
    private String password;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="role_id")
    @NotNull
    private Role role;

    @JsonBackReference
    @OneToMany(mappedBy="user",fetch = FetchType.LAZY)
    private Set<Merchant> merchants;

    @JsonBackReference
    @OneToMany(mappedBy="user",fetch = FetchType.LAZY)
    private Set<UserLink> userLinks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public Set<Merchant> getMerchants() {
        return merchants;
    }

    public void setMerchants(Set<Merchant> merchants) {
        this.merchants = merchants;
    }

    public Set<UserLink> getUserLinks() {
        return userLinks;
    }

    public void setUserLinks(Set<UserLink> userLinks) {
        this.userLinks = userLinks;
    }

    public Collection<Role> getRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return roles;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public User(String firstName, String lastName, String email, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() { }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role.toString() + '\'' +
                '}';
    }

    @JsonBackReference
    public boolean isAdminRole() {
        return (this.role != null) && (this.role.getId() == Role.ADMIN_ID);
    }

    @JsonBackReference
    public boolean isUserRole() {
        return (this.role != null) && (this.role.getId() == Role.USER_ID);
    }
}
