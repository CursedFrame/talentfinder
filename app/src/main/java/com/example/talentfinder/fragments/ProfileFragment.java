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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.talentfinder.R;
import com.example.talentfinder.adapters.ChipAdapter;
import com.example.talentfinder.adapters.ProjectPreviewAdapter;
import com.example.talentfinder.databinding.FragmentProfileBinding;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.models.Discussion;
import com.example.talentfinder.models.Project;
import com.example.talentfinder.models.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    private User user;
    private FragmentProfileBinding binding;
    private FragmentManager fragmentManager;
    private List<Project> projects;
    private List<String> tags;
    private LinearLayoutManager projectsLinearLayoutManager, tagsLinearLayoutManager;
    private ProjectPreviewAdapter projectPreviewAdapter;
    private ChipAdapter tagsChipAdapter;
    private Discussion discussion = null;
    private Context context;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(ParseUser user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getFragmentManager();
        context = getContext();

        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");

        // Projects adapter and RecyclerView
        projectsLinearLayoutManager = new LinearLayoutManager(context);
        projects = new ArrayList<>();
        projectPreviewAdapter = new ProjectPreviewAdapter(context, projects, fragmentManager);
        binding.fragmentProfileRvProjects.setAdapter(projectPreviewAdapter);
        binding.fragmentProfileRvProjects.setLayoutManager(projectsLinearLayoutManager);

        // User skill and experience adapter and RecyclerView
        tagsLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        tags = new ArrayList<>();
        tags.add(user.getTalentTag());
        tags.add(user.getSubTalentTag());
        tags.add(user.getSkillTag());
        tagsChipAdapter = new ChipAdapter(context, tags, GlobalConstants.CHIP_FILTER);
        binding.fragmentProfileRvSkillsExperience.setAdapter(tagsChipAdapter);
        binding.fragmentProfileRvSkillsExperience.setLayoutManager(tagsLinearLayoutManager);

        // Bind user name and location
        binding.fragmentProfileTvProfileName.setText(user.getName());
        if (user.getLocation() != null){
            binding.fragmentProfileTvProfileLocation.setText(user.getLocation());
        }

        // Bind user profile picture
        Glide.with(context)
                .load(user.getImage().getUrl())
                .circleCrop()
                .into(binding.fragmentProfileIvProfilePicture);

        // Adjusting FloatingActionButtons based on if current user or not
        if (user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
            binding.fragmentProfileFabChangePicture.setVisibility(View.VISIBLE);
            setOnClickFabChangePicture();
        }
        else {
            binding.fragmentProfileFabDiscussion.setVisibility(View.VISIBLE);
            setOnClickFabDiscussion();
        }

        checkDiscussion();
        populateTypeUser();
        getProjects();
    }

    private void getProjects(){
        ParseQuery<ParseObject> query = user.getProjects().getQuery();
        query.orderByAscending(Project.KEY_CREATED_AT);
        query.include(Project.KEY_USER);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error getting user projects", e);
                    return;
                }

                for (ParseObject object : objects){
                    projects.add((Project) object);
                }
                projectPreviewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setOnClickFabChangePicture(){
        binding.fragmentProfileFabChangePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goChangeProfilePhotoDialogFragment();
            }
        });
    }

    private void setOnClickFabDiscussion(){
        // On "Start Discussion" button click, take user to the start discussion dialog fragment to start discussion with project creator
        binding.fragmentProfileFabDiscussion.setOnClickListener(new View.OnClickListener() {
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
    }

    private void checkDiscussion(){
        ParseQuery<Discussion> query1 = ParseQuery.getQuery(Discussion.class);
        query1.whereEqualTo("user", ParseUser.getCurrentUser());
        query1.whereEqualTo("recipient", user);

        ParseQuery<Discussion> query2 = ParseQuery.getQuery(Discussion.class);
        query2.whereEqualTo("recipient", ParseUser.getCurrentUser());
        query2.whereEqualTo("user", user);

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
                    Log.e(TAG, "Did not find discussion with this user");
                    return;
                }
                discussion = object;
            }
        });
    }

    private void populateTypeUser(){
        // If the profile user is a Facebook user, show Facebook connection widget
        if (user.getFacebookConnected()){
            getFacebookPhoto();
            binding.include.connectionFacebookProfileName.setText(user.getName());
        }
        else {
            binding.include.connectionFacebookClContainer.setVisibility(View.GONE);
        }
    }

    private void getFacebookPhoto(){
        String facebookUrl = "https://www.graph.facebook.com/" + user.getFacebookId() + "/picture?type=large";
        Glide.with(context)
                .load(facebookUrl)
                .into(binding.include.connectionFacebookIvProfileImage);

    }

    private void goChangeProfilePhotoDialogFragment(){
        ChangeProfilePhotoDialogFragment changeProfilePhotoDialogFragment = ChangeProfilePhotoDialogFragment.newInstance();
        changeProfilePhotoDialogFragment.show(fragmentManager, changeProfilePhotoDialogFragment.getTag());
    }

    private void goDiscussionFragment(){
        DiscussionFragment discussionFragment = DiscussionFragment.newInstance(discussion);
        fragmentManager.beginTransaction().addToBackStack(discussionFragment.getTag()).replace(R.id.includeMainViewContainer_mainContainer, discussionFragment).commit();
    }

    private void createDiscussionDialog(){
        StartDiscussionDialogFragment startDiscussionDialogFragment = StartDiscussionDialogFragment.newInstance(user);
        startDiscussionDialogFragment.show(fragmentManager, startDiscussionDialogFragment.getTag());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}