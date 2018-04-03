package com.allcode.coupit.Coupit.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
    public static String dateToISO8601(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public static JsonNode getBody(HttpServletRequest request){
        try{
            BufferedReader reader = request.getReader();
            String body = reader.readLine();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(body);
        }catch (Exception e){
            System.out.println(e.toString());
        }

        return null;

    }


    public static String validSringParam(String param){
        if(param == null) return null;
        if(param.equals("null")) return null;
        return param;
    }

    public static List<String> validateRequiredParams(String[] paramsName, Object[] paramsValue){
        List<String> invalidParams = new ArrayList<String>();
        int i = 0;
        for(Object param : paramsValue){
            if(param == null){
                invalidParams.add(paramsName[i]);
            }
            i++;
        }

        if(invalidParams.size() == 0){
            return null;
        }

        return invalidParams;
    }
}
