package com.allcode.coupit.Coupit.controller;

import java.util.Optional;
import java.util.UUID;
import com.allcode.coupit.Coupit.handler.ConstraintViolationExceptionHandler;
import com.allcode.coupit.Coupit.handler.ErrorResponse;
import com.allcode.coupit.Coupit.model.Role;
import com.allcode.coupit.Coupit.model.Session;
import com.allcode.coupit.Coupit.model.User;
import com.allcode.coupit.Coupit.repository.RoleRepository;
import com.allcode.coupit.Coupit.repository.SessionRepository;
import com.allcode.coupit.Coupit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolationException;

@RestController
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SessionRepository sessionRepository;

    //-------------------Retrieve All Users--------------------------------------------------------

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
        String digestPassword = User.getDigestPassword(password);
        User user = userRepository.findByEmailAndPassword(email, digestPassword);
        if(user == null){
            ErrorResponse error = new ErrorResponse("Authentication Error: Email Or Password incorrect");
            return new ResponseEntity<ErrorResponse>(error,HttpStatus.UNAUTHORIZED);//You many decide to return HttpStatus.NOT_FOUND
        }
        String token = UUID.randomUUID().toString();
        Session session = new Session(user, token);
        try{
            sessionRepository.save(session);
        }catch (ConstraintViolationException ex){
            return ConstraintViolationExceptionHandler.getResponse(ex);
        }

        return new ResponseEntity<Session>(session, HttpStatus.OK);
    }

    @RequestMapping(value = "/invalid_token", method = RequestMethod.GET)
    public ResponseEntity<?> invalid_token() {
        ErrorResponse error = new ErrorResponse("Invalid Token");
        return new ResponseEntity<ErrorResponse>(error,HttpStatus.BAD_REQUEST);//You many decide to return HttpStatus.NOT_FOUND
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String validatePassword,
            @RequestParam String clientName
    ) {

        if(!validatePassword.equals(password)){
            ErrorResponse error = new ErrorResponse("the password and validation do not match");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
        }


        User repeatUser = userRepository.findByEmail(email);
        if (repeatUser != null) {
            ErrorResponse error = new ErrorResponse("the user " + email + " already exist");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.CONFLICT);
        }

        Optional<Role> role = roleRepository.findById(Role.CLIENT_ID);

        String digestPassword = User.getDigestPassword(password);
        User user = new User(firstName, lastName, email, digestPassword, role.get());

        try{
            userRepository.save(user);
        }catch (ConstraintViolationException ex){
            return ConstraintViolationExceptionHandler.getResponse(ex);
        }

        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

}



