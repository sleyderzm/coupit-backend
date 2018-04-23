package com.allcode.coupit.controllers;

import com.allcode.coupit.handlers.ErrorResponse;
import com.allcode.coupit.handlers.MessageResponse;
import com.allcode.coupit.handlers.Utils;
import com.allcode.coupit.models.*;
import com.allcode.coupit.repositories.*;
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
@CrossOrigin(origins = "*")
public class PurchaseController {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserLinkRepository userLinkRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Purchase> getPurchases(){ return purchaseRepository.findAll(); }

    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPurchase(@RequestBody String json) {
        // Post Params
        JSONObject request = new JSONObject(json);

        String uid = null;
        if(request.has("uid")){ uid = request.getString("uid"); }

        Long productId = null;
        if(request.has("product_id")){ productId = request.getLong("product_id"); }

        Long userId = null;
        if(request.has("user_id")){ userId = request.getLong("user_id"); }

        Integer amount = null;
        if(request.has("amount")){ amount = request.getInt("amount"); }

        String[] fieldsToValidate = new String[] { "uid", "userId", "amount", "productId" };
        List<String> errors = this.validatePurchase(null, uid, userId, amount, productId, fieldsToValidate);

        if(errors.size() != 0){
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        UserLink userLink = userLinkRepository.findByUid(uid);
        Product product;
        if(userLink == null){
            product = productRepository.findById(productId).get();
        }else{
            product = userLink.getProduct();
        }

        User user = userRepository.findById(userId).get();
        Merchant merchant = product.getMerchant();
        Currency currency = product.getCurrency();
        String productName = product.getName();
        String productDescription = product.getDescription();
        Double productPrice = product.getPrice();

        Purchase purchase = new Purchase(userLink, user, product, merchant, currency, productName, productPrice, productDescription, amount);
        Purchase savedPurchase = purchaseRepository.save(purchase);

        if(savedPurchase.getId() == null) {
            ErrorResponse error = new ErrorResponse("Error when saving the purchase");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
        }

        Double valueTransaction = amount * productPrice;
        if(userLink == null){
            Transaction transaction = new Transaction(valueTransaction, currency, merchant.getUser(), savedPurchase);
            transactionRepository.save(transaction);
        }else{
            Double partForMerchant = valueTransaction * 0.95;
            if(Utils.getDecimalPlaces(partForMerchant) > currency.getDecimals()){
                partForMerchant = Utils.roundDecimals(partForMerchant, currency.getDecimals());
            }

            Transaction transaction1 = new Transaction(partForMerchant, currency, merchant.getUser(), savedPurchase);
            transactionRepository.save(transaction1);

            Double partForOwnerLink = valueTransaction * 0.05;
            if(Utils.getDecimalPlaces(partForMerchant) > currency.getDecimals()){
                partForOwnerLink = Utils.roundDecimals(partForOwnerLink, currency.getDecimals());
            }

            Transaction transaction2 = new Transaction(partForOwnerLink, currency, userLink.getUser(), savedPurchase);
            transactionRepository.save(transaction2);
        }

        return new ResponseEntity<Purchase>(savedPurchase, HttpStatus.CREATED);

    }

    private List<String> validatePurchase(Long id, String uid, Long userId, Integer amount,Long productId, String[] fieldsToValidate){
        List<String> errors = new ArrayList<>();

        if(Arrays.asList(fieldsToValidate).contains("id")) {
            try{
                Purchase purchase = purchaseRepository.findById(id).get();
                if (purchase == null){
                    errors.add("Purchase not exists");
                }
            }catch (Exception ex){  errors.add("Purchase not exists"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("uid")) {
            UserLink userLink = userLinkRepository.findByUid(uid);
            if(userLink == null){
                try{
                    Product product = productRepository.findById(productId).get();
                    if (product == null){
                        errors.add("Product is not valid");
                    }
                }catch (Exception ex){  errors.add("Product is not valid"); }
            }
        }

        if(Arrays.asList(fieldsToValidate).contains("amount")) {
            if(amount == null || amount <= 0){ errors.add("Amount must be greater than or equal to zero"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("userId")) {
            try{
                User user = userRepository.findById(userId).get();
                if (user == null){
                    errors.add("User is not valid");
                }
            }catch (Exception ex){  errors.add("User is not valid"); }
        }



        return errors;
    }
}