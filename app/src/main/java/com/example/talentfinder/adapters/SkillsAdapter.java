package com.example.talentfinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talentfinder.R;
import com.google.android.material.chip.Chip;

import java.util.List;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.ViewHolder> {

    public static final String TAG = "SkillsAdapter";

    Context context;
    List<String> skills;

    public SkillsAdapter(Context context, List<String> skills) {
        this.context = context;
        this.skills = skills;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_skill, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String skill = skills.get(position);
        holder.bind(skill);
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        Chip chipSkill;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chipSkill = itemView.findViewById(R.id.chipSkill);
        }

        public void bind(final String skill){
            chipSkill.setText(skill);
            chipSkill.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    skills.remove(skill);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
