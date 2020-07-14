package com.example.talentfinder.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.talentfinder.R;
import com.example.talentfinder.models.Project;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    List<String> strings;
    List<Project> projects;
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        strings = new ArrayList<>();
        projects = new ArrayList<>();

        ParseQuery<Project> query = ParseQuery.getQuery(Project.class);

        query.findInBackground(new FindCallback<Project>() {
            @Override
            public void done(List<Project> objects, ParseException e) {
                if (e != null){
                    Log.e("oh no", "Issue with getting posts", e);
                    return;
                }

                projects.addAll(objects);

                strings = projects.get(0).getTags();

                test = findViewById(R.id.texttest);

                String bigstring = "";

                for (String string : strings){
                    bigstring += string;
                }

                test.setText(bigstring);
            }
        });



    }
}