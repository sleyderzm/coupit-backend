package com.allcode.coupit.handlers;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

public class MiddlewareRequest {

    private String apiUrl = "";

    public MiddlewareRequest(){
        this.apiUrl = System.getenv("MIDDLEWARE_API_URL");
    }

    public JSONObject get(String path) {

        String transactionUrl = apiUrl.concat(path);
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(transactionUrl);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);

        ObjectMapper mapper = new ObjectMapper();
        JSONObject root = new JSONObject(response.getBody());
        return root;
    }

    public JSONObject post(String path, List<String> params) {

        String transactionUrl = apiUrl.concat(path);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

        JSONObject json = new JSONObject();

        HttpEntity<String> httpEntity = new HttpEntity <String> (json.toString(), httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(transactionUrl, httpEntity, String.class);

        JSONObject root = new JSONObject(response);
        return root;
    }
}
