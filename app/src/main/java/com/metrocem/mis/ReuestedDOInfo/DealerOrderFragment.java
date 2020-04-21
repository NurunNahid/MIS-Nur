package com.metrocem.mis.ReuestedDOInfo;

import android.content.Intent;
import android.graphics.Color;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Activity.OrderDetailActivity;
import com.metrocem.mis.Adapter.DOAdapter;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.Model.CurrentUser;
import com.metrocem.mis.Container.DOOrderContainer;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.Order;
import com.metrocem.mis.Model.OrderList;
import com.metrocem.mis.R;
import com.metrocem.mis.Subclasses.CheckNetworkConnection;
import com.metrocem.mis.Utilities.APIDataManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerOrderFragment extends Fragment implements DOAdapter.SelectedDO {

    private RecyclerView doRecyclerView;
    private TextView connectionStatus;
    //DODatabaseHelper myDb;
    //private EditText searchEditText;
    private ArrayList<DOOrderContainer> doArray,filteredDOArray;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private KProgressHUD hud;
    private ImageView noItem;
    private String status="requested";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_order, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.order_info);

        initializeWidgets(view);

        //myDb = new DODatabaseHelper(getContext());

        getData(status);


        TabLayout tabLayout=view.findViewById(R.id.tabBtn);
        tabLayout.setTabTextColors(Color.parseColor("#EFEFEF"), Color.parseColor("#ffffff"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //String status;
//                if(tab.getText().toString().toLowerCase().equals("requested")){
//                    status = "requested" ;
//                }else{
//                    status = "approved";
//                }
                status = tab.getText().toString().toLowerCase();

                getData(status);

                //Log.d("tab", String.valueOf(tab.getText()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Search DO
//        searchEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                filterItem(s);
//            }
//        });


        // Refresh DO order
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderData();
            }
        });

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

    private void initializeWidgets(View view){

        noItem = view.findViewById(R.id.noItemStatus);
        connectionStatus = view.findViewById(R.id.networkStatus);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        //searchEditText = view.findViewById(R.id.searchEditText);

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

    private void getData(String order_status){

        if(CheckNetworkConnection.isNetworkAvailable(getContext())){
            getOrderRequest(order_status);
            connectionStatus.setVisibility(View.GONE);
        }else{
            doArray.clear();
            doArray = APIDataManager.getDOListFromLocal(getContext(), status);

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


    private void getOrderData(){

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


    private void getOrderRequest(final String status){


        if(!mSwipeRefreshLayout.isRefreshing()) {
            hud.show();
        }

        CurrentUser loggedInUser = DataManager.getCurrentUser(getActivity());

        Call<OrderList> call;
        if(loggedInUser.role.toLowerCase().equals("dealer")){
            call = MainActivity.apiClient.getOrderList(loggedInUser.userId);

        }else{
            call = MainActivity.apiClient.getOrderListByEmployee(loggedInUser.userId);

        }

        call.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {

                if(response.isSuccessful()) {

                    DataManager.removeDOOrderList(getContext());
                    doArray.clear();

                    OrderList orderList = response.body();
                    List<Order> orders = orderList.getData();
                    //Collections.reverse(orders);

                    ArrayList<DOOrderContainer> doArrayList = new ArrayList<DOOrderContainer>();

                    for (Order order: orders){

                        //Log.d("response", order.getDealerName());
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

                    doArray = APIDataManager.getDOListFromLocal(getContext(), status);


                    if (doArray.size() != 0){
                        Collections.reverse(doArray);
                        parseData(doArray);
                        noItem.setVisibility(View.GONE);
                        doRecyclerView.setVisibility(View.VISIBLE);

                    }else{

                        noItem.setVisibility(View.VISIBLE);
                        doRecyclerView.setVisibility(View.GONE);

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
        DOAdapter doAdapter = new DOAdapter(getActivity(), doArray, this);
        doRecyclerView.setAdapter(doAdapter);
    }

    private void filterItem(String searchedText){

        //String searchedText = charText.toString().toLowerCase(Locale.getDefault());
        filteredDOArray.clear();

        if (searchedText.length() == 0){
            filteredDOArray.addAll(doArray);

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


    @Override
    public void selectedDO(DOOrderContainer selectedOrder) {

        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        intent.putExtra("SELECTED_DO", (Serializable) selectedOrder);
        startActivity(intent);
    }
}
