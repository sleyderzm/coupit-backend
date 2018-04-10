package com.allcode.coupit.controllers;

import com.allcode.coupit.models.Role;
import com.allcode.coupit.models.User;
import com.allcode.coupit.repositories.UserRepository;
import com.allcode.coupit.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import com.allcode.coupit.handlers.ErrorResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Arrays;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public Iterable<User> getUsers(){ return userRepository.findAll(); }

    @PostMapping
    public ResponseEntity<?> createUser(
        @RequestParam(value = "first_name", required=true) final String firstName,
        @RequestParam(value = "last_name", required=true) final String lastName,
        @RequestParam(value = "email", required=true) final String email,
        @RequestParam(value = "password", required=true) final String password,
        @RequestParam(value = "repeat_password", required=true) final String repeatPassword,
        @RequestParam(value = "role_id", required=true) final long roleId
    ) {
        String[] fieldsToValidate = new String[] { "email","password", "role_id" };
        List<String> errors = this.validateUser(roleId, email, password, repeatPassword, fieldsToValidate);
        if(errors.size() == 0){
            Role userRole = roleRepository.findById(roleId).get();

            User user = new User(firstName, lastName, email, passwordEncoder.encode(password), userRole);
            User savedUser = userRepository.save(user) ;

            if(savedUser.getId().equals(null))
            {
                ErrorResponse error = new ErrorResponse("Error when saving the user");
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
            }
            else{
                return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
            }
        }
        else{
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(
            @PathVariable(name="id", required=true) Long id,
            @RequestParam(value = "first_name", required=true) final String firstName,
            @RequestParam(value = "last_name", required=true) final String lastName,
            @RequestParam(value = "role_id", required=true) final long roleId
    ) {
        String[] fieldsToValidate = new String[] { "email" };
        List<String> errors = this.validateUser(roleId, null, null, null, fieldsToValidate);
        if(errors.size() == 0){
            Role userRole = roleRepository.findById(roleId).get();

            User user = userRepository.findById(id).get();

            if(user.getId().equals(null))
            {
                ErrorResponse error = new ErrorResponse("User with ID " + id + " not found");
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
            }
            else{
                user.setFirstName(firstName);
                user.setFirstName(lastName);
                user.setRole(userRole);
                User savedUser = userRepository.save(user);
                return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
            }
        }
        else{
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    public List<String> validateUser(long roleId, String email, String password, String repeatPassword, String[] fieldsToValidate){
        List<String> errors = new ArrayList<>();

        final Pattern valid_email_regex =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = valid_email_regex.matcher(email);

        if(! matcher.find() && Arrays.asList(fieldsToValidate).contains("email")){
            errors.add("Email is not valid");
        }

        if(! password.equals(repeatPassword) && Arrays.asList(fieldsToValidate).contains("password")){ errors.add("Passwords do not match"); }

        User repeatUser = userRepository.findByEmail(email);

        if (repeatUser != null && Arrays.asList(fieldsToValidate).contains("email")) { errors.add("Email " + email + " already exist"); }

        try{
            Role userRole = roleRepository.findById(roleId).get();
            if (userRole.equals(null) && Arrays.asList(fieldsToValidate).contains("role_id")){
                errors.add("Role not exists");
            }
        }
        catch (Exception ex){ if(Arrays.asList(fieldsToValidate).contains("role_id")){ errors.add("Role not exists"); } }

        return errors;
    }


}