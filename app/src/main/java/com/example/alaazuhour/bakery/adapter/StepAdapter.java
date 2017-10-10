package com.example.alaazuhour.bakery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alaazuhour.bakery.R;
import com.example.alaazuhour.bakery.fragment.RecipeDetailFragment;
import com.example.alaazuhour.bakery.model.Ingredient;
import com.example.alaazuhour.bakery.model.Step;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getContext;

/**
 * Created by alaazuhouer on 29/09/17.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.RecyclerViewHolder> {
    ArrayList<Step> stepsList;
    RecipeDetailFragment mContext;
    StepClickListener mListener;
    public StepAdapter(@NonNull RecipeDetailFragment context, ArrayList<Step> steps) {
        this.stepsList= steps;
        this.mContext = context;
        this.mListener = (StepClickListener) context;
    }


    public void swapArray(ArrayList<Step> steps){
        stepsList = steps;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent,  false);
        StepAdapter.RecyclerViewHolder viewHolder = new StepAdapter.RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.stepDes.setText(stepsList.get(position).getShortDescription());

    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }



    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView stepDes;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            stepDes = (TextView) itemView.findViewById(R.id.textView3);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.onStepItemClick(clickedPosition);
        }
    }

    public interface StepClickListener{
        void onStepItemClick(int index);
    }
}
