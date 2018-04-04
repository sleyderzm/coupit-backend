package com.allcode.coupit.Coupit.controller.rest;
import java.util.List;
import java.util.Optional;

import com.allcode.coupit.Coupit.handler.ConstraintViolationExceptionHandler;
import com.allcode.coupit.Coupit.handler.ErrorResponse;
import com.allcode.coupit.Coupit.model.Role;
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
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> listAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        if(users.size() == 0){
            ErrorResponse error = new ErrorResponse("Empty records");
            return new ResponseEntity<ErrorResponse>(error,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Iterable>(users, HttpStatus.OK);
    }

    //-------------------Retrieve Single User--------------------------------------------------------

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("id") long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.equals(Optional.empty())) {
            ErrorResponse error = new ErrorResponse("User with id " + id + " not found");
            return new ResponseEntity<ErrorResponse>(error,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user.get(), HttpStatus.OK);
    }



    //-------------------Create a User--------------------------------------------------------

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String validatePassword,
            @RequestParam Long roleId,
            @RequestParam(required = false) Integer clientId
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

        Optional<Role> role = roleRepository.findById(roleId);
        if (role.equals(Optional.empty())) {
            ErrorResponse error = new ErrorResponse("the Role not exist");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
        }

        User currentUser = sessionRepository.getCurrentUser();


        String digestPassword = User.getDigestPassword(password);
        User user = new User(firstName, lastName, email, digestPassword, role.get());
        try{
            userRepository.save(user);
        }catch (ConstraintViolationException ex){
            return ConstraintViolationExceptionHandler.getResponse(ex);
        }

        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }


    //------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id,
           @RequestParam(required = false) String firstName,
           @RequestParam(required = false) String lastName,
           @RequestParam(required = false) String email) {

        Optional<User> user = userRepository.findById(id);

        if (user.equals(Optional.empty())) {
            ErrorResponse error = new ErrorResponse("User with id " + id + " not found");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
        }

        User currentUser = user.get();

        if(firstName != null) currentUser.setFirstName(firstName);
        if(lastName != null) currentUser.setLastName(lastName);
        if(email != null) currentUser.setEmail(email);

        try{
            userRepository.save(currentUser);
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
