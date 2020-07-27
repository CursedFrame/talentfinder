package com.example.talentfinder.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.talentfinder.R;
import com.example.talentfinder.activities.LoginActivity;
import com.example.talentfinder.adapters.ProjectPreviewAdapter;
import com.example.talentfinder.databinding.FragmentProfileBinding;
import com.example.talentfinder.interfaces.ParseUserKey;
import com.example.talentfinder.models.Discussion;
import com.example.talentfinder.models.Project;
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

    private ParseUser user;
    private FragmentProfileBinding binding;
    private FragmentManager fragmentManager;
    private List<Project> projects;
    private LinearLayoutManager linearLayoutManager;
    private ProjectPreviewAdapter projectPreviewAdapter;
    private Discussion discussion = null;

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

        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");

        linearLayoutManager = new LinearLayoutManager(getContext());
        projects = new ArrayList<>();
        projectPreviewAdapter = new ProjectPreviewAdapter(getContext(), projects, fragmentManager);
        binding.fragmentProfileRvProjects.setAdapter(projectPreviewAdapter);
        binding.fragmentProfileRvProjects.setLayoutManager(linearLayoutManager);

        // Bind user name and location
        binding.fragmentProfileTvProfileName.setText(user.getString(ParseUserKey.PROFILE_NAME));
        if (user.getString(ParseUserKey.PROFILE_LOCATION) != null){
            binding.fragmentProfileTvProfileLocation.setText(user.getString(ParseUserKey.PROFILE_LOCATION));
        }

        // Bind user profile picture
        Glide.with(getContext())
                .load(user.getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl())
                .circleCrop()
                .into(binding.fragmentProfileIvProfilePicture);

        // If the profile user is the current user, show settings icon, but don't show "Start Discussion" button
        if (user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
            binding.fragmentProfileBtnDiscussion.setVisibility(View.GONE);
        }
        else {
            binding.fragmentProfileBtnSettings.setVisibility(View.GONE);
            checkDiscussion();
        }

        // On Settings button click, open drop down menu for settings
        binding.fragmentProfileBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSettingsPopUpMenu();
            }
        });

        // On "Start Discussion" button click, take user to the start discussion dialog fragment to start discussion with project creator
        binding.fragmentProfileBtnDiscussion.setOnClickListener(new View.OnClickListener() {
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

        getProjects();
    }

    private void getProjects(){
        ParseQuery<ParseObject> query = user.getRelation(ParseUserKey.CURRENT_PROJECTS).getQuery();
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
    private void createSettingsPopUpMenu(){
        PopupMenu popupMenu = new PopupMenu(getContext(), binding.fragmentProfileBtnSettings);
        popupMenu.getMenuInflater().inflate(R.menu.menu_settings, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_change_profile_photo:
                        goChangeProfilePhotoDialogFragment();
                        break;
                    case R.id.action_log_out:
                        ParseUser.logOut();
                        goLoginActivity();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        popupMenu.show();
    }

    private void checkDiscussion(){
        // Get discussion where "user" is the current user and "recipient" is the recipient user
        final ParseQuery<Discussion> query = ParseQuery.getQuery(Discussion.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereEqualTo("recipient", user);
        query.include(Discussion.KEY_USER);
        query.include(Discussion.KEY_RECIPIENT);
        query.getFirstInBackground(new GetCallback<Discussion>() {
            @Override
            public void done(Discussion object, ParseException e) {
                // If discussion does not exist, get discussion where "recipient" is the current user and "user" is the recipient user
                if (e != null){
                    query.whereEqualTo("recipient", ParseUser.getCurrentUser());
                    query.whereEqualTo("user", user);
                    query.include(Discussion.KEY_USER);
                    query.include(Discussion.KEY_RECIPIENT);
                    query.getFirstInBackground(new GetCallback<Discussion>() {
                        @Override
                        public void done(Discussion object, ParseException e) {
                            // If discussion does not exist between two users, allow current user to create a discussion
                            if (e != null){
                                String string = "Start Discussion";
                                binding.fragmentProfileBtnDiscussion.setText(string);
                                return;
                            }

                            String string = "Continue Discussion";
                            binding.fragmentProfileBtnDiscussion.setText(string);
                            discussion = object;
                        }
                    });
                    return;
                }
                String string = "Continue Discussion";
                binding.fragmentProfileBtnDiscussion.setText(string);
                discussion = object;
            }
        });
    }

    private void goLoginActivity(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
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