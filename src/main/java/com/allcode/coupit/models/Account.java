package com.allcode.coupit.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "accounts")
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name="id")
    private Long id;

    @Column(name="public_key",columnDefinition = "TEXT")
    @NotBlank
    private String publicKey;

    @Column(name="private_key",columnDefinition = "TEXT")
    @NotBlank
    private String privateKey;

    @Column(name="address",columnDefinition = "TEXT")
    @NotBlank
    private String address;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    @NotNull
    private User user;

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getpublicKey() { return publicKey; }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() { return privateKey; }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Account(String address, String publicKey, String privateKey, User user) {
        this.address = address;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.user = user;
    }

    public Account() { }

    @Override
    public String toString() {
        return "Merchant{" +
                "id=" + id +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", user='" + user.toString() + '\'' +
                '}';
    }
}
