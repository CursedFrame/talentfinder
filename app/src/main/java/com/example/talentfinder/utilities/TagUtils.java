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

public abstract class TagUtils {

    public static int getWeight(Project project, List<String> specifiedTags, Context context){
        // Gets tags in order of Talent, Subtalent, and Skill
        List<String> tags = project.getTags();
        int weight = 0;
        boolean isTalent = false;

        // Populate tag String List with only compared tags
        tags = getComparedTagList(tags, specifiedTags);

        // If no tags are similar, return 0 weight
        if (tags.size() == 0){
            return 0;
        }

        return calculateWeight(tags, weight, isTalent, context);
    }

    public static int getWeight(ParseUser user, List<String> specifiedTags, Context context){
        // Gets tags in order of Talent, Subtalent, and Skill
        List<String> tags = new ArrayList<>();
        tags.add(user.getString(ParseUserKey.TAG_TALENT));
        tags.add(user.getString(ParseUserKey.TAG_SUBTALENT));
        tags.add(user.getString(ParseUserKey.TAG_SKILL));

        int weight = 0;
        boolean isTalent = false;

        // Populate tag String List with only compared tags
        tags = getComparedTagList(tags, specifiedTags);

        // If no tags are similar, return 0 weight
        if (tags.size() == 0){
            return 0;
        }

        weight = calculateWeight(tags, weight, isTalent, context);

        return weight;
    }

    private static List<String> getComparedTagList(List<String> projectTags, List<String> specifiedTags){
        List<String> comparedTags = new ArrayList<>();

        compare_tags_loop:
        for (int i = 0 ; i < projectTags.size() ; i++){
            for (int j = 0 ; j < specifiedTags.size() ; j++){
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

    private static int calculateWeight(List<String> tags, int weight, boolean isTalent, Context context){

        Resources resources = context.getResources();
        String tagTalent = "";

        // First check for talent, if chosen by user, will always be the first tag in the String list
        List<String> talents = Arrays.asList(resources.getStringArray(R.array.talent));
        for (String talent : talents){
            for (String tag : tags) {
                if (talent.equals(tag)) {
                    isTalent = true;
                    weight += 1.0;
                    tagTalent = talent;
                    break;
                }
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

            compare_subtalent_loop:
            for (String subtalent : subtalents){
                for (String tag : tags) {
                    if (tag.equals("Subtalent")){
                        break compare_subtalent_loop;
                    }

                    if (subtalent.equals(tag)) {
                        weight += 1.0;
                        break compare_subtalent_loop;
                    }
                }
            }
        }

        // Third check for skill
        List<String> skills = Arrays.asList(resources.getStringArray(R.array.skill));

        compare_skill_loop:
        for (String skill : skills){
            for (String tag : tags){
                if (tag.equals("Skill")){
                    break compare_skill_loop;
                }

                if (skill.equals(tag)){
                    weight += 1.0;
                    break compare_skill_loop;
                }
            }
        }

        return weight;
    }
}
