package com.allcode.coupit.controllers;

import com.allcode.coupit.models.Product;
import com.allcode.coupit.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public Iterable<Product> getProduct(){ return productRepository.findAll(); }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product) ;
        if(savedProduct.getId().equals(null))
        {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<Product>(savedProduct, HttpStatus.CREATED);
        }
    }
}