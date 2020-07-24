package com.example.talentfinder.utilities;

import android.content.Context;
import android.content.res.Resources;

import com.example.talentfinder.R;
import com.example.talentfinder.interfaces.ParseUserKey;
import com.example.talentfinder.models.Project;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagUtils {

    private Context context;

    private double weight;
    private boolean isTalent;
    private String tagTalent;

    public TagUtils(Context context) {
        this.context = context;
    }

    public double getWeight(Project project, List<String> specifiedTags){
        // Gets tags in order of Talent, Subtalent, and Skill
        List<String> tags = project.getTags();
        weight = 0;
        isTalent = false;

        // Populate tag String List with only compared tags
        tags = getComparedTagList(tags, specifiedTags);

        // If no tags are similar, return 0 weight
        if (tags.size() == 0){
            return 0;
        }

        calculateWeight(tags);

        return weight;
    }

    public double getWeight(ParseUser user, List<String> specifiedTags){
        // Gets tags in order of Talent, Subtalent, and Skill
        List<String> tags = new ArrayList<>();
        tags.add(user.getString(ParseUserKey.TAG_TALENT));
        tags.add(user.getString(ParseUserKey.TAG_SUBTALENT));
        tags.add(user.getString(ParseUserKey.TAG_SKILL));

        weight = 0;
        isTalent = false;

        // Populate tag String List with only compared tags
        tags = getComparedTagList(tags, specifiedTags);

        // If no tags are similar, return 0 weight
        if (tags.size() == 0){
            return 0;
        }

        calculateWeight(tags);

        return weight;
    }

    private static List<String> getComparedTagList(List<String> projectTags, List<String> specifiedTags){
        List<String> comparedTags = new ArrayList<>();

        compare_tags_loop:
        for (int i = 0 ; i < projectTags.size() ; i++){
            for (int j = 0 ; j < specifiedTags.size() ; i++){
                if (comparedTags.size() == 3){
                    break compare_tags_loop;
                }
                if (projectTags.get(i).equals(specifiedTags.get(j))){
                    comparedTags.add(projectTags.get(i));
                }
            }
        }

        return comparedTags;
    }

    private void calculateWeight(List<String> tags){

        Resources resources = context.getResources();

        // First check for talent, if chosen by user, will always be the first tag in the String list
        List<String> talents = Arrays.asList(resources.getStringArray(R.array.talent));

        compareTalent:
        for (int i = 0 ; i < talents.size() ; i++){
            if (talents.get(i).equals(tags.get(0))){
                isTalent = true;
                weight += 1.0;
                tagTalent = talents.get(i);
                break compareTalent;
            }
        }

        // Second check for subtalent
        if (isTalent) {
            List<String> subtalents = null;
            switch (tagTalent) {
                case "Art":
                    subtalents = Arrays.asList(resources.getStringArray(R.array.art_subs));
                    break;
                case "Comedy":
                    subtalents = Arrays.asList(resources.getStringArray(R.array.comedy_subs));
                    break;
                case "Drawing":
                    subtalents = Arrays.asList(resources.getStringArray(R.array.drawing_subs));
                    break;
                case "Graphics":
                    subtalents = Arrays.asList(resources.getStringArray(R.array.graphics_subs));
                    break;
                case "Music":
                    subtalents = Arrays.asList(resources.getStringArray(R.array.music_subs));
                    break;
                case "Photography":
                    subtalents = Arrays.asList(resources.getStringArray(R.array.photography_subs));
                    break;
                case "Programming":
                    subtalents = Arrays.asList(resources.getStringArray(R.array.programming_subs));
                    break;
                case "Singing":
                    subtalents = Arrays.asList(resources.getStringArray(R.array.singing_subs));
                    break;
                case "Teaching":
                    subtalents = Arrays.asList(resources.getStringArray(R.array.teaching_subs));
                    break;
                case "Writing":
                    subtalents = Arrays.asList(resources.getStringArray(R.array.writing_subs));
                    break;
                default:
                    break;
            }

            for (int i = 0 ; i < subtalents.size() ; i++){
                if (subtalents.get(i).equals(tags.get(1))){
                    weight += 1.0;
                    break;
                }
            }
        }

        // Third check for skill
        List<String> skills = Arrays.asList(resources.getStringArray(R.array.skill));
        if (isTalent){
            for (int i = 0 ; i < skills.size() ; i++){
                if (skills.get(i).equals(tags.get(2))){
                    weight += 1.0;
                    break;
                }
            }
        }
        else {
            for (int i = 0 ; i < skills.size() ; i++){
                if (skills.get(i).equals(tags.get(0))){
                    weight += 1.0;
                    break;
                }
            }
        }
    }
}
