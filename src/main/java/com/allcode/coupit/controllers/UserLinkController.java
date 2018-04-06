package com.allcode.coupit.controllers;

import com.allcode.coupit.models.Product;
import com.allcode.coupit.models.UserLink;
import com.allcode.coupit.repositories.ProductRepository;
import com.allcode.coupit.repositories.UserLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user_links")
public class UserLinkController {

    @Autowired
    private UserLinkRepository uerLinkRepository;

    @GetMapping
    public Iterable<UserLink> getUserLinks(){ return uerLinkRepository.findAll(); }
}