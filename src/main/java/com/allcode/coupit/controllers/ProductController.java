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
import com.allcode.coupit.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping
    public Iterable<Product> getProduct(
                                        @RequestParam(required = false) Integer pageNumber,
                                        @RequestParam(required = false) Integer pageSize
    ){
        User currentUser = userService.getCurrentUser();
        if(pageNumber == null)pageNumber = 0;
        if(pageSize == null)pageSize = 10;
        Set<Merchant> merchants = currentUser.getMerchants();
        return productRepository.findByMerchantIn(merchants, PageRequest.of(pageNumber, pageSize));
    }

    @GetMapping(path="/list")
    public Iterable<Product> getProductAll(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ){
        User currentUser = userService.getCurrentUser();
        if(pageNumber == null)pageNumber = 0;
        if(pageSize == null)pageSize = 10;
        return productRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<?> getProducts(
            @PathVariable Long id
    ){
        Product product = null;
        try {
            product = productRepository.findById(id).get();
        }catch (Exception e){
            ErrorResponse error = new ErrorResponse("Product not found");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
        }

        User currentUser = userService.getCurrentUser();

        if(!product.hasPermission(currentUser)){
            ErrorResponse error = new ErrorResponse("You're not authorized");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProduct(@RequestBody String json) {
        JSONObject request = new JSONObject(json);

        Long merchantId = null;
        Long currencyId = null;
        String name = null;
        String description = null;
        Double price = null;

        if(request.has("merchantId")) merchantId = request.getLong("merchantId");
        if(request.has("currencyId")) currencyId = request.getLong("currencyId");
        if(request.has("name")) name = request.getString("name");
        if(request.has("description")) description = request.getString("description");
        if(request.has("price")) price = request.getDouble("price");

        String[] fieldsToValidate = new String[] { "merchantId", "name", "description", "price", "currencyId" };
        List<String> errors = this.validateProduct(null, merchantId, name, description, price, currencyId, fieldsToValidate);
        if(errors.size() == 0){
            User currentUser = userService.getCurrentUser();

            Merchant merchant = merchantRepository.findById(merchantId).get();
            if (!merchant.hasPermission(currentUser)){
                ErrorResponse error = new ErrorResponse("You're not authorized");
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNAUTHORIZED);
            }

            Currency currency = currencyRepository.findById(currencyId).get();

            //validate length of decimals
            if(Utils.getDecimalPlaces(price) > currency.getDecimals()){
                ErrorResponse error = new ErrorResponse(currency.getName() + " just have " + currency.getDecimals() + " decimals" );
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
            }

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


    @PutMapping(value="/{id}")
    public ResponseEntity<?> updateProduct(
            @RequestBody String json,
            @PathVariable Long id
    ) {
        JSONObject request = new JSONObject(json);
        Long currencyId = null;
        String name = null;
        String description = null;
        Double price = null;

        if(request.has("currencyId")){
            currencyId = request.getLong("currencyId");
        }

        if(request.has("name")){
            name = request.getString("name");
        }

        if(request.has("description")){
            description = request.getString("description");
        }

        if(request.has("price")){
            price = request.getDouble("price");
        }

        String[] fieldsToValidate = new String[] {"id"};
        List<String> errors = this.validateProduct(id,null,name,description,price,currencyId,fieldsToValidate);
        if(errors.size() == 0){
            User currentUser = userService.getCurrentUser();
            Product product = null;
                try{
                    product = productRepository.findById(id).get();

                    if(!product.hasPermission(currentUser)){
                        ErrorResponse error = new ErrorResponse("You're not authorized");
                        return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNAUTHORIZED);
                    }

                }catch (Exception e) {
                    ErrorResponse error = new ErrorResponse("Product doesn't exist");
                    return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNAUTHORIZED);
                }

            Currency currency = null;
            if(currencyId != null){
                try{
                    currency = currencyRepository.findById(currencyId).get();
                    product.setCurrency(currency);
                }catch (Exception e){
                    ErrorResponse error = new ErrorResponse("Currency doesn't exist");
                    return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNAUTHORIZED);
                }
            }

            if(price != null){
                if(Utils.getDecimalPlaces(price) > currency.getDecimals()){
                    ErrorResponse error = new ErrorResponse(currency.getName() + " just have " + currency.getDecimals() + " decimals" );
                    return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
                }
                product.setPrice(price);
            }


            if(name != null && !name.isEmpty()){
                product.setName(name);
            }

            if(description != null && !description.isEmpty()){
                product.setDescription(description);
            }

            Product savedProduct = productRepository.save(product);
            if(savedProduct.getId() == null) {
                ErrorResponse error = new ErrorResponse("Error when saving the product");
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<Product>(savedProduct, HttpStatus.CREATED);
            }
        }else{
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    private List<String> validateProduct(Long id, Long merchantId, String name, String description, Double price, Long currencyId, String[] fieldsToValidate){
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

        if(Arrays.asList(fieldsToValidate).contains("id")){
            try{
                Product product = productRepository.findById(id).get();
            }
            catch (Exception ex){ errors.add("Product doesn't exist"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("merchantId")){
            try{
                Merchant merchant = merchantRepository.findById(merchantId).get();
            }
            catch (Exception ex){ errors.add("Merchant doesn't exist"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("currencyId")){
            try{
                Currency currency = currencyRepository.findById(currencyId).get();
            }
            catch (Exception ex){ errors.add("Currency doesn't exist"); }
        }

        return errors;
    }
}