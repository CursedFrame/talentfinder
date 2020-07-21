package com.example.talentfinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talentfinder.R;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.models.Project;

import java.util.List;

public class ProjectPreviewAdapter extends RecyclerView.Adapter<ProjectPreviewAdapter.ViewHolder> {

    Context context;
    List<Project> projects;

    public ProjectPreviewAdapter(Context context, List<Project> projects) {
        this.context = context;
        this.projects = projects;
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

        LinearLayoutManager linearLayoutManager;
        ChipAdapter tagsAdapter;
//        List<String> tags;
        RecyclerView rvTags;
        TextView tvProjectTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvTags = itemView.findViewById(R.id.rvTags);
            tvProjectTitle = itemView.findViewById(R.id.tvProjectTitle);

            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//            tags = new ArrayList<>();
        }

        public void bind(Project project){

            tagsAdapter = new ChipAdapter(context, project.getTags(), GlobalConstants.CHIP_FILTER);

            rvTags.setAdapter(tagsAdapter);
            rvTags.setLayoutManager(linearLayoutManager);

            tvProjectTitle.setText(project.getTitle());
        }
    }
}
