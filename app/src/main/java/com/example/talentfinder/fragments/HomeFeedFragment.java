package com.example.talentfinder.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talentfinder.adapters.HomeFeedProjectsAdapter;
import com.example.talentfinder.R;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.models.Project;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

// Fragment for showing the home feed with user created projects
public class HomeFeedFragment extends Fragment {

    public static final String TAG = "HomeFeedFragment";

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    HomeFeedProjectsAdapter projectsAdapter;
    List<Project> projects;

    public HomeFeedFragment() {
        // Required empty public constructor
    }


    public static HomeFeedFragment newInstance() {
        HomeFeedFragment fragment = new HomeFeedFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recycler view and adapter creation
        recyclerView = view.findViewById(R.id.rvHomeFeed);
        linearLayoutManager = new LinearLayoutManager(getContext());
        projects = new ArrayList<>();
        projectsAdapter = new HomeFeedProjectsAdapter(getContext(), projects, getFragmentManager());
        recyclerView.setAdapter(projectsAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        queryProjects();
    }

    public void queryProjects(){
        Log.i(TAG, "Querying projects");
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
        query.setLimit(GlobalConstants.PROJECT_LIMIT);
        query.addDescendingOrder(Project.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Project>() {
            @Override
            public void done(List<Project> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error loading projects", e);
                    return;
                }

                projects.addAll(objects);
                projectsAdapter.notifyDataSetChanged();
            }
        });
    }
}