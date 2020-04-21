package com.metrocem.mis.Home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Container.DOOrderContainer;
import com.metrocem.mis.R;
import com.metrocem.mis.Model.Collection;
import com.metrocem.mis.Model.CollectionAmount;
import com.metrocem.mis.Model.CurrentUser;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.Due;
import com.metrocem.mis.Model.DueAmount;
import com.metrocem.mis.Model.Order;
import com.metrocem.mis.Model.OrderList;
import com.metrocem.mis.Subclasses.CheckNetworkConnection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView collectionAmountTV, dueAmountTV, orderRequested, orderDelivered, orderPartialDelivered, c;
    private KProgressHUD hud;
    private Integer requestedCount = 0, deliveredCount = 0, partialDeliveredCount = 0, draftCount = 0, inTransitCount = 0, receivedCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.main, menu);
        //menu.clear();
        //super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            menu.findItem(R.id.action_sync).setVisible(true);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.menu_home);

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        orderRequested = view.findViewById(R.id.orderRequestedTV);
        orderDelivered = view.findViewById(R.id.deliveredTV);
        orderPartialDelivered = view.findViewById(R.id.partialDeliveredTV);

        TextView creditLimitTV = view.findViewById(R.id.creditLimitTV);
        String creditLimit = "৳"+loggedInUser.creditLimit.toString();
        creditLimitTV.setText(creditLimit);

        collectionAmountTV = view.findViewById(R.id.collectionAmountTV);
        dueAmountTV = view.findViewById(R.id.dueAmountTV);
        TextView nameTV = view.findViewById(R.id.nameTV);
        TextView emailTV = view.findViewById(R.id.emailTV);
        TextView phoneNoTV = view.findViewById(R.id.phoneNoTV);
        ImageView user_photo = view.findViewById(R.id.userPhoto);

        String imagePath = "http://misstage.nurtech.xyz"+loggedInUser.photo;
        //Log.d("image path", imagePath);
        Picasso.with(getContext()).load(imagePath).fit().centerCrop().into(user_photo);

        if (loggedInUser.userName != null){
            nameTV.setText(loggedInUser.userName);
        }
        if (loggedInUser.email != null) {
            emailTV.setText(loggedInUser.email);
        }
        if (loggedInUser.phone != null) {
            phoneNoTV.setText(loggedInUser.phone);
        }

        if (loggedInUser.collectionAmount == null){
            if (CheckNetworkConnection.isNetworkAvailable(getContext())){
                getCollectionAmount();

            }else {
                //Toast.makeText(getContext(),"Connection Error!", Toast.LENGTH_SHORT).show();
            }
        }else {
            String amount = "৳"+loggedInUser.collectionAmount;
            collectionAmountTV.setText(amount);
        }

        if (loggedInUser.dueAmount == null){
            if (CheckNetworkConnection.isNetworkAvailable(getContext())){
                getDueAmount();

            }else {
                //Toast.makeText(getContext(),"Connection Error!", Toast.LENGTH_SHORT).show();
            }
        }else {
            String due_amount = "৳"+loggedInUser.dueAmount;
            dueAmountTV.setText(due_amount);
        }


        if (CheckNetworkConnection.isNetworkAvailable(getContext())){
            getOrderRequest();

        }else {

            getOrderCount();
            //Toast.makeText(getContext(),"Connection Error!", Toast.LENGTH_SHORT).show();
        }

    }



    // Order Count
    private void getOrderRequest(){

        hud.show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());

        Call<OrderList> call = MainActivity.apiClient.getOrderList(loggedInUser.userId);

        call.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {

                Log.d("response", String.valueOf(response.code()));

                if(response.isSuccessful()) {

                    OrderList orderList = response.body();
                    List<Order> orders;
                    if (orderList != null){
                        orders = orderList.getData();
                        for (Order order: orders){

                            //Log.d("response2", order.getDealerName());
                            if (order.getStatus().toLowerCase().equals("requested")) {
                                requestedCount +=1;

                            }
                            if (order.getStatus().toLowerCase().equals("delivered")) {
                                deliveredCount +=1;

                            }
                            if (order.getStatus().toLowerCase().equals("partial_delivered")) {
                                partialDeliveredCount +=1;

                            }
                        }
                    }

                    setOrderText();

                    hud.dismiss();


                }else {

                    try {
                        hud.dismiss();
                        //Log.d("response3", "post submitted to API.");

                    } catch (Exception e) {
                        hud.dismiss();
                        //Log.d("response4", "post submitted to API." + e.getMessage().toString());

                    }
                }
            }

            @Override
            public void onFailure(Call<OrderList> call, Throwable t) {

                //Log.d("response5", t.getLocalizedMessage());

                hud.dismiss();
            }
        });
    }

    private void getCollectionAmount(){

        hud.show();

        final CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());

        Call<Collection> call = MainActivity.apiClient.getCollectionAmount(loggedInUser.userId, null, null);

        call.enqueue(new Callback<Collection>() {
            @Override
            public void onResponse(Call<Collection> call, Response<Collection> response) {

                Log.d("response", String.valueOf(response.code()));

                if(response.isSuccessful()) {

                    Collection collection = response.body();
                    CollectionAmount collectionAmount = collection.getData();

                    Integer total_amount = collectionAmount.getTotal();
                    String amount = "৳"+total_amount.toString();
                    collectionAmountTV.setText(amount);

                    //CurrentUser currentUser = DataManager.getCurrentUser(getContext());
                    loggedInUser.collectionAmount = total_amount;
                    DataManager.setCurrentUser(loggedInUser, getContext());

                    //String title = "Collection Report";
                    //String msg = "Total Collection Received BDT "+total_amount+".";
                    //DataManager.alertShow(title, msg, getActivity());

                    hud.dismiss();

                }else {
                    try {
                        hud.dismiss();
                    } catch (Exception e) {
                        hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Collection> call, Throwable t) {

                hud.dismiss();
            }
        });
    }

    private void getDueAmount(){

//        final KProgressHUD hud = KProgressHUD.create(getActivity())
//                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
//                .setLabel("Please wait")
//                .setMaxProgress(30)
//                .show();

        final CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());

        Call<Due> call = MainActivity.apiClient.getDueAmount(loggedInUser.userId);

        call.enqueue(new Callback<Due>() {
            @Override
            public void onResponse(Call<Due> call, Response<Due> response) {

                Log.d("responseOne", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    Log.d("responseTwo", String.valueOf(response.body().getData()));
                    Log.d("response", response.body().getData().toString());
                    Due due = response.body();
                    DueAmount collectionAmount = due.getData();
                    Integer total_amount = collectionAmount.getTotal();

                    String due_amount = "৳"+total_amount.toString();

                    dueAmountTV.setText(due_amount);

                    //CurrentUser currentUser = new CurrentUser();
                    loggedInUser.dueAmount = total_amount;
                    DataManager.setCurrentUser(loggedInUser, getContext());

                    //String title = "Due Balance";
                    //String msg = "Your last due balance is BDT "+total_amount+".";
                    //DataManager.alertShow(title, msg, getActivity());

                    //hud.dismiss();


                }else {

                    try {
                        //hud.dismiss();
                        Log.d("response3", "post submitted to API." + response.errorBody());

                    } catch (Exception e) {
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        //hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Due> call, Throwable t) {

                //hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }

    private void getOrderCount(){
        ArrayList allDOArray = DataManager.getDOOrderList(getContext());

        if (allDOArray != null){

            for (int i=0; i<allDOArray.size();i++){
                DOOrderContainer order = (DOOrderContainer) allDOArray.get(i);
                if (order.status.toLowerCase().equals("requested")) {
                    requestedCount +=1;

                }
                if (order.status.toLowerCase().equals("delivered")) {
                    deliveredCount +=1;

                }
                if (order.status.toLowerCase().equals("partial_delivered")) {
                    partialDeliveredCount +=1;

                }
            }
        }

        setOrderText();
    }

    private void setOrderText(){
        orderRequested.setText(String.valueOf(requestedCount));
        orderDelivered.setText(String.valueOf(deliveredCount));
        orderPartialDelivered.setText(String.valueOf(partialDeliveredCount));
    }
}
