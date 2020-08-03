package com.example.talentfinder.adapters;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.example.talentfinder.fragments.ProjectContributionFeedFragment;
import com.example.talentfinder.fragments.ProjectFragment;
import com.example.talentfinder.fragments.StartDiscussionDialogFragment;
import com.example.talentfinder.interfaces.ParseUserKey;
import com.example.talentfinder.models.Project;
import com.google.android.material.chip.Chip;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {

    public static final String TAG = "ProjectsAdapter";

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
        TextView tvTitle, tvFinderName, tvDescription;
        ImageView ivFinderProfilePicture, ivOptionalContext;
        ConstraintLayout clFinderProfileContainer;
        Chip chipTalent, chipSubtalent, chipSkill;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.itemProject_tvProjectTitle);
            tvFinderName = itemView.findViewById(R.id.itemProject_tvName);
            tvDescription = itemView.findViewById(R.id.itemProject_tvDescription);
            ivFinderProfilePicture = itemView.findViewById(R.id.itemProject_ivProfilePicture);
            ivOptionalContext = itemView.findViewById(R.id.itemProject_ivContextImage);
            clFinderProfileContainer = itemView.findViewById(R.id.itemProject_clProfileContainer);
            chipTalent = itemView.findViewById(R.id.itemProject_chipTalent);
            chipSubtalent = itemView.findViewById(R.id.itemProject_chipSubtalent);
            chipSkill = itemView.findViewById(R.id.itemProject_chipSkill);
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
            tvFinderName.setText(project.getUser().getString(ParseUserKey.PROFILE_NAME));
            tvDescription.setText(project.getDescription());
            chipTalent.setText(project.getTalentTag());
            chipSubtalent.setText(project.getSubTalentTag());
            chipSkill.setText(project.getSkillTag());

            if (project.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl() != null) {
                Glide.with(context)
                        .load(project.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl())
                        .circleCrop()
                        .into(ivFinderProfilePicture);
            }

            if (project.getImage() != null) {
                int radius = 30;
                int margin = 10;
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
                    fragmentManager.beginTransaction().addToBackStack(profileFragment.getTag()).replace(R.id.includeMainViewContainer_mainContainer, profileFragment).commit();
                }
            });

            clFinderProfileContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    StartDiscussionDialogFragment startDiscussionDialogFragment = StartDiscussionDialogFragment.newInstance(user);
                    startDiscussionDialogFragment.show(fragmentManager, startDiscussionDialogFragment.getTag());
                    return true;
                }
            });
        }

        public void setOnClickItemView(){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            itemView.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        ProjectFragment projectFragment = ProjectFragment.newInstance(project);
                        fragmentManager.beginTransaction().addToBackStack(projectFragment.getTag()).replace(R.id.includeMainViewContainer_mainContainer, projectFragment).commit();
                        return super.onSingleTapConfirmed(e);
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        ProjectContributionFeedFragment projectContributionFeedFragment = ProjectContributionFeedFragment.newInstance(project);
                        fragmentManager.beginTransaction().addToBackStack(projectContributionFeedFragment.getTag()).replace(R.id.includeMainViewContainer_mainContainer, projectContributionFeedFragment).commit();
                        return super.onDoubleTap(e);
                    }
        // implement here other callback methods like onFling, onScroll as necessary
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d("TEST", "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
        }
    }

    public void refresh(List<Project> list) {
        clear();
        addAll(list);
    }

    public void clear() {
        projects = new ArrayList<>();
        this.notifyDataSetChanged();
    }

    public void addAll(List<Project> list) {
        projects.addAll(list);
        this.notifyDataSetChanged();
    }


}
