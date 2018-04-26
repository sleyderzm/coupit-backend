package com.allcode.coupit.models;

import com.allcode.coupit.handlers.Utils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @Column(name="price")
    private Long priceLong;

    @Column(name="description",columnDefinition = "TEXT")
    @NotBlank
    private String description;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="merchant_id")
    @NotNull
    private Merchant merchant;

    @Transient
    private Double price;


    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="currency_id")
    private Currency currency;

    @JsonBackReference
    @OneToMany(mappedBy="product",fetch = FetchType.LAZY)
    private Set<UserLink> userLinks;

    @JsonBackReference
    @OneToMany(mappedBy="product",fetch = FetchType.LAZY)
    private Set<Purchase> purchases;

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Double getPrice() {
        if(this.price == null){
            if(this.priceLong == null) return null;
            this.price = Utils.priceToDouble(this.priceLong, this.currency);
        }
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
        this.priceLong = Utils.priceToLong(this.price, this.currency);
    }

    public Long getPriceLong() {
        return priceLong;
    }

    public void setPriceLong(Long priceLong) {
        this.priceLong = priceLong;
        this.price = Utils.priceToDouble(this.priceLong, this.currency);
    }

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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Product(String name, Double price, String description, Merchant merchant, Currency currency) {
        this.price = price;
        this.name = name;
        this.description = description;
        this.merchant = merchant;
        this.currency = currency;
        this.priceLong = Utils.priceToLong(this.price, this.currency);
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

    public Boolean hasPermission(User user){
        return this.getMerchant().hasPermission(user);
    }
}
