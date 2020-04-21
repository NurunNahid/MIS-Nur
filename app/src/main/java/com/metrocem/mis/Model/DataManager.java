package com.metrocem.mis.Subclasses;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.annotation.MainThread;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;

import android.graphics.drawable.ColorDrawable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.metrocem.mis.CommissionAndIncentive.PromotionContainer;
import com.metrocem.mis.Container.ChallanContainer;
import com.metrocem.mis.Container.DOOrderContainer;
import com.metrocem.mis.Container.DealerListContainer;
import com.metrocem.mis.Container.DeliveredOrderContainer;
import com.metrocem.mis.Container.DeliveryModeContainer;
import com.metrocem.mis.Container.ProductContainer;
import com.metrocem.mis.Container.RetailerContainer;
import com.metrocem.mis.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataManager {

    private static String USER_PREFERENCE = "USER_PREFERENCE";
    private static String USER_KEY = "USER_";
    private static String MODE_PREFERENCE = "MODE_PREFERENCE";
    private static String MODE_KEY = "MODE_KEY";
    private static String PRODUCT_PREFERENCE = "PRODUCT_PREFERENCE";
    private static String PRODUCT_KEY = "PRODUCT_KEY";
    private static String RETAILER_PREFERENCE = "RETAILER_PREFERENCE";
    private static String RETAILER_KEY = "RETAILER_KEY";
    private static String DO_PREFERENCE = "DO_PREFERENCE";
    private static String DO_KEY = "DO_KEY";
    private static String DELIVERED_PREFERENCE = "DELIVERED_PREFERENCE";
    private static String DELIVERED_KEY = "DELIVERED_KEY";
    private static String PROMOTION_PREFERENCE = "PROMOTION_PREFERENCE";
    private static String PROMOTION_KEY = "PROMOTION_KEY";
    private static String DEALER_PREFERENCE = "DEALER_PREFERENCE";
    private static String DEALER_KEY = "DEALER_KEY";
    private static String CHALLAN_PREFERENCE = "CHALLAN_PREFERENCE";
    private static String CHALLAN_KEY = "CHALLAN_KEY";
    private static String SECURITY_PREFERENCE = "SECURITY_PREFERENCE";
    private static String SECURITY_KEY = "SECURITY_KEY";
    private static String FIREBASE_PREFERENCE = "FIREBASE_PREFERENCE";
    private static String TOKEN_KEY = "TOKEN_KEY";

    public static void storeToken(String token, Context context){


        SharedPreferences sp = context.getSharedPreferences(FIREBASE_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Gson gson = new Gson();
        String userString = gson.toJson(token);
        editor.putString(TOKEN_KEY, userString);

        editor.commit();
    }

    public static String getToken(Context context){
        SharedPreferences spFirebase = context.getSharedPreferences(FIREBASE_PREFERENCE, context.MODE_PRIVATE);
        return spFirebase.getString(TOKEN_KEY, null);

    }
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

    public static void setDeliveryModeItems(ArrayList type, Context context){

        SharedPreferences cartSP = context.getSharedPreferences(MODE_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = cartSP.edit();

        Gson gson = new Gson();
        String cartString = gson.toJson(type);
        //Log.d("cart", cartString);
        editor.putString(MODE_KEY, cartString);

        editor.commit();
    }

    public static ArrayList getDeliveryModeItems(Context context){

        SharedPreferences settings = context.getSharedPreferences(MODE_PREFERENCE, context.MODE_PRIVATE);

        List<DeliveryModeContainer> modeList = null;

        if (settings.contains(MODE_KEY)) {

            String items = settings.getString(MODE_KEY, null);
            Gson gson = new Gson();

            DeliveryModeContainer[] favoriteItems = gson.fromJson(items,
                    DeliveryModeContainer[].class);

            modeList = Arrays.asList(favoriteItems);
            modeList = new ArrayList<DeliveryModeContainer>(modeList);

        }
        else
            return null;

        return (ArrayList<DeliveryModeContainer>) modeList;
    }

    public static void removeDeliveryMode(Context context){

        SharedPreferences cartSP = context.getSharedPreferences(MODE_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = cartSP.edit();
        editor.clear();
        editor.commit();
    }


    public static void setProductItems(ArrayList product, Context context){

        SharedPreferences productSP = context.getSharedPreferences(PRODUCT_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = productSP.edit();

        Gson gson = new Gson();
        String productString = gson.toJson(product);
        //Log.d("cart", cartString);
        editor.putString(PRODUCT_KEY, productString);

        editor.commit();
    }

    public static ArrayList getProductItems(Context context){

        SharedPreferences settings = context.getSharedPreferences(PRODUCT_PREFERENCE, context.MODE_PRIVATE);

        List<ProductContainer> productArray = null;

        if (settings.contains(PRODUCT_KEY)) {

            String items = settings.getString(PRODUCT_KEY, null);
            Gson gson = new Gson();

            ProductContainer[] favoriteItems = gson.fromJson(items,
                    ProductContainer[].class);

            productArray = Arrays.asList(favoriteItems);
            productArray = new ArrayList<ProductContainer>(productArray);

        }
        else
            return null;

        return (ArrayList<ProductContainer>) productArray;
    }

    public static void removeProductItems(Context context){

        SharedPreferences cartSP = context.getSharedPreferences(PRODUCT_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = cartSP.edit();
        editor.clear();
        editor.commit();
    }

    public static void setDealerList(ArrayList dealer, Context context){

        SharedPreferences dealerSP = context.getSharedPreferences(DEALER_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dealerSP.edit();

        Gson gson = new Gson();
        String dealerString = gson.toJson(dealer);
        //Log.d("cart", cartString);
        editor.putString(DEALER_KEY, dealerString);

        editor.commit();
    }

    public static ArrayList getDealerList(Context context){

        SharedPreferences settings = context.getSharedPreferences(DEALER_PREFERENCE, context.MODE_PRIVATE);

        List<DealerListContainer> dealerList = null;

        if (settings.contains(DEALER_KEY)) {

            String items = settings.getString(DEALER_KEY, null);
            Gson gson = new Gson();

            DealerListContainer[] favoriteItems = gson.fromJson(items,
                    DealerListContainer[].class);

            dealerList = Arrays.asList(favoriteItems);
            dealerList = new ArrayList<DealerListContainer>(dealerList);

        }
        else
            return null;

        return (ArrayList<DealerListContainer>) dealerList;
    }

    public static void removeDealerList(Context context){

        SharedPreferences dealerSP = context.getSharedPreferences(DEALER_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dealerSP.edit();
        editor.clear();
        editor.commit();
    }


    public static void setRetailerList(ArrayList retailer, Context context){

        SharedPreferences productSP = context.getSharedPreferences(RETAILER_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = productSP.edit();

        Gson gson = new Gson();
        String retailerString = gson.toJson(retailer);
        //Log.d("cart", cartString);
        editor.putString(RETAILER_KEY, retailerString);

        editor.commit();
    }

    public static ArrayList getRetailerList(Context context){

        SharedPreferences settings = context.getSharedPreferences(RETAILER_PREFERENCE, context.MODE_PRIVATE);

        List<RetailerContainer> retailerList = null;

        if (settings.contains(RETAILER_KEY)) {

            String items = settings.getString(RETAILER_KEY, null);
            Gson gson = new Gson();

            RetailerContainer[] favoriteItems = gson.fromJson(items,
                    RetailerContainer[].class);

            retailerList = Arrays.asList(favoriteItems);
            retailerList = new ArrayList<RetailerContainer>(retailerList);

        }
        else
            return null;

        return (ArrayList<RetailerContainer>) retailerList;
    }

    public static void removeRetailerList(Context context){

        SharedPreferences cartSP = context.getSharedPreferences(RETAILER_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = cartSP.edit();
        editor.clear();
        editor.commit();
    }


    public static void setDOOrderList(ArrayList do_order, Context context){

        SharedPreferences doOrderSP = context.getSharedPreferences(DO_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = doOrderSP.edit();

        Gson gson = new Gson();
        String doString = gson.toJson(do_order);
        //Log.d("cart", cartString);
        editor.putString(DO_KEY, doString);

        editor.commit();
    }

    public static ArrayList getDOOrderList(Context context){

        SharedPreferences settings = context.getSharedPreferences(DO_PREFERENCE, context.MODE_PRIVATE);

        List<DOOrderContainer> doOrderList = null;

        if (settings.contains(DO_KEY)) {

            String items = settings.getString(DO_KEY, null);
            Gson gson = new Gson();

            DOOrderContainer[] favoriteItems = gson.fromJson(items,
                    DOOrderContainer[].class);

            doOrderList = Arrays.asList(favoriteItems);
            doOrderList = new ArrayList<DOOrderContainer>(doOrderList);

        }
        else
            return null;

        return (ArrayList<DOOrderContainer>) doOrderList;
    }

    public static void removeDOOrderList(Context context){

        SharedPreferences cartSP = context.getSharedPreferences(DO_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = cartSP.edit();
        editor.clear();
        editor.commit();
    }

//    public static void setDeliveredOrderList(ArrayList do_order, Context context){
//
//        SharedPreferences doOrderSP = context.getSharedPreferences(DELIVERED_PREFERENCE, context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = doOrderSP.edit();
//
//        Gson gson = new Gson();
//        String doString = gson.toJson(do_order);
//        //Log.d("cart", cartString);
//        editor.putString(DELIVERED_KEY, doString);
//
//        editor.commit();
//    }

//    public static ArrayList getDeliveredOrderList(Context context){
//
//        SharedPreferences settings = context.getSharedPreferences(DELIVERED_PREFERENCE, context.MODE_PRIVATE);
//
//        List<DeliveredOrderContainer> doOrderList = null;
//
//        if (settings.contains(DELIVERED_KEY)) {
//
//            String items = settings.getString(DELIVERED_KEY, null);
//            Gson gson = new Gson();
//
//            DeliveredOrderContainer[] favoriteItems = gson.fromJson(items,
//                    DeliveredOrderContainer[].class);
//
//            doOrderList = Arrays.asList(favoriteItems);
//            doOrderList = new ArrayList<DeliveredOrderContainer>(doOrderList);
//
//        }
//        else
//            return null;
//
//        return (ArrayList<DeliveredOrderContainer>) doOrderList;
//    }

//    public static void removeDeliveredOrderList(Context context){
//
//        SharedPreferences cartSP = context.getSharedPreferences(DELIVERED_PREFERENCE, context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = cartSP.edit();
//        editor.clear();
//        editor.commit();
//    }

    public static void setChallanList(ArrayList challan, Context context){

        SharedPreferences doOrderSP = context.getSharedPreferences(CHALLAN_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = doOrderSP.edit();

        Gson gson = new Gson();
        String challanString = gson.toJson(challan);
        //Log.d("cart", cartString);
        editor.putString(CHALLAN_KEY, challanString);

        editor.commit();
    }

    public static ArrayList getChallanList(Context context){

        SharedPreferences settings = context.getSharedPreferences(CHALLAN_PREFERENCE, context.MODE_PRIVATE);

        List<ChallanContainer> challanList = null;

        if (settings.contains(CHALLAN_KEY)) {

            String items = settings.getString(CHALLAN_KEY, null);
            Gson gson = new Gson();

            ChallanContainer[] favoriteItems = gson.fromJson(items,
                    ChallanContainer[].class);

            challanList = Arrays.asList(favoriteItems);
            challanList = new ArrayList<ChallanContainer>(challanList);

        }
        else
            return null;

        return (ArrayList<ChallanContainer>) challanList;
    }

    public static void removeChallanList(Context context){

        SharedPreferences challanSP = context.getSharedPreferences(CHALLAN_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = challanSP.edit();
        editor.clear();
        editor.commit();
    }


    public static void setPromotionList(ArrayList promotion, Context context){

        SharedPreferences promotionSP = context.getSharedPreferences(PROMOTION_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = promotionSP.edit();

        Gson gson = new Gson();
        String doString = gson.toJson(promotion);
        //Log.d("cart", cartString);
        editor.putString(PROMOTION_KEY, doString);

        editor.commit();
    }

    public static ArrayList getPromotionList(Context context){

        SharedPreferences settings = context.getSharedPreferences(PROMOTION_PREFERENCE, context.MODE_PRIVATE);

        List<PromotionContainer> promotionList = null;

        if (settings.contains(PROMOTION_KEY)) {

            String items = settings.getString(PROMOTION_KEY, null);
            Gson gson = new Gson();

            PromotionContainer[] favoriteItems = gson.fromJson(items,
                    PromotionContainer[].class);

            promotionList = Arrays.asList(favoriteItems);
            promotionList = new ArrayList<PromotionContainer>(promotionList);

        }
        else
            return null;

        return (ArrayList<PromotionContainer>) promotionList;
    }

    public static void removePromotionList(Context context){

        SharedPreferences promotionSP = context.getSharedPreferences(PROMOTION_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = promotionSP.edit();
        editor.clear();
        editor.commit();
    }


    public static void setSecurityList(ArrayList security, Context context){

        SharedPreferences securitySP = context.getSharedPreferences(SECURITY_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = securitySP.edit();

        Gson gson = new Gson();
        String securityString = gson.toJson(security);
        //Log.d("cart", cartString);
        editor.putString(SECURITY_KEY, securityString);

        editor.commit();
    }

    public static ArrayList getSecurityList(Context context){

        SharedPreferences settings = context.getSharedPreferences(SECURITY_PREFERENCE, context.MODE_PRIVATE);

        List<SecurityInfoContainer> securityList = null;

        if (settings.contains(SECURITY_KEY)) {

            String items = settings.getString(SECURITY_KEY, null);
            Gson gson = new Gson();

            SecurityInfoContainer[] favoriteItems = gson.fromJson(items,
                    SecurityInfoContainer[].class);

            securityList = Arrays.asList(favoriteItems);
            securityList = new ArrayList<SecurityInfoContainer>(securityList);

        }
        else
            return null;

        return (ArrayList<SecurityInfoContainer>) securityList;
    }

    public static void removeSecurityList(Context context){

        SharedPreferences securitySP = context.getSharedPreferences(SECURITY_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = securitySP.edit();
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
            }
        });
        builderInner.show();
    }

    public static void onlyAlertShow(String msgString, Context context){

        AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
        builderInner.setTitle("Error!");
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

    public static String getUniqueIMEIId(Context context) {
        Log.d("response hello", "call");
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            String imei = telephonyManager.getDeviceId();
            Log.e("response zero", "=" + imei);
            if (imei != null && !imei.isEmpty()) {
                return imei;
            } else {
                return android.os.Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "not_found";
    }




//    DeliveryModeContainer type = new DeliveryModeContainer();
//    ArrayList arrayList = new ArrayList();
//
//    //modelist.add(new DeliveryModeContainer());
//
//                    for (int i = 0; i < modeList.size(); i++) {
//        type.deliveryType = modeList.get(i).getName();
//        arrayList.add(type);
//
//    }
//
//
//
//                    DataManager.setDeliveryModeItems(arrayList, getContext());
//
//    deliveryModeType = DataManager.getDeliveryModeItems(getContext());
//
//                    modeAdapter.notifyDataSetChanged();



}
