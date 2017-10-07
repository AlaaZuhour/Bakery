package com.example.alaazuhour.bakery.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alaazuhour.bakery.R;
import com.example.alaazuhour.bakery.UpdateBakeryService;
import com.example.alaazuhour.bakery.adapter.StepAdapter;
import com.example.alaazuhour.bakery.model.Ingredient;
import com.example.alaazuhour.bakery.model.Recipe;
import com.example.alaazuhour.bakery.model.Step;

import java.util.ArrayList;


public class RecipeDetailFragment extends Fragment implements StepAdapter.StepClickListener{


    private RecipeDetailFragmentListener  mListener;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Recipe recipe = getActivity().getIntent().getExtras().getParcelable("recipe");
        ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) recipe.getIngredients();
        final ArrayList<Step> steps = (ArrayList<Step>) recipe.getSteps();
        LinearLayoutManager layoutManager;
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        TextView ingredientView = (TextView) rootView.findViewById(R.id.textView4);
        RecyclerView stepView = (RecyclerView) rootView.findViewById(R.id.steps_list);
        layoutManager =
                new LinearLayoutManager(getActivity());
        stepView.setLayoutManager(layoutManager);
        stepView.setHasFixedSize(true);
        StepAdapter stepAdapter = new StepAdapter(this,steps);
        ArrayList<String> widgetIngredient= new ArrayList<>();

        for(int i =0;i<ingredients.size();i++){
            ingredientView.append("\t\u2022 "+ ingredients.get(i).getIngredient()+"\n");
            ingredientView.append("\t\t\t Quantity: "+ingredients.get(i).getQuantity()+"\n");
            ingredientView.append("\t\t\t Measure: "+ingredients.get(i).getMeasure()+"\n\n");

        }
        stepView.setAdapter(stepAdapter);

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeDetailFragmentListener ) {
            mListener = (RecipeDetailFragmentListener ) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStepItemClick(int index) {
        mListener.onStepDetailClick(index);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface RecipeDetailFragmentListener {
        void onStepDetailClick(int selected);
    }
}
