package com.example.alaazuhour.bakery.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alaazuhour.bakery.MainActivity;
import com.example.alaazuhour.bakery.R;
import com.example.alaazuhour.bakery.RecipeDetailActivity;
import com.example.alaazuhour.bakery.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getContext;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder>{
    private ArrayList<Recipe> recipesList;
   private Context mContext;
    private ItemClickListener mListener;
   public RecipeAdapter(Context context,ArrayList<Recipe> recipes){
       this.recipesList = recipes;
       mListener = (ItemClickListener) context;
       this.mContext = context;
   }

    public void swapArray(ArrayList<Recipe> recipes){
        recipesList = recipes;
        this.notifyDataSetChanged();
    }

    @Override
    public RecipeAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent,  false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecyclerViewHolder holder, int position) {
        holder.recipeName.setText(recipesList.get(position).getName());
        String imageUrl=recipesList.get(position).getImage();

        if (imageUrl!="") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(mContext).load(builtUri).into(holder.recipeImage);
        }


    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeName;
        ImageView recipeImage;


        public RecyclerViewHolder(View itemView) {
            super(itemView);

            recipeName = (TextView) itemView.findViewById(R.id.textView3);
            recipeImage = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.onItemClickListener(clickedPosition);
        }
    }

    public interface ItemClickListener{
        void onItemClickListener(int postion);
    }
}
