package com.allcode.coupit.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "blockchains")
public class Blockchain {

    public static final String NEO = "NEO";

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    @NotBlank
    private String name;

    @JsonBackReference
    @OneToMany(mappedBy="blockchain",fetch = FetchType.LAZY)
    private Set<Currency> currencies;

    public Blockchain() {
    }

    public Blockchain(@NotBlank String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Blockchain{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
