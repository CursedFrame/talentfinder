package com.example.talentfinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.talentfinder.R;
import com.example.talentfinder.activities.MainActivity;
import com.example.talentfinder.adapters.ProjectsAdapter;
import com.example.talentfinder.adapters.UserAdapter;
import com.example.talentfinder.databinding.FragmentHomeFeedBinding;
import com.example.talentfinder.models.Project;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

// Fragment for showing the home feed with user created projects
public class HomeFeedFragment extends Fragment {

    public static final String TAG = "HomeFeedFragment";

    private LinearLayoutManager linearLayoutManager;
    private ProjectsAdapter projectsAdapter;
    private UserAdapter usersAdapter;
    private List<Project> projects;
    private List<ParseUser> users;
    private FragmentManager fragmentManager;
    private FragmentHomeFeedBinding binding;
    private DividerItemDecoration dividerItemDecoration;
    private ImageView btnTags;
    private MainActivity activity;
    private Context context;
    private boolean isClicked = false;

    public HomeFeedFragment() {
        // Required empty public constructor
    }


    public static HomeFeedFragment newInstance(List<Project> projects, List<ParseUser> users) {
        HomeFeedFragment fragment = new HomeFeedFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("projects", (ArrayList<? extends Parcelable>) projects);
        args.putParcelableArrayList("users", (ArrayList<? extends Parcelable>) users);
        fragment.setArguments(args);
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

        fragmentManager = getFragmentManager();
        context = getContext();

        // Get tags button reference
        activity = (MainActivity) getActivity();
        this.btnTags = activity.btnTags;
        btnTags.setVisibility(View.VISIBLE);

        // Recycler view and adapter creation for projects
        linearLayoutManager = new LinearLayoutManager(context);
        projects = new ArrayList<>();
        projects = getArguments().getParcelableArrayList("projects");
        projectsAdapter = new ProjectsAdapter(context, projects, getFragmentManager());
        binding.fragmentHomeFeedRvHomeFeed.setAdapter(projectsAdapter);
        binding.fragmentHomeFeedRvHomeFeed.setLayoutManager(linearLayoutManager);

        // Recycler view and adapter creation for users
        users = new ArrayList<>();
        users = getArguments().getParcelableArrayList("users");
        usersAdapter = new UserAdapter(context, users, getFragmentManager());

        // Add divider for RecyclerView
        dividerItemDecoration = new DividerItemDecoration(binding.fragmentHomeFeedRvHomeFeed.getContext(), linearLayoutManager.getOrientation());
        binding.fragmentHomeFeedRvHomeFeed.addItemDecoration(dividerItemDecoration);

        binding.fragmentHomeFeedFabHomeFeedSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked){
                    switchProjectAdapter();
                    isClicked = false;
                }
                else {
                    switchUserAdapter();
                    isClicked = true;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        btnTags.setVisibility(View.GONE);
    }

    // Used to update adapter when a new list of projects are introduced
    // Mainly used in Main Activity
    public void updateAdapter(List<Project> newProjects){
        projectsAdapter.refresh(newProjects);
    }

    public void switchUserAdapter(){
        binding.fragmentHomeFeedRvHomeFeed.setAdapter(usersAdapter);
        binding.fragmentHomeFeedRvHomeFeed.setLayoutManager(linearLayoutManager);
        binding.fragmentHomeFeedFabHomeFeedMain.setImageDrawable(context.getDrawable(R.drawable.ic_user));
    }

    public void switchProjectAdapter(){
        binding.fragmentHomeFeedRvHomeFeed.setAdapter(projectsAdapter);
        binding.fragmentHomeFeedRvHomeFeed.setLayoutManager(linearLayoutManager);
        binding.fragmentHomeFeedFabHomeFeedMain.setImageDrawable(context.getDrawable(R.drawable.ic_project));
    }

}