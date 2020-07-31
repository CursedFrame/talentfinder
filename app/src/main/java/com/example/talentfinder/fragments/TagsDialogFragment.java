package com.example.talentfinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.talentfinder.R;
import com.example.talentfinder.activities.MainActivity;
import com.example.talentfinder.databinding.FragmentTagsDialogBinding;
import com.example.talentfinder.interfaces.GlobalConstants;

import java.util.ArrayList;
import java.util.List;

public class TagsDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "TagsDialogFragment";

    public static final int TYPE_SORT = 100;
    public static final int TYPE_FILTER = 101;

    FragmentTagsDialogBinding binding;
    MainActivity mainActivity;
    Context context;
    List<String> currentTags;
    int type;

    public TagsDialogFragment() {
        // Required empty public constructor
    }

    public static TagsDialogFragment newInstance(List<String> tags) {
        TagsDialogFragment fragment = new TagsDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("currentTags", (ArrayList<String>) tags);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTagsDialogBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        mainActivity = (MainActivity) getActivity();

        if (currentTags != null) {
            for (String tag : currentTags) {
                Log.i(TAG, "onViewCreated: " + tag);
            }
        }

        // Array adapter for "Skill" spinner
        ArrayAdapter<CharSequence> spnSkillAdapter = ArrayAdapter.createFromResource(context, R.array.skill, R.layout.support_simple_spinner_dropdown_item);
        spnSkillAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.fragmentTagsDialogSpnSkill.setAdapter(spnSkillAdapter);

        // Array adapter for "Talent" spinner
        ArrayAdapter<CharSequence> spnTalentAdapter = ArrayAdapter.createFromResource(context, R.array.talent, R.layout.support_simple_spinner_dropdown_item);
        spnTalentAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.fragmentTagsDialogSpnTalent.setAdapter(spnTalentAdapter);

        // Binding listener for when talent item is selected. Initializes and updates an array adapter for "Subtalent" spinner
        binding.fragmentTagsDialogSpnTalent.setOnItemSelectedListener(this);

        // After user has picked tags, saves current tag list
        binding.fragmentTagsDialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.tags.clear();
                mainActivity.tags.add(binding.fragmentTagsDialogSpnTalent.getSelectedItem().toString());
                mainActivity.tags.add(binding.fragmentTagsDialogSpnSubTalent.getSelectedItem().toString());
                mainActivity.tags.add(binding.fragmentTagsDialogSpnSkill.getSelectedItem().toString());
                if (type == TYPE_SORT){
                    mainActivity.sortProjectsByTag();
                }
                else if (type == TYPE_FILTER){
                    mainActivity.filterProjectsByTag();
                }
                dismiss();
            }
        });

        binding.fragmentTagsDialogRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (binding.fragmentTagsDialogRadioSort.isChecked()){
                    type = TYPE_SORT;
                }
                else {
                    type = TYPE_FILTER;
                }
            }
        });

        binding.fragmentTagsDialogRadioSort.toggle();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        ArrayAdapter<CharSequence> spnSubTalentAdapter = null;

        switch (position){
            case GlobalConstants.POSITION_TALENT_NO:
                break;
            case GlobalConstants.POSITION_TALENT_ART:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.art_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case GlobalConstants.POSITION_TALENT_COMEDY:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.comedy_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case GlobalConstants.POSITION_TALENT_DRAWING:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.drawing_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case GlobalConstants.POSITION_TALENT_GRAPHICS:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.graphics_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case GlobalConstants.POSITION_TALENT_MUSIC:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.music_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case GlobalConstants.POSITION_TALENT_PHOTOGRAPHY:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.photography_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case GlobalConstants.POSITION_TALENT_PROGRAMMING:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.programming_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case GlobalConstants.POSITION_TALENT_SINGING:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.singing_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case GlobalConstants.POSITION_TALENT_TEACHING:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.teaching_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case GlobalConstants.POSITION_TALENT_WRITING:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.writing_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
        }

        if (position == GlobalConstants.POSITION_TALENT_NO){
            binding.fragmentTagsDialogSpnSubTalent.setVisibility(View.GONE);
        }
        else {
            binding.fragmentTagsDialogSpnSubTalent.setVisibility(View.VISIBLE);
        }

        binding.fragmentTagsDialogSpnSubTalent.setAdapter(spnSubTalentAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}