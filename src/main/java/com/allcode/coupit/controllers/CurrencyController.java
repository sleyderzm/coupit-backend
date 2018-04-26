package com.allcode.coupit.controllers;

import com.allcode.coupit.handlers.ErrorResponse;
import com.allcode.coupit.handlers.MessageResponse;
import com.allcode.coupit.handlers.Utils;
import com.allcode.coupit.models.Blockchain;
import com.allcode.coupit.models.Currency;
import com.allcode.coupit.repositories.BlockchainRepository;
import com.allcode.coupit.repositories.CurrencyRepository;
import org.json.JSONArray;
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
@RequestMapping("/currencies")
@CrossOrigin(origins = "*")
public class CurrencyController {

    @Autowired
    private BlockchainRepository blockchainRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping
    public Iterable<Currency> getPaginatedCurrencies(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ){
        if(pageNumber == null)pageNumber = 0;
        if(pageSize == null)pageSize = 10;
        return currencyRepository.findAllByOrderByName(PageRequest.of(pageNumber, pageSize));
    }

    @GetMapping(value="/list")
    public Iterable<Currency> getCurrencies(
    ){
        return currencyRepository.findAllByOrderByName();
    }

    @PostMapping(path="/update", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCurrencies() {

        JSONObject response = Utils.readJsonFromUrl(System.getenv("NEO_API") + "/v1/tokens");
        if(response == null){
            ErrorResponse error = new ErrorResponse("Error while trying to get the currencies");
        }

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