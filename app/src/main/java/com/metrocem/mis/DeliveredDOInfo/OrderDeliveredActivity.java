package com.metrocem.mis.DeliveryInfo;

import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Adapter.DOAdapter;
import com.metrocem.mis.Container.DOOrderContainer;
import com.metrocem.mis.Container.DeliveredOrderContainer;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.R;
import com.metrocem.mis.Model.CurrentUser;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.Order;
import com.metrocem.mis.Model.OrderList;
import com.metrocem.mis.Subclasses.CheckNetworkConnection;
import com.metrocem.mis.Subclasses.Container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDeliveredActivity extends AppCompatActivity {

    String[] monthNameArray;
    //TextView btnView;
    RecyclerView doRecyclerView;
    TextView noItem, connectionStatus, btnView;
    KProgressHUD hud;
    String order_status;
    private ArrayList <DOOrderContainer> doArray,filteredDOArray;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_delivered);

        Bundle extras = getIntent().getExtras();
        order_status= extras.getString("ORDER_STATUS");
        String title;
        if(order_status.toLowerCase().equals("partial_delivered")){
            title = "Partial Delivered";
        }else{
            title = "Delivered";
        }

        getSupportActionBar().setTitle(title); // for set actionbar title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.toolbar_gradient));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);
        noItem = findViewById(R.id.noItemStatus);
        connectionStatus = findViewById(R.id.networkStatus);
        hud = KProgressHUD.create(OrderDeliveredActivity.this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        //hud = ProgressHud.setHud(this);

        doArray = new ArrayList<DOOrderContainer>();
        filteredDOArray = new ArrayList<DOOrderContainer>();

        doRecyclerView = findViewById(R.id.orderDeliveredListView);
        doRecyclerView.setHasFixedSize(true);
        doRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList doOrderArray = DataManager.getDOOrderList(this);
        if (doOrderArray != null){
            for (int i=0; i<doOrderArray.size();i++){
                DOOrderContainer doOrderlist = (DOOrderContainer) doOrderArray.get(i);
                if(doOrderlist.status.toLowerCase().equals(order_status.toLowerCase())){
                    doArray.add(doOrderlist);
                }
            }
        }

        if (doArray != null){

            parseData(doArray);


        }else {

            getOrderData();

        }

        // Refresh DO order
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderData();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                //overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getOrderData(){

        if (CheckNetworkConnection.isNetworkAvailable(OrderDeliveredActivity.this)){
            getOrderRequest();

        }else {
            connectionStatus.setVisibility(View.VISIBLE);
            //Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
        }
    }


    private void parseData(ArrayList orders) {
        DOAdapter doAdapter = new DOAdapter(this, orders);
        doRecyclerView.setAdapter(doAdapter);
    }

    private void getOrderRequest(){



        if(!mSwipeRefreshLayout.isRefreshing()) {
            hud.show();
        }
        CurrentUser loggedInUser = DataManager.getCurrentUser(this);

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

                    DataManager.removeDOOrderList(OrderDeliveredActivity.this);
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
                            DataManager.setDOOrderList(doArrayList, OrderDeliveredActivity.this);
                        }


                        Log.d("response order", String.valueOf(DataManager.getDOOrderList(OrderDeliveredActivity.this).size()));

                        ArrayList allDOArray = DataManager.getDOOrderList(OrderDeliveredActivity.this);
                        //Log.d("response order", String.valueOf(DataManager.getDOOrderList(OrderDeliveredActivity.this).size()));


                        if (allDOArray != null){

                            for (int i=0; i<allDOArray.size();i++){
                                DOOrderContainer doOrderlist = (DOOrderContainer) allDOArray.get(i);
                                if(doOrderlist.status.toLowerCase().equals(order_status.toLowerCase())){
                                    doArray.add(doOrderlist);
                                }
                            }
                        }
                        if (doArray != null){
                            Collections.reverse(doArray);
                            parseData(doArray);
                            noItem.setVisibility(View.INVISIBLE);

                        }else{
                            noItem.setVisibility(View.VISIBLE);
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


}
