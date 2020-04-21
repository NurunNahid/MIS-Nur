package com.metrocem.mis.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Adapter.SecurityAdapter;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.R;
import com.metrocem.mis.Subclasses.ChallanInfo;
import com.metrocem.mis.Subclasses.ChallanList;
import com.metrocem.mis.Subclasses.CurrentUser;
import com.metrocem.mis.Subclasses.DataManager;
import com.metrocem.mis.Subclasses.Order;
import com.metrocem.mis.Subclasses.OrderList;
import com.metrocem.mis.Subclasses.SecurityInfo;
import com.metrocem.mis.Subclasses.SecurityInfoContainer;
import com.metrocem.mis.Subclasses.SecurityList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeDashboard extends Fragment {

    private TextView collectionAmountTV, dueAmountTV, orderRequested, orderDelivered, orderPartialDelivered, challanDraft, challanInTransit, challanReceived;
    private TextView noItem, connectionStatus, showAllBtn;
    private KProgressHUD hud;
    private RecyclerView securityRecyclerView;
    private Integer requestedCount = 0, deliveredCount = 0, partialDeliveredCount = 0, draftCount = 0, inTransitCount = 0, receivedCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employee_home, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.menu_home);


        connectionStatus = view.findViewById(R.id.networkStatus);
        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        orderRequested = view.findViewById(R.id.orderRequestedTV);
        orderDelivered = view.findViewById(R.id.deliveredTV);
        orderPartialDelivered = view.findViewById(R.id.partialDeliveredTV);

        challanDraft = view.findViewById(R.id.challanDraftTV);
        challanInTransit = view.findViewById(R.id.challanInTransitTV);
        challanReceived = view.findViewById(R.id.challanReceivedTV);

        securityRecyclerView = view.findViewById(R.id.securityListView);
        securityRecyclerView.setHasFixedSize(true);
        securityRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        showAllBtn = view.findViewById(R.id.showAllBtn);

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());
        TextView nameTV = view.findViewById(R.id.nameTV);
        TextView emailTV = view.findViewById(R.id.emailTV);
        TextView phoneNoTV = view.findViewById(R.id.phoneNoTV);
        if (loggedInUser.userName != null){
            nameTV.setText(loggedInUser.userName);
        }
        if (loggedInUser.email != null) {
            emailTV.setText(loggedInUser.email);
        }
        if (loggedInUser.phone != null) {
            phoneNoTV.setText(loggedInUser.phone);
        }
        //if(loggedInUser.)
        ImageView user_photo = view.findViewById(R.id.userPhoto);

        String imagePath = "http://misstage.nurtech.xyz"+loggedInUser.photo;
        Log.d("image path", imagePath);
        Picasso.with(getContext()).load(imagePath).fit().centerCrop().into(user_photo);

        if (isNetworkAvailable()){

            getSecurityList();
            getOrderRequest();
            getChallanList();
        }else {
            Toast.makeText(getContext(),"Connection Error!", Toast.LENGTH_SHORT).show();
        }



    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void parseData(List<SecurityInfoContainer> securityInfos) {
        SecurityAdapter securityAdapter = new SecurityAdapter(getContext(), securityInfos);
        securityRecyclerView.setAdapter(securityAdapter);
    }

    // get Individual Dealer Security
    private void getSecurityList(){

        hud.show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());
//        final String accept = "application/json";
//        final String token = "Bearer " + loggedInUser.accessToken;

