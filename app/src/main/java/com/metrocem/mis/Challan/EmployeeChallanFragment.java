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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Adapter.ChallanAdapter;
import com.metrocem.mis.Container.ChallanContainer;
import com.metrocem.mis.Container.DealerListContainer;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.R;
import com.metrocem.mis.Subclasses.ChallanInfo;
import com.metrocem.mis.Subclasses.ChallanList;
import com.metrocem.mis.Subclasses.CurrentUser;
import com.metrocem.mis.Subclasses.DataManager;
import com.metrocem.mis.Subclasses.Dealer;
import com.metrocem.mis.Subclasses.DealerList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeChallanFragment extends Fragment {

    private ArrayList<String> dealerList;
    private ArrayList dealerArray, challanArray, filteredChallanArray;
    //private Spinner dealerDropdown;
    private ArrayAdapter<String> dealerAdapter;
    private KProgressHUD hud;
    private TextView connectionStatus, noItem;
    private RecyclerView doRecyclerView;
    ChallanAdapter challanAdapter;
    //private EditText searchEditText;
    //private CurrentUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.emploee_challan_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.challan_info);

        CurrentUser currentUser = DataManager.getCurrentUser(getContext());

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        connectionStatus = view.findViewById(R.id.networkStatus);
        noItem = view.findViewById(R.id.noItemStatus);

        doRecyclerView = view.findViewById(R.id.challanListView);
        doRecyclerView.setHasFixedSize(true);
        doRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        challanArray = new ArrayList<>();
        filteredChallanArray = new ArrayList<>();

        //challanAdapter = new ChallanAdapter(getContext(), challanArray);
        //doRecyclerView.setAdapter(challanAdapter);

        if (currentUser.role.equals("Dealer") || currentUser.role.equals("dealer")){
            getChallanListByDealer(currentUser.userId);
        }

        dealerList = new ArrayList<>();
        dealerArray = DataManager.getDealerList(getContext());
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
                Toast.makeText(getContext(),"Connection Error!", Toast.LENGTH_SHORT).show();
            }
        }

        Spinner dealerDropdown = view.findViewById(R.id.dealerSpinner);
        //final String[] typeItems = new String[]{"Product Type"};
        dealerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, dealerList);
        //typeAdapter = new ArrayAdapter<>(getContext(),  android.R.layout.simple_spinner_dropdown_item, productTypes);
        dealerDropdown.setAdapter(dealerAdapter);
        dealerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                //selectedDealerIndex = position;
                //locationDropdown.setSelection(0);

                if(position==0){

                    if (isNetworkAvailable()){
                        getChallanList();

                    }else {
                        connectionStatus.setVisibility(View.VISIBLE);
                        //Toast.makeText(this,"Connection Error!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    DealerListContainer dealer = (DealerListContainer) dealerArray.get(position-1);
                    if (isNetworkAvailable()){
                        getChallanListByDealer(dealer.dealer_id);

                    }else {
                        connectionStatus.setVisibility(View.VISIBLE);
                        //Toast.makeText(this,"Connection Error!", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        EditText searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.setHint("Search by dealer name or status");
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
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void parseData(List<ChallanContainer> challans) {
        ChallanAdapter challanAdapter = new ChallanAdapter(getContext(), challans);
        doRecyclerView.setAdapter(challanAdapter);
        //challanAdapter.notifyDataSetChanged();

    }

    public void getDealerList(){

        hud.show();
        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());
        /*final String accept = "application/json";
        final String token = "Bearer " + loggedInUser.accessToken;
        Log.d("response", token);

        String API_BASE_URL = "http://mis.nurtech.xyz/api/v1/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", accept)
                        .addHeader("Authorization", token)
                        .build();
                return chain.proceed(request);
            }
        });
        //Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).client(httpClient.build()).build();

        //OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS);


        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());

        //Retrofit retrofit = builder.build();
        Retrofit retrofit = builder.client(httpClient.build()).build();
        ApiClient userApiClient = retrofit.create(ApiClient.class);
        Call<DealerList> call = userApiClient.getDealerList(loggedInUser.userId);*/

        Call<DealerList> call = MainActivity.apiClient.getDealerList(loggedInUser.userId);

        call.enqueue(new Callback<DealerList>() {
            @Override
            public void onResponse(Call<DealerList> call, Response<DealerList> response) {

                Log.d("response", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    DealerList deliveryModeList = response.body();
                    List<Dealer> dealers = deliveryModeList.getData();

                    if (dealers.size() > 0){
                        ArrayList dealerArrayList = new ArrayList();

                        for (Dealer dealer: dealers){

                            Log.d("response", dealer.getName());

                            DealerListContainer dealerContainer = new DealerListContainer();
                            dealerContainer.dealer_name = dealer.getName();
                            dealerContainer.dealer_id = dealer.getId();
                            dealerContainer.organization = dealer.getOrganization();
                            dealerContainer.address = dealer.getAddress();
                            dealerContainer.phone = dealer.getPhone();
                            dealerArrayList.add(dealerContainer);
                            DataManager.setDealerList(dealerArrayList, getContext());


                        }

                        dealerArray = DataManager.getDealerList(getContext());


                        for (int i = 0; i<dealerArray.size(); i++){
                            DealerListContainer item = (DealerListContainer) dealerArray.get(i);
                            Log.d("response", item.dealer_name);

                            dealerList.add(item.organization+" - "+item.dealer_name);
                        }



                        dealerAdapter.notifyDataSetChanged();
                    }



//                    dealerArray = new String[dealers.size()];
//
//
//                    for(int i=0; i<dealers.size(); i++){
//
//                        dealerArray[i] = dealers.get(i).getName() + " - " + dealers.get(i).getOrganization();
//
//                    }

                    //String name = modeList.get(0).getName();
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
            public void onFailure(Call<DealerList> call, Throwable t) {

                hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }

    private void getChallanList(){

        DataManager.removeChallanList(getContext());

        hud.show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());
        /*final String accept = "application/json";
        final String token = "Bearer " + loggedInUser.accessToken;
        Log.d("response", token);
        Log.d("response", loggedInUser.userId.toString());

        //Gson gson = new GsonBuilder()
        //        .setLenient()
        //        .create();


        String API_BASE_URL = "http://mis.nurtech.xyz/api/v1/";

//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(new Interceptor() {
//                                      @Override
//                                      public okhttp3.Response intercept(Chain chain) throws IOException {
//                                          Request original = chain.request();
//
//                                          Request request = original.newBuilder()
//                                                  .header("Accept", accept)
//                                                  .header("Accept", "Bearer"+token)
//                                                  .method(original.method(), original.body())
//                                                  .build();
//
//                                          return chain.proceed(request);
//                                      }
//                                  });
//
//        OkHttpClient client = httpClient.build();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(API_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(client)
//                .build();


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", accept)
                        .addHeader("Authorization", token)
                        .build();
                return chain.proceed(request);
            }
        });
        //Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).client(httpClient.build()).build();

        //OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS);


        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());

        //Retrofit retrofit = builder.build();
        Retrofit retrofit = builder.client(httpClient.build()).build();
        ApiClient userApiClient = retrofit.create(ApiClient.class);
        Call<ChallanList> call = userApiClient.getChallanListByEmployee(loggedInUser.userId);*/

        Call<ChallanList> call = MainActivity.apiClient.getChallanListByEmployee(loggedInUser.userId);

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

    private void getChallanListByDealer(Integer dealer_id){

        DataManager.removeChallanList(getContext());

        hud.show();

        /*CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());
        final String accept = "application/json";
        final String token = "Bearer " + loggedInUser.accessToken;
        Log.d("response", token);
        Log.d("response", loggedInUser.userId.toString());

        //Gson gson = new GsonBuilder()
        //        .setLenient()
        //        .create();


        String API_BASE_URL = "http://mis.nurtech.xyz/api/v1/";

//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(new Interceptor() {
//                                      @Override
//                                      public okhttp3.Response intercept(Chain chain) throws IOException {
//                                          Request original = chain.request();
//
//                                          Request request = original.newBuilder()
//                                                  .header("Accept", accept)
//                                                  .header("Accept", "Bearer"+token)
//                                                  .method(original.method(), original.body())
//                                                  .build();
//
//                                          return chain.proceed(request);
//                                      }
//                                  });
//
//        OkHttpClient client = httpClient.build();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(API_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(client)
//                .build();


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", accept)
                        .addHeader("Authorization", token)
                        .build();
                return chain.proceed(request);
            }
        });
        //Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).client(httpClient.build()).build();

        //OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS);


        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());

        //Retrofit retrofit = builder.build();
        Retrofit retrofit = builder.client(httpClient.build()).build();
        ApiClient userApiClient = retrofit.create(ApiClient.class);
        Call<ChallanList> call = userApiClient.getChallanListByDealer(dealer_id);*/

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
                    Collections.reverse(challanArray);

                    //DOOrderContainer doOrder = new DOOrderContainer();
                    if (challanArray != null){
                        parseData(challanArray);
                        noItem.setVisibility(View.INVISIBLE);

                    }else{
                        noItem.setVisibility(View.VISIBLE);
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

                    ChallanContainer challan = (ChallanContainer) challanArray.get(i);

                    if (challan.dealerName.toLowerCase(Locale.getDefault()).contains(searchedText) || challan.status.toLowerCase(Locale.getDefault()).contains(searchedText)){

                        filteredChallanArray.add(challanArray.get(i));

                    }

                }

                parseData(filteredChallanArray);
            }

        }


    }


}
