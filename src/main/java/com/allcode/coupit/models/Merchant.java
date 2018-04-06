package com.allcode.coupit.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "merchants")
public class Merchant implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name="id")
    private Long id;

    @Column(name="merchant_type")
    @NotBlank
    private String merchantType;

    @Column(name="name")
    @NotBlank
    private String name;

    @Column(name="website_url")
    @NotBlank
    private String websiteUrl;

    @JsonManagedReference
    @ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name="user_id")
    @NotNull
    private User user;

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantType() { return merchantType; }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String setName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Merchant(String merchantType, String name, String websiteUrl, User user) {
        this.merchantType = merchantType;
        this.name = name;
        this.websiteUrl = websiteUrl;
        this.user = user;
    }

    public Merchant() { }

    @Override
    public String toString() {
        return "Merchant{" +
                "id=" + id +
                ", merchantType='" + merchantType + '\'' +
                ", name='" + name + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", user='" + user.toString() + '\'' +
                '}';
    }
}
