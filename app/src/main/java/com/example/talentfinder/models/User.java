package com.example.talentfinder.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;

@ParseClassName("User")
public class User extends ParseObject {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_IMAGE = "profileImage";
    public static final String KEY_NAME = "name";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_TAG_SKILL = "tagSkill";
    public static final String KEY_TAG_TALENT = "tagTalent";
    public static final String KEY_TAG_SUBTALENT = "tagSubTalent";
    public static final String KEY_FACEBOOK_LINK = "facebookLink";
    public static final String KEY_FACEBOOK_CONNECTED = "facebookConnected";
    public static final String KEY_FACEBOOK_ID = "facebookId";

    public static final String KEY_DISCUSSIONS = "currentDiscussions";
    public static final String KEY_PROJECTS = "currentProjects";

    private int userWeight;

    // GET User username
    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    // GET/SET Profile image
    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    // GET/SET Profile name
    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name){
        put(KEY_NAME, name);
    }

    // GET/SET User location
    public String getLocation() {
        return getString(KEY_LOCATION);
    }

    public void setLocation(String location){
        put(KEY_LOCATION, location);
    }

    // GET/SET Skill tag
    public String getTagSkill() {
        return getString(KEY_TAG_SKILL);
    }

    public void setTagSkill(String skill){
        put(KEY_TAG_SKILL, skill);
    }

    // GET/SET Talent tag
    public String getTagTalent() {
        return getString(KEY_TAG_TALENT);
    }

    public void setTagTalent(String talent){
        put(KEY_TAG_TALENT, talent);
    }

    // GET/SET Subtalent tag
    public String getTagSubtalent() {
        return getString(KEY_TAG_SUBTALENT);
    }

    public void setTagSubtalent(String subtalent){
        put(KEY_TAG_SUBTALENT, subtalent);
    }

    // GET/SET Facebook link
    public String getFacebookLink() {
        return getString(KEY_FACEBOOK_LINK);
    }

    public void setFacebookLink(String facebookLink){
        put(KEY_FACEBOOK_LINK, facebookLink);
    }

    // GET/SET Facebook connected boolean
    public boolean getFacebookConnected() {
        return getBoolean(KEY_FACEBOOK_CONNECTED);
    }

    public void setFacebookConnected(boolean facebookConnected){
        put(KEY_FACEBOOK_CONNECTED, facebookConnected);
    }

    // GET/SET Facebook Id
    public String getFacebookId() {
        return getString(KEY_FACEBOOK_ID);
    }

    public void setFacebookId(String facebookId){
        put(KEY_FACEBOOK_ID, facebookId);
    }

    // GET/SET User discussions
    public ParseRelation<Discussion> getDiscussions(){
        return getRelation(KEY_DISCUSSIONS);
    }

    // GET/SET User projects
    public ParseRelation<Project> getProjects(){
        return getRelation(KEY_PROJECTS);
    }
}
