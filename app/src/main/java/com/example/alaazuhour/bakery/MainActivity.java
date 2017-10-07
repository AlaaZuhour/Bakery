package com.example.alaazuhour.bakery;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alaazuhour.bakery.adapter.RecipeAdapter;
import com.example.alaazuhour.bakery.fragment.RecipeDetailFragment;
import com.example.alaazuhour.bakery.model.Ingredient;
import com.example.alaazuhour.bakery.model.Recipe;
import com.example.alaazuhour.bakery.retrofit.IRecipe;
import com.example.alaazuhour.bakery.retrofit.RetrofitBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ItemClickListener {
    private ArrayList<Recipe> recipes;
    private RecyclerView recipesListView;
    private RecipeAdapter recipeAdapter;
    private GridLayoutManager layoutManager;
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
        recipesListView = (RecyclerView) findViewById(R.id.recipes_list);
        int ot = getResources().getConfiguration().orientation;
        layoutManager =
                new GridLayoutManager(this,ot == Configuration.ORIENTATION_LANDSCAPE ? 4 : 1);
        recipesListView.setLayoutManager(layoutManager);
        recipesListView.setHasFixedSize(true);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Baking App");
        if(savedInstanceState != null){
            recipes = savedInstanceState.getParcelableArrayList("recipes");
            if(recipes != null) {
                recipeAdapter = new RecipeAdapter(this, recipes);
                recipesListView.setAdapter(recipeAdapter);
            }else{
                showErrorMessage();
            }
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
                    if(recipes != null) {
                        recipeAdapter = new RecipeAdapter(MainActivity.this, recipes);
                        recipesListView.setAdapter(recipeAdapter);
                    }else{
                        showErrorMessage();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }

    }

    private void showErrorMessage() {
        Toast.makeText(this,"Please Connect to the internet and restart the app",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClickListener(int postion) {
        UpdateBakeryService.startBakingService(this, (ArrayList<Ingredient>) recipes.get(postion).getIngredients());
        Intent intent = new Intent(MainActivity.this,RecipeDetailActivity.class);
        intent.putExtra("recipe",recipes.get(postion));
        startActivity(intent);

    }
}
