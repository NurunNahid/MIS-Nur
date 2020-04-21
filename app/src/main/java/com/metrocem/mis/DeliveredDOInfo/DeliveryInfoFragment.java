package com.metrocem.mis.DeliveredDOInfo;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Activity.OrderDetailActivity;
import com.metrocem.mis.Adapter.DOAdapter;
import com.metrocem.mis.Container.DOOrderContainer;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.Model.CurrentUser;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.Order;
import com.metrocem.mis.Model.OrderList;
import com.metrocem.mis.R;
import com.metrocem.mis.Subclasses.CheckNetworkConnection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryInfoFragment extends Fragment implements DOAdapter.SelectedDO {

    private ArrayList <DOOrderContainer> doArray,filteredDOArray;
    private RecyclerView doRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private KProgressHUD hud;
    private TextView  connectionStatus;
    private String status="delivered";
    private ImageView noItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_delivery, null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.main, menu);
        //menu.clear();
        //super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            menu.findItem(R.id.action_search).setVisible(true);

            MenuItem item = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    filterItem(s);
                    return false;
                }
            });

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.delivery_info);

        doArray = new ArrayList<DOOrderContainer>();
        filteredDOArray = new ArrayList<DOOrderContainer>();
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        noItem = view.findViewById(R.id.noItemStatus);
        connectionStatus = view.findViewById(R.id.networkStatus);

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        TabLayout tabLayout=view.findViewById(R.id.tabBtn);
        tabLayout.setTabTextColors(Color.parseColor("#EFEFEF"), Color.parseColor("#ffffff"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //String status;
                if(tab.getText().toString().toLowerCase().equals("partial delivered")){
                    status = "partial_delivered" ;
                }else{
                    status = "delivered";
                }

                getData(status);

                Log.d("tab", String.valueOf(tab.getText()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        doRecyclerView = view.findViewById(R.id.orderDeliveredListView);
        doRecyclerView.setHasFixedSize(true);
        doRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getData(status);

        // Refresh DO order
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //getData(status);
                if (CheckNetworkConnection.isNetworkAvailable(getContext())){
                    getOrderRequest(status);
                    connectionStatus.setVisibility(View.INVISIBLE);
                }else{
                    connectionStatus.setVisibility(View.VISIBLE);
                    if(mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });

        //tabLayout.getTabAt(0).select();

    }

    private void parseData(ArrayList<DOOrderContainer> orders) {
        DOAdapter doAdapter = new DOAdapter(getContext(), orders, this);
        doRecyclerView.setAdapter(doAdapter);
    }

    private void getData(String order_status){

        if(CheckNetworkConnection.isNetworkAvailable(getContext())){
            getOrderRequest(order_status);
        }else{
            ArrayList doOrderArray = DataManager.getDOOrderList(getContext());
            doArray.clear();
            if (doOrderArray != null){
                for (int i=0; i<doOrderArray.size();i++){
                    DOOrderContainer doOrderlist = (DOOrderContainer) doOrderArray.get(i);
                    if(doOrderlist.status.toLowerCase().equals(order_status.toLowerCase())){
                        doArray.add(doOrderlist);
                    }
                }
            }

            if (doArray.size() != 0){
                noItem.setVisibility(View.GONE);
                doRecyclerView.setVisibility(View.VISIBLE);
                parseData(doArray);
                //doRecyclerView.setAdapter(new DOAdapter(getContext(),doArray));

            }else{
                doRecyclerView.setVisibility(View.GONE);
                noItem.setVisibility(View.VISIBLE);
            }
        }


    }

    private void filterItem(String searchedText){

        //String searchedText = charText.toString().toLowerCase(Locale.getDefault());
        filteredDOArray.clear();

        if (searchedText.length() == 0){
            filteredDOArray.addAll(doArray);
            //parseData(filteredDOArray);

        }
        else {
            //itemArray.clear();
            if (doArray.size() > 0){

                for (int i= 0; i<doArray.size(); i++){

                    DOOrderContainer doOrder = (DOOrderContainer) doArray.get(i);

                    if (doOrder.doNumber.toLowerCase(Locale.getDefault()).contains(searchedText) || doOrder.productName.toLowerCase(Locale.getDefault()).contains(searchedText)){

                        filteredDOArray.add(doArray.get(i));

                    }

                }


            }

        }
        if (filteredDOArray.size() != 0){
            noItem.setVisibility(View.GONE);
            doRecyclerView.setVisibility(View.VISIBLE);
            parseData(filteredDOArray);
            //doRecyclerView.setAdapter(new DOAdapter(getContext(),doArray));

        }else{
            doRecyclerView.setVisibility(View.GONE);
            noItem.setVisibility(View.VISIBLE);
        }


    }

//    private void getOrderData(){
//
//        if (CheckNetworkConnection.isNetworkAvailable(getContext())){
//            getOrderRequest();
//
//        }else {
//            //connectionStatus.setVisibility(View.VISIBLE);
//            Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void getOrderRequest(final String order_status){



        if(!mSwipeRefreshLayout.isRefreshing()) {
            hud.show();
        }
        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());

        Call<OrderList> call;
        if(loggedInUser.role.toLowerCase().equals("dealer")){
            call = MainActivity.apiClient.getOrderList(loggedInUser.userId);

        }else {
            call = MainActivity.apiClient.getOrderListByEmployee(loggedInUser.userId);
        }
        call.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {

                if(response.isSuccessful()) {

                    OrderList orderList = response.body();
                    List<Order> orders = orderList.getData();

                    DataManager.removeDOOrderList(getContext());
                    doArray.clear();


                    ArrayList<DOOrderContainer> doArrayList = new ArrayList<DOOrderContainer>();

                    if (orders.size() > 0){


                        for (Order order: orders){

                            Log.d("order", order.getDoNumber());
                            DOOrderContainer doOrderContainer = new DOOrderContainer();
                            doOrderContainer.id = order.getId();
                            doOrderContainer.type = order.getType();
                            doOrderContainer.actualBagQty = order.getActualBagQty();
                            doOrderContainer.unitPrice = order.getUnitPrice();
                            doOrderContainer.doNumber = order.getDoNumber();
                            doOrderContainer.deliveryMode = order.getDeliveryMode();
                            doOrderContainer.approvedAt = order.getApprovedAt();
                            doOrderContainer.status = order.getStatus();
                            doOrderContainer.createdAt = order.getCreatedAt();
                            doOrderContainer.employeeName = order.getEmployeeName();
                            doOrderContainer.dealerName = order.getDealerName();
                            doOrderContainer.productName = order.getProductName();
                            doOrderContainer.numberOfChallan = order.getNumberOfChallan();
                            doOrderContainer.deliveryAddress = order.getDeliveryAddress().getAddress();
                            doOrderContainer.contactNumber = order.getDeliveryAddress().getContactNumber();
                            doArrayList.add(doOrderContainer);
                            DataManager.setDOOrderList(doArrayList, getContext());
                        }


                        Log.d("response order", String.valueOf(DataManager.getDOOrderList(getContext())));

                        ArrayList allDOArray = DataManager.getDOOrderList(getContext());
                        //Log.d("response order", String.valueOf(DataManager.getDOOrderList(OrderDeliveredActivity.this).size()));


                        if (allDOArray != null){

                            for (int i=0; i<allDOArray.size();i++){
                                Log.d("response status", status);
                                DOOrderContainer doOrderList = (DOOrderContainer) allDOArray.get(i);
                                if(doOrderList.status.toLowerCase().equals(order_status.toLowerCase())){
                                    doArray.add(doOrderList);
                                }
                            }
                        }
                        if (doArray.size() != 0){
                            Collections.reverse(doArray);
                            doRecyclerView.setVisibility(View.VISIBLE);
                            noItem.setVisibility(View.GONE);
                            parseData(doArray);

                        }else{
                            noItem.setVisibility(View.VISIBLE);
                            doRecyclerView.setVisibility(View.GONE);
                        }
                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    //String name = modeList.get(0).getDealerName();
                    //Log.d("response", name);
                    hud.dismiss();


                }else {

                    try {
                        hud.dismiss();
                        //Log.d("response3", "post submitted to API.");

                    } catch (Exception e) {
                        //Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderList> call, Throwable t) {

                hud.dismiss();
                //Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }

//    @Override
//    public void onDOItemClick(int position) {
//
//        //ArrayList<DOOrderContainer> order = doArray.get(position);
//        //Pair[] pair = new Pair[5];
//        //pair[0] = new Pair<View, String>(doArray.get(position).actualBagQty,"qty_tran");
//        //pair[1] = new Pair<View, String>(doRecyclerHolder.do_number,"do_no_tran");
//        //pair[2] = new Pair<View, String>(doRecyclerHolder.do_number,"product_name_tran");
//        //pair[3] = new Pair<View, String>(doRecyclerHolder.do_number,"dealer_tran");
//        //pair[4] = new Pair<View, String>(doRecyclerHolder.do_number,"date_tran");
//        //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pair);
//        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
//        intent.putExtra("SELECTED_DO", (Serializable) doArray.get(position));
//        startActivity(intent);
//    }

    @Override
    public void selectedDO(DOOrderContainer selectedOrder) {
//        Pair[] pair = new Pair[5];
//        pair[0] = new Pair<View, String>(String.valueOf(selectedOrder.actualBagQty),"qty_tran");
//        pair[1] = new Pair<String, String>(selectedOrder.doNumber,"do_no_tran");
//        pair[2] = new Pair<String, String>(selectedOrder.productName,"product_name_tran");
//        pair[3] = new Pair<String, String>(selectedOrder.dealerName,"dealer_tran");
//        pair[4] = new Pair<String, String>(selectedOrder.createdAt,"date_tran");
//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) getContext(), pair);
        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        intent.putExtra("SELECTED_DO", (Serializable) selectedOrder);
        startActivity(intent);
    }
}
