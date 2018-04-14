package com.allcode.coupit.controllers;

import com.allcode.coupit.handlers.CryptUtils;
import com.allcode.coupit.handlers.ErrorResponse;
import com.allcode.coupit.handlers.MessageResponse;
import com.allcode.coupit.handlers.MiddlewareRequest;
import com.allcode.coupit.models.*;
import com.allcode.coupit.models.Blockchain;
import com.allcode.coupit.repositories.AccountRepository;
import com.allcode.coupit.repositories.BlockchainRepository;
import com.allcode.coupit.repositories.RoleRepository;
import com.allcode.coupit.repositories.BlockchainRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/blockchains")
public class BlockchainController {

    @Autowired
    private BlockchainRepository blockchainRepository;

    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Blockchain> getBlockchains(){ return blockchainRepository.findAll(); }

    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBlockchain(@RequestBody String json) {
        // Post Params
        JSONObject request = new JSONObject(json);
        String name = request.getString("name");
        String[] fieldsToValidate = new String[] { "name" };
        List<String> errors = this.validateBlockchain(null, name, fieldsToValidate);

        if(errors.size() != 0){
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if(blockchainRepository.findByName(name) != null){
            ErrorResponse errorResponse = new ErrorResponse("Blockchain with name "+name+" already exists");
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Blockchain blockchain = new Blockchain(name);
        Blockchain savedBlockchain = blockchainRepository.save(blockchain);

        if(savedBlockchain.getId().equals(null)) {
            ErrorResponse error = new ErrorResponse("Error when saving the blockchain");
            return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Blockchain>(savedBlockchain, HttpStatus.CREATED);

    }

    @PutMapping(path="/{id}", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBlockchain(@PathVariable(name="id", required=true) Long id, @RequestBody String json) {
        // Put Params
        JSONObject request = new JSONObject(json);
        String name = request.getString("name");
        String[] fieldsToValidate = new String[] { "id", "name" };
        List<String> errors = this.validateBlockchain(id, name, fieldsToValidate);

        if(errors.size() != 0){
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Blockchain blockchain;
        try{
            blockchain = blockchainRepository.findById(id).get();
        }catch (Exception ex){
            ErrorResponse errorResponse = new ErrorResponse("Blockchain not exists");
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        blockchain.setName(name);
        Blockchain savedBlockchain = blockchainRepository.save(blockchain);
        return new ResponseEntity<Blockchain>(savedBlockchain, HttpStatus.OK);
    }

    @DeleteMapping(path="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBlockchain(@PathVariable(name="id", required=true) Long id) {
        String[] fieldsToValidate = new String[] { "id" };
        List<String> errors = this.validateBlockchain(id,null, fieldsToValidate);

        if(errors.size() != 0){
            ErrorResponse errorResponse = new ErrorResponse(String.join(", ", errors));
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try{
            blockchainRepository.deleteById(id);
        }catch (Exception ex){
            ErrorResponse errorResponse = new ErrorResponse("Blockchain not exists");
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = new MessageResponse("Blockchain successfully deleted");
        return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);

    }

    private List<String> validateBlockchain(Long id, String name, String[] fieldsToValidate){
        List<String> errors = new ArrayList<>();

        if(Arrays.asList(fieldsToValidate).contains("name")) {
            if(name.equals(null) || name.equals("")){ errors.add("Name can not be empty"); }
        }

        if(Arrays.asList(fieldsToValidate).contains("id")) {
            try{
                Blockchain blockchain = blockchainRepository.findById(id).get();
                if (blockchain.equals(null)){
                    errors.add("Blockchain not exists");
                }
            }catch (Exception ex){  errors.add("Blockchain not exists"); }
        }



        return errors;
    }
}