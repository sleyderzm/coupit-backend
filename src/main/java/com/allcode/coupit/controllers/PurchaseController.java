package com.allcode.coupit.controllers;

import com.allcode.coupit.handlers.ErrorResponse;
import com.allcode.coupit.handlers.MessageResponse;
import com.allcode.coupit.models.*;
import com.allcode.coupit.repositories.PurchaseRepository;
import com.allcode.coupit.repositories.UserLinkRepository;
import com.allcode.coupit.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserLinkRepository userLinkRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Purchase> getPurchases(){ return purchaseRepository.findAll(); }

    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPurchase(@RequestBody String json) {
        // Post Params
        JSONObject request = new JSONObject(json);
        String uid = request.getString("uid");
        Long userId = request.getLong("user_id");
        Integer amount = request.getInt("amount");

        String[] fieldsToValidate = new String[] { "uid", "userId", "amount" };
        List<String> errors = this.validatePurchase(null, uid, userId, amount, fieldsToValidate);

        if(errors.size() != 0){
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        UserLink userLink = userLinkRepository.findByUid(uid);
        User user = userRepository.findById(userId).get();
        Product product = userLink.getProduct();
        Merchant merchant = product.getMerchant();
        Currency currency = product.getCurrency();
        String productName = product.getName();
        String productDescription = product.getDescription();
        Double productPrice = product.getPrice();

        Purchase purchase = new Purchase(userLink, user, product, merchant, currency, productName, productPrice, productDescription, amount);
        Purchase savedPurchase = purchaseRepository.save(purchase);

        if(savedPurchase.getId().equals(null)) {
            ErrorResponse error = new ErrorResponse("Error when saving the purchase");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Purchase>(savedPurchase, HttpStatus.CREATED);

    }

    @DeleteMapping(path="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePurchase(@PathVariable(name="id", required=true) Long id) {
        String[] fieldsToValidate = new String[] { "id" };
        List<String> errors = this.validatePurchase(id,null, null, null, fieldsToValidate);

        if(errors.size() != 0){
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try{
            purchaseRepository.deleteById(id);
        }catch (Exception ex){
            ErrorResponse errorResponse = new ErrorResponse("Purchase not exists");
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = new MessageResponse("Purchase successfully deleted");
        return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);

    }

    private List<String> validatePurchase(Long id, String uid, Long userId, Integer amount, String[] fieldsToValidate){
        List<String> errors = new ArrayList<>();

        if(Arrays.asList(fieldsToValidate).contains("id")) {
            try{
                Purchase purchase = purchaseRepository.findById(id).get();
                if (purchase.equals(null)){
                    errors.add("Purchase not exists");
                }
            }catch (Exception ex){  errors.add("Purchase not exists"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("uid")) {
            try{
                UserLink userLink = userLinkRepository.findByUid(uid);
                if (userLink.equals(null)){
                    errors.add("uid is not valid");
                }
            }catch (Exception ex){  errors.add("uid is not valid"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("amount")) {
            if(amount == null || amount <= 0){ errors.add("Amount must be greater than or equal to zero"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("userId")) {
            try{
                User buyer = userRepository.findById(userId).get();
                if (buyer.equals(null)){
                    errors.add("Buyer is not valid");
                }
            }catch (Exception ex){  errors.add("Buyer is not valid"); }
        }



        return errors;
    }
}