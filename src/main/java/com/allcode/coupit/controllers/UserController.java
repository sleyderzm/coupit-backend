package com.allcode.coupit.controllers;

import com.allcode.coupit.models.Role;
import com.allcode.coupit.models.User;
import com.allcode.coupit.repositories.UserRepository;
import com.allcode.coupit.repositories.RoleRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.allcode.coupit.handlers.ErrorResponse;
import com.allcode.coupit.handlers.MessageResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Arrays;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public Iterable<User> getUsers(){ return userRepository.findAll(); }

    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody String json) {
        // Post Params
        JSONObject request = new JSONObject(json);
        long roleId = request.getLong("role_id");
        String firstName = request.getString("first_name");
        String lastName = request.getString("last_name");
        String email = request.getString("email");
        String password = request.getString("password");
        String repeatPassword = request.getString("repeat_password");

        String[] fieldsToValidate = new String[] { "email","password", "roleId", "fisrtName", "lastName" };
        Long id = new Long(0);
        List<String> errors = this.validateUser(id, roleId, firstName, lastName, email, password, repeatPassword, fieldsToValidate);
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

    @PutMapping(path="/{id}", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable(name="id", required=true) Long id, @RequestBody String json) {
        // Put Params
        JSONObject request = new JSONObject(json);
        long roleId = request.getLong("role_id");
        String firstName = request.getString("first_name");
        String lastName = request.getString("last_name");

        String[] fieldsToValidate = new String[] { "roleId", "firstName", "lastName", "id" };
        List<String> errors = this.validateUser(id, roleId, firstName, lastName, null, null, null, fieldsToValidate);
        if(errors.size() == 0){
            Role userRole = roleRepository.findById(roleId).get();
            User user = userRepository.findById(id).get();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole(userRole);
            User savedUser = userRepository.save(user);
            return new ResponseEntity<User>(savedUser, HttpStatus.OK);
        }
        else{
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable(name="id", required=true) Long id) {
        String[] fieldsToValidate = new String[] { "id" };
        List<String> errors = this.validateUser(id, 0, null, null, null, null, null, fieldsToValidate);
        if(errors.size() == 0){
            userRepository.deleteById(id);
            MessageResponse messageResponse = new MessageResponse("User successfully deleted");
            return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
        }
        else{
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    private List<String> validateUser(long id, long roleId, String firstName, String lastName, String email, String password, String repeatPassword, String[] fieldsToValidate){
        List<String> errors = new ArrayList<>();

        final Pattern valid_email_regex =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        if(Arrays.asList(fieldsToValidate).contains("firstName")) {
            if(firstName.equals(null) || firstName.equals("")){ errors.add("First Name can not be empty"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("lastName")) {
            if(lastName.equals(null) || lastName.equals("")){ errors.add("Last Name can not be empty"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("email")) {
            Matcher matcher = valid_email_regex.matcher(email);
            User repeatUser = userRepository.findByEmail(email);
            if(email.equals(null) || email.equals("")){ errors.add("Email can not be empty"); }
            else if(! matcher.find()){ errors.add("Email is not valid"); }
            else if (repeatUser != null) { errors.add("Email " + email + " already exist"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("password")) {
            if(password.equals(null) || password.equals("")){ errors.add("Password can not be empty"); }
            else if(repeatPassword.equals(null) || repeatPassword.equals("")){ errors.add("Repeat Password can not be empty"); }
            else if(! password.equals(repeatPassword)) { errors.add("Passwords do not match"); }
        }

        try{
            User user = userRepository.findById(id).get();
            if (user.equals(null) && Arrays.asList(fieldsToValidate).contains("id")){
                errors.add("User not exists");
            }
        }
        catch (Exception ex){ if(Arrays.asList(fieldsToValidate).contains("id")){ errors.add("User not exists"); } }


        try{
            Role userRole = roleRepository.findById(roleId).get();
            if (userRole.equals(null) && Arrays.asList(fieldsToValidate).contains("roleId")){
                errors.add("Role not exists");
            }
        }
        catch (Exception ex){ if(Arrays.asList(fieldsToValidate).contains("roleId")){ errors.add("Role not exists"); } }

        return errors;
    }
}