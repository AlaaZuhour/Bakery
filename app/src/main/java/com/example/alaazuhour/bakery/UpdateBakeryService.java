
package com.example.alaazuhour.bakery;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.example.alaazuhour.bakery.model.Ingredient;

import java.util.ArrayList;


public class UpdateBakeryService extends IntentService {


    public UpdateBakeryService() {
        super("UpdateBakeryService");
    }

    public static void startBakingService(Context context, ArrayList<Ingredient> ingredientsList) {
        Intent intent = new Intent(context, UpdateBakeryService.class);
        intent.putParcelableArrayListExtra("ingredent_list", ingredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<Ingredient> ingredientsList = intent.getExtras().getParcelableArrayList("ingredent_list");
            Intent intent1 = new Intent("android.appwidget.action.APPWIDGET_UPDATE1");
            String ingredients = null;
            for(int i =0;i<ingredientsList.size();i++){
                ingredients= "\t\u2022 "+ ingredientsList.get(i).getIngredient()+"\n"+
                "\t\t\t Quantity: "+ingredientsList.get(i).getQuantity()+"\n"+
                "\t\t\t Measure: "+ingredientsList.get(i).getMeasure()+"\n\n";

            }
            intent.putExtra("ingredents", ingredients);
            sendBroadcast(intent1);

        }
    }


}
