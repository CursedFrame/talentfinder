package com.example.talentfinder.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Project")
public class Project extends ParseObject implements Comparable<Project>{
    public static final String KEY_USER = "user";
    public static final String KEY_TITLE = "title";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CONTRIBUTION_COUNT = "contributionCount";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CONTRIBUTIONS = "contributions";
    public static final String KEY_TAG_SKILL = "tagSkill";
    public static final String KEY_TAG_TALENT = "tagTalent";
    public static final String KEY_TAG_SUBTALENT = "tagSubTalent";

    private int projectWeight;

    // Unique methods
    public List<String> getTags(){
        List<String> tags = new ArrayList<>();

        tags.add(getTalentTag());
        tags.add(getSubTalentTag());
        tags.add(getSkillTag());

        return tags;
    }

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

    // GET/SET Description
    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    // GET/SET Skill Tag
    public String getSkillTag(){
        return getString(KEY_TAG_SKILL);
    }

    public void setSkillTag(String skill){
        put(KEY_TAG_SKILL, skill);
    }

    // GET/SET Talent Tag
    public String getTalentTag(){
        return getString(KEY_TAG_TALENT);
    }

    public void setTalentTag(String talent){
        put(KEY_TAG_TALENT, talent);
    }

    // GET/SET Subtalent Tag
    public String getSubTalentTag(){
        return getString(KEY_TAG_SUBTALENT);
    }

    public void setSubTalentTag(String subtalent){
        put(KEY_TAG_SUBTALENT, subtalent);
    }

    // GET/SET Project weight
    public int getProjectWeight(){
        return projectWeight;
    }

    public void setProjectWeight(int projectWeight){
        this.projectWeight = projectWeight;
    }

    @Override
    public int compareTo(Project other) {
        return (other.getProjectWeight() - this.getProjectWeight());
    }
}
