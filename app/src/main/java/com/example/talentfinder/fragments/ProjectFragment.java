package com.example.talentfinder.fragments;

import android.content.Context;
import android.os.Bundle;
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
import com.example.talentfinder.interfaces.Key_ParseUser;
import com.example.talentfinder.models.Project;

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

        binding.tvProjectTitle.setText(project.getTitle());
        binding.tvProjectCreatorName.setText(project.getUser().getString(Key_ParseUser.PROFILE_NAME));
        binding.tvContributions.setText(getString(R.string.contributions, project.getContributionCount()));
        binding.tvDescription.setText(project.getDescription());

        if (project.getImage() != null){
            Glide.with(context)
                    .load(project.getImage().getUrl())
                    .into(binding.ivOptionalContext);
        }

        Glide.with(context)
                .load(project.getUser().getParseFile(Key_ParseUser.PROFILE_IMAGE).getUrl())
                .into(binding.ivProjectProfileImage);

        // On "Start Discussion" button click, take user to the start discussion dialog fragment to start discussion with project creator
        binding.btnStartDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartDiscussionDialogFragment startDiscussionDialogFragment = StartDiscussionDialogFragment.newInstance(project.getUser());
                fragmentManager.beginTransaction().addToBackStack(startDiscussionDialogFragment.getTag()).replace(R.id.clContainer, startDiscussionDialogFragment).commit();
            }
        });

        // On "Create" button click, take user to the contribution create screen
        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContributeFragment contributeFragment = ContributeFragment.newInstance(project);
                fragmentManager.beginTransaction().addToBackStack(contributeFragment.getTag()).replace(R.id.clContainer, contributeFragment).commit();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}