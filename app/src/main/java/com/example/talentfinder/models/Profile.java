package com.example.talentfinder.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Profile")
public class Profile extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_PROFILE_IMAGE = "profileImage";
    public static final String KEY_NAME = "name";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_FACEBOOK_CONNECTION_BOOL = "facebookConnection";
    public static final String KEY_CURRENT_PROJECTS = "currentProjects";
    public static final String KEY_SKILLS_EXPERIENCE = "skillsExperience";

    // GET/SET User
    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    // GET/SET Profile Image
    public ParseFile getProfileImage() {
        return getParseFile(KEY_PROFILE_IMAGE);
    }

    public void setProfileImage(ParseFile image) {
        put(KEY_PROFILE_IMAGE, image);
    }

    // GET/SET Name
    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    // GET/SET Location
    public String getLocation() {
        return getString(KEY_LOCATION);
    }

    public void setLocation(String location){
        put(KEY_LOCATION, location);
    }

    // GET/SET Facebook Connection Boolean
    public boolean getFacebookConnectionBoolean(){
        return getBoolean(KEY_FACEBOOK_CONNECTION_BOOL);
    }

    public void setFacebookConnectionBoolean(boolean bool){
        put(KEY_FACEBOOK_CONNECTION_BOOL, bool);
    }

    // GET/SET Current Projects for User
    public List<Project> getCurrentProjects(){
        return getList(KEY_CURRENT_PROJECTS);
    }

    public void setCurrentProjects(List<Project> projects){
        put(KEY_CURRENT_PROJECTS, projects);
    }

    // GET/SET Skills and Experience
    public List<String> getSkillsExperience() {
        return getList(KEY_SKILLS_EXPERIENCE);
    }

    public void setSkillsExperience(List<String> skillsExperience){
        put(KEY_SKILLS_EXPERIENCE, skillsExperience);
    }

}
