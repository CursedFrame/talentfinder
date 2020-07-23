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
import com.example.talentfinder.interfaces.ParseUserKey;
import com.example.talentfinder.models.Contribution;

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
        TextView tvCreatorName;
        ImageView ivCreatorProfilePicture, ivContributionMedia, ivVideoThumbnail;
        ConstraintLayout clCreatorProfileContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Contribution contribution){
            this.contribution = contribution;

            // Bind photo contribution unique properties
            if (contribution.getMediaTypeCode() == GlobalConstants.MEDIA_PHOTO){
                clCreatorProfileContainer = itemView.findViewById(R.id.itemContributionPhoto_clCreatorProfileContainer);
                tvCreatorName = itemView.findViewById(R.id.itemContributionPhoto_tvCreatorName);
                ivCreatorProfilePicture = itemView.findViewById(R.id.itemContributionPhoto_ivCreatorProfilePicture);
                ivContributionMedia = itemView.findViewById(R.id.itemContributionPhoto_ivContributionMedia);

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
                ivCreatorProfilePicture = itemView.findViewById(R.id.itemContributionVideo_ivCreatorProfilePicture);
                ivVideoThumbnail = itemView.findViewById(R.id.itemContributionVideo_ivVideoThumbnail);

                if (contribution.getMedia() != null){
                    Glide.with(context)
                            .asBitmap()
                            .load(contribution.getMedia().getUrl())
                            .into(ivVideoThumbnail);
                }

            }

            // Bind similar properties
            tvCreatorName.setText(contribution.getUser().getString(ParseUserKey.PROFILE_NAME));
            if (contribution.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE) != null){
                Glide.with(context)
                        .load(contribution.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl())
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
                    fragmentManager.beginTransaction().addToBackStack(profileFragment.getTag()).replace(R.id.activityMain_clContainer, profileFragment).commit();
                }
            });
        }

        public void setOnClickItemView(){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContributionDetailFragment contributionDetailFragment = ContributionDetailFragment.newInstance(contribution);
                    fragmentManager.beginTransaction().addToBackStack(contributionDetailFragment.getTag()).replace(R.id.activityMain_clContainer, contributionDetailFragment).commit();
                }
            });
        }
    }
}
