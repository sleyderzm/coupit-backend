package com.allcode.coupit.controllers;

import com.allcode.coupit.handlers.ErrorResponse;
import com.allcode.coupit.handlers.MessageResponse;
import com.allcode.coupit.models.Blockchain;
import com.allcode.coupit.models.Currency;
import com.allcode.coupit.repositories.BlockchainRepository;
import com.allcode.coupit.repositories.CurrencyRepository;
import org.json.JSONArray;
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
@RequestMapping("/currencies")
public class CurrencyController {

    @Autowired
    private BlockchainRepository blockchainRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Currency> getCurrencies(){ return currencyRepository.findAll(); }

    @PostMapping(path="/update", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCurrencies() {
        //TODO: capture json from NEO API
        String json = "{\"current_height\":3632,\"message\":\"\",\"page\":0,\"page_len\":500,\"results\":[{\"block\":3574,\"contract\":{\"author\":\"AndresJaramillo\",\"code\":{\"hash\":\"0xcb9f3b7c6fb1cf2c13a40637c189bdd066a272b4\",\"parameters\":\"0710\",\"returntype\":5,\"script\":\"\"},\"code_version\":\"0.0.1\",\"description\":\"CoupitSC\",\"email\":\"jandresjc@gmail.com\",\"name\":\"Coupit\",\"properties\":{\"dynamic_invoke\":false,\"storage\":true},\"version\":0},\"token\":{\"contract_address\":\"AMfXUmH4G9wTjdBzeB73Y936ocE4D5xrPj\",\"decimals\":8,\"name\":\"Coupit\",\"script_hash\":\"0xdb6cabefe8ae4a5e9f1ecc31b1f7ec00766c9d40\",\"symbol\":\"COU\"},\"tx\":\"0xbfe05958b7b343b3394c51933d58ba9116b0ef7e26d3e58f405fcdae0b459b82\",\"type\":\"SmartContract.Contract.Create\"}],\"total\":1}"; 
        JSONObject response = new JSONObject(json);

        JSONArray results = response.getJSONArray("results");
        Blockchain blockchain = blockchainRepository.findByName(Blockchain.NEO);

        for(int n = 0; n < results.length(); n++) {
            JSONObject result = results.getJSONObject(n);
            JSONObject token = result.getJSONObject("token");

            String hash = token.getString("script_hash");
            if(currencyRepository.findByHashAndBlockchain(hash, blockchain) == null){
                String name = token.getString("name");
                Integer decimals = token.getInt("decimals");
                String contractAddress = token.getString("contract_address");
                String symbol = token.getString("symbol");
                Currency currency = new Currency(name, decimals, contractAddress, hash, symbol, blockchain);
                currencyRepository.save(currency);
            }
        }

        MessageResponse messageResponse = new MessageResponse("Done");
        return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
    }

}