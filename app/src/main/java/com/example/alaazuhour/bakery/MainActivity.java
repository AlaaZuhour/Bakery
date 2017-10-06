package com.example.alaazuhour.bakery;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.alaazuhour.bakery.adapter.RecipeAdapter;
import com.example.alaazuhour.bakery.fragment.RecipeDetailFragment;
import com.example.alaazuhour.bakery.model.Recipe;
import com.example.alaazuhour.bakery.retrofit.IRecipe;
import com.example.alaazuhour.bakery.retrofit.RetrofitBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Recipe> recipes;
    private GridView recipesListView;
    private RecipeAdapter recipeAdapter;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipesListView = (GridView) findViewById(R.id.recipes_list);
        int ot = getResources().getConfiguration().orientation;
        recipesListView.setNumColumns(ot == Configuration.ORIENTATION_LANDSCAPE ? 4: 1);
        if(savedInstanceState != null){
            recipes = savedInstanceState.getParcelableArrayList("recipes");
            recipeAdapter = new RecipeAdapter(MainActivity.this, recipes);
            recipesListView.setAdapter(recipeAdapter);
        }else {
            IRecipe recipeService = RetrofitBuilder.Retrieve();
            Call<ArrayList<Recipe>> recipeList = recipeService.getRecipe();

            mIdlingResource = (SimpleIdlingResource) getIdlingResource();
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(false);
            }
            recipeList.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                    recipes = response.body();
                    Log.d("response", recipes.get(0).getName());
                    if (mIdlingResource != null) {
                        mIdlingResource.setIdleState(true);
                    }
                    recipeAdapter = new RecipeAdapter(MainActivity.this, recipes);
                    recipesListView.setAdapter(recipeAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }

        recipesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,RecipeDetailActivity.class);
                intent.putExtra("recipe",recipes.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
      outState.putParcelableArrayList("recipes",recipes);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

}
