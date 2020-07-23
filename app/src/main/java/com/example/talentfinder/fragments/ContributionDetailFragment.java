package com.example.talentfinder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
            photoBinding.fragmentContributionDetailPhotoTvProfileName.setText(contribution.getUser().getString(ParseUserKey.PROFILE_NAME));
            photoBinding.fragmentContributionDetailPhotoTvProfileLocation.setText(contribution.getUser().getString(ParseUserKey.PROFILE_LOCATION));
            if (contribution.getMedia() != null){
                Glide.with(getContext())
                        .load(contribution.getMedia().getUrl())
                        .into(photoBinding.fragmentContributionDetailPhotoIvMediaPhoto);
            }

            if (contribution.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE) != null){
                Glide.with(getContext())
                        .load(contribution.getUser().getParseFile(ParseUserKey.PROFILE_IMAGE).getUrl())
                        .circleCrop()
                        .into(photoBinding.fragmentContributionDetailPhotoIvProfilePicture);
            }
        }
    }
}