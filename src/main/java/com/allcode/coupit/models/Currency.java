package com.allcode.coupit.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    @NotBlank
    private String name;

    @Column(name="decimals")
    @NotNull
    private Integer decimals;

    @Column(name="contract_address")
    @NotBlank
    private String contractAddress;

    @Column(name="hash")
    @NotBlank
    private String hash;

    @Column(name="symbol")
    @NotBlank
    private String symbol;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="blockchain_id")
    @NotNull
    private Blockchain blockchain;

    @JsonBackReference
    @OneToMany(mappedBy="currency",fetch = FetchType.LAZY)
    private Set<Product> products;

    @JsonBackReference
    @OneToMany(mappedBy="currency",fetch = FetchType.LAZY)
    private Set<Purchase> purchases;

    public Currency(@NotBlank String name, @NotNull Integer decimals, @NotBlank String contractAddress, @NotBlank String hash, @NotBlank String symbol, @NotNull Blockchain blockchain) {
        this.name = name;
        this.decimals = decimals;
        this.contractAddress = contractAddress;
        this.hash = hash;
        this.symbol = symbol;
        this.blockchain = blockchain;
    }

    public Currency() {
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

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", decimals=" + decimals +
                ", contractAddress='" + contractAddress + '\'' +
                ", hash='" + hash + '\'' +
                ", symbol='" + symbol + '\'' +
                ", blockchain=" + blockchain.getName() +
                '}';
    }
}
