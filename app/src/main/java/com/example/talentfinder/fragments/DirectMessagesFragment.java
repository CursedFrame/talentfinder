package com.example.talentfinder.fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.interfaces.Key_ParseUser;
import com.example.talentfinder.models.Discussion;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

// Fragment allowing user to access their current discussions
public class DirectMessagesFragment extends Fragment {

    public static final String TAG = "MessagesFragment";

    LinearLayoutManager linearLayoutManager;
    List<Discussion> discussions;
    DiscussionsAdapter discussionsAdapter;
    FragmentDirectMessagesBinding binding;
    DividerItemDecoration dividerItemDecoration;


    public DirectMessagesFragment() {
        // Required empty public constructor
    }

    public static DirectMessagesFragment newInstance() {
        DirectMessagesFragment fragment = new DirectMessagesFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
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
        discussionsAdapter = new DiscussionsAdapter(getContext(), discussions, getFragmentManager());
        binding.rvDiscussions.setAdapter(discussionsAdapter);
        binding.rvDiscussions.setLayoutManager(linearLayoutManager);

        // Add divider for RecyclerView
        dividerItemDecoration = new DividerItemDecoration(binding.rvDiscussions.getContext(), linearLayoutManager.getOrientation());
        binding.rvDiscussions.addItemDecoration(dividerItemDecoration);

        queryDiscussions();
    }

    public void queryDiscussions(){
        ParseQuery<ParseObject> query = ParseUser.getCurrentUser().getRelation(Key_ParseUser.CURRENT_DISCUSSIONS).getQuery();
        query.setLimit(GlobalConstants.DISCUSSION_LIMIT);
        query.addDescendingOrder(Discussion.KEY_UPDATED_AT);
        query.include(Discussion.KEY_USER);
        query.include(Discussion.KEY_RECIPIENT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error loading discussions", e);
                    return;
                }

                for (ParseObject object : objects){
                    discussions.add((Discussion) object);
                }
                discussionsAdapter.notifyDataSetChanged();
            }
        });
    }
}