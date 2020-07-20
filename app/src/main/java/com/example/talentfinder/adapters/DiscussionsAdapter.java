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
import com.example.talentfinder.interfaces.Key_ParseUser;
import com.example.talentfinder.models.Discussion;
import com.example.talentfinder.models.Message;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class DiscussionsAdapter extends RecyclerView.Adapter<DiscussionsAdapter.ViewHolder> {

    public static final String TAG = "DiscussionsAdapter";

    Context context;
    List<Discussion> discussions;
    FragmentManager fragmentManager;

    public DiscussionsAdapter(Context context, List<Discussion> discussions, FragmentManager fragmentManager) {
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
            clDiscussion = itemView.findViewById(R.id.clDiscussion);
            ivDiscussionRecipientUser = itemView.findViewById(R.id.ivDiscussionRecipientUser);
            tvDiscussionRecipientUser = itemView.findViewById(R.id.tvDiscussionRecipientUser);
            tvDiscussionMessageContent = itemView.findViewById(R.id.tvDiscussionMessageContent);
            tvDiscussionMessageTimestamp = itemView.findViewById(R.id.tvDiscussionMessageTimestamp);
        }

        public void bind(final Discussion discussion){

            // On discussion click, open that discussion
            clDiscussion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DiscussionFragment discussionFragment = DiscussionFragment.newInstance(discussion);
                    fragmentManager.beginTransaction().replace(R.id.clContainer, discussionFragment).addToBackStack(discussionFragment.getTag()).commit();
                }
            });

            // Load recipient user profile image
            Glide.with(context)
                    .load(discussion.getRecipient().getParseFile(Key_ParseUser.PROFILE_IMAGE).getUrl())
                    .circleCrop()
                    .into(ivDiscussionRecipientUser);

            // Set recipient user name
            tvDiscussionRecipientUser.setText(discussion.getRecipient().getString(Key_ParseUser.PROFILE_NAME));

            // Get latest message in the discussion
            discussion.getRelation("messages").getQuery()
                    .orderByDescending(Message.KEY_CREATED_AT)
                    .include(Message.KEY_USER)
                    .getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e != null){
                                Log.e(TAG, "Unable to get most recent message", e);
                            }

                            // If message user equals the current user output "You: {message content}" as message preview text
                            else if (object.getParseUser(Message.KEY_USER).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                                tvDiscussionMessageContent.setText(context.getString(R.string.message_current_user, object.getString(Message.KEY_MESSAGE_CONTENT)));
                                tvDiscussionMessageTimestamp.setText(object.getString(Message.KEY_CREATED_AT));
                                return;
                            }

                            // Else output "{recipient user}: {message content}" as message preview text
                            tvDiscussionMessageContent.setText(context.getString(R.string.message_recipient_user,
                                    object.getParseUser(Message.KEY_USER).getString(Key_ParseUser.PROFILE_NAME) ,
                                    object.getString(Message.KEY_MESSAGE_CONTENT)));
                            tvDiscussionMessageTimestamp.setText(object.getString(Message.KEY_CREATED_AT));
                        }
                    });
        }
    }
}
