package com.example.alaazuhour.bakery.retrofit;

import com.example.alaazuhour.bakery.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by alaazuhouer on 29/09/17.
 */

public interface IRecipe {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
