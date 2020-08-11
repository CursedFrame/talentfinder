package com.example.talentfinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.talentfinder.R;
import com.example.talentfinder.databinding.FragmentProjectBinding;
import com.example.talentfinder.models.Discussion;
import com.example.talentfinder.models.Project;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProjectFragment extends Fragment {

    public static final String TAG = "ProjectFragment";

    private Context context;
    private Project project;
    private FragmentProjectBinding binding;
    public FragmentManager fragmentManager;
    private Discussion discussion = null;

    public ProjectFragment() {
        // Required empty public constructor
    }

    public static ProjectFragment newInstance(Project project) {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();
        args.putParcelable("project", project);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProjectBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        fragmentManager = getFragmentManager();
        project = getArguments().getParcelable("project");

        binding.fragmentProjectTvProjectTitle.setText(project.getTitle());
        binding.fragmentProjectTvProjectCreatorName.setText(project.getUser().getName());
        binding.fragmentProjectTvContributions.setText(getString(R.string.contributions, project.getContributionCount()));
        binding.fragmentProjectTvDescription.setText(project.getDescription());

        if (project.getImage() != null){
            Glide.with(context)
                    .load(project.getImage().getUrl())
                    .into(binding.fragmentProjectIvContext);
        }

        Glide.with(context)
                .load(project.getUser().getImage().getUrl())
                .circleCrop()
                .into(binding.fragmentProjectIvProjectProfileImage);

        // If the project creator is the current user, don't show the "Start Discussion" button
        if (project.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
            binding.fragmentProjectBtnDiscussion.setVisibility(View.GONE);
            binding.fragmentProjectBtnCreate.setVisibility(View.GONE);
        }
        else {
            checkDiscussion();
        }

        // On "Start Discussion" button click, take user to the start discussion dialog fragment to start discussion with project creator
        binding.fragmentProjectBtnDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (discussion != null){
                    goDiscussionFragment();
                }
                else {
                    createDiscussionDialog();
                }
            }
        });

        // On "Create" button click, take user to the contribution create screen
        binding.fragmentProjectBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContributeFragment contributeFragment = ContributeFragment.newInstance(project);
                fragmentManager.beginTransaction().addToBackStack(contributeFragment.getTag()).replace(R.id.includeMainViewContainer_mainContainer, contributeFragment).commit();
            }
        });

    }

    private void checkDiscussion(){
        ParseQuery<Discussion> query1 = ParseQuery.getQuery(Discussion.class);
        query1.whereEqualTo("user", ParseUser.getCurrentUser());
        query1.whereEqualTo("recipient", project.getUser());

        ParseQuery<Discussion> query2 = ParseQuery.getQuery(Discussion.class);
        query2.whereEqualTo("recipient", ParseUser.getCurrentUser());
        query2.whereEqualTo("user", project.getUser());

        List<ParseQuery<Discussion>> queryList = new ArrayList<>();
        queryList.add(query1);
        queryList.add(query2);

        ParseQuery<Discussion> query = ParseQuery.or(queryList);
        query.include(Discussion.KEY_USER);
        query.include(Discussion.KEY_RECIPIENT);

        query.getFirstInBackground(new GetCallback<Discussion>() {
            @Override
            public void done(Discussion object, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error getting discussion", e);
                    return;
                }

                discussion = object;
            }
        });
    }

    private void goDiscussionFragment(){
        DiscussionFragment discussionFragment = DiscussionFragment.newInstance(discussion);
        fragmentManager.beginTransaction().addToBackStack(discussionFragment.getTag()).replace(R.id.includeMainViewContainer_mainContainer, discussionFragment).commit();
    }

    private void createDiscussionDialog(){
        StartDiscussionDialogFragment startDiscussionDialogFragment = StartDiscussionDialogFragment.newInstance(project.getUser());
        startDiscussionDialogFragment.show(fragmentManager, startDiscussionDialogFragment.getTag());
    }
}