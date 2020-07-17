package com.example.talentfinder.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.talentfinder.R;
import com.example.talentfinder.databinding.FragmentStartDiscussionDialogBinding;
import com.example.talentfinder.interfaces.Key_ParseUser;
import com.example.talentfinder.models.Discussion;
import com.example.talentfinder.models.Message;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class StartDiscussionDialogFragment extends DialogFragment {

    public static final String TAG = "StartDiscussionDialogFr";

    private FragmentManager fragmentManager;
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

//        if (getDialog() != null && getDialog().getWindow() != null) {
//            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getFragmentManager();
        recipientUser = getArguments().getParcelable("user");

        binding.tvRecipientUserName.setText(getString(R.string.start_discussion, recipientUser.getString(Key_ParseUser.PROFILE_NAME)));

        // On button send message click, create message and then discussion
        binding.btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Message cannot be empty
                if (binding.etComposeMessage.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Message cannot be empty!", Toast.LENGTH_SHORT);
                    return;
                }

                final Message message = new Message();
                message.setUser(ParseUser.getCurrentUser());
                message.setMessageContent(binding.etComposeMessage.getText().toString());

                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Log.e(TAG, "Message was not saved", e);
                            return;
                        }

                        final Discussion discussion = new Discussion();
                        discussion.setUser(ParseUser.getCurrentUser());
                        discussion.setRecipient(recipientUser);
                        discussion.getRelation("messages").add(message);

                        discussion.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null){
                                    Log.e(TAG, "Discussion was not saved", e);
                                    return;
                                }
                                DiscussionFragment discussionFragment = DiscussionFragment.newInstance(discussion);
                                fragmentManager.beginTransaction().replace(R.id.clContainer, discussionFragment).addToBackStack(discussionFragment.getTag()).commit();
                                dismiss();
                            }
                        });
                    }
                });
            }
        });

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}