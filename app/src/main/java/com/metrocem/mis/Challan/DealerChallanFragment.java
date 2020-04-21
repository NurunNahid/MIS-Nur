package com.metrocem.mis.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
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
import com.metrocem.mis.Adapter.ChallanAdapter;
import com.metrocem.mis.Container.ChallanContainer;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.R;
import com.metrocem.mis.Subclasses.ChallanInfo;
import com.metrocem.mis.Subclasses.ChallanList;
import com.metrocem.mis.Subclasses.CurrentUser;
import com.metrocem.mis.Subclasses.DataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerChallanFragment extends Fragment {

    //private CurrentUser currentUser;
    private KProgressHUD hud;
    private TextView connectionStatus, noItem;
    private RecyclerView challanRecyclerView;
    private ArrayList challanArray, filteredChallanArray;
    //private EditText searchEditText;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.emploee_challan_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.challan_info);


        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        connectionStatus = view.findViewById(R.id.networkStatus);
        noItem = view.findViewById(R.id.noItemStatus);
        CardView cardView = view.findViewById(R.id.challan_card_view);
        cardView.setVisibility(View.GONE);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        challanRecyclerView = view.findViewById(R.id.challanListView);
        challanRecyclerView.setHasFixedSize(true);
        challanRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        challanArray = new ArrayList<>();
        filteredChallanArray = new ArrayList<>();

        ArrayList challanArray = DataManager.getChallanList(getContext());
        if (challanArray != null){

            parseData(challanArray);
            //getOrderData();


        }else {

            getChallanData();

        }



        EditText searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //SaleItemActivity.this.saleAdapter.filterItem(s);
                //saleItemListView.invalidateViews();

                filterItem(s);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
                getChallanData();
            }
        });
    }

    private void getChallanData(){

        CurrentUser currentUser = DataManager.getCurrentUser(getContext());

        if (isNetworkAvailable()){
            getChallanListByDealer(currentUser.userId);

        }else {
            connectionStatus.setVisibility(View.VISIBLE);
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }            //Toast.makeText(this,"Connection Error!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void parseData(List<ChallanContainer> challans) {
        ChallanAdapter challanAdapter = new ChallanAdapter(getContext(), challans);
        challanRecyclerView.setAdapter(challanAdapter);
        //challanAdapter.notifyDataSetChanged();

    }

    private void getChallanListByDealer(Integer dealer_id){

        DataManager.removeChallanList(getContext());

        if(!mSwipeRefreshLayout.isRefreshing()) {
            hud.show();
        }
        Call<ChallanList> call = MainActivity.apiClient.getChallanListByDealer(dealer_id);

        call.enqueue(new Callback<ChallanList>() {
            @Override
            public void onResponse(Call<ChallanList> call, Response<ChallanList> response) {

                Log.d("response", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    Log.d("response", String.valueOf(response.body()));
                    ChallanList challanList = response.body();
                    List<ChallanInfo> challanInfos = challanList.getData();
                    Log.d("response count", String.valueOf(challanInfos.size()));

                    ArrayList challanArrayList = new ArrayList();

                    for (ChallanInfo challan: challanInfos){


                        ChallanContainer challanContainer = new ChallanContainer();
                        challanContainer.id = challan.getId();
                        challanContainer.bagQuantity = challan.getBagQuantity();
                        challanContainer.unitPrice = challan.getUnitPrice();
                        challanContainer.doNumber = challan.getDoNumber();
                        challanContainer.deliveryMode = challan.getDeliveryMode();
                        challanContainer.doProcessing = challan.getDoProcessing();
                        challanContainer.status = challan.getStatus();
                        challanContainer.doAllocating = challan.getDoAllocating();
                        challanContainer.employeeName = challan.getEmployeeName();
                        challanContainer.dealerName = challan.getDealerName();
                        challanContainer.productName = challan.getProductName();
                        challanContainer.driverName = challan.getDriverName();
                        challanContainer.driverPhone = challan.getDriverPhone();
                        challanContainer.deliveryAddress = challan.getDeliveryAddress().getAddress();
                        challanContainer.dealerPhone = challan.getDeliveryAddress().getContactNumber();
                        challanContainer.vehicleNumber = challan.getVehicleNumber();

                        challanArrayList.add(challanContainer);
                        DataManager.setChallanList(challanArrayList, getContext());

                    }

                    challanArray = DataManager.getChallanList(getContext());

                    //DOOrderContainer doOrder = new DOOrderContainer();
                    if (challanArray != null){
                        Collections.reverse(challanArray);
                        parseData(challanArray);
                        noItem.setVisibility(View.INVISIBLE);

                    }else{
                        noItem.setVisibility(View.VISIBLE);
                    }

                    if(mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    //challanAdapter = new ChallanAdapter(getContext(), challanArray);
                    //doRecyclerView.setAdapter(challanAdapter);
                    //parseData(challanArray);
                    //challanAdapter.notifyDataSetChanged();

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
            public void onFailure(Call<ChallanList> call, Throwable t) {

                hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }

    public void filterItem(Editable charText){

        String searchedText = charText.toString().toLowerCase(Locale.getDefault());
        filteredChallanArray.clear();

        if (searchedText.length() == 0){
            filteredChallanArray.addAll(challanArray);
            parseData(filteredChallanArray);

        }
        else {
            //itemArray.clear();
            if (challanArray.size() > 0){

                for (int i= 0; i<challanArray.size(); i++){

                    ChallanContainer challanOrder = (ChallanContainer) challanArray.get(i);

                    if (challanOrder.doNumber.toLowerCase(Locale.getDefault()).contains(searchedText) || challanOrder.status.toLowerCase(Locale.getDefault()).contains(searchedText)){

                        filteredChallanArray.add(challanArray.get(i));

                    }

                }

                parseData(filteredChallanArray);
            }

        }


    }


}
