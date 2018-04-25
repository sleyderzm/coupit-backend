package com.allcode.coupit.handlers;

import com.allcode.coupit.models.Currency;
import org.json.JSONObject;


import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

public class Utils {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url){
        try {
            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            is.close();
            return json;
        }catch (Exception e){
            return null;
        }
    }

    public static Long priceToLong(Double price, Currency currency){
        price = price * Math.pow(10, currency.getDecimals());
        return  price.longValue();
    }

    public static Double priceToDouble(Long priceLong, Currency currency){
        return priceLong * Math.pow(10, (-1) * currency.getDecimals());
    }

    public static Integer getDecimalPlaces(Double n){
        String text = Double.toString(Math.abs(n));
        Integer integerPlaces = text.indexOf('.');
        return text.length() - integerPlaces - 1;
    }

    public static Double roundDecimals(Double d, Integer p){
        String pattern = "#.";
        for(int i = 0; i < p; i++){
            pattern = pattern + "#";
            i++;
        }
        DecimalFormat df = new DecimalFormat(pattern);
        df.setRoundingMode(RoundingMode.CEILING);
        return Double.parseDouble(df.format(d));
    }

    public static boolean isValidURL(String urlStr) {
        try {
            URL url = new URL(urlStr);
            return true;
        }
        catch (MalformedURLException e) {
            return false;
        }
    }

}