//        String API_BASE_URL = "http://mis.nurtech.xyz/api/v1/";
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request request = chain.request().newBuilder()
//                        .addHeader("Accept", accept)
//                        .addHeader("Authorization", token)
//                        .build();
//                return chain.proceed(request);
//            }
//        });


        //Retrofit retrofit = builder.build();
        //Retrofit retrofit = builder.client(httpClient.build()).build();
        //ApiClient userApiClient = retrofit.create(ApiClient.class);
        //Call<SecurityList> call = userApiClient.getSecurityList(loggedInUser.userId);

        Call<SecurityList> call = MainActivity.apiClient.getSecurityList(loggedInUser.userId);


        call.enqueue(new Callback<SecurityList>() {
            @Override
            public void onResponse(Call<SecurityList> call, Response<SecurityList> response) {

                //Log.d("response", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    SecurityList securityList = response.body();
                    List<SecurityInfo> securityInfos = securityList.getData();

                    ArrayList securityArrayList = new ArrayList();

                    DataManager.removeSecurityList(getContext());
                    for (SecurityInfo securityInfo: securityInfos){

                        SecurityInfoContainer info = new SecurityInfoContainer();
                        info.dealerName = securityInfo.getName();
                        info.dealerId = securityInfo.getDealerId();
                        info.bank_guarantee = securityInfo.getBankGuarantee();
                        info.security_cheque = securityInfo.getSecurityCheque();
                        info.blank_cheque = securityInfo.getBlankCheque();
                        securityArrayList.add(info);
                        DataManager.setSecurityList(securityArrayList, getContext());

                    }

                    ArrayList securityArray = DataManager.getSecurityList(getContext());
                    //Log.d("response 200", String.valueOf(securityArray.size()));


                    //DOOrderContainer doOrder = new DOOrderContainer();
                    if (securityArray != null){
                        parseData(securityArray);
                        //noItem.setVisibility(View.INVISIBLE);
                        if (securityArray.size() > 3){
                            showAllBtn.setVisibility(View.VISIBLE);
                        }
                    }else{
                        //noItem.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<SecurityList> call, Throwable t) {

                hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }

    // Order Count
    private void getOrderRequest(){

        hud.show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());

        Call<OrderList> call = MainActivity.apiClient.getOrderListByEmployee(loggedInUser.userId);

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

                    orderRequested.setText(String.valueOf(requestedCount));
                    orderDelivered.setText(String.valueOf(deliveredCount));
                    orderPartialDelivered.setText(String.valueOf(partialDeliveredCount));

                    hud.dismiss();


                }else {

                    try {
                        hud.dismiss();
                        Log.d("response3", "post submitted to API.");

                    } catch (Exception e) {
                        hud.dismiss();
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());

                    }
                }
            }

            @Override
            public void onFailure(Call<OrderList> call, Throwable t) {

                Log.d("response5", t.getLocalizedMessage());

                hud.dismiss();
            }
        });
    }

    private void getChallanList(){

        DataManager.removeChallanList(getContext());

        hud.show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());
//        final String accept = "application/json";
//        final String token = "Bearer " + loggedInUser.accessToken;
//
//        String API_BASE_URL = "http://mis.nurtech.xyz/api/v1/";
//
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//
//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request request = chain.request().newBuilder()
//                        .addHeader("Accept", accept)
//                        .addHeader("Authorization", token)
//                        .build();
//                return chain.proceed(request);
//            }
//        });
//        //Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).client(httpClient.build()).build();
//
//        //OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS);
//
//
//        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());
//
//        //Retrofit retrofit = builder.build();
//        Retrofit retrofit = builder.client(httpClient.build()).build();
//        ApiClient userApiClient = retrofit.create(ApiClient.class);
//        Call<ChallanList> call = userApiClient.getChallanListByEmployee(loggedInUser.userId);

        Call<ChallanList> call = MainActivity.apiClient.getChallanListByEmployee(loggedInUser.userId);

        call.enqueue(new Callback<ChallanList>() {
            @Override
            public void onResponse(Call<ChallanList> call, Response<ChallanList> response) {

                if(response.isSuccessful()) {

                    ChallanList challanList = response.body();
                    //List<ChallanInfo> challanInfos = challanList.getData();
                    List<ChallanInfo> challanInfos = null;
                    if (challanList != null){
                        challanInfos = challanList.getData();
                        for (ChallanInfo challan: challanInfos){

                            //Log.d("response2", challan.getDealerName());
                            if (challan.getStatus().toLowerCase().equals("draft")) {
                                draftCount +=1;

                            }
                            if (challan.getStatus().toLowerCase().equals("in_transit")) {
                                inTransitCount +=1;

                            }
                            if (challan.getStatus().toLowerCase().equals("received")) {
                                receivedCount +=1;

                            }
                        }
                    }

                    challanDraft.setText(String.valueOf(draftCount));
                    challanInTransit.setText(String.valueOf(inTransitCount));
                    challanReceived.setText(String.valueOf(receivedCount));


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



}
