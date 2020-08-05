package com.example.talentfinder.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.talentfinder.databinding.FragmentContributionDetailPhotoBinding;
import com.example.talentfinder.databinding.FragmentContributionDetailVideoBinding;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.example.talentfinder.interfaces.ParseUserKey;
import com.example.talentfinder.models.Contribution;

public class ContributionDetailFragment extends Fragment {

    public static final String TAG = "ContributionDetailFragment";

    private Contribution contribution;
    private Context context;
    private FragmentContributionDetailPhotoBinding photoBinding;
    private FragmentContributionDetailVideoBinding videoBinding;

    public ContributionDetailFragment() {
        // Required empty public constructor
    }

    public static ContributionDetailFragment newInstance(Contribution contribution) {
        ContributionDetailFragment fragment = new ContributionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("contribution", contribution);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        contribution = getArguments().getParcelable("contribution");

        View view;

        if (contribution.getMediaTypeCode() == GlobalConstants.MEDIA_PHOTO){
            photoBinding = FragmentContributionDetailPhotoBinding.inflate(inflater, container, false);
            view = photoBinding.getRoot();
        }
        else {
            videoBinding = FragmentContributionDetailVideoBinding.inflate(inflater, container, false);
            view = videoBinding.getRoot();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (contribution.getMediaTypeCode() == GlobalConstants.MEDIA_PHOTO){
            photoBinding.fragmentContributionDetailPhotoPbMedia.setVisibility(View.VISIBLE);
            photoBinding.fragmentContributionDetailPhotoTvProfileName.setText(contribution.getUser().getString(ParseUserKey.PROFILE_NAME));
            photoBinding.fragmentContributionDetailPhotoTvProfileLocation.setText(contribution.getUser().getString(ParseUserKey.PROFILE_LOCATION));
            photoBinding.fragmentContributionDetailPhotoTvContentDescription.setText(contribution.getContentDescription());
            if (contribution.getMedia() != null){
                Glide.with(context)
                        .load(contribution.getMedia().getUrl())
                        .into(photoBinding.fragmentContributionDetailPhotoIvMediaPhoto);
            }

            if (contribution.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE) != null){
                Glide.with(context)
                        .load(contribution.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl())
                        .circleCrop()
                        .into(photoBinding.fragmentContributionDetailPhotoIvProfilePicture);
            }
            photoBinding.fragmentContributionDetailPhotoPbMedia.setVisibility(View.GONE);
        }

        else {
            videoBinding.fragmentContributionDetailVideoPbMedia.setVisibility(View.VISIBLE);
            videoBinding.fragmentContributionDetailVideoTvProfileName.setText(contribution.getUser().getString(ParseUserKey.PROFILE_NAME));
            videoBinding.fragmentContributionDetailVideoTvProfileLocation.setText(contribution.getUser().getString(ParseUserKey.PROFILE_LOCATION));
            videoBinding.fragmentContributionDetailVideoTvContentDescription.setText(contribution.getContentDescription());
            if (contribution.getMedia() != null){

                videoBinding.fragmentContributionDetailVideoVvMediaVideo.setVideoPath(contribution.getMedia().getUrl());

                // When video size is adjusted, attach media controller
                // Prevents media controller from displaying in entire itemView
                videoBinding.fragmentContributionDetailVideoVvMediaVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        videoBinding.fragmentContributionDetailVideoPbMedia.setVisibility(View.GONE);
                        videoBinding.fragmentContributionDetailVideoIvPlay.setVisibility(View.VISIBLE);

                        videoBinding.fragmentContributionDetailVideoIvPlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                videoBinding.fragmentContributionDetailVideoIvPlay.setVisibility(View.GONE);
                                videoBinding.fragmentContributionDetailVideoVvMediaVideo.start();
                                videoBinding.fragmentContributionDetailVideoVvMediaVideo.setOnClickListener(null);
                            }
                        });

                        mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                            @Override
                            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                MediaController mediaController = new MediaController(context);
                                videoBinding.fragmentContributionDetailVideoVvMediaVideo.setMediaController(mediaController);
                                mediaController.setAnchorView(videoBinding.fragmentContributionDetailVideoVvMediaVideo);
                            }
                        });
                    }
                });
            }

            if (contribution.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE) != null){
                Glide.with(context)
                        .load(contribution.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl())
                        .circleCrop()
                        .into(videoBinding.fragmentContributionDetailVideoIvProfilePicture);
            }
        }
    }
}