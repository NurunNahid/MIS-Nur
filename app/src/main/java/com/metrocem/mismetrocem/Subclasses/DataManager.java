package com.metrocem.mismetrocem.Subclasses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;

public class DataManager {

    private static String USER_PREFERENCE = "USER_PREFERENCE";
    private static String USER_KEY = "USER_";


    public static void setCurrentUser(CurrentUser user, Context context){


        SharedPreferences sp = context.getSharedPreferences(USER_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Gson gson = new Gson();
        String userString = gson.toJson(user);
        editor.putString(USER_KEY, userString);

        editor.commit();
    }

    public static CurrentUser getCurrentUser(Context context) {

        SharedPreferences settings = context.getSharedPreferences(USER_PREFERENCE, context.MODE_PRIVATE);

        CurrentUser user = null;

        if (settings.contains(USER_KEY)) {
            String jsonFavorites = settings.getString(USER_KEY, null);

            if (jsonFavorites != null){
                Gson gson = new Gson();
                CurrentUser currentUser = gson.fromJson(jsonFavorites,
                        CurrentUser.class);

                if (currentUser != null){

                    user = currentUser;
                }
            }

        }
        return  user;
    }

    public static void removeCurrentUser(Context context){

        SharedPreferences cartSP = context.getSharedPreferences(USER_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = cartSP.edit();
        editor.clear();
        editor.commit();
    }



    public static void alertShow(String title, String msgString, Context context){

        AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
        builderInner.setTitle(title);
        builderInner.setMessage(msgString);
        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
                //onBackPressed();
            }
        });
        builderInner.show();
    }

}
