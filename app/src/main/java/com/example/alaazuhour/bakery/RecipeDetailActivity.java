package com.example.alaazuhour.bakery;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.alaazuhour.bakery.fragment.RecipeDetailFragment;
import com.example.alaazuhour.bakery.fragment.RecipeStepDetailFragment;
import com.example.alaazuhour.bakery.model.Recipe;

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailFragment.RecipeDetailFragmentListener,
        RecipeStepDetailFragment.OnButtonClickListener {
    private boolean inStepsFragment ;
    private int stepIndex;
    public static Recipe recipe;
    private RecipeDetailFragment fragment;
    private RecipeStepDetailFragment fragment1;
    private long playerPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragment = new RecipeDetailFragment();
        fragment1 = new RecipeStepDetailFragment();

        recipe = getIntent().getExtras().getParcelable("recipe");
        getSupportActionBar().setTitle(recipe.getName());

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                if (findViewById(R.id.fragment_container2)==null) {
                    if (fm.getBackStackEntryCount() > 1) {
                        //go back to "Recipe Detail" screen
                        fm.popBackStack(null, 0);
                        inStepsFragment = false;
                    } else if (fm.getBackStackEntryCount() > 0) {
                        //go back to "Recipe" screen
                        finish();

                    }


                }
                else {

                    //go back to "Recipe" screen
                    finish();

                }

            }
        });
        if(savedInstanceState == null ){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(null)
                    .commit();
            if (findViewById(R.id.recipe_layout).getTag() != null && findViewById(R.id.recipe_layout).getTag().equals("tablet-land")) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container2, fragment1).addToBackStack(null)
                        .commit();
            }

        }else{
            if(savedInstanceState.getBoolean("in_steps")) {
                playerPos = savedInstanceState.getLong("playerPos");
                onStepDetailClick(savedInstanceState.getInt("step_index"));
            }
            else {
                if(fragment != null){
                    fragment.setRecyclerPostion(savedInstanceState.getInt("recycler"));
                    fragment.setPosition(savedInstanceState.getIntArray("scroll"));
                }
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
    }

//    @Override
//    public void onBackPressed() {
//        if(findViewById(R.id.fragment_container2) == null) {
//            FragmentManager fm = getSupportFragmentManager();
//            if (fm.getBackStackEntryCount() > 1) {
//                //go back to "Recipe Detail" screen
//                fm.popBackStack(null, 0);
//            } else if (fm.getBackStackEntryCount() > 0) {
//                //go back to "Recipe" screen
//                finish();
//
//            }
//        }else {
//            finish();
//        }
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(fragment != null) {
            outState.putInt("recycler", fragment.getRecyclerPostion());
            outState.putIntArray("scroll", fragment.getPosition());
        }
        if(fragment1 != null){
            outState.putLong("playerPos",fragment1.getPlayerPostion());
        }
        outState.putInt("step_index",stepIndex);
        outState.putBoolean("in_steps",inStepsFragment);

    }

    @Override
    public void onStepDetailClick(int index) {
        inStepsFragment = true;
        stepIndex = index;
        Bundle arg = new Bundle();
        arg.putInt("selectd",index);
        fragment1 = new RecipeStepDetailFragment();
        fragment1.setArguments(arg);
        fragment1.setPlayerPostion(playerPos);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.recipe_layout).getTag()!=null && findViewById(R.id.recipe_layout).getTag().equals("tablet-land")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container2, fragment1).addToBackStack(null)
                    .commit();

        }
        else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment1).addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onButtonClick(int i) {
        onStepDetailClick(i);
    }
}
