
package com.example.alaazuhour.bakery;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;


public class UpdateBakeryService extends IntentService {


    public UpdateBakeryService() {
        super("UpdateBakeryService");
    }

    public static void startBakingService(Context context, ArrayList<String> ingredientsList) {
        Intent intent = new Intent(context, UpdateBakeryService.class);
        intent.putExtra("ingredent_list", ingredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<String> ingredientsList = intent.getExtras().getStringArrayList("ingredent_list");
            Intent intent1 = new Intent("android.appwidget.action.APPWIDGET_UPDATE1");
            intent.putExtra("ingredent_list", ingredientsList);
            sendBroadcast(intent1);

        }
    }


}
