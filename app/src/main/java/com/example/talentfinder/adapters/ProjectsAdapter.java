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
import com.example.talentfinder.interfaces.Key_ParseUser;
import com.example.talentfinder.models.Project;
import com.parse.ParseUser;

import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {

    Context context;
    List<Project> projects;
    FragmentManager fragmentManager;

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


        TextView tvTitle, tvFinderName;
        ImageView ivFinderProfilePicture, ivOptionalContext;
        ConstraintLayout clFinderProfileContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvFinderName = itemView.findViewById(R.id.tvFinderName);
            ivFinderProfilePicture = itemView.findViewById(R.id.ivFinderProfilePicture);
            ivOptionalContext = itemView.findViewById(R.id.ivOptionalContext);
            clFinderProfileContainer = itemView.findViewById(R.id.clFinderProfileContainer);
        }

        public void bind(final Project project){

            bindProjectData(project);

            // On profile click, take user to the project creator's user profile page
            setOnClickClFinderProfileContainer(project.getUser());

            // On project click, take user to that project's detail view
            setOnClickItemView(project);
        }

        public void bindProjectData(Project project){
            tvTitle.setText(project.getTitle());
            tvFinderName.setText(project.getUser().getUsername());

            if (project.getUser().getParseFile(Key_ParseUser.PROFILE_IMAGE).getUrl() != null) {
                Glide.with(context)
                        .load(project.getUser().getParseFile(Key_ParseUser.PROFILE_IMAGE).getUrl())
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
                    fragmentManager.beginTransaction().addToBackStack(profileFragment.getTag()).replace(R.id.clContainer, profileFragment).commit();
                }
            });
        }

        public void setOnClickItemView(final Project project){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProjectFragment projectFragment = ProjectFragment.newInstance(project);
                    fragmentManager.beginTransaction().addToBackStack(projectFragment.getTag()).replace(R.id.clContainer, projectFragment).commit();
                }
            });
        }
    }
}
