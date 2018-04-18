package com.allcode.coupit.models;

import com.allcode.coupit.handlers.Utils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name="id")
    private Long id;

    @Column(name="value")
    @NotNull
    private Long valueLong;

    @Transient
    private Double value;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="currency_id")
    private Currency currency;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="purchase_id")
    private Purchase purchase;

    public Transaction() {
    }

    public Transaction(Double value, Currency currency, User user, Purchase purchase) {
        this.value = value;
        this.currency = currency;
        this.user = user;
        this.purchase = purchase;
        this.valueLong = Utils.priceToLong(this.value, this.currency);
    }

    public Double getValue() {
        if(this.value == null){
            if(this.valueLong == null) return null;
            this.value = Utils.priceToDouble(this.valueLong, this.currency);
        }
        return value;
    }

    public void setValue(Double price) {
        this.value = price;
        this.valueLong = Utils.priceToLong(this.value, this.currency);
    }

    public Long getPriceLong() {
        return valueLong;
    }

    public void setPriceLong(Long priceLong) {
        this.valueLong = priceLong;
        this.value = Utils.priceToDouble(this.valueLong, this.currency);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
