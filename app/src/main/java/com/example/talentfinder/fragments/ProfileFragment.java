package com.example.talentfinder.fragments;

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

        // Projects adapter and RecyclerView
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



        if (user == ParseUser.getCurrentUser()){
            // On profile picture click, open drop down menu for change profile picture
            binding.fragmentProfileIvProfilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createChangeProfilePicturePopUpMenu();
                }
            });
        }

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

        populateTypeUser();

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
    private void createChangeProfilePicturePopUpMenu(){
        PopupMenu popupMenu = new PopupMenu(getContext(), binding.fragmentProfileIvProfilePicture);
        popupMenu.getMenuInflater().inflate(R.menu.menu_profile_picture, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_change_profile_photo:
                        goChangeProfilePhotoDialogFragment();
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
                    String string = "Start Discussion";
                    binding.fragmentProfileBtnDiscussion.setText(string);
                    binding.fragmentProfileBtnDiscussion.setVisibility(View.VISIBLE);
                    return;
                }

                String string = "Continue Discussion";
                binding.fragmentProfileBtnDiscussion.setText(string);
                binding.fragmentProfileBtnDiscussion.setVisibility(View.VISIBLE);
                discussion = object;
            }
        });
    }

    private void populateTypeUser(){
        // If the profile user is the current user, show settings icon, but don't show "Start Discussion" button
        if (user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
            binding.fragmentProfileBtnDiscussion.setVisibility(View.INVISIBLE);
        }
        else {
            checkDiscussion();
        }

        // If the profile user is a Facebook user, show Facebook connection widget
        if (user.getBoolean(ParseUserKey.FACEBOOK_CONNECTED)){
            getFacebookPhoto();
            binding.include.connectionFacebookProfileName.setText(user.getString(ParseUserKey.PROFILE_NAME));
        }
        else {
            binding.include.connectionFacebookClContainer.setVisibility(View.GONE);
        }
    }

    private void getFacebookPhoto(){
        String facebookUrl = "https://www.graph.facebook.com/" + user.getNumber(ParseUserKey.FACEBOOK_ID) + "/picture?type=large";
        Glide.with(this)
                .asBitmap()
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