package com.allcode.coupit.models;

import com.allcode.coupit.handlers.Utils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name="id")
    private Long id;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_link_id")
    private UserLink userLink;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="product_id")
    private Product product;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="merchant_id")
    private Merchant merchant;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="currency_id")
    private Currency currency;

    @Column(name="product_name")
    private String productName;

    @Column(name="product_price")
    private Long productPriceLong;

    @Column(name="product_description", columnDefinition="TEXT")
    private String productDescription;

    @Column(name="amount")
    private Integer amount;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name="created_at", columnDefinition = "timestamptz")
    private Date createdAt;

    @JsonBackReference
    @OneToMany(mappedBy="purchase",fetch = FetchType.LAZY)
    private Set<Transaction> transactions;

    @Transient
    private Double productPrice;


    public Purchase() {
    }

    public Purchase(UserLink userLink, User user, Product product, Merchant merchant, Currency currency, String productName, Double productPrice, String productDescription, Integer amount) {
        this.userLink = userLink;
        this.user = user;
        this.product = product;
        this.merchant = merchant;
        this.currency = currency;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.amount = amount;
        this.createdAt = new Date();
        this.productPriceLong = Utils.priceToLong(this.productPrice, this.currency);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public UserLink getUserLink() {
        return userLink;
    }

    public void setUserLink(UserLink userLink) {
        this.userLink = userLink;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        if(this.productPrice == null){
            if(this.productPriceLong == null) return null;
            this.productPrice = Utils.priceToDouble(this.productPriceLong, this.currency);
        }
        return productPrice;
    }

    public void setProductPrice(Double price) {
        this.productPrice = price;
        this.productPriceLong = Utils.priceToLong(this.productPrice, this.currency);
    }

    public Long getPriceLong() {
        return productPriceLong;
    }

    public void setPriceLong(Long priceLong) {
        this.productPriceLong = priceLong;
        this.productPrice = Utils.priceToDouble(this.productPriceLong, this.currency);
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productDescription='" + productDescription + '\'' +
                ", createdAt=" + createdAt.toString() +
                '}';
    }
}
