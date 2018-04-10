package com.allcode.coupit.handlers;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;

public class ErrorResponse {
    private String message;
    private String error = "Bad Request";
    private int status = 400;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+0000");
    private String timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = simpleDateFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public ErrorResponse(){
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", status='" + status + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
