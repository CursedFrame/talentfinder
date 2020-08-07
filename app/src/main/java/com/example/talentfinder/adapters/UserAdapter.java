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
import com.example.talentfinder.models.User;
import com.google.android.material.chip.Chip;

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
        Chip chipSkill, chipTalent, chipSubtalent;
        LinearLayoutManager tagsLinearLayoutManager;
        List<String> tags;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserImage = itemView.findViewById(R.id.itemUser_ivProfilePicture);
            tvUserLocation = itemView.findViewById(R.id.itemUser_tvLocation);
            tvUserName = itemView.findViewById(R.id.itemUser_tvName);
            chipSkill = itemView.findViewById(R.id.itemUser_chipSkill);
            chipTalent = itemView.findViewById(R.id.itemUser_chipTalent);
            chipSubtalent = itemView.findViewById(R.id.itemUser_chipSubtalent);
        }

        public void bind(User user){
            this.user = user;
            tvUserLocation.setText(user.getLocation());
            tvUserName.setText(user.getName());
            Glide.with(context)
                    .load(user.getImage().getUrl())
                    .circleCrop()
                    .into(ivUserImage);

            chipTalent.setText(user.getTalentTag());
            chipSubtalent.setText(user.getSubTalentTag());
            chipSkill.setText(user.getSkillTag());

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
