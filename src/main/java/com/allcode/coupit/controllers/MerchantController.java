package com.allcode.coupit.controllers;

import com.allcode.coupit.handlers.ErrorResponse;
import com.allcode.coupit.models.Merchant;
import com.allcode.coupit.models.User;
import com.allcode.coupit.repositories.MerchantRepository;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Iterable<Merchant> getMerchants(){ return merchantRepository.findAll(); }

    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createMerchant(@RequestBody String json) {
        // Post Params
        JSONObject request = new JSONObject(json);
        long userId = request.getLong("user_id");
        String merchantType = request.getString("merchant_type");
        String name = request.getString("name");
        String websiteUrl = request.getString("website_url");

        String[] fieldsToValidate = new String[] { "userId","merchantType", "name", "websiteUrl" };
        Long id = new Long(0);
        List<String> errors = this.validateMerchant(id, userId, merchantType, name, websiteUrl, fieldsToValidate);
        if(errors.size() == 0){
            User user = userRepository.findById(userId).get();

            Merchant merchant = new Merchant(merchantType, name, websiteUrl, user);
            Merchant savedMerchant = merchantRepository.save(merchant) ;

            if(savedMerchant.getId().equals(null))
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

    private List<String> validateMerchant(long id, long userId, String merchantType, String name, String websiteUrl, String[] fieldsToValidate){
        List<String> errors = new ArrayList<>();

        final Pattern valid_url_regex =
                Pattern.compile("^(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$");

        if(Arrays.asList(fieldsToValidate).contains("merchantType")) {
            if(merchantType.equals(null) || merchantType.equals("")){ errors.add("Merchant Type can not be empty"); }
            else if(! Merchant.PERSON_COMPANY_TYPE.equals(merchantType)
                    && ! Merchant.PERSON_MERCHANT_TYPE.equals(merchantType)){ errors.add("Merchant Type is not valid"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("name")) {
            if(name.equals(null) || name.equals("")){ errors.add("Name can not be empty"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("websiteUrl")) {
            Matcher matcher = valid_url_regex.matcher(websiteUrl);
            if(websiteUrl.equals(null) || websiteUrl.equals("")){ errors.add("Website Url can not be empty"); }
            else if(! matcher.find()){ errors.add("Website Url is not valid"); }
        }

        try{
            User user = userRepository.findById(userId).get();
            if (user.equals(null) && Arrays.asList(fieldsToValidate).contains("userId")){
                errors.add("User not exists");
            }
        }
        catch (Exception ex){ if(Arrays.asList(fieldsToValidate).contains("userId")){ errors.add("User not exists"); } }

        return errors;
    }
}