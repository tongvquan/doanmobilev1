package com.doan.doan.model;

public class ResponseLogin {
    private String token;

    public ResponseLogin() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ResponseLogin{" +
                "token='" + token + '\'' +
                '}';
    }
}
