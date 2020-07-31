package com.example.talentfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.talentfinder.R;
import com.example.talentfinder.fragments.CreateFragment;
import com.example.talentfinder.fragments.DirectMessagesFragment;
import com.example.talentfinder.fragments.HomeFeedFragment;
import com.example.talentfinder.fragments.ProfileFragment;
import com.example.talentfinder.fragments.SearchFragment;
import com.example.talentfinder.fragments.TagsDialogFragment;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.models.Discussion;
import com.example.talentfinder.models.Project;
import com.example.talentfinder.utilities.TagUtils;
import com.facebook.login.LoginManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    public List<Discussion> discussions;
    public List<Project> projects;
    private DrawerLayout drawerLayout;
    public MaterialButton btnTags;
    private NavigationView navigationView;
    public List<String> tags = new ArrayList<>(3);
    public boolean tagsSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        btnTags = findViewById(R.id.includeMainViewContainer_tagsbutton);

        // Queries discussions and projects
        queryObjects();

        Toolbar toolbar = findViewById(R.id.includeMainViewContainer_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.activityMain_drawer_layout);

        navigationView = findViewById(R.id.activityMain_main_navigation);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        btnTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagsDialogFragment tagsDialogFragment = TagsDialogFragment.newInstance(tags);
                tagsDialogFragment.show(fragmentManager, tagsDialogFragment.getTag());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                Log.i(TAG, "onOptionsItemSelected: logging");
                return true;
            case R.id.action_log_out:
                ParseUser.logOut();
                LoginManager.getInstance().logOut();
                goLoginActivity();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if (fragmentManager.getBackStackEntryCount() > 0){
            Log.i(TAG, "Popping backstack");
            fragmentManager.popBackStackImmediate();
        }
        else {
            super.onBackPressed();
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        Fragment fragment = null;
                        switch (menuItem.getItemId()){
                            case R.id.action_home_main:
                                Log.i(TAG, "Moving to home feed fragment");
                                fragment = HomeFeedFragment.newInstance(projects);
                                break;
                            case R.id.action_search_main:
                                Log.i(TAG, "Moving to search fragment");
                                fragment = SearchFragment.newInstance();
                                break;
                            case R.id.action_create_main:
                                Log.i(TAG, "Moving to create fragment");
                                fragment = CreateFragment.newInstance();
                                break;
                            case R.id.action_messages_main:
                                Log.i(TAG, "Moving to messages fragment");
                                fragment = DirectMessagesFragment.newInstance(discussions);
                                break;
                            case R.id.action_profile:
                                Log.i(TAG, "Moving to profile fragment");
                                fragment = ProfileFragment.newInstance(ParseUser.getCurrentUser());
                                break;
                            default:
                                break;
                        }

                        fragmentManager.beginTransaction().disallowAddToBackStack().replace(R.id.includeMainViewContainer_mainContainer, fragment, "Main").commit();
                        return true;
                    }
                });
    }

    public void setInitialFragment(){
        Fragment initialFragment = HomeFeedFragment.newInstance(projects);
        fragmentManager.beginTransaction().disallowAddToBackStack().replace(R.id.includeMainViewContainer_mainContainer, initialFragment, "Main").commit();
        navigationView.setCheckedItem(R.id.action_home_main);
        btnTags.setVisibility(View.VISIBLE);
    }

    public void queryObjects(){
        queryDiscussions();

        queryProjects();
    }

    public void queryDiscussions(){
        discussions = new ArrayList<>();

        ParseQuery<Discussion> query1 = ParseQuery.getQuery(Discussion.class);
        query1.whereEqualTo(Discussion.KEY_USER, ParseUser.getCurrentUser());

        ParseQuery<Discussion> query2 = ParseQuery.getQuery(Discussion.class);
        query2.whereEqualTo(Discussion.KEY_RECIPIENT, ParseUser.getCurrentUser());

        List<ParseQuery<Discussion>> queryList = new ArrayList<>();
        queryList.add(query1);
        queryList.add(query2);

        ParseQuery<Discussion> query = ParseQuery.or(queryList);
        query.setLimit(GlobalConstants.DISCUSSION_LIMIT);
        query.addDescendingOrder(Discussion.KEY_UPDATED_AT);
        query.include(Discussion.KEY_USER);
        query.include(Discussion.KEY_MESSAGES);
        query.include(Discussion.KEY_RECIPIENT);
        query.findInBackground(new FindCallback<Discussion>() {
            @Override
            public void done(List<Discussion> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error loading discussions", e);
                    return;
                }

                Log.i(TAG, "done: "+ objects.size());

                for (Discussion discussion : objects){
                    Log.i(TAG, "done: "+ discussion.getRecipient().getObjectId());
                }

                discussions.addAll(objects);
            }
        });
    }

    public void queryProjects(){
        projects = new ArrayList<>();

        Log.i(TAG, "Querying projects");
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
        query.setLimit(GlobalConstants.PROJECT_LIMIT);
        query.addDescendingOrder(Project.KEY_CREATED_AT);
        query.include(Project.KEY_USER);
        query.findInBackground(new FindCallback<Project>() {
            @Override
            public void done(List<Project> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error loading projects", e);
                    return;
                }

                // Sets the initial fragment (home feed) after projects
                // Array list has been populated
                setInitialFragment();

                projects.addAll(objects);
            }
        });
    }

    public void sortProjectsByTag(){
        for (Project project : projects){
            project.setProjectWeight(TagUtils.getWeight(project, tags, this));
            Log.i(TAG, "sortProjectsByTag: "+  project.getProjectWeight());
        }

        Collections.sort(projects, new Comparator<Project>() {
            @Override
            public int compare(Project o1, Project o2) {
                return o1.compareTo(o2);
            }
        });

        // Refresh fragment to update RecyclerView data
        HomeFeedFragment currentHomeFragment = (HomeFeedFragment) fragmentManager.findFragmentByTag("Main");
        currentHomeFragment.updateAdapter(projects);
    }

    public void filterProjectsByTag(){
        projects = new ArrayList<>();
        loadProjectsByTalentTag();
    }

    private void loadProjectsByTalentTag(){
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
        query.setLimit(GlobalConstants.PROJECT_PER_TAG_LIMIT);
        query.include(Project.KEY_USER);
        query.whereContains(Project.KEY_TAG_TALENT, tags.get(GlobalConstants.TAG_POSITION_TALENT));
        query.findInBackground(new FindCallback<Project>() {
            @Override
            public void done(List<Project> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error loading talent projects", e);
                    return;
                }
                projects.addAll(objects);
                loadProjectsBySubtalentTag();
            }
        });
    }

    private void loadProjectsBySubtalentTag(){
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
        query.setLimit(GlobalConstants.PROJECT_PER_TAG_LIMIT);
        query.include(Project.KEY_USER);
        query.whereContains(Project.KEY_TAG_SUBTALENT, tags.get(GlobalConstants.TAG_POSITION_SUBTALENT));
        query.whereNotEqualTo(Project.KEY_TAG_TALENT, tags.get(GlobalConstants.TAG_POSITION_TALENT));
        query.findInBackground(new FindCallback<Project>() {
            @Override
            public void done(List<Project> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error loading subtalent projects", e);
                    return;
                }
                projects.addAll(objects);
                loadProjectsBySkillTag();
            }
        });
    }

    private void loadProjectsBySkillTag(){
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
        query.setLimit(GlobalConstants.PROJECT_PER_TAG_LIMIT);
        query.include(Project.KEY_USER);
        query.whereContains(Project.KEY_TAG_SKILL, tags.get(GlobalConstants.TAG_POSITION_SKILL));
        query.whereNotEqualTo(Project.KEY_TAG_SUBTALENT, tags.get(GlobalConstants.TAG_POSITION_SUBTALENT));
        query.whereNotEqualTo(Project.KEY_TAG_TALENT, tags.get(GlobalConstants.TAG_POSITION_TALENT));
        query.findInBackground(new FindCallback<Project>() {
            @Override
            public void done(List<Project> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error loading skill projects", e);
                    return;
                }
                projects.addAll(objects);
                sortProjectsByTag();
            }
        });
    }

    private void goLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}