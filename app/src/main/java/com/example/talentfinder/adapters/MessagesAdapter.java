package com.example.talentfinder.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talentfinder.R;
import com.example.talentfinder.interfaces.Key_ParseUser;
import com.example.talentfinder.models.Message;
import com.parse.ParseUser;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    Context context;
    List<Message> messages;

    public MessagesAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout clMessage;
        ImageView ivMessageUserImage;
        TextView tvMessageContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clMessage = itemView.findViewById(R.id.clMessage);
            ivMessageUserImage = itemView.findViewById(R.id.ivMessageUserImage);
            tvMessageContent = itemView.findViewById(R.id.tvMessageContent);
        }

        public void bind(Message message){
            // If message belongs to current user, bind right
            if (message.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {

                // Set constraints for binding current user message to right of screen
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(clMessage);
                setConstraintsCurrentUserMessage(constraintSet);
                constraintSet.applyTo(clMessage);

            }

            // Else, bind left
            Glide.with(context)
                    .load(message.getUser().getParseFile(Key_ParseUser.PROFILE_IMAGE).getUrl())
                    .circleCrop()
                    .into(ivMessageUserImage);
            tvMessageContent.setText(getMessageString(message));
        }
    }

    // Function for returning specially formatted string with bold user name
    public SpannableStringBuilder getMessageString(Message message){
        String messageUserName = message.getUser().getString(Key_ParseUser.PROFILE_NAME);
        SpannableStringBuilder messageText = new SpannableStringBuilder( messageUserName + ": " + message.getMessageContent());
        messageText.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, messageUserName.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return messageText;
    }

    // Setting constraints for current user's message
    public void setConstraintsCurrentUserMessage(ConstraintSet constraintSet){
        constraintSet.clear(R.id.ivMessageUserImage, ConstraintSet.START);
        constraintSet.connect(R.id.ivMessageUserImage, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
        constraintSet.clear(R.id.tvMessageContent, ConstraintSet.START);
        constraintSet.connect(R.id.tvMessageContent, ConstraintSet.END, R.id.ivMessageUserImage, ConstraintSet.START, 8);
    }
}
