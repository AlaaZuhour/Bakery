package com.example.alaazuhour.bakery.widget;

import android.appwidget.AppWidgetManager;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.alaazuhour.bakery.R;
import com.example.alaazuhour.bakery.adapter.RecipeAdapter;
import com.example.alaazuhour.bakery.model.Ingredient;
import com.example.alaazuhour.bakery.model.Recipe;
import com.example.alaazuhour.bakery.retrofit.IRecipe;
import com.example.alaazuhour.bakery.retrofit.RetrofitBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;


public class ConfigurationActivity extends AppCompatActivity implements RecipeAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> recipes;

    private int widgetId;
     private int recipeId;
    private final static String TAG=ConfigurationActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            recipeId=savedInstanceState.getInt("recipeId");
            widgetId=savedInstanceState.getInt(EXTRA_APPWIDGET_ID);
            recipes=savedInstanceState.getParcelableArrayList("recipes");
        }
        setContentView(R.layout.activity_main);
        //if the user hit the return button or cancel the config activity it wont add the widget

        recyclerView=(RecyclerView)findViewById(R.id.recipes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        IRecipe recipeService = RetrofitBuilder.Retrieve();
        Call<ArrayList<Recipe>> recipeList = recipeService.getRecipe();
        recipeList.enqueue(new Callback<ArrayList<Recipe>>(){

            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                recipes = response.body();
                if(recipes != null) {
                    recipeAdapter = new RecipeAdapter(ConfigurationActivity.this, recipes);
                    recyclerView.setAdapter(recipeAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {

            }
        });

        setResult(RESULT_CANCELED);





    }


    private void showWidgwt(int position){
        //first we want to extract the widget id

        widgetId= AppWidgetManager.INVALID_APPWIDGET_ID;
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            widgetId=bundle.getInt(EXTRA_APPWIDGET_ID,INVALID_APPWIDGET_ID);


                ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) recipes.get(position).getIngredients();
            StringBuilder ingredient = new StringBuilder();
        for(int i =0;i<ingredients.size();i++){
            ingredient.append("\t\u2022 "+ ingredients.get(i).getIngredient()+"\n"+
                    "\t\t\t Quantity: "+ingredients.get(i).getQuantity()+"\n"+
                    "\t\t\t Measure: "+ingredients.get(i).getMeasure()+"\n\n");
        }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

            int [] array=new int[1];
            array[0]=widgetId;


            BakeryAppWidget.updateBakingWidgets(this, appWidgetManager,ingredient.toString(),array);



           setResult(RESULT_OK);
            finish();
        }
        if (widgetId == INVALID_APPWIDGET_ID) {

            finish();
        }

        }





    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelableArrayList("recipes",recipes);
        outState.putInt(EXTRA_APPWIDGET_ID,widgetId);
        outState.putInt("recipeId",recipeId);
    }

    @Override
    public void onItemClickListener(int postion) {
        showWidgwt(postion);
    }
}