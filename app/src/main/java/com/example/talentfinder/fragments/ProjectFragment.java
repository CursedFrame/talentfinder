package com.example.talentfinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.talentfinder.R;
import com.example.talentfinder.databinding.FragmentProjectBinding;
import com.example.talentfinder.interfaces.ParseUserKey;
import com.example.talentfinder.models.Discussion;
import com.example.talentfinder.models.Project;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProjectFragment extends Fragment {

    public static final String TAG = "ProjectFragment";

    private Context context;
    private Project project;
    private FragmentProjectBinding binding;
    public FragmentManager fragmentManager;

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
        binding.fragmentProjectTvProjectCreatorName.setText(project.getUser().getString(ParseUserKey.PROFILE_NAME));
        binding.fragmentProjectTvContributions.setText(getString(R.string.contributions, project.getContributionCount()));
        binding.fragmentProjectTvDescription.setText(project.getDescription());

        if (project.getImage() != null){
            Glide.with(context)
                    .load(project.getImage().getUrl())
                    .into(binding.fragmentProjectIvProjectContextImage);
        }

        Glide.with(context)
                .load(project.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl())
                .into(binding.fragmentProjectIvProjectProfileImage);

        // If the project creator is the current user, don't show the "Start Discussion" button
        if (project.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
            binding.fragmentProjectBtnStartDiscussion.setVisibility(View.GONE);
        }

        // On "Start Discussion" button click, take user to the start discussion dialog fragment to start discussion with project creator
        binding.fragmentProjectBtnStartDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get discussion where "user" is the current user and "recipient" is the recipient user

                final ParseQuery<Discussion> query = ParseQuery.getQuery(Discussion.class);
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.whereEqualTo("recipient", project.getUser());
                query.getFirstInBackground(new GetCallback<Discussion>() {
                    @Override
                    public void done(Discussion object, ParseException e) {
                        // If discussion does not exist, get discussion where "recipient" is the current user and "user" is the recipient user

                        if (e != null){
                            query.whereEqualTo("recipient", ParseUser.getCurrentUser());
                            query.whereEqualTo("user", project.getUser());
                            query.getFirstInBackground(new GetCallback<Discussion>() {
                                @Override
                                public void done(Discussion object, ParseException e) {
                                    // If discussion does not exist between two users, allow current user to create a discussion

                                    if (e != null){
                                        StartDiscussionDialogFragment startDiscussionDialogFragment = StartDiscussionDialogFragment.newInstance(project.getUser());
                                        startDiscussionDialogFragment.show(fragmentManager, startDiscussionDialogFragment.getTag());
                                        return;
                                    }

                                    Toast.makeText(context, "You have already started a dicussion with this person.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });

                            return;
                        }

                        Toast.makeText(context, "You have already started a dicussion with this person.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        });

        // On "Create" button click, take user to the contribution create screen
        binding.fragmentProjectBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContributeFragment contributeFragment = ContributeFragment.newInstance(project);
                fragmentManager.beginTransaction().addToBackStack(contributeFragment.getTag()).replace(R.id.activityMain_clContainer, contributeFragment).commit();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}