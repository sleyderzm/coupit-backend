package com.allcode.coupit.controllers;

import com.allcode.coupit.handlers.ErrorResponse;
import com.allcode.coupit.handlers.Utils;
import com.allcode.coupit.models.Merchant;
import com.allcode.coupit.models.User;
import com.allcode.coupit.repositories.MerchantRepository;
import com.allcode.coupit.repositories.UserRepository;
import com.allcode.coupit.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/merchants")
@CrossOrigin(origins = "*")
public class MerchantController {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserService userService;

    @GetMapping
    public Iterable<Merchant> getPaginatedMerchants(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ){
        User currentUser = userService.getCurrentUser();
        if(pageNumber == null)pageNumber = 0;
        if(pageSize == null)pageSize = 10;
        return merchantRepository.findByUserOrderByName(currentUser, PageRequest.of(pageNumber, pageSize));
    }

    @GetMapping(value="/list")
    public Iterable<Merchant> getMerchants(
    ){
        User currentUser = userService.getCurrentUser();
        return merchantRepository.findByUserOrderByName(currentUser);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<?> getMerchant(
            @PathVariable Long id
    ){
        Merchant merchant = null;
        try {
            merchant = merchantRepository.findById(id).get();
        }catch (Exception e){
            ErrorResponse error = new ErrorResponse("Merchant not found");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
        }

        User currentUser = userService.getCurrentUser();

        if(!merchant.hasPermission(currentUser)){
            ErrorResponse error = new ErrorResponse("You're not authorized");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<Merchant>(merchant, HttpStatus.OK);
    }

    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createMerchant(@RequestBody String json) {

        JSONObject request = new JSONObject(json);
        String merchantType = request.getString("merchantType");
        String name = request.getString("name");
        String websiteUrl = request.getString("websiteUrl");

        String[] fieldsToValidate = new String[] {"merchantType", "name", "websiteUrl" };
        Long id = new Long(0);
        List<String> errors = this.validateMerchant(id, merchantType, name, websiteUrl, fieldsToValidate);
        if(errors.size() == 0){
            User currentUser = userService.getCurrentUser();

            Merchant merchant = new Merchant(merchantType, name, websiteUrl, currentUser);
            Merchant savedMerchant = merchantRepository.save(merchant) ;

            if(savedMerchant.getId() == null)
            {
                ErrorResponse error = new ErrorResponse("Error when saving the merchant");
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
            }
            else{
                return new ResponseEntity<Merchant>(savedMerchant, HttpStatus.CREATED);
            }
        }
        else{
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<?> updateMerchant(
            @RequestBody String json,
            @PathVariable Long id
    ) {
        JSONObject request = new JSONObject(json);
        String merchantType = null;
        String name = null;
        String websiteUrl = null;

        if(request.has("merchantType")){
            merchantType = request.getString("merchantType");
        }

        if(request.has("name")){
            name = request.getString("name");
        }

        if(request.has("websiteUrl")){
            websiteUrl = request.getString("websiteUrl");
        }

        String[] fieldsToValidate = new String[] {"id" };
        List<String> errors = this.validateMerchant(id, merchantType, name, websiteUrl, fieldsToValidate);
        if(errors.size() == 0){
            Merchant merchant = merchantRepository.findById(id).get();
            User currentUser = userService.getCurrentUser();
            if(!merchant.hasPermission(currentUser)){
                ErrorResponse error = new ErrorResponse("You're not authorized");
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNAUTHORIZED);
            }

            if(merchantType != null && !merchantType.equals("")){
                merchant.setMerchantType(merchantType);
            }

            if(name != null && !name.equals("")){
                merchant.setName(name);
            }

            if(websiteUrl != null && !websiteUrl.equals("")){
                merchant.setWebsiteUrl(websiteUrl);
            }

            Merchant savedMerchant = merchantRepository.save(merchant) ;

            if(savedMerchant.getId() == null)
            {
                ErrorResponse error = new ErrorResponse("Error when saving the merchant");
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
            }
            else{
                return new ResponseEntity<Merchant>(savedMerchant, HttpStatus.CREATED);
            }
        }
        else{
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    private List<String> validateMerchant(long id, String merchantType, String name, String websiteUrl, String[] fieldsToValidate){
        List<String> errors = new ArrayList<>();

        if(Arrays.asList(fieldsToValidate).contains("merchantType")) {
            if(merchantType == null || merchantType.equals("")){ errors.add("Merchant type can not be empty"); }
            else if(! Merchant.PERSON_COMPANY_TYPE.equals(merchantType)
                    && ! Merchant.PERSON_MERCHANT_TYPE.equals(merchantType)){ errors.add("Merchant type is not valid"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("name")) {
            if(name == null || name.equals("")){ errors.add("Name can not be empty"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("id")){
            try{
                Merchant merchant = merchantRepository.findById(id).get();
            }
            catch (Exception ex){  errors.add("Merchant not exists"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("websiteUrl")) {
            if(websiteUrl == null || websiteUrl.equals("")){ errors.add("Website url can not be empty"); }
            else if(!Utils.isValidURL(websiteUrl)){ errors.add("Website url is not valid"); }
        }

        return errors;
    }
}