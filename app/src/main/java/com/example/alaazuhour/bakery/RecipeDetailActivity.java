package com.example.alaazuhour.bakery;

import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alaazuhour.bakery.fragment.RecipeDetailFragment;
import com.example.alaazuhour.bakery.fragment.RecipeStepDetailFragment;
import com.example.alaazuhour.bakery.model.Recipe;
import com.example.alaazuhour.bakery.model.Step;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailFragment.RecipeDetailFragmentListener,
        RecipeStepDetailFragment.OnButtonClickListener {
    private boolean inStepsFragment ;
    private int stepIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Recipe recipe = getIntent().getExtras().getParcelable("recipe");
        this.setTitle(recipe.getName());
        if(savedInstanceState != null ){
            onStepDetailClick(savedInstanceState.getInt("step_index"));
        }else {
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(null)
                    .commit();
            if (findViewById(R.id.recipe_layout).getTag() != null && findViewById(R.id.recipe_layout).getTag().equals("tablet-land")) {
                RecipeStepDetailFragment fragment1 = new RecipeStepDetailFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container2, fragment1).addToBackStack(null)
                        .commit();
            }

        }
    }

    @Override
    public void onBackPressed() {
        if(findViewById(R.id.fragment_container2) == null) {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 1) {
                //go back to "Recipe Detail" screen
                fm.popBackStack(null, 0);
            } else if (fm.getBackStackEntryCount() > 0) {
                //go back to "Recipe" screen
                finish();

            }
        }else {
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
       outState.putInt("step_index",stepIndex);
        outState.putBoolean("in_steps",inStepsFragment);
    }

    @Override
    public void onStepDetailClick(int index) {
        inStepsFragment = true;
        stepIndex = index;
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        Bundle arg = new Bundle();
        arg.putInt("selectd",index);
        fragment.setArguments(arg);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.recipe_layout).getTag()!=null && findViewById(R.id.recipe_layout).getTag().equals("tablet-land")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container2, fragment).addToBackStack(null)
                    .commit();

        }
        else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onButtonClick(int i) {
        onStepDetailClick(i);
    }
}
