package com.allcode.coupit.controllers;

import com.allcode.coupit.handlers.ErrorResponse;
import com.allcode.coupit.models.Merchant;
import com.allcode.coupit.models.Product;
import com.allcode.coupit.models.User;
import com.allcode.coupit.models.UserLink;
import com.allcode.coupit.repositories.UserLinkRepository;
import com.allcode.coupit.repositories.UserRepository;
import com.allcode.coupit.repositories.ProductRepository;
import com.allcode.coupit.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user_links")
@CrossOrigin(origins = "*")
public class UserLinkController {

    @Autowired
    private UserLinkRepository userLinkRepository;

    @GetMapping
    public Iterable<UserLink> getUserLinks(){ return userLinkRepository.findAll(); }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @GetMapping(path="/list")
    public Iterable<UserLink> getPromotedProductByUser(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ){
        User currentUser = userService.getCurrentUser();
        if(pageNumber == null)pageNumber = 0;
        if(pageSize == null)pageSize = 10;
        return userLinkRepository.findByUserIn(currentUser,PageRequest.of(pageNumber, pageSize));
    }

    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUserLink(@RequestBody String json) {
        // Post Params
        JSONObject request = new JSONObject(json);
        User currentUser = userService.getCurrentUser();
        long productId = request.getLong("productId");
        long userId = currentUser.getId();
        String[] fieldsToValidate = new String[] { "productId", "userId" };
        Long id = new Long(0);
        List<String> errors = this.validateUserLink(id, productId, userId, fieldsToValidate);
        if(errors.size() == 0){
            Product product = productRepository.findById(productId).get();
            User user = userRepository.findById(userId).get();
            String uid = UUID.randomUUID().toString();
            UserLink userLink = new UserLink(uid, user, product);
            UserLink savedUserLink = userLinkRepository.save(userLink) ;

            if(savedUserLink.getId() == null)
            {
                ErrorResponse error = new ErrorResponse("Error when saving the product");
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
            }
            else{
                return new ResponseEntity<UserLink>(savedUserLink, HttpStatus.CREATED);
            }
        }
        else{
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    private List<String> validateUserLink(long id, long productId, long userId, String[] fieldsToValidate){
        List<String> errors = new ArrayList<>();


        try{
            User user = userRepository.findById(userId).get();
            if (user == null && Arrays.asList(fieldsToValidate).contains("userId")){
                errors.add("User not exists");
            }
        }
        catch (Exception ex){ if(Arrays.asList(fieldsToValidate).contains("userId")){ errors.add("User doesn't exist"); } }


        try{
            Product product = productRepository.findById(productId).get();
            if (product == null && Arrays.asList(fieldsToValidate).contains("productId")){
                errors.add("Product not exists");
            }
        }
        catch (Exception ex){ if(Arrays.asList(fieldsToValidate).contains("productId")){ errors.add("Product doesn't exist"); } }

        return errors;
    }
}