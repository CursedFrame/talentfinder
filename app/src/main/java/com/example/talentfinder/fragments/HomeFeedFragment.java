package com.example.talentfinder.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.talentfinder.activities.MainActivity;
import com.example.talentfinder.adapters.ProjectsAdapter;
import com.example.talentfinder.databinding.FragmentHomeFeedBinding;
import com.example.talentfinder.models.Project;
import com.google.android.material.button.MaterialButton;

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
    private MaterialButton btnTags;

    public HomeFeedFragment() {
        // Required empty public constructor
    }


    public static HomeFeedFragment newInstance(List<Project> projects) {
        HomeFeedFragment fragment = new HomeFeedFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("projects", (ArrayList<? extends Parcelable>) projects);
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

        // Get tags button reference
        MainActivity activity = (MainActivity) getActivity();
        this.btnTags = activity.btnTags;
        btnTags.setVisibility(View.VISIBLE);

        // Recycler view and adapter creation
        linearLayoutManager = new LinearLayoutManager(getContext());
        projects = new ArrayList<>();
        projects = getArguments().getParcelableArrayList("projects");
        projectsAdapter = new ProjectsAdapter(getContext(), projects, getFragmentManager());
        binding.fragmentHomeFeedRvHomeFeed.setAdapter(projectsAdapter);
        binding.fragmentHomeFeedRvHomeFeed.setLayoutManager(linearLayoutManager);

        // Add divider for RecyclerView
        dividerItemDecoration = new DividerItemDecoration(binding.fragmentHomeFeedRvHomeFeed.getContext(), linearLayoutManager.getOrientation());
        binding.fragmentHomeFeedRvHomeFeed.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        btnTags.setVisibility(View.GONE);
    }
}