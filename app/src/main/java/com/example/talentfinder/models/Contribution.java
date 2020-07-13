package com.example.talentfinder.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Contribution")
public class Contribution extends ParseObject {
    public static final String KEY_MEDIA = "media";
    public static final String KEY_USER_DESCRIPTION = "userDescription";
    public static final String KEY_SKILLS_DESCRIPTION = "skillsDescription";

    // GET/SET Media
    public ParseFile getMedia() {
        return getParseFile(KEY_MEDIA);
    }

    public void setMedia(ParseFile file){
        put(KEY_MEDIA, file);
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
}
