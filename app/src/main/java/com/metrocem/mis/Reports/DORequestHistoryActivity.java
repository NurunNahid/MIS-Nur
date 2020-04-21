package com.metrocem.mis.Reports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DORequestHistoryActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, DOAdapter.SelectedDO {

    private ArrayList<DOOrderContainer> doArray;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView noItem, connectionStatus;
    private KProgressHUD hud;
    private RecyclerView doRecyclerView;
    private TextView startDateStr, endDateStr, goBtn;
    private String btnTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dorequest_history);

        getSupportActionBar().setTitle(R.string.do_request_history); // for set actionbar title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.toolbar_gradient));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeWidgets();


        ArrayList doOrderArray = DataManager.getDOOrderList(this);
        if (doOrderArray != null){

            for (int i=0; i<doOrderArray.size();i++){
                DOOrderContainer doOrderlist = (DOOrderContainer) doOrderArray.get(i);
                if(doOrderlist.status.toLowerCase().equals("requested")){
                    doArray.add(doOrderlist);
                }
            }
            parseData(doArray);
            //getOrderData();
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

    private void initializeWidgets(){

        noItem = findViewById(R.id.noItemStatus);
        connectionStatus = findViewById(R.id.networkStatus);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);
        TextView startDate = findViewById(R.id.startDateTV);
        TextView endDate = findViewById(R.id.endDateTV);
        goBtn = findViewById(R.id.goBtn);
        startDateStr = findViewById(R.id.startDateStr);
        endDateStr = findViewById(R.id.endDateStr);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        goBtn.setOnClickListener(this);

        doArray = new ArrayList<DOOrderContainer>();

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        doRecyclerView = findViewById(R.id.orderListView);
        doRecyclerView.setHasFixedSize(true);
        doRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void getOrderData(){

        if (CheckNetworkConnection.isNetworkAvailable(this)){
            getOrderRequest();
        }else {
            connectionStatus.setVisibility(View.VISIBLE);
            //Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseData(ArrayList doArray) {
        //DOAdapter doAdapter = new DOAdapter(this, doArray);
        //doRecyclerView.setAdapter(doAdapter);
    }


    private void getOrderRequest(){


        if(!mSwipeRefreshLayout.isRefreshing()) {
            hud.show();
        }

        CurrentUser loggedInUser = DataManager.getCurrentUser(this);
        String fromDate;
        String toDate;
        if (startDateStr.getText().toString().isEmpty() || endDateStr.getText().toString().isEmpty()){
            fromDate = null;
            toDate = null;
        }else{
            fromDate = startDateStr.getText().toString();
            toDate = endDateStr.getText().toString();
        }

        Call<OrderList> call = MainActivity.apiClient.getOrderList(loggedInUser.userId);

        call.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {

                if(response.isSuccessful()) {

                    DataManager.removeDOOrderList(DORequestHistoryActivity.this);
                    doArray.clear();

                    OrderList orderList = response.body();
                    List<Order> orders = orderList.getData();
                    Log.d("response count", String.valueOf(orders.size()));

                    Collections.reverse(orders);

                    ArrayList<DOOrderContainer> doArrayList = new ArrayList<DOOrderContainer>();

                    for (Order order: orders){

                        Log.d("response", order.getDealerName());
                        //if (order.getStatus().toLowerCase().equals("requested")) {

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
                        DataManager.setDOOrderList(doArrayList, DORequestHistoryActivity.this);

                        //}
                    }

                    ArrayList allDOArray = DataManager.getDOOrderList(DORequestHistoryActivity.this);

                    if (allDOArray != null){

                        for (int i=0; i<allDOArray.size();i++){
                            DOOrderContainer doOrderlist = (DOOrderContainer) allDOArray.get(i);
                            if(doOrderlist.status.toLowerCase().equals("requested")){
                                doArray.add(doOrderlist);
                            }
                        }
                    }


                    //DOOrderContainer doOrder = new DOOrderContainer();
                    if (doArray != null){
                        //Collections.reverse(doArray);

                        Log.d("order count", String.valueOf(doArray.size()));
                        parseData(doArray);
                        noItem.setVisibility(View.INVISIBLE);

                    }else{
                        noItem.setVisibility(View.VISIBLE);
                    }

                    if(mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }



                    //String name = modeList.get(0).getDealerName();
                    //Log.d("response", name);
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

    private void showDatePickerDialog(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)

        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        String month1 = String.valueOf(month);
        String day1 = String.valueOf(day);
        if(month < 10){
            month1 = "0"+month;
        }
        if(day < 10){
            day1 = "0"+day;

        }
        String date = year + "-" + month1 + "-" + day1;

        if(btnTitle.equals("start")){
            startDateStr.setText(date);
        }else {
            endDateStr.setText(date);
        }
        Log.d("response Date", "onDateSe" + date);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.goBtn:
                getOrderData();
                break;
            case R.id.startDateTV:
                btnTitle = "start";
                showDatePickerDialog();
                break;
            case R.id.endDateTV:
                btnTitle = "end";
                showDatePickerDialog();
                break;
        }
    }


    @Override
    public void selectedDO(DOOrderContainer selectedOrder) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("SELECTED_DO", (Serializable) selectedOrder);
        startActivity(intent);
    }
}
