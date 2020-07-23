package com.example.talentfinder.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.talentfinder.R;
import com.example.talentfinder.databinding.ActivityMainBinding;
import com.example.talentfinder.fragments.CreateFragment;
import com.example.talentfinder.fragments.DirectMessagesFragment;
import com.example.talentfinder.fragments.HomeFeedFragment;
import com.example.talentfinder.fragments.ProfileFragment;
import com.example.talentfinder.fragments.SearchFragment;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.interfaces.ParseUserKey;
import com.example.talentfinder.models.Discussion;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private ActivityMainBinding binding;

    private ArrayList<Discussion> discussions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        discussions = new ArrayList<>();
        queryDiscussions();

        setOnClickBottomNavigationMain();
        binding.bottomNavigationMain.setSelectedItemId(R.id.action_home_main);
    }

    public void setOnClickBottomNavigationMain(){
        binding.bottomNavigationMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Menu menu = binding.bottomNavigationMain.getMenu();
//                menu.findItem(R.id.action_home_main);
//                menu.findItem(R.id.action_search_main);
//                menu.findItem(R.id.action_create_main);
//                menu.findItem(R.id.action_messages_main);

                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.action_home_main:
                        Log.i(TAG, "Moving to home feed fragment");
                        fragment = HomeFeedFragment.newInstance();
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

                fragmentManager.beginTransaction().disallowAddToBackStack().replace(R.id.activityMain_clContainer, fragment).commit();
                return true;
            }
        });
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

    public void queryDiscussions(){
        ParseQuery<ParseObject> query = ParseUser.getCurrentUser().getRelation(ParseUserKey.CURRENT_DISCUSSIONS).getQuery();
        query.setLimit(GlobalConstants.DISCUSSION_LIMIT);
        query.addDescendingOrder(Discussion.KEY_UPDATED_AT);
        query.include(Discussion.KEY_USER);
        query.include(Discussion.KEY_MESSAGES);
        query.include(Discussion.KEY_RECIPIENT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error loading discussions", e);
                    return;
                }

                for (ParseObject object : objects){
                    discussions.add((Discussion) object);
                }
            }
        });
    }
}