package com.example.alaazuhour.bakery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alaazuhour.bakery.R;
import com.example.alaazuhour.bakery.model.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends ArrayAdapter<Recipe>{
    private ArrayList<Recipe> recipesList;
    public RecipeAdapter(@NonNull Context context, ArrayList<Recipe> recipes) {
        super(context, 0,recipes);
        this.recipesList= recipes;
    }

    @Override
    public int getCount() {
        return recipesList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(),R.layout.recipe_item, null);
        }
        Recipe current = recipesList.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.textView3);

        title.setText(current.getName());

        convertView.setTag(position);
        return convertView;
    }

    public void swapArray(ArrayList<Recipe> recipes){
        recipesList = recipes;
        this.notifyDataSetChanged();
    }
}
