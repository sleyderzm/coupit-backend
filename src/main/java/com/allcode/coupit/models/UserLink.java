package com.allcode.coupit.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "user_links")
public class UserLink implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name="id")
    private Long id;

    @Column(name="uid")
    @NotBlank
    private String uid;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="product_id")
    @NotNull
    private Product product;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    @NotNull
    private User user;

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }

    public void setProduct(Product user) { this.product = product; }

    public UserLink(String uid, User user, Product product) {
        this.uid = uid;
        this.user = user;
        this.product = product;
    }

    public UserLink() { }

    @Override
    public String toString() {
        return "UserLink{" +
                "id=" + id +
                ", product='" + product + '\'' +
                ", product='" + user.toString() + '\'' +
                ", user='" + user.toString() + '\'' +
                '}';
    }
}
