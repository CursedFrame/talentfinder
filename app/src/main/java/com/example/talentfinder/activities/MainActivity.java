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
import com.example.talentfinder.fragments.HomeFeedFragment;
import com.example.talentfinder.fragments.MessagesFragment;
import com.example.talentfinder.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            ParseUser.logIn("dave", "dave");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.bottomNavigationMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Menu menu = activityMainBinding.bottomNavigationMain.getMenu();
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
                        fragment = MessagesFragment.newInstance();
                        break;
                    default:
                        break;
                }

                fragmentManager.beginTransaction().replace(R.id.clContainer, fragment).commit();
                return true;
            }
        });
        activityMainBinding.bottomNavigationMain.setSelectedItemId(R.id.action_home_main);
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
}