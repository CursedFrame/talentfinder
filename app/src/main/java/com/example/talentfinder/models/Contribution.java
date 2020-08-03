package com.example.talentfinder.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Contribution")
public class Contribution extends ParseObject {
    public static final String KEY_MEDIA = "media";
    public static final String KEY_USER = "user";
    public static final String KEY_PROJECT = "project";
    public static final String KEY_USER_DESCRIPTION = "userDescription";
    public static final String KEY_SKILLS_DESCRIPTION = "skillsDescription";
    public static final String KEY_MEDIA_TYPE_CODE = "mediaTypeCode";
    public static final String KEY_PRIVATE_CONTRIBUTION = "privateContribution";

    // GET/SET Media
    public ParseFile getMedia() {
        return getParseFile(KEY_MEDIA);
    }

    public void setMedia(ParseFile file){
        put(KEY_MEDIA, file);
    }

    // GET/SET User
    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    // GET/SET Project
    public ParseObject getProject(){
        return getParseObject(KEY_PROJECT);
    }

    public void setProject(Project project){
        put(KEY_PROJECT, project);
    }

    // GET/SET User Description
    public String getUserDescription(){
        return getString(KEY_USER_DESCRIPTION);
    }

    public void setUserDescription(String description){
        put(KEY_USER_DESCRIPTION, description);
    }

    // GET/SET Skills Description
    public String getSkillsDescription(){
        return getString(KEY_SKILLS_DESCRIPTION);
    }

    public void setSkillsDescription(String description){
        put(KEY_SKILLS_DESCRIPTION, description);
    }

    // GET/SET Media Type Code
    public int getMediaTypeCode(){
        return getInt(KEY_MEDIA_TYPE_CODE);
    }

    public void setMediaTypeCode(int mediaTypeCode){
        put(KEY_MEDIA_TYPE_CODE, mediaTypeCode);
    }

    // GET/SET Contribution Privacy Bool
    public boolean getPrivateContributionBool(){
        return getBoolean(KEY_PRIVATE_CONTRIBUTION);
    }

    public void setPrivateContributionBool(boolean privateContributionBool){
        put(KEY_PRIVATE_CONTRIBUTION, privateContributionBool);
    }
}
