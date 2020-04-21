package com.metrocem.mis.EmployeeOrderInfo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Adapter.DOAdapter;
import com.metrocem.mis.Container.DealerListContainer;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.R;
import com.metrocem.mis.Model.CurrentUser;
import com.metrocem.mis.Container.DOOrderContainer;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.Dealer;
import com.metrocem.mis.Model.DealerList;
import com.metrocem.mis.Model.Order;
import com.metrocem.mis.Model.OrderList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestedOrderActivity extends AppCompatActivity {

    RecyclerView doRecyclerView;
    TextView noItem, connectionStatus, btnView;
    ArrayList<String> dealerList;
    ArrayList dealerArray;
    Spinner dealerDropdown;
    ArrayAdapter<String> dealerAdapter;
    KProgressHUD hud;
    String do_status, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_requested_order);

        Bundle extras = getIntent().getExtras();
        do_status= extras.getString("DO_STATUS");
        if (do_status.equals("requested")){
            title = "Requested";
        }else if(do_status.equals("approved")){
            title = "Approved/Pending";
        }else{
            title = "Allocated";
        }

        //Intent mIntent = getIntent();
        //int intValue = mIntent.getIntExtra("DO_STATUS", 0);
        Log.d("response", do_status);

        getSupportActionBar().setTitle(title+" DO History"); // for set actionbar title
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorGreen)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.toolbar_gradient));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noItem = findViewById(R.id.noItemStatus);
        connectionStatus = findViewById(R.id.networkStatus);

        hud = KProgressHUD.create(RequestedOrderActivity.this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        dealerList = new ArrayList<>();
        dealerArray = DataManager.getDealerList(RequestedOrderActivity.this);
        //Log.d("product", String.valueOf(productList.size()));
        //productType.clear();
        dealerList.add("All Dealer");
        if (dealerArray == null){

            getDealerList();

//            for (int i = 0; i<dealerArray.size(); i++){
//                DealerListContainer dealer = (DealerListContainer) dealerArray.get(i);
//                dealerList.add(dealer.organization+" - "+dealer.dealer_name);
//            }

        }else {

            if (isNetworkAvailable()){

                getDealerList();

            }else {
                Toast.makeText(RequestedOrderActivity.this,"Connection Error!", Toast.LENGTH_SHORT).show();
            }
        }

        dealerDropdown = findViewById(R.id.dealerSpinner);
        //final String[] typeItems = new String[]{"Product Type"};
        dealerAdapter = new ArrayAdapter<String>(RequestedOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, dealerList);
        //typeAdapter = new ArrayAdapter<>(getContext(),  android.R.layout.simple_spinner_dropdown_item, productTypes);
        dealerDropdown.setAdapter(dealerAdapter);
        dealerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(position==0){

                    if (isNetworkAvailable()){
                        getOrderRequest();

                    }else {
                        connectionStatus.setVisibility(View.VISIBLE);
                        //Toast.makeText(this,"Connection Error!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    DealerListContainer dealer = (DealerListContainer) dealerArray.get(position-1);
                    getOrderRequestByDealer(dealer.dealer_id);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        //btnView = findViewById(R.id.btnView);
//        btnView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                optionsBtnClick();
//            }
//        });

        doRecyclerView = findViewById(R.id.orderListView);
        doRecyclerView.setHasFixedSize(true);
        doRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //DataManager.removeDOOrderList(this);
        ArrayList doOrderArray = DataManager.getDOOrderList(this);
        if (doOrderArray != null){

            //getOrderRequest();

            //parseData(doOrderArray);

        }else {

            if (isNetworkAvailable()){
                //getOrderRequest();

            }else {
                connectionStatus.setVisibility(View.VISIBLE);
                Toast.makeText(this,"Connection Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    public void optionsBtnClick() {
//
//        // setup the alert builder
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Choose your option");
//
//        // add a list
//        builder.setItems(dealerArray, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                btnView.setText(dealerArray[which]);
//
//            }
//        });
//
//        builder.setNegativeButton("Cancel", null);
//
//        // create and show the alert dialog
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }


    private void getOrderRequest(){

        DataManager.removeDOOrderList(this);

        hud.show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(this);
        Call<OrderList> call = MainActivity.apiClient.getOrderListByEmployee(loggedInUser.userId);
        call.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {

                if(response.isSuccessful()) {

                    OrderList orderList = response.body();
                    List<Order> orders = orderList.getData();

                    ArrayList doArrayList = new ArrayList();

                    for (Order order: orders){

                        if (order.getStatus().equals(do_status.toLowerCase())) {

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
                            DataManager.setDOOrderList(doArrayList, RequestedOrderActivity.this);
                        }

                    }

                    ArrayList doArray = DataManager.getDOOrderList(RequestedOrderActivity.this);

                    if (doArray != null){
                        Collections.reverse(doArray);
                        parseData(doArray);
                        doRecyclerView.setVisibility(View.VISIBLE);
                        noItem.setVisibility(View.INVISIBLE);

                    }else{
                        noItem.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<OrderList> call, Throwable t) {

                hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }


    private void parseData(ArrayList orders) {
        DOAdapter doAdapter = new DOAdapter(this, orders);
        doRecyclerView.setAdapter(doAdapter);
    }

    public void getDealerList(){

        //hud.show();
        CurrentUser loggedInUser = DataManager.getCurrentUser(RequestedOrderActivity.this);

        Call<DealerList> call = MainActivity.apiClient.getDealerList(loggedInUser.userId);

        call.enqueue(new Callback<DealerList>() {
            @Override
            public void onResponse(Call<DealerList> call, Response<DealerList> response) {

                //Log.d("response", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    DealerList deliveryModeList = response.body();
                    List<Dealer> dealers = deliveryModeList.getData();

                    if (dealers.size() > 0){
                        ArrayList dealerArrayList = new ArrayList();

                        for (Dealer dealer: dealers){

                            DealerListContainer dealerContainer = new DealerListContainer();
                            dealerContainer.dealer_name = dealer.getName();
                            dealerContainer.dealer_id = dealer.getId();
                            dealerContainer.organization = dealer.getOrganization();
                            dealerContainer.address = dealer.getAddress();
                            dealerContainer.phone = dealer.getPhone();
                            dealerArrayList.add(dealerContainer);
                            DataManager.setDealerList(dealerArrayList, RequestedOrderActivity.this);

                        }

                        dealerArray = DataManager.getDealerList(RequestedOrderActivity.this);


                        for (int i = 0; i<dealerArray.size(); i++){
                            DealerListContainer item = (DealerListContainer) dealerArray.get(i);
                            //Log.d("response", item.dealer_name);

                            dealerList.add(item.organization+" - "+item.dealer_name);
                        }

                        Log.d("response", String.valueOf(dealerList.size()));
                        dealerAdapter.notifyDataSetChanged();
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


    private void getOrderRequestByDealer(Integer dealer_id){

        DataManager.removeDOOrderList(this);

        hud.show();

        Call<OrderList> call = MainActivity.apiClient.getOrderList(dealer_id);

        call.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {

                if(response.isSuccessful()) {

                    OrderList orderList = response.body();
                    List<Order> orders = orderList.getData();

                    ArrayList doArrayList = new ArrayList();

                    for (Order order: orders){

                        if (order.getStatus().toLowerCase().equals(do_status.toLowerCase())) {

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
                            DataManager.setDOOrderList(doArrayList, RequestedOrderActivity.this);
                        }


                    }

                    ArrayList doArray = DataManager.getDOOrderList(RequestedOrderActivity.this);

                    if (doArray != null){
                        Collections.reverse(doArray);
                        parseData(doArray);
                        doRecyclerView.setVisibility(View.VISIBLE);
                        noItem.setVisibility(View.INVISIBLE);

                    }else{
                        doRecyclerView.setVisibility(View.INVISIBLE);
                        noItem.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<OrderList> call, Throwable t) {

                hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
