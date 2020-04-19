package com.example.eceapp;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static boolean IsBlank(String string){
        if(string.equals("")){
            return true;
        }
        return  false;
    }//end of IsBlank

    public static boolean ValidPostal(String string){
        Pattern postal = Pattern.compile("^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$");
        Matcher matcher = postal.matcher(string);
        if(matcher.matches()){
            return true;
        }
        return false;
    }//end of ValidPostal

    public static boolean ValidEmail(String string){
        Pattern email = Patterns.EMAIL_ADDRESS;
        Matcher matcher = email.matcher(string);
        if(matcher.matches()){
            return true;
        }
        return false;
    }//end of ValidPostal

    public static boolean ValidPhone(String string){
        Pattern phone = Patterns.PHONE;
        Matcher matcher = phone.matcher(string);
        if(matcher.matches()){
            return true;
        }
        return false;
    }//end of ValidPhone
}
