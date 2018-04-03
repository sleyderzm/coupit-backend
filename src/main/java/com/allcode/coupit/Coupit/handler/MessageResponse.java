package com.allcode.coupit.Coupit.handler;

public class MessageResponse {
    private String message;
    private Boolean error;

    public MessageResponse(String message) {
        this.message = message;
        this.error = false;
    }

    public MessageResponse(){
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "message=" + message +
                ", error='" + true + '\'' +
                '}';
    }
}
