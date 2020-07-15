package com.example.talentfinder;

import android.app.Application;

import com.example.talentfinder.models.Contribution;
import com.example.talentfinder.models.Discussion;
import com.example.talentfinder.models.Message;
import com.example.talentfinder.models.Project;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Project.class);
        ParseObject.registerSubclass(Contribution.class);
        ParseObject.registerSubclass(Discussion.class);
        ParseObject.registerSubclass(Message.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("talent-finder-app") // should correspond to APP_ID env variable
                .clientKey("o8VALZ2OEnZdf4vs7acWYbQTWeqmqlIT")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://talent-finder-app.herokuapp.com/parse/").build());
    }
}