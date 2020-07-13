package com.example.talentfinder.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Project")
public class Project extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_TITLE = "title";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CONTRIBUTION_COUNT = "contributionCount";
    public static final String KEY_TAGS = "tags";


    // GET/SET User
    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    // GET/SET Title
    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    // GET/SET Project Image
    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    // GET/SET Contribution Count
    public Integer getContributionCount() {
        return getInt(KEY_CONTRIBUTION_COUNT);
    }

    public void setContributionCount(int count){
        put(KEY_CONTRIBUTION_COUNT, count);
    }

    // GET/SET Tags
    public List<String> getTags() {
        return getList(KEY_TAGS);
    }

    public void setTags(List<String> tags) {
        put(KEY_TAGS, tags);
    }
}
