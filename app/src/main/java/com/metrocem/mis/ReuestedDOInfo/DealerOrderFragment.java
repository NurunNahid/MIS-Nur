package com.metrocem.mis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Adapter.DOAdapter;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.Model.CurrentUser;
import com.metrocem.mis.Container.DOOrderContainer;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.Order;
import com.metrocem.mis.Model.OrderList;
import com.metrocem.mis.Subclasses.CheckNetworkConnection;
import com.metrocem.mis.Utilities.APIDataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import SQLite.DODatabaseHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {

    private RecyclerView doRecyclerView;
    private TextView noItem, connectionStatus;
    //DODatabaseHelper myDb;
    private EditText searchEditText;
    private ArrayList<DOOrderContainer> doArray,filteredDOArray;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private KProgressHUD hud;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.order_info);

        initializeWidgets(view);

        //myDb = new DODatabaseHelper(getContext());

        ArrayList doOrderArray = DataManager.getDOOrderList(getContext());
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


        // Search DO
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filterItem(s);
            }
        });


        // Refresh DO order
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderData();
            }
        });

    }

    private void initializeWidgets(View view){

        noItem = view.findViewById(R.id.noItemStatus);
        connectionStatus = view.findViewById(R.id.networkStatus);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        searchEditText = view.findViewById(R.id.searchEditText);

        doArray = new ArrayList<DOOrderContainer>();
        filteredDOArray = new ArrayList<DOOrderContainer>();

        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        doRecyclerView = view.findViewById(R.id.orderListView);
        doRecyclerView.setHasFixedSize(true);
        doRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    private void getOrderData(){

        if (CheckNetworkConnection.isNetworkAvailable(getContext())){
            getOrderRequest();
        }else {
            connectionStatus.setVisibility(View.VISIBLE);
            //Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
        }
    }


    private void getOrderRequest(){


        if(!mSwipeRefreshLayout.isRefreshing()) {
            hud.show();
        }

        CurrentUser loggedInUser = DataManager.getCurrentUser(getActivity());

        Call<OrderList> call = MainActivity.apiClient.getOrderList(loggedInUser.userId);

        call.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {

                if(response.isSuccessful()) {

                    DataManager.removeDOOrderList(getContext());
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
                            DataManager.setDOOrderList(doArrayList, getContext());

                        //}
                    }

                    ArrayList allDOArray = DataManager.getDOOrderList(getContext());

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

    private void parseData(ArrayList doArray) {
        DOAdapter doAdapter = new DOAdapter(getActivity(), doArray);
        doRecyclerView.setAdapter(doAdapter);
    }

    private void filterItem(Editable charText){

        String searchedText = charText.toString().toLowerCase(Locale.getDefault());
        filteredDOArray.clear();

        if (searchedText.length() == 0){
            filteredDOArray.addAll(doArray);
            parseData(filteredDOArray);

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

                parseData(filteredDOArray);
            }

        }


    }


}
