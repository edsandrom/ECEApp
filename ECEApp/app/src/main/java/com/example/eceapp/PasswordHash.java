package com.example.eceapp;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {

    private String password;

    public PasswordHash(){

    }

    public PasswordHash(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String generatedPassword(String password){

        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for(int i=0; i<bytes.length; i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            password = sb.toString();

        }
        catch(NoSuchAlgorithmException ex){
            Log.e("Algorithm Error", ex.getMessage());
        }

        return password;
    }
}
