package com.example.talentfinder.fragments;

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
import com.example.talentfinder.databinding.FragmentProfileBinding;
import com.example.talentfinder.interfaces.Key_ParseUser;
import com.example.talentfinder.models.Discussion;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    private ParseUser user;
    private FragmentProfileBinding binding;
    private FragmentManager fragmentManager;

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

        binding.tvProfileName.setText(user.getString(Key_ParseUser.PROFILE_NAME));
        if (user.getString(Key_ParseUser.PROFILE_LOCATION) != null){
            binding.tvProfileLocation.setText(user.getString(Key_ParseUser.PROFILE_LOCATION));
        }

        Glide.with(getContext())
                .load(user.getParseFile(Key_ParseUser.PROFILE_IMAGE).getUrl())
                .circleCrop()
                .into(binding.ivProfilePicture);

        // If the profile user is the current user, don't show the "Start Discussion" button
        if (user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
            binding.btnStartDiscussion.setVisibility(View.GONE);
        }

        // On "Start Discussion" button click, take user to the start discussion dialog fragment to start discussion with project creator
        binding.btnStartDiscussion.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // Get discussion where "user" is the current user and "recipient" is the recipient user

                final ParseQuery<Discussion> query = ParseQuery.getQuery(Discussion.class);
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.whereEqualTo("recipient", user);
                query.getFirstInBackground(new GetCallback<Discussion>() {
                    @Override
                    public void done(Discussion object, ParseException e) {
                        // If discussion does not exist, get discussion where "recipient" is the current user and "user" is the recipient user

                        if (e != null){
                            query.whereEqualTo("recipient", ParseUser.getCurrentUser());
                            query.whereEqualTo("user", user);
                            query.getFirstInBackground(new GetCallback<Discussion>() {
                                @Override
                                public void done(Discussion object, ParseException e) {
                                    // If discussion does not exist between two users, allow current user to create a discussion

                                    if (e != null){
                                        StartDiscussionDialogFragment startDiscussionDialogFragment = StartDiscussionDialogFragment.newInstance(user);
                                        startDiscussionDialogFragment.show(fragmentManager, startDiscussionDialogFragment.getTag());
                                        return;
                                    }

                                    Toast.makeText(getContext(), "You have already started a dicussion with this person.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });

                            return;
                        }

                        Toast.makeText(getContext(), "You have already started a dicussion with this person.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}