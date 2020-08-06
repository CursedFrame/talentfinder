package com.example.talentfinder.interfaces;

public interface GlobalConstants {
    // Limits
    public static final int PROJECT_LIMIT = 20;
    public static final int PROJECT_PER_TAG_LIMIT = 10;
    public static final int DISCUSSION_LIMIT = 20;
    public static final int USER_LIMIT = 20;

    // Request codes
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 101;
    public static final int PICK_PHOTO_CODE = 102;
    public static final int PICK_VIDEO_CODE = 103;

    // Chip type codes
    public static final int CHIP_ENTRY = 201;
    public static final int CHIP_CHOICE = 202;
    public static final int CHIP_FILTER = 203;
    public static final int CHIP_ACTION = 204;

    // Project type codes
    public static final int PROJECT_FULL = 301;
    public static final int PROJECT_PREVIEW = 302;

    // Media type codes
    public static final int MEDIA_PHOTO = 401;
    public static final int MEDIA_VIDEO = 402;

    // Talent types
    public static final int POSITION_TALENT_NO = 0;
    public static final int POSITION_TALENT_ART = 1;
    public static final int POSITION_TALENT_COMEDY = 2;
    public static final int POSITION_TALENT_DRAWING = 3;
    public static final int POSITION_TALENT_GRAPHICS = 4;
    public static final int POSITION_TALENT_MUSIC = 5;
    public static final int POSITION_TALENT_PHOTOGRAPHY = 6;
    public static final int POSITION_TALENT_PROGRAMMING = 7;
    public static final int POSITION_TALENT_SINGING = 8;
    public static final int POSITION_TALENT_TEACHING = 9;
    public static final int POSITION_TALENT_WRITING = 10;

    // Tag positions
    public static final int TAG_POSITION_TALENT = 0;
    public static final int TAG_POSITION_SUBTALENT = 1;
    public static final int TAG_POSITION_SKILL = 2;

    // Tag default labels
    public static final String TALENT_TAG = "Talent";
    public static final String SUBTALENT_TAG = "Subtalent";
    public static final String SKILL_TAG = "Skill";

    // Placeholder image URL
    public static final String PLACEHOLDER_URL = "https://generative-placeholders.glitch.me/image?width=600&height=300&style=triangles&gap=30";
}
