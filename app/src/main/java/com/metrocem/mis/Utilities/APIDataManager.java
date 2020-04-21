package com.metrocem.mis.Utilities;

import android.content.Context;
import android.util.Log;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Container.DOOrderContainer;
import com.metrocem.mis.Container.DealerListContainer;
import com.metrocem.mis.Container.DeliveredOrderContainer;
import com.metrocem.mis.Container.DeliveryModeContainer;
import com.metrocem.mis.Container.ProductContainer;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.Model.CurrentUser;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.Dealer;
import com.metrocem.mis.Model.DealerList;
import com.metrocem.mis.Model.DeliveryMode;
import com.metrocem.mis.Model.DeliveryModeList;
import com.metrocem.mis.Model.Order;
import com.metrocem.mis.Model.OrderList;
import com.metrocem.mis.Model.ProductList;
import com.metrocem.mis.Model.ProductType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIDataManager {


//    public static void getOrderRequest(final Context context){
//
//        DataManager.removeDOOrderList(context);
//
//        //hud.show();
//
//        CurrentUser loggedInUser = DataManager.getCurrentUser(context);
//
//        Call<OrderList> call;
//        if(loggedInUser.role.toLowerCase().equals("dealer")){
//            call = MainActivity.apiClient.getOrderList(loggedInUser.userId);
//
//        }else {
//            call = MainActivity.apiClient.getOrderListByEmployee(loggedInUser.userId);
//        }
//        call.enqueue(new Callback<OrderList>() {
//            @Override
//            public void onResponse(Call<OrderList> call, Response<OrderList> response) {
//
//                if(response.isSuccessful()) {
//
//
//                    OrderList orderList = response.body();
//                    List<Order> orders = orderList.getData();
//
//                    ArrayList<DOOrderContainer> doArrayList = new ArrayList<DOOrderContainer>();
//                    ArrayList doArray = new ArrayList();
//                    DataManager.removeDOOrderList(context);
//                    if (orders.size() > 0){
//
//                        for (Order order: orders){
//
//                            //if (order.getStatus().toLowerCase().equals(order_status.toLowerCase())) {
//
//                                DeliveredOrderContainer doOrderContainer = new DeliveredOrderContainer();
//                                doOrderContainer.id = order.getId();
//                                doOrderContainer.type = order.getType();
//                                doOrderContainer.actualBagQty = order.getActualBagQty();
//                                doOrderContainer.unitPrice = order.getUnitPrice();
//                                doOrderContainer.doNumber = order.getDoNumber();
//                                doOrderContainer.deliveryMode = order.getDeliveryMode();
//                                doOrderContainer.approvedAt = order.getApprovedAt();
//                                doOrderContainer.status = order.getStatus();
//                                doOrderContainer.createdAt = order.getCreatedAt();
//                                doOrderContainer.employeeName = order.getEmployeeName();
//                                doOrderContainer.dealerName = order.getDealerName();
//                                doOrderContainer.productName = order.getProductName();
//                                doOrderContainer.numberOfChallan = order.getNumberOfChallan();
//                                doOrderContainer.deliveryAddress = order.getDeliveryAddress().getAddress();
//                                doOrderContainer.contactNumber = order.getDeliveryAddress().getContactNumber();
//                                doArrayList.add(doOrderContainer);
//                                DataManager.setDOOrderList(doArrayList, context);
//                            //}
//
//
//                        }
//
//                        //doArray = DataManager.getDOOrderList(context);
//
//
//                    }
//
//                    //String name = modeList.get(0).getDealerName();
//                    //Log.d("response", name);
//                    //hud.dismiss();
//
//
//                }else {
//
//                    try {
//                        //hud.dismiss();
//                        //Log.d("response3", "post submitted to API.");
//
//                    } catch (Exception e) {
//                        //Log.d("response4", "post submitted to API." + e.getMessage().toString());
//                        //hud.dismiss();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<OrderList> call, Throwable t) {
//
//                //hud.dismiss();
//                //Log.d("Exception call", t.getLocalizedMessage());
//            }
//
//
//        });
//
//        //return null;
//    }

    public static void getProductList(final Context ctx){

        Call<ProductList> call = MainActivity.apiClient.getProductList();

        call.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {

                if(response.isSuccessful()) {

                    ProductList productList = response.body();
                    List<ProductType> productTypeList = productList.getProductList();

                    if(productTypeList.size() > 0){

                        ArrayList<ProductContainer> productArrayList = new ArrayList<ProductContainer>();

                        for (ProductType product: productTypeList){

                            //Log.d("product", String.valueOf(product.getName()));

                            ProductContainer proType = new ProductContainer();
                            proType.product_name = product.getName();
                            proType.product_id = product.getId();
                            productArrayList.add(proType);
                            DataManager.setProductItems(productArrayList, ctx);

                        }


                    }

                }else {

                    try {
                        //hud.dismiss();
                        Log.d("response3", "post submitted to API.");

                    } catch (Exception e) {
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        //hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {

                //hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }

    public static void getDeliveryModeList(final Context ctx){


        Call<DeliveryModeList> call = MainActivity.apiClient.getDeliveryModeList();

        call.enqueue(new Callback<DeliveryModeList>() {
            @Override
            public void onResponse(Call<DeliveryModeList> call, Response<DeliveryModeList> response) {

                if(response.isSuccessful()) {

                    DeliveryModeList deliveryModeList = response.body();
                    List<DeliveryMode> modeList = deliveryModeList.getData();

                    if(modeList.size() > 0){
                        ArrayList<DeliveryModeContainer> arrayList = new ArrayList<DeliveryModeContainer>();

                        for (DeliveryMode mode: modeList){

                            DeliveryModeContainer type = new DeliveryModeContainer();
                            type.deliveryType = mode.getName();
                            arrayList.add(type);
                            DataManager.setDeliveryModeItems(arrayList, ctx);


                        }

                    }


                    //hud.dismiss();


                }else {

                    try {
                        //hud.dismiss();
                        Log.d("response3", "post submitted to API.");

                    } catch (Exception e) {
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        //hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeliveryModeList> call, Throwable t) {

                //hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }

    public static void getDealerList(final Context ctx){

        final KProgressHUD hud = KProgressHUD.create(ctx)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30)
                .show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(ctx);

        Call<DealerList> call = MainActivity.apiClient.getDealerList(loggedInUser.userId);

        call.enqueue(new Callback<DealerList>() {
            @Override
            public void onResponse(Call<DealerList> call, Response<DealerList> response) {

                Log.d("response", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    DealerList deliveryModeList = response.body();
                    List<Dealer> dealers = deliveryModeList.getData();

                    if (dealers.size() > 0){
                        ArrayList<DealerListContainer> dealerArrayList = new ArrayList<DealerListContainer>();

                        for (Dealer dealer: dealers){

                            //Log.d("response", dealer.getName());

                            DealerListContainer dealerContainer = new DealerListContainer();
                            dealerContainer.dealer_name = dealer.getName();
                            dealerContainer.dealer_id = dealer.getId();
                            dealerContainer.organization = dealer.getOrganization();
                            dealerContainer.address = dealer.getAddress();
                            dealerContainer.phone = dealer.getPhone();
                            dealerArrayList.add(dealerContainer);
                            DataManager.setDealerList(dealerArrayList, ctx);


                        }
                    }

                    hud.dismiss();


                }else {

                    try {
                        hud.dismiss();
                        Log.d("response3", "post submitted to API.");

                    } catch (Exception e) {
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DealerList> call, Throwable t) {

                hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }


    public static ArrayList<String> getRetailerFromLocal(Context context){
        ArrayList<String> retailers = new ArrayList<>();

        ArrayList retailerArray = DataManager.getRetailerList(context);
        retailers.clear();
        retailers.add("--Select Retailer--");
        if (retailerArray != null) {

            //getProductList();

            for (int i = 0; i < retailerArray.size(); i++) {
                ProductContainer product = (ProductContainer) retailerArray.get(i);
                retailers.add(product.product_name);
            }
        }
        return retailers;
    }

    public static ArrayList<String> getProductFromLocal(Context context){
        ArrayList<String> productType = new ArrayList<>();

        ArrayList productArray = DataManager.getProductItems(context);
        productType.clear();
        productType.add("--Select your Product--");
        if (productArray != null) {

            //getProductList();

            for (int i = 0; i < productArray.size(); i++) {
                ProductContainer product = (ProductContainer) productArray.get(i);
                productType.add(product.product_name);
            }
        }
        return productType;
    }

    public static ArrayList<String> getDeliveryModeFromLocal(Context context){
        ArrayList<String> deliveryType = new ArrayList<>();

        ArrayList deliveryModeType = DataManager.getDeliveryModeItems(context);
        deliveryType.clear();
        deliveryType.add("--Select Delivery Mode--");
        if (deliveryModeType != null){

            for (int i = 0; i<deliveryModeType.size(); i++){
                DeliveryModeContainer item = (DeliveryModeContainer) deliveryModeType.get(i);
                deliveryType.add(item.deliveryType);
            }

        }
        return deliveryType;
    }

    public static ArrayList<DOOrderContainer> getDOListFromLocal(Context context, String order_status){

        ArrayList<DOOrderContainer> doArray = new ArrayList<DOOrderContainer>();
        ArrayList allDOArray = DataManager.getDOOrderList(context);

        if (allDOArray != null){

            for (int i=0; i<allDOArray.size();i++){
                DOOrderContainer doOrderList = (DOOrderContainer) allDOArray.get(i);
                if(doOrderList.status.toLowerCase().equals(order_status)){
                    doArray.add(doOrderList);
                }
            }
        }

        return doArray;
    }

}
