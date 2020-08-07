package com.example.talentfinder.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_MESSAGE_CONTENT = "messageContent";

    // GET/SET User
    public User getUser(){
        return (User) getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    // GET/SET Message Content
    public String getMessageContent(){
        return getString(KEY_MESSAGE_CONTENT);
    }

    public void setMessageContent(String messageContent){
        put(KEY_MESSAGE_CONTENT, messageContent);
    }
}

