package com.example.talentfinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talentfinder.R;
import com.example.talentfinder.interfaces.GlobalConstants;
import com.google.android.material.chip.Chip;

import java.util.List;

public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ViewHolder> {

    public static final String TAG = "ChipAdapter";

    Context context;
    List<String> skills;
    int chipCode;

    public ChipAdapter(Context context, List<String> skills, int chipCode) {
        this.context = context;
        this.skills = skills;
        this.chipCode = chipCode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String skill = skills.get(position);
        holder.bind(skill);
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        Chip chipSkill;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chipSkill = itemView.findViewById(R.id.itemChip_chipItem);
        }

        public void bind(final String skill){

            switch (chipCode){
                case GlobalConstants.CHIP_ENTRY:
                    chipSkill.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            skills.remove(skill);
                            notifyDataSetChanged();
                        }
                    });
                    break;
                case GlobalConstants.CHIP_FILTER:
                    chipSkill.setCloseIconVisible(false);
                    chipSkill.setClickable(false);
                    break;
                case GlobalConstants.CHIP_ACTION:
                    break;
                case GlobalConstants.CHIP_CHOICE:
                    break;
                default:
                    break;
            }
            chipSkill.setText(skill);
        }
    }
}
