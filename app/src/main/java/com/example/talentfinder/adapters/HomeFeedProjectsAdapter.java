package com.example.talentfinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talentfinder.R;
import com.example.talentfinder.fragments.ProfileFragment;
import com.example.talentfinder.fragments.ProjectFragment;
import com.example.talentfinder.models.Project;

import java.util.List;

public class HomeFeedProjectsAdapter extends RecyclerView.Adapter<HomeFeedProjectsAdapter.ViewHolder> {

    Context context;
    List<Project> projects;
    FragmentManager fragmentManager;

    public HomeFeedProjectsAdapter(Context context, List<Project> projects, FragmentManager fragmentManager){
        this.context = context;
        this.projects = projects;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.bind(project);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ConstraintLayout clFinderProfileContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            clFinderProfileContainer = itemView.findViewById(R.id.clFinderProfileContainer);

            // On project click, take user to that project's detail view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProjectFragment projectFragment = ProjectFragment.newInstance();
                    fragmentManager.beginTransaction().addToBackStack(projectFragment.getTag()).replace(R.id.clContainer, projectFragment).commit();
                }
            });

            // On profile click, take user to the project creator's user profile page
            clFinderProfileContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFragment profileFragment = ProfileFragment.newInstance();
                    fragmentManager.beginTransaction().addToBackStack(profileFragment.getTag()).replace(R.id.clContainer, profileFragment).commit();
                }
            });
        }

        public void bind(Project project){
            tvTitle.setText(project.getTitle());
        }
    }
}
