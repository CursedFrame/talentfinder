package com.example.talentfinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talentfinder.R;
import com.example.talentfinder.fragments.ProfileFragment;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> users;
    private Context context;
    private FragmentManager fragmentManager;

    public UserAdapter(Context context, List<User> users, FragmentManager fragmentManager) {
        this.users = users;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivUserImage;
        User user;
        TextView tvUserLocation, tvUserName;
        RecyclerView rvTags;
        ChipAdapter tagsAdapter;
        LinearLayoutManager tagsLinearLayoutManager;
        List<String> tags;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserImage = itemView.findViewById(R.id.itemUser_ivProfilePicture);
            tvUserLocation = itemView.findViewById(R.id.itemUser_tvLocation);
            tvUserName = itemView.findViewById(R.id.itemUser_tvName);
            rvTags = itemView.findViewById(R.id.itemUser_rvTags);
        }

        public void bind(User user){
            this.user = user;
            tvUserLocation.setText(user.getLocation());
            tvUserName.setText(user.getName());
            Glide.with(context)
                    .load(user.getImage().getUrl())
                    .circleCrop()
                    .into(ivUserImage);

            // User skill and experience adapter and RecyclerView
            tagsLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            tags = new ArrayList<>();
            tags.add(user.getTalentTag());
            tags.add(user.getSubTalentTag());
            tags.add(user.getSkillTag());
            tagsAdapter = new ChipAdapter(context, tags, GlobalConstants.CHIP_FILTER);
            rvTags.setAdapter(tagsAdapter);
            rvTags.setLayoutManager(tagsLinearLayoutManager);

            setOnClickItemView();
        }

        private void setOnClickItemView(){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goProfileFragment();
                }
            });
        }
        private void goProfileFragment(){
            ProfileFragment profileFragment = ProfileFragment.newInstance(user);
            fragmentManager.beginTransaction().addToBackStack(profileFragment.getTag()).replace(R.id.includeMainViewContainer_mainContainer, profileFragment).commit();
        }
    }

    public void refresh(List<User> list) {
        clear();
        addAll(list);
    }

    public void clear() {
        users = new ArrayList<>();
        this.notifyDataSetChanged();
    }

    public void addAll(List<User> list) {
        users.addAll(list);
        this.notifyDataSetChanged();
    }
}
