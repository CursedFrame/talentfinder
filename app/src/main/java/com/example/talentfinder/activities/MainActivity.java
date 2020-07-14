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
import com.example.talentfinder.fragments.CreateFragment;
import com.example.talentfinder.fragments.HomeFeedFragment;
import com.example.talentfinder.fragments.MessagesFragment;
import com.example.talentfinder.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationMain);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Menu menu = bottomNavigationView.getMenu();
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
                        fragment = new SearchFragment();
                        break;
                    case R.id.action_create_main:
                        fragment = new CreateFragment();
                        break;
                    case R.id.action_messages_main:
                        fragment = new MessagesFragment();
                        break;
                    default:
                        break;
                }

                fragmentManager.beginTransaction().replace(R.id.clContainer, fragment).commit();
                return true;
            }
        });
    }
}