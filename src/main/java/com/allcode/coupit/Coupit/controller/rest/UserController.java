package com.allcode.coupit.Coupit.controller.rest;
import java.util.List;

import com.allcode.coupit.Coupit.handler.ConstraintViolationExceptionHandler;
import com.allcode.coupit.Coupit.handler.ErrorResponse;
import com.allcode.coupit.Coupit.model.Role;
import com.allcode.coupit.Coupit.model.User;
import com.allcode.coupit.Coupit.service.RoleService;
import com.allcode.coupit.Coupit.service.SessionService;
import com.allcode.coupit.Coupit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolationException;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Autowired
    SessionService sessionService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> listAllUsers() {
        List users = userService.list();
        if(users.size() == 0){
            ErrorResponse error = new ErrorResponse("Empty records");
            return new ResponseEntity<ErrorResponse>(error,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List>(users, HttpStatus.OK);
    }

    //-------------------Retrieve Single User--------------------------------------------------------

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("id") Integer id) {
        User user = userService.findById(id);
        if (user == null) {
            ErrorResponse error = new ErrorResponse("User with id " + id + " not found");
            return new ResponseEntity<ErrorResponse>(error,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }



    //-------------------Create a User--------------------------------------------------------

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String validatePassword,
            @RequestParam Integer roleId,
            @RequestParam(required = false) Integer clientId
    ) {

        if(!validatePassword.equals(password)){
            ErrorResponse error = new ErrorResponse("the password and validation do not match");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
        }


        User repeatUser = userService.findByEmail(email);
        if (repeatUser != null) {
            ErrorResponse error = new ErrorResponse("the user " + email + " already exist");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.CONFLICT);
        }

        Role role = roleService.findById(roleId);
        if (role == null) {
            ErrorResponse error = new ErrorResponse("the Role not exist");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
        }

        User currentUser = sessionService.getCurrentUser();


        String digestPassword = User.getDigestPassword(password);
        User user = new User(firstName, lastName, email, digestPassword, role);
        try{
            userService.save(user);
        }catch (ConstraintViolationException ex){
            return ConstraintViolationExceptionHandler.getResponse(ex);
        }

        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }


    //------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("id") Integer id,
           @RequestParam(required = false) String firstName,
           @RequestParam(required = false) String lastName,
           @RequestParam(required = false) String email) {

        User currentUser = userService.findById(id);

        if (currentUser==null) {
            ErrorResponse error = new ErrorResponse("User with id " + id + " not found");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
        }

        if(firstName != null) currentUser.setFirstName(firstName);
        if(lastName != null) currentUser.setLastName(lastName);
        if(email != null) currentUser.setEmail(email);

        try{
            userService.save(currentUser);
        }catch (ConstraintViolationException ex){
            return ConstraintViolationExceptionHandler.getResponse(ex);
        }
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }

    //------------------- Delete a User --------------------------------------------------------

    /*
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        User user = userService.findById(id);
        if (user == null) {
            ErrorResponse error = new ErrorResponse("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
        }

        userService.delete(user);
        MessageResponse message = new MessageResponse("Done");
        return new ResponseEntity<MessageResponse>(message,HttpStatus.OK);
    }*/
}
