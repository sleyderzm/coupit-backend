package com.allcode.coupit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import com.allcode.coupit.repositories.UserRepository;

@Controller
@RequestMapping(path="/main")
@CrossOrigin(origins = "*")
public class MainController {
    @Autowired
    private UserRepository userRepository;
}
