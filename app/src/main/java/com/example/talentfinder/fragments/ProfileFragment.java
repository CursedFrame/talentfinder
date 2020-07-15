package com.example.talentfinder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.talentfinder.databinding.FragmentProfileBinding;
import com.example.talentfinder.interfaces.Key_ParseUser;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    private ParseUser user;
    private FragmentProfileBinding binding;

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}