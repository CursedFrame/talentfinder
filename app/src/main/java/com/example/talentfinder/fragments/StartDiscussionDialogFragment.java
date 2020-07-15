package com.example.talentfinder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.talentfinder.databinding.FragmentStartDiscussionDialogBinding;
import com.parse.ParseUser;

public class StartDiscussionDialogFragment extends DialogFragment {

    private FragmentStartDiscussionDialogBinding binding;
    private ParseUser recipientUser;

    public StartDiscussionDialogFragment() {
        // Required empty public constructor
    }

    public static StartDiscussionDialogFragment newInstance(ParseUser user) {
        StartDiscussionDialogFragment fragment = new StartDiscussionDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStartDiscussionDialogBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipientUser = getArguments().getParcelable("user");
    }
}