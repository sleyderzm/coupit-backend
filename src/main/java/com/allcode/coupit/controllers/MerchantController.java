package com.allcode.coupit.controllers;

import com.allcode.coupit.models.Merchant;
import com.allcode.coupit.repositories.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

    @Autowired
    private MerchantRepository merchantRepository;

    @GetMapping
    public Iterable<Merchant> getMerchants(){ return merchantRepository.findAll(); }
}