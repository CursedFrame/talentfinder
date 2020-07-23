package com.example.talentfinder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.talentfinder.adapters.DiscussionsAdapter;
import com.example.talentfinder.databinding.FragmentDirectMessagesBinding;
import com.example.talentfinder.models.Discussion;

import java.util.ArrayList;

// Fragment allowing user to access their current discussions
public class DirectMessagesFragment extends Fragment {

    public static final String TAG = "MessagesFragment";

    LinearLayoutManager linearLayoutManager;
    ArrayList<Discussion> discussions;
    DiscussionsAdapter discussionsAdapter;
    FragmentDirectMessagesBinding binding;
    DividerItemDecoration dividerItemDecoration;


    public DirectMessagesFragment() {
        // Required empty public constructor
    }

    public static DirectMessagesFragment newInstance(ArrayList<Discussion> discussions) {
        DirectMessagesFragment fragment = new DirectMessagesFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("discussions", discussions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDirectMessagesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recycler view and adapter creation
        linearLayoutManager = new LinearLayoutManager(getContext());
        discussions = new ArrayList<>();
        discussions = getArguments().getParcelableArrayList("discussions");
        discussionsAdapter = new DiscussionsAdapter(getContext(), discussions, getFragmentManager());
        binding.fragmentDirectMessagesRvDiscussions.setAdapter(discussionsAdapter);
        binding.fragmentDirectMessagesRvDiscussions.setLayoutManager(linearLayoutManager);

        // Add divider for RecyclerView
        dividerItemDecoration = new DividerItemDecoration(binding.fragmentDirectMessagesRvDiscussions.getContext(), linearLayoutManager.getOrientation());
        binding.fragmentDirectMessagesRvDiscussions.addItemDecoration(dividerItemDecoration);
    }
}