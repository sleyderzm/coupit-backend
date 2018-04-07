package com.allcode.coupit.controllers;

import com.allcode.coupit.models.Product;
import com.allcode.coupit.models.User;
import com.allcode.coupit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Iterable<User> getUsers(){ return userRepository.findAll(); }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) {
        User savedUser = userRepository.save(user) ;
        if(savedUser.getId().equals(null))
        {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
        }
    }
}