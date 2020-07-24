package com.example.talentfinder.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.talentfinder.adapters.MessagesAdapter;
import com.example.talentfinder.databinding.FragmentDiscussionBinding;
import com.example.talentfinder.interfaces.ParseUserKey;
import com.example.talentfinder.models.Discussion;
import com.example.talentfinder.models.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class DiscussionFragment extends Fragment {

    public static final String TAG = "DiscussionFragment";

    FragmentDiscussionBinding binding;
    Discussion discussion;
    List<Message> messages;
    MessagesAdapter messagesAdapter;
    LinearLayoutManager linearLayoutManager;

    public DiscussionFragment() {
        // Required empty public constructor
    }

    public static DiscussionFragment newInstance(Discussion discussion) {
        DiscussionFragment fragment = new DiscussionFragment();
        Bundle args = new Bundle();
        args.putParcelable("discussion", discussion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDiscussionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        discussion = getArguments().getParcelable("discussion");

        linearLayoutManager = new LinearLayoutManager(getContext());
        messages = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(getContext(), messages);
        binding.fragmentDiscussionRvMessages.setAdapter(messagesAdapter);
        binding.fragmentDiscussionRvMessages.setLayoutManager(linearLayoutManager);

        if (discussion.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
            Glide.with(getContext())
                    .load(discussion.getRecipient().getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl())
                    .circleCrop()
                    .into(binding.fragmentDiscussionIvOppositeUserImage);

            binding.fragmentDiscussionTvOppositeUserName.setText(discussion.getRecipient().getString(ParseUserKey.PROFILE_NAME));
        }
        else {
            Glide.with(getContext())
                    .load(discussion.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl())
                    .circleCrop()
                    .into(binding.fragmentDiscussionIvOppositeUserImage);

            binding.fragmentDiscussionTvOppositeUserName.setText(discussion.getUser().getString(ParseUserKey.PROFILE_NAME));
        }

        // On "send" button click, create a new message, relate it to discussion, and upload to server
        binding.fragmentDiscussionBtnSendNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Message cannot be empty
                if (binding.fragmentDiscussionEtNewMessageContent.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Message cannot be empty!", Toast.LENGTH_SHORT);
                    return;
                }
                final Message message = new Message();
                message.setUser(ParseUser.getCurrentUser());
                message.setMessageContent(binding.fragmentDiscussionEtNewMessageContent.getText().toString());
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Log.e(TAG, "Message was not saved", e);
                            return;
                        }
                        discussion.getRelation("messages").add(message);
                        discussion.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null){
                                    Log.e(TAG, "Discussion was not saved", e);
                                    return;
                                }
                                messages.add(message);
                                messagesAdapter.notifyDataSetChanged();
                                binding.fragmentDiscussionEtNewMessageContent.setText("");
                            }
                        });
                    }
                });
            }
        });

        queryMessages();
    }

    public void queryMessages(){
        ParseQuery<ParseObject> query = discussion.getRelation(Discussion.KEY_MESSAGES).getQuery();
        query.orderByAscending(Message.KEY_CREATED_AT);
        query.include(Message.KEY_USER);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error loading messages", e);
                    return;
                }
                for (ParseObject object : objects){
                    messages.add((Message) object);
                }
                messagesAdapter.notifyDataSetChanged();
            }
        });
    }
}