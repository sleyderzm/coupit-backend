package com.allcode.coupit.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotBlank;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements Serializable{

    public static final long ADMIN_ID = 1;
    public static final long CLIENT_ID = 2;
    public static final long SUBSCRIBER_ID = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    @NotBlank
    private String name;

    @JsonBackReference
    @OneToMany(mappedBy="role",fetch = FetchType.LAZY)
    private Set<User> users;

    public Role(String name) {
        this.name = name;
    }

    public Role() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

