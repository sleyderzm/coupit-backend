package com.allcode.coupit.controllers;

import com.allcode.coupit.handlers.ErrorResponse;
import com.allcode.coupit.handlers.MessageResponse;
import com.allcode.coupit.handlers.S3;
import com.allcode.coupit.models.User;
import com.allcode.coupit.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody String json) {
        // Post Params
        JSONObject request = new JSONObject(json);
        String email = null;
        String password = null;
        if(request.has("email")) email = request.getString("email");
        if(request.has("password")) password  = request.getString("password");

        List<String> errors = new ArrayList<>();


        if(email == null || email.equals("")){
            errors.add("Email can not be empty");
        }else{
            final Pattern valid_email_regex =
                    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = valid_email_regex.matcher(email);
            if(! matcher.find()){ errors.add("Email is not valid"); }
        }

        if(password == null || password.equals("")){ errors.add("Password can not be empty"); }

        if(errors.size() > 0){
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }


        User user = userRepository.findByEmail(email);
        if(user == null){
            ErrorResponse error = new ErrorResponse("Authentication Error: Email incorrect");
            return new ResponseEntity<ErrorResponse>(error,HttpStatus.UNAUTHORIZED);//You many decide to return HttpStatus.NOT_FOUND
        }

        if(!passwordEncoder.matches(password, user.getPassword())){
            ErrorResponse error = new ErrorResponse("Authentication Error: Password incorrect");
            return new ResponseEntity<ErrorResponse>(error,HttpStatus.UNAUTHORIZED);//You many decide to return HttpStatus.NOT_FOUND
        }

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(value="/putSignedURL", method = RequestMethod.GET)
    public ResponseEntity<?> putSignedURL(
            @RequestParam String contentType
    ) {

        URL url = S3.putSignedURL(contentType);
        if (url == null){
            ErrorResponse error = new ErrorResponse("Error when try to connect to s3");
            return new ResponseEntity<ErrorResponse>(error,HttpStatus.INTERNAL_SERVER_ERROR);//You many decide to return HttpStatus.NOT_FOUND
        }

        MessageResponse message = new MessageResponse(url.toString());
        return new ResponseEntity<MessageResponse>(message,HttpStatus.OK);//You many decide to return HttpStatus.NOT_FOUND

    }

}
