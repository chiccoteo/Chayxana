package com.chayxana.chayxana.payload.SMS;


public class Data {
    private String token;

    public Data() {
    }
    public Data(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Data{" +
                "token='" + token + '\'' +
                '}';
    }
}
