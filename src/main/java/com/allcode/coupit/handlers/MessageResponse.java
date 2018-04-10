package com.allcode.coupit.handlers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MessageResponse {
    private String message;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+0000");
    private String timestamp;

    public MessageResponse(String message) {
        this.message = message;
        this.timestamp = simpleDateFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public MessageResponse(){
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
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
