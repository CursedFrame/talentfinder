package com.example.talentfinder.adapters;

import android.content.Context;
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
import com.example.talentfinder.fragments.ProfileFragment;
import com.example.talentfinder.fragments.ProjectFragment;
import com.example.talentfinder.interfaces.ParseUserKey;
import com.example.talentfinder.models.Project;
import com.parse.ParseUser;

import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {

    private Context context;
    private List<Project> projects;
    private FragmentManager fragmentManager;

    public ProjectsAdapter(Context context, List<Project> projects, FragmentManager fragmentManager){
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

        Project project;
        TextView tvTitle, tvFinderName;
        ImageView ivFinderProfilePicture, ivOptionalContext;
        ConstraintLayout clFinderProfileContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.itemProject_tvProjectTitle);
            tvFinderName = itemView.findViewById(R.id.itemProject_tvName);
            ivFinderProfilePicture = itemView.findViewById(R.id.itemProject_ivProfilePicture);
            ivOptionalContext = itemView.findViewById(R.id.itemProject_ivContextImage);
            clFinderProfileContainer = itemView.findViewById(R.id.itemProject_clProfileContainer);
        }

        public void bind(final Project project){

            this.project = project;

            bindProjectData();

            // On profile click, take user to the project creator's user profile page
            setOnClickClFinderProfileContainer(project.getUser());

            // On project click, take user to that project's detail view
            setOnClickItemView();
        }

        public void bindProjectData(){
            tvTitle.setText(project.getTitle());
            tvFinderName.setText(project.getUser().getUsername());

            if (project.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl() != null) {
                Glide.with(context)
                        .load(project.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl())
                        .circleCrop()
                        .into(ivFinderProfilePicture);
            }

            if (project.getImage() != null) {
                Glide.with(context)
                        .load(project.getImage().getUrl())
                        .into(ivOptionalContext);
            }
        }

        public void setOnClickClFinderProfileContainer(final ParseUser user){
            clFinderProfileContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFragment profileFragment = ProfileFragment.newInstance(user);
                    fragmentManager.beginTransaction().addToBackStack(profileFragment.getTag()).replace(R.id.activityMain_clContainer, profileFragment).commit();
                }
            });
        }

        public void setOnClickItemView(){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProjectFragment projectFragment = ProjectFragment.newInstance(project);
                    fragmentManager.beginTransaction().addToBackStack(projectFragment.getTag()).replace(R.id.activityMain_clContainer, projectFragment).commit();
                }
            });
        }
    }
}
