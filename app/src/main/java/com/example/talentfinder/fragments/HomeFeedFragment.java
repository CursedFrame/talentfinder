package com.example.talentfinder.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.talentfinder.adapters.ProjectsAdapter;
import com.example.talentfinder.databinding.FragmentHomeFeedBinding;
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

    private LinearLayoutManager linearLayoutManager;
    private ProjectsAdapter projectsAdapter;
    private List<Project> projects;
    private FragmentHomeFeedBinding binding;
    private DividerItemDecoration dividerItemDecoration;

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
        binding = FragmentHomeFeedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recycler view and adapter creation
        linearLayoutManager = new LinearLayoutManager(getContext());
        projects = new ArrayList<>();
        projectsAdapter = new ProjectsAdapter(getContext(), projects, getFragmentManager());
        binding.rvHomeFeed.setAdapter(projectsAdapter);
        binding.rvHomeFeed.setLayoutManager(linearLayoutManager);

        // Add divider for RecyclerView
        dividerItemDecoration = new DividerItemDecoration(binding.rvHomeFeed.getContext(), linearLayoutManager.getOrientation());
        binding.rvHomeFeed.addItemDecoration(dividerItemDecoration);

        queryProjects();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void queryProjects(){
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

                projects.addAll(objects);
                projectsAdapter.notifyDataSetChanged();
            }
        });
    }
}