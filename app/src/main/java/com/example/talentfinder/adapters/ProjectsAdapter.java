package com.example.talentfinder.adapters;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talentfinder.R;
import com.example.talentfinder.fragments.DiscussionFragment;
import com.example.talentfinder.fragments.ProfileFragment;
import com.example.talentfinder.fragments.ProjectContributionFeedFragment;
import com.example.talentfinder.fragments.ProjectFragment;
import com.example.talentfinder.fragments.StartDiscussionDialogFragment;
import com.example.talentfinder.models.Discussion;
import com.example.talentfinder.models.Project;
import com.example.talentfinder.models.User;
import com.google.android.material.chip.Chip;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
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
        User user;
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
            this.user = project.getUser();

            bindProjectData();

            // On profile click, take user to the project creator's user profile page
            setOnClickClFinderProfileContainer();

            // On project click, take user to that project's detail view
            setOnClickItemView();
        }

        public void bindProjectData(){
            tvTitle.setText(project.getTitle());
            tvFinderName.setText(project.getUser().getName());
            tvDescription.setText(project.getDescription());
            chipTalent.setText(project.getTalentTag());
            chipSubtalent.setText(project.getSubTalentTag());
            chipSkill.setText(project.getSkillTag());

            if (project.getUser().getImage().getUrl() != null) {
                Glide.with(context)
                        .load(project.getUser().getImage().getUrl())
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

        public void setOnClickClFinderProfileContainer(){
            clFinderProfileContainer.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        goProfileFragment();
                        return super.onSingleTapConfirmed(e);
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        if (!project.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                            checkDiscussion();
                        }
                        else {
                            clFinderProfileContainer.startAnimation(AnimationUtils.loadAnimation(context, R.anim.view_shake));

                        }
                        super.onLongPress(e);
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
                        goProjectFragment();
                        return super.onSingleTapConfirmed(e);
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        goProjectContributionFeedFragment();
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

        private void checkDiscussion(){
            ParseQuery<Discussion> query1 = ParseQuery.getQuery(Discussion.class);
            query1.whereEqualTo("user", ParseUser.getCurrentUser());
            query1.whereEqualTo("recipient", project.getUser());

            ParseQuery<Discussion> query2 = ParseQuery.getQuery(Discussion.class);
            query2.whereEqualTo("recipient", ParseUser.getCurrentUser());
            query2.whereEqualTo("user", project.getUser());

            List<ParseQuery<Discussion>> queryList = new ArrayList<>();
            queryList.add(query1);
            queryList.add(query2);

            ParseQuery<Discussion> query = ParseQuery.or(queryList);
            query.include(Discussion.KEY_USER);
            query.include(Discussion.KEY_RECIPIENT);

            query.getFirstInBackground(new GetCallback<Discussion>() {
                @Override
                public void done(Discussion object, ParseException e) {
                    if (e != null){
                        goStartDiscussionDialogFragment();
                        return;
                    }
                    goDiscussionFragment(object);
                }
            });
        }

        private void goStartDiscussionDialogFragment(){
            StartDiscussionDialogFragment startDiscussionDialogFragment = StartDiscussionDialogFragment.newInstance(project.getUser());
            startDiscussionDialogFragment.show(fragmentManager, startDiscussionDialogFragment.getTag());
        }

        private void goProfileFragment(){
            ProfileFragment profileFragment = ProfileFragment.newInstance(user);
            fragmentManager.beginTransaction().addToBackStack(profileFragment.getTag()).replace(R.id.includeMainViewContainer_mainContainer, profileFragment).commit();
        }

        private void goProjectFragment(){
            ProjectFragment projectFragment = ProjectFragment.newInstance(project);
            fragmentManager.beginTransaction().addToBackStack(projectFragment.getTag()).replace(R.id.includeMainViewContainer_mainContainer, projectFragment).commit();
        }

        private void goDiscussionFragment(Discussion discussion){
            DiscussionFragment discussionFragment = DiscussionFragment.newInstance(discussion);
            fragmentManager.beginTransaction().addToBackStack(discussionFragment.getTag()).replace(R.id.includeMainViewContainer_mainContainer, discussionFragment).commit();
        }

        private void goProjectContributionFeedFragment(){
            ProjectContributionFeedFragment projectContributionFeedFragment = ProjectContributionFeedFragment.newInstance(project);
            fragmentManager.beginTransaction().addToBackStack(projectContributionFeedFragment.getTag()).replace(R.id.includeMainViewContainer_mainContainer, projectContributionFeedFragment).commit();
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
