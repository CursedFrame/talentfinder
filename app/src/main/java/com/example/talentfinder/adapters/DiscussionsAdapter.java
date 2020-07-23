package com.example.talentfinder.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talentfinder.R;
import com.example.talentfinder.fragments.DiscussionFragment;
import com.example.talentfinder.interfaces.ParseUserKey;
import com.example.talentfinder.models.Discussion;
import com.example.talentfinder.models.Message;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

public class DiscussionsAdapter extends RecyclerView.Adapter<DiscussionsAdapter.ViewHolder> {

    public static final String TAG = "DiscussionsAdapter";

    Context context;
    ArrayList<Discussion> discussions;
    FragmentManager fragmentManager;

    public DiscussionsAdapter(Context context, ArrayList<Discussion> discussions, FragmentManager fragmentManager) {
        this.context = context;
        this.discussions = discussions;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_discussion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Discussion discussion = discussions.get(position);
        holder.bind(discussion);
    }

    @Override
    public int getItemCount() {
        return discussions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout clDiscussion;
        ImageView ivDiscussionRecipientUser;
        TextView tvDiscussionRecipientUser;
        TextView tvDiscussionMessageContent;
        TextView tvDiscussionMessageTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clDiscussion = itemView.findViewById(R.id.itemDiscussion_clDiscussion);
            ivDiscussionRecipientUser = itemView.findViewById(R.id.itemDiscussion_ivRecipientUser);
            tvDiscussionRecipientUser = itemView.findViewById(R.id.itemDiscussion_tvRecipientUser);
            tvDiscussionMessageContent = itemView.findViewById(R.id.itemDiscussion_tvMessageContent);
            tvDiscussionMessageTimestamp = itemView.findViewById(R.id.itemDiscussion_tvMessageTimestamp);
        }

        public void bind(final Discussion discussion){

            // On discussion click, open that discussion
            setOnClickClDiscussion(discussion);

            // Set oppositeUser's image and name to imageview and textview
            ParseUser oppositeUser;
            if (discussion.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                oppositeUser = discussion.getRecipient();
            }
            else {
                oppositeUser = discussion.getUser();
            }
            loadProfilePicture(oppositeUser);

            // Get latest message in the discussion and bind
            discussion.getRelation(Discussion.KEY_MESSAGES).getQuery()
                    .orderByDescending(Message.KEY_CREATED_AT)
                    .include(Message.KEY_USER)
                    .getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e != null){
                                Log.e(TAG, "Unable to get most recent message", e);
                            }

                            else {
                                loadMessageContent(object);
                            }
                        }
                    });
        }

        public void setOnClickClDiscussion(final Discussion discussion){
            clDiscussion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DiscussionFragment discussionFragment = DiscussionFragment.newInstance(discussion);
                    fragmentManager.beginTransaction().replace(R.id.activityMain_clContainer, discussionFragment).addToBackStack(discussionFragment.getTag()).commit();
                }
            });
        }

        public void loadProfilePicture(ParseUser user){
            Glide.with(context)
                    .load(user.getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl())
                    .circleCrop()
                    .into(ivDiscussionRecipientUser);

            tvDiscussionRecipientUser.setText(user.getString(ParseUserKey.PROFILE_NAME));
        }

        public void loadMessageContent(ParseObject object){
            // If message user equals the current user output "You: {message content}" as message preview text
            if (object.getParseUser(Message.KEY_USER).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                tvDiscussionMessageContent.setText(context.getString(R.string.message_current_user, object.getString(Message.KEY_MESSAGE_CONTENT)));
            }

            // Else output "{recipient user}: {message content}" as message preview text
            else {
                tvDiscussionMessageContent.setText(context.getString(R.string.message_recipient_user,
                        object.getParseUser(Message.KEY_USER).getString(ParseUserKey.PROFILE_NAME),
                        object.getString(Message.KEY_MESSAGE_CONTENT)));
            }
            tvDiscussionMessageTimestamp.setText(object.getString(Message.KEY_CREATED_AT));
        }
    }
}
