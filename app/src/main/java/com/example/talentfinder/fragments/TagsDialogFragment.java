package com.example.talentfinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.talentfinder.R;
import com.example.talentfinder.activities.MainActivity;
import com.example.talentfinder.databinding.FragmentTagsDialogBinding;

public class TagsDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "TagsDialogFragment";

    FragmentTagsDialogBinding binding;
    MainActivity mainActivity;
    Context context;

    public TagsDialogFragment() {
        // Required empty public constructor
    }

    public static TagsDialogFragment newInstance() {
        TagsDialogFragment fragment = new TagsDialogFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
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
                mainActivity.sortProjectsByTag();
                dismiss();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Log.i(TAG, "onItemSelected: triggered");
        ArrayAdapter<CharSequence> spnSubTalentAdapter = null;

        switch (position){
            case 0:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.art_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 1:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.comedy_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 2:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.drawing_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 3:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.graphics_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 4:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.music_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 5:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.photography_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 6:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.programming_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 7:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.singing_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 8:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.teaching_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            case 9:
                spnSubTalentAdapter = ArrayAdapter.createFromResource(context, R.array.writing_subs, R.layout.support_simple_spinner_dropdown_item);
                break;
            default:
                break;
        }
        binding.fragmentTagsDialogSpnSubTalent.setAdapter(spnSubTalentAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}