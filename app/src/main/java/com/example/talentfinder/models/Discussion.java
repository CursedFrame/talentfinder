package com.example.talentfinder.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Discussion")
public class Discussion extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_RECIPIENT = "recipient";
    public static final String KEY_MESSAGES = "messages";

    // GET/SET User
    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    // GET/SET Recipient
    public ParseUser getRecipient(){
        return getParseUser(KEY_RECIPIENT);
    }

    public void setRecipient(ParseUser recipient){
        put(KEY_RECIPIENT, recipient);
    }

    // GET/SET Messages
    public List<Message> getMessages(){
        return getList(KEY_MESSAGES);
    }

    public void setMessages(List<Message> messages){
        put(KEY_MESSAGES, messages);
    }
}
