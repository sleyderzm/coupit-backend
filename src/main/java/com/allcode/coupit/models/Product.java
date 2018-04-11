package com.allcode.coupit.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    @NotBlank
    private String name;

    @Column(name="price")
    private double price;

    @Column(name="description",columnDefinition = "TEXT")
    @NotBlank
    private String description;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="merchant_id")
    @NotNull
    private Merchant merchant;

    @JsonBackReference
    @OneToMany(mappedBy="product",fetch = FetchType.LAZY)
    private Set<UserLink> userLinks;

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) { this.price = price; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public Merchant getMerchant() { return merchant; }

    public void setMerchant(Merchant merchant) { this.merchant = merchant; }

    public Set<UserLink> getUserLinks() {
        return userLinks;
    }

    public void setUserLinks(Set<UserLink> userLinks) {
        this.userLinks = userLinks;
    }

    public Product(String name, double price, String description, Merchant merchant) {
        this.price = price;
        this.name = name;
        this.description = description;
        this.merchant = merchant;
    }

    public Product() { }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", merchant='" + merchant.toString() + '\'' +
                '}';
    }
}
