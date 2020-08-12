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
import com.example.talentfinder.fragments.ContributionDetailFragment;
import com.example.talentfinder.fragments.ProfileFragment;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.models.Contribution;
import com.example.talentfinder.models.User;
import com.google.android.material.chip.Chip;

import java.util.List;

public class ContributionAdapter extends RecyclerView.Adapter<ContributionAdapter.ViewHolder> {

    private Context context;
    private List<Contribution> contributions;
    private FragmentManager fragmentManager;

    public ContributionAdapter(Context context, List<Contribution> contributions, FragmentManager fragmentManager) {
        this.context = context;
        this.contributions = contributions;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getItemViewType(int position) {
        if (contributions.get(position).getMediaTypeCode() == GlobalConstants.MEDIA_PHOTO){
            return GlobalConstants.MEDIA_PHOTO;
        }
        else {
            return GlobalConstants.MEDIA_VIDEO;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == GlobalConstants.MEDIA_PHOTO){
            view = LayoutInflater.from(context).inflate(R.layout.item_contribution_photo, parent, false);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.item_contribution_video, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contribution contribution = contributions.get(position);
        holder.bind(contribution);
    }

    @Override
    public int getItemCount() {
        return contributions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        Contribution contribution;
        TextView tvCreatorName, tvDescription;
        ImageView ivCreatorProfilePicture, ivContributionMedia, ivVideoThumbnail;
        ConstraintLayout clCreatorProfileContainer;
        Chip chipTalent, chipSubtalent, chipSkill;
        User contributionUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Contribution contribution){
            this.contribution = contribution;

            // Bind photo contribution unique properties
            if (contribution.getMediaTypeCode() == GlobalConstants.MEDIA_PHOTO){
                clCreatorProfileContainer = itemView.findViewById(R.id.itemContributionPhoto_clCreatorProfileContainer);
                tvCreatorName = itemView.findViewById(R.id.itemContributionPhoto_tvCreatorName);
                tvDescription = itemView.findViewById(R.id.itemContributionPhoto_tvDescription);
                ivCreatorProfilePicture = itemView.findViewById(R.id.itemContributionPhoto_ivCreatorProfilePicture);
                ivContributionMedia = itemView.findViewById(R.id.itemContributionPhoto_ivContributionMedia);
                chipTalent = itemView.findViewById(R.id.itemContributionPhoto_chipTalent);
                chipSubtalent = itemView.findViewById(R.id.itemContributionPhoto_chipSubtalent);
                chipSkill = itemView.findViewById(R.id.itemContributionPhoto_chipSkill);

                if (contribution.getMedia() != null){
                    Glide.with(context)
                            .load(contribution.getMedia().getUrl())
                            .into(ivContributionMedia);
                }
            }

            // Bind video contribution unique properties
            else {
                clCreatorProfileContainer = itemView.findViewById(R.id.itemContributionVideo_clCreatorProfileContainer);
                tvCreatorName = itemView.findViewById(R.id.itemContributionVideo_tvCreatorName);
                tvDescription = itemView.findViewById(R.id.itemContributionVideo_tvDescription);
                ivCreatorProfilePicture = itemView.findViewById(R.id.itemContributionVideo_ivCreatorProfilePicture);
                ivVideoThumbnail = itemView.findViewById(R.id.itemContributionVideo_ivContributionMedia);
                chipTalent = itemView.findViewById(R.id.itemContributionVideo_chipTalent);
                chipSubtalent = itemView.findViewById(R.id.itemContributionVideo_chipSubtalent);
                chipSkill = itemView.findViewById(R.id.itemContributionVideo_chipSkill);

                if (contribution.getMedia() != null){
                    Glide.with(context)
                            .asBitmap()
                            .load(contribution.getMedia().getUrl())
                            .into(ivVideoThumbnail);
                }

            }

            // Bind and initialize similar properties
            contributionUser = contribution.getUser();
            tvCreatorName.setText(contributionUser.getName());
            tvDescription.setText(contribution.getContentDescription());
            chipTalent.setText(contributionUser.getTalentTag());
            chipSubtalent.setText(contributionUser.getSubTalentTag());
            chipSkill.setText(contributionUser.getSkillTag());
            if (contributionUser.getImage() != null){
                Glide.with(context)
                        .load(contributionUser.getImage().getUrl())
                        .circleCrop()
                        .into(ivCreatorProfilePicture);
            }
            setOnClickClCreatorProfileContainer();
            setOnClickItemView();
        }

        public void setOnClickClCreatorProfileContainer(){
            clCreatorProfileContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFragment profileFragment = ProfileFragment.newInstance(contribution.getUser());
                    fragmentManager.beginTransaction().addToBackStack(profileFragment.getTag()).replace(R.id.includeMainViewContainer_mainContainer, profileFragment).commit();
                }
            });
        }

        public void setOnClickItemView(){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContributionDetailFragment contributionDetailFragment = ContributionDetailFragment.newInstance(contribution);
                    fragmentManager.beginTransaction().addToBackStack(contributionDetailFragment.getTag()).replace(R.id.includeMainViewContainer_mainContainer, contributionDetailFragment).commit();
                }
            });
        }
    }
}
