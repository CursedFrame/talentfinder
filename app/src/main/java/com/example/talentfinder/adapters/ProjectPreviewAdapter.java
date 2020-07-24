package com.example.talentfinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talentfinder.R;
import com.example.talentfinder.fragments.ProjectContributionFeedFragment;
import com.example.talentfinder.fragments.ProjectFragment;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.models.Project;
import com.parse.ParseUser;

import java.util.List;

public class ProjectPreviewAdapter extends RecyclerView.Adapter<ProjectPreviewAdapter.ViewHolder> {

    Context context;
    List<Project> projects;
    FragmentManager fragmentManager;

    public ProjectPreviewAdapter(Context context, List<Project> projects, FragmentManager fragmentManager) {
        this.context = context;
        this.projects = projects;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_project_preview, parent, false);
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

        ProgressBar pbProjectPreview;
        Project project;
        LinearLayoutManager linearLayoutManager;
        ChipAdapter tagsAdapter;
        RecyclerView rvTags;
        TextView tvProjectTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvTags = itemView.findViewById(R.id.itemProjectPreview_rvTags);
            tvProjectTitle = itemView.findViewById(R.id.itemProjectPreview_tvProjectTitle);
            pbProjectPreview = itemView.findViewById(R.id.itemProjectPreview_pbProjectPreview);

            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        }

        public void bind(Project project){

            pbProjectPreview.setVisibility(ProgressBar.VISIBLE);

            this.project = project;



            tagsAdapter = new ChipAdapter(context, project.getTags(), GlobalConstants.CHIP_FILTER);

            rvTags.setAdapter(tagsAdapter);
            rvTags.setLayoutManager(linearLayoutManager);

            tvProjectTitle.setText(project.getTitle());

            setOnClickItemView();

            pbProjectPreview.setVisibility(ProgressBar.INVISIBLE);
        }

        public void setOnClickItemView(){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (project.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                        ProjectContributionFeedFragment projectContributionFeedFragment = ProjectContributionFeedFragment.newInstance(project);
                        fragmentManager.beginTransaction().addToBackStack(projectContributionFeedFragment.getTag()).replace(R.id.activityMain_clContainer, projectContributionFeedFragment).commit();
                    }
                    else {
                        ProjectFragment projectFragment = ProjectFragment.newInstance(project);
                        fragmentManager.beginTransaction().addToBackStack(projectFragment.getTag()).replace(R.id.activityMain_clContainer, projectFragment).commit();
                    }
                }
            });
        }
    }
}
