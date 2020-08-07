package com.example.talentfinder.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Discussion")
public class Discussion extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_RECIPIENT = "recipient";
    public static final String KEY_MESSAGES = "messages";

    // GET/SET User
    public User getUser(){
        return (User) getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    // GET/SET Recipient
    public User getRecipient(){
        return (User) getParseUser(KEY_RECIPIENT);
    }

    public void setRecipient(ParseUser recipient){
        put(KEY_RECIPIENT, recipient);
    }
}
