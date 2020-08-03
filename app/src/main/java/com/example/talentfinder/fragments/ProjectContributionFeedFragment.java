package com.example.talentfinder.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.talentfinder.adapters.ContributionAdapter;
import com.example.talentfinder.databinding.FragmentProjectContributionFeedBinding;
import com.example.talentfinder.models.Contribution;
import com.example.talentfinder.models.Project;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProjectContributionFeedFragment extends Fragment {

    public static final String TAG = "ProjectContributionFeed";

    private FragmentManager fragmentManager;

    private FragmentProjectContributionFeedBinding binding;
    private Project project;
    private List<Contribution> contributions;
    private LinearLayoutManager linearLayoutManager;
    private ContributionAdapter contributionAdapter;
    private DividerItemDecoration dividerItemDecoration;

    public ProjectContributionFeedFragment() {
        // Required empty public constructor
    }

    public static ProjectContributionFeedFragment newInstance(Project project) {
        ProjectContributionFeedFragment fragment = new ProjectContributionFeedFragment();
        Bundle args = new Bundle();
        args.putParcelable("project", project);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProjectContributionFeedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getFragmentManager();
        project = getArguments().getParcelable("project");

        // Recycler view and adapter creation
        linearLayoutManager = new LinearLayoutManager(getContext());
        contributions = new ArrayList<>();
        contributionAdapter = new ContributionAdapter(getContext(), contributions, getFragmentManager());
        binding.fragmentProjectContributionFeedRvContributions.setAdapter(contributionAdapter);
        binding.fragmentProjectContributionFeedRvContributions.setLayoutManager(linearLayoutManager);

        // Add divider for RecyclerView
        dividerItemDecoration = new DividerItemDecoration(binding.fragmentProjectContributionFeedRvContributions.getContext(), linearLayoutManager.getOrientation());
        binding.fragmentProjectContributionFeedRvContributions.addItemDecoration(dividerItemDecoration);

        binding.fragmentProjectContributionFeedTvProjectName.setText(project.getTitle());

        getContributions();
    }

    private void getContributions(){
        ParseQuery<ParseObject> query = project.getRelation(Project.KEY_CONTRIBUTIONS).getQuery();

        // Only query public contributions if the viewer is not the project's user
        if (ParseUser.getCurrentUser() != project.getUser()) {
            query.whereEqualTo(Contribution.KEY_PRIVATE_CONTRIBUTION, false);
        }

        query.orderByAscending(Contribution.KEY_CREATED_AT);
        query.include(Contribution.KEY_USER);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error loading project contributions", e );
                    return;
                }
                for (ParseObject object : objects){
                    contributions.add((Contribution) object);
                }
                contributionAdapter.notifyDataSetChanged();
            }
        });
    }
}