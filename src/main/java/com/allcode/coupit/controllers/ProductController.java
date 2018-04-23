package com.allcode.coupit.controllers;

import com.allcode.coupit.handlers.ErrorResponse;
import com.allcode.coupit.handlers.Utils;
import com.allcode.coupit.models.Currency;
import com.allcode.coupit.models.Merchant;
import com.allcode.coupit.models.Product;
import com.allcode.coupit.models.User;
import com.allcode.coupit.repositories.CurrencyRepository;
import com.allcode.coupit.repositories.MerchantRepository;
import com.allcode.coupit.repositories.ProductRepository;
import com.allcode.coupit.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public Iterable<Product> getProduct(){
        return productRepository.findAll();
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProduct(@RequestBody String json) {
        // Post Params
        JSONObject request = new JSONObject(json);
        long merchantId = request.getLong("merchant_id");
        long currencyId = request.getLong("currency_id");
        String name = request.getString("name");
        String description = request.getString("description");
        double price = request.getDouble("price");

        String[] fieldsToValidate = new String[] { "merchantId", "name", "description", "price", "currencyId" };
        Long id = new Long(0);
        List<String> errors = this.validateProduct(id, merchantId, name, description, price, currencyId, fieldsToValidate);
        if(errors.size() == 0){
            Currency currency = currencyRepository.findById(currencyId).get();

            //validate length of decimals
            if(Utils.getDecimalPlaces(price) > currency.getDecimals()){
                ErrorResponse error = new ErrorResponse(currency.getName() + " just have " + currency.getDecimals() + " decimals" );
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
            }

            Merchant merchant = merchantRepository.findById(merchantId).get();
            Product product = new Product(name, price, description, merchant, currency);
            Product savedProduct = productRepository.save(product) ;

            if(savedProduct.getId() == null)
            {
                ErrorResponse error = new ErrorResponse("Error when saving the product");
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
            }
            else{
                return new ResponseEntity<Product>(savedProduct, HttpStatus.CREATED);
            }
        }
        else{
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    private List<String> validateProduct(long id, long merchantId, String name, String description, double price, long currencyId, String[] fieldsToValidate){
        List<String> errors = new ArrayList<>();

        if(Arrays.asList(fieldsToValidate).contains("name")) {
            if(name == null || name.equals("")){ errors.add("Name can not be empty"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("description")) {
            if(description == null || description.equals("")){ errors.add("Description can not be empty"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("price")) {
            if(price < 0){ errors.add("Price must be greater than or equal to zero"); }
        }

        try{
            Merchant merchant = merchantRepository.findById(merchantId).get();
            if (merchant == null && Arrays.asList(fieldsToValidate).contains("merchantId")){
                errors.add("Merchant not exists");
            }
        }
        catch (Exception ex){ if(Arrays.asList(fieldsToValidate).contains("merchantId")){ errors.add("Merchant not exists"); } }

        try{
            Currency currency = currencyRepository.findById(currencyId).get();
            if (currency == null && Arrays.asList(fieldsToValidate).contains("currencyId")){
                errors.add("Currency not exists");
            }
        }
        catch (Exception ex){ if(Arrays.asList(fieldsToValidate).contains("currencyId")){ errors.add("Currency not exists"); } }

        return errors;
    }
}