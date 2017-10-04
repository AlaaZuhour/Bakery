package com.example.alaazuhour.bakery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alaazuhour.bakery.R;
import com.example.alaazuhour.bakery.model.Ingredient;
import com.example.alaazuhour.bakery.model.Step;

import java.util.ArrayList;

/**
 * Created by alaazuhouer on 29/09/17.
 */

public class StepAdapter extends ArrayAdapter<Step> {
    ArrayList<Step> stepsList;
    public StepAdapter(@NonNull Context context, ArrayList<Step> steps) {
        super(context, 0,steps);
        this.stepsList= steps;
    }

    @Override
    public int getCount() {
        return stepsList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.recipe_item, null);
        }
        Step current = stepsList.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.textView3);

        title.setText(current.getShortDescription());

        convertView.setTag(position);
        return convertView;
    }

    public void swapArray(ArrayList<Step> steps){
        stepsList = steps;
        this.notifyDataSetChanged();
    }
}
