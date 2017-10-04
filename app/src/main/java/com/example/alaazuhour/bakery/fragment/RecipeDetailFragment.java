package com.example.alaazuhour.bakery.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class RecipeDetailFragment extends Fragment {


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
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        TextView ingredientView = (TextView) rootView.findViewById(R.id.textView4);
        ListView stepView = (ListView) rootView.findViewById(R.id.steps_list);
        StepAdapter stepAdapter = new StepAdapter(getActivity(),steps);
        ArrayList<String> widgetIngredient= new ArrayList<>();

        for(int i =0;i<ingredients.size();i++){
            ingredientView.append("\t\u2022 "+ ingredients.get(i).getIngredient()+"\n");
            ingredientView.append("\t\t\t Quantity: "+ingredients.get(i).getQuantity()+"\n");
            ingredientView.append("\t\t\t Measure: "+ingredients.get(i).getMeasure()+"\n\n");

            widgetIngredient.add(ingredients.get(i).getIngredient()+"\n"+
                    "Quantity: "+ingredients.get(i).getQuantity()+"\n"+
                    "Measure: "+ingredients.get(i).getMeasure()+"\n");
        }
        stepView.setAdapter(stepAdapter);
        stepView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.onStepDetailClick(i);
            }
        });

        UpdateBakeryService.startBakingService(getActivity(),widgetIngredient);
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
        // TODO: Update argument type and name
        void onStepDetailClick(int selected);
    }
}
