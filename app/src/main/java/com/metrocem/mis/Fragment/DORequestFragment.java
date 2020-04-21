package com.metrocem.mismetrocem.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.metrocem.mismetrocem.R;
import com.metrocem.mismetrocem.SignIn.ApiClient;
import com.metrocem.mismetrocem.Subclasses.CurrentUser;
import com.metrocem.mismetrocem.Subclasses.DORequest;
import com.metrocem.mismetrocem.Subclasses.DORequestResponse;
import com.metrocem.mismetrocem.Subclasses.DataManager;
import com.metrocem.mismetrocem.Subclasses.DealerAddress;
import com.metrocem.mismetrocem.Subclasses.DealerAddressData;
import com.metrocem.mismetrocem.Subclasses.DeliveryMode;
import com.metrocem.mismetrocem.Container.DeliveryModeContainer;
import com.metrocem.mismetrocem.Subclasses.DeliveryModeList;
import com.metrocem.mismetrocem.Container.ProductContainer;
import com.metrocem.mismetrocem.Subclasses.ProductList;
import com.metrocem.mismetrocem.Subclasses.ProductType;
import com.metrocem.mismetrocem.Subclasses.Retailer;
import com.metrocem.mismetrocem.Container.RetailerContainer;
import com.metrocem.mismetrocem.Subclasses.RetailerList;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.metrocem.mismetrocem.EmployeeDO.DealerCreditInfo;
import com.metrocem.mismetrocem.EmployeeDO.DealerCreditList;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DORequestFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner dealerDropdown, retailerDropdown, typeDropdown, modeDropdown, locationDropdown;
    String[] items;
    EditText creditLimitET, billEditText, dueAmountET,blockAmountET, nameET, addressET, contactNoET, capacityEditText, noteEditText, doNumberEditText, priceEditText;
    ArrayAdapter<String> typeAdapter, modeAdapter, retailerAdapter;
    //ArrayList productTypes;
    ArrayList<String> deliveryType, productType, retailerList;
    //List<DeliveryModeContainer> modelist;
    //SwipeRefreshLayout swipeRefresh;
    TextView createDOBtn;
    public Integer productId, retailerId;
    String retailer_name, retailer_contact_no, retailer_address, dealer_name, dealer_no, dealer_address;
    Integer selectedRetailerIndex, selectedProductIndex, selectedModeIndex, selectedlocationIndex;
    ArrayList productArray, retailerArray;
    KProgressHUD hud;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.do_request);

        productType = new ArrayList<>();
        retailerList = new ArrayList<>();
        deliveryType = new ArrayList<>();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());
        creditLimitET = view.findViewById(R.id.creditEditText);
        dueAmountET = view.findViewById(R.id.dueEditText);
        blockAmountET = view.findViewById(R.id.blockAmountEditText);
        blockAmountET.setText("0");
        //bagQtyET = view.findViewById(R.id.bagQtyEditText);
        nameET = view.findViewById(R.id.nameEditText);
        addressET = view.findViewById(R.id.addressEditText);
        contactNoET = view.findViewById(R.id.contactNoEditText);
        capacityEditText = view.findViewById(R.id.capacityEditText);
        noteEditText = view.findViewById(R.id.noteEditText);
        //doNumberEditText = view.findViewById(R.id.doNumberEditText);
        //priceEditText = view.findViewById(R.id.priceEditText);

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        dealerDropdown = view.findViewById(R.id.spinner1);
        if (loggedInUser.role.equals("Dealer") || loggedInUser.role.equals("dealer")){
            items = new String[]{loggedInUser.userName};
//            String creditLimit = loggedInUser.creditLimit.toString();
//            creditLimitET.setText(creditLimit);
//            if (loggedInUser.dueAmount != null){
//                dueAmountET.setText(loggedInUser.dueAmount.toString());
//            }else{
//                dueAmountET.setText("0");
//            }

        }else {
            items = new String[]{"Dealer Name"};
        }

        getDealerInfo();
        getDealerAddress();

        DataManager.removeRetailerList(getContext());
        //DataManager.removeProductItems(getContext());
        //DataManager.removeDeliveryMode(getContext());
        retailerArray = DataManager.getRetailerList(getContext());
        retailerList.clear();
        retailerList.add("--Select Retailer--");
        if (retailerArray != null){
            //getRetailerList();
            for (int i = 0; i<retailerArray.size(); i++){
                RetailerContainer retailer = (RetailerContainer) retailerArray.get(i);
                Log.d("product id", String.valueOf(retailer.retailer_id));
                retailerList.add(retailer.retailer_name);
            }

        }else {

            if (isNetworkAvailable()){
                getRetailerList();

            }else {
                Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
            }
        }

        productArray = DataManager.getProductItems(getContext());
        //Log.d("product", String.valueOf(productList.size()));
        productType.clear();
        productType.add("--Select your Product--");
        if (productArray != null){

            for (int i = 0; i<productArray.size(); i++){
                ProductContainer product = (ProductContainer) productArray.get(i);
                productType.add(product.product_name);
            }

        }else {

            if (isNetworkAvailable()){

                getProductList();

            }else {
                Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
            }
        }

        final ArrayList deliveryModeType = DataManager.getDeliveryModeItems(getContext());
        deliveryType.clear();
        deliveryType.add("--Select Delivery Mode--");
        if (deliveryModeType != null){

            for (int i = 0; i<deliveryModeType.size(); i++){
                DeliveryModeContainer item = (DeliveryModeContainer) deliveryModeType.get(i);
                deliveryType.add(item.deliveryType);
            }

        }else {
            if (isNetworkAvailable()){
                getDeliveryModeList();

            }else {
                Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        dealerDropdown.setAdapter(adapter);

        retailerDropdown = view.findViewById(R.id.retailerSpinner);
        //final String[] retailerItems = new String[]{"Retailer Name"};
        retailerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, retailerList);
        retailerDropdown.setAdapter(retailerAdapter);
        retailerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedRetailerIndex = position;
                locationDropdown.setSelection(0);

                if (position>0){
                    RetailerContainer retailer = (RetailerContainer) retailerArray.get(position-1);
                    Log.d("value", String.valueOf(retailer.address));
                    retailerId = retailer.retailer_id;
                    retailer_name = retailer.retailer_name;
                    retailer_contact_no = retailer.phone;
                    retailer_address = retailer.address;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        typeDropdown = view.findViewById(R.id.productSpinner);
        //final String[] typeItems = new String[]{"Product Type"};
        typeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, productType);
        //typeAdapter = new ArrayAdapter<>(getContext(),  android.R.layout.simple_spinner_dropdown_item, productTypes);
        typeDropdown.setAdapter(typeAdapter);
        typeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                Log.d("response", String.valueOf(productType.size()));
                selectedProductIndex = position;
                if (position>0){
                    Log.d("response new", String.valueOf(productArray));
                    ProductContainer product = (ProductContainer) productArray.get(position-1);
                    Log.d("response", String.valueOf(product.product_id));
                    productId = product.product_id;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        modeDropdown = view.findViewById(R.id.deliverySpinner);
        //final String[] modeItems = new String[]{"Delivery Mode", "MCL", "FOB"};
        modeAdapter = new ArrayAdapter<String>(getContext(),  android.R.layout.simple_spinner_dropdown_item, deliveryType);
        //typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeDropdown.setAdapter(modeAdapter);
        modeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedModeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });



        locationDropdown = view.findViewById(R.id.locationSpinner);
        final String[] locationItems = new String[]{"--Select Location--","Shop","Site"};
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, locationItems);
        locationDropdown.setAdapter(locationAdapter);
        locationDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedlocationIndex = position;

                if (locationItems[position].equals("Shop")){
                    //RetailerContainer retailer = (RetailerContainer) retailerArray.get(retailerId);
                    if (selectedRetailerIndex != 0){
                        nameET.setText(retailer_name);
                        addressET.setText(retailer_address);
                        contactNoET.setText(retailer_contact_no);
                    }else{
                        nameET.setText(dealer_name);
                        addressET.setText(dealer_address);
                        contactNoET.setText(dealer_no);
                    }

                }else{
                    nameET.getText().clear();
                    addressET.getText().clear();
                    contactNoET.getText().clear();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        //Data user = new Data();

        //Log.d("response", user.getEmail());
        //sendDORequest();
        //getRetailerList();
        //getDeliveryModeList();
        //getProductList();

//        swipeRefresh = view.findViewById(R.id.swipeRefresh);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Log.d("response refresh","call");
//                getProductList();
//                swipeRefresh.setRefreshing(false);
//
//            }
//        });



        billEditText = view.findViewById(R.id.billEditText);

//        billEditText.addTextChangedListener(new TextWatcher() {
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
//                //SaleItemActivity.this.saleAdapter.filterItem(s);
//                //saleItemListView.invalidateViews();
//
//                if (s.length() > 0){
//                    String searchedText = s.toString();
//                    String result = String.valueOf(Integer.parseInt(searchedText)*1.04);
//                    Integer actualBag = math(Float.parseFloat(result));
//                    Log.d("response", String.valueOf(result)+" "+actualBag);
//
//                    //bagQtyET.setText(actualBag.toString());
//
//
//                }else{
//                    //bagQtyET.setText("0");
//                }
//
//            }
//        });

        createDOBtn = view.findViewById(R.id.createDOBtn);
        createDOBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()){
                    checkParameter();
                }else {
                    Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        switch (parent.getId()){
            case R.id.productSpinner:
                //Do another thing
                Log.d("call","call");
                Toast.makeText(getContext(), "Option Selected: " + parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.deliverySpinner:
                //Do something
                Log.d("call","call");
                Toast.makeText(getContext(), "Alarm Selected: " + parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.locationSpinner:
                //Do another thing
                Log.d("call","call");
                Toast.makeText(getContext(), "Option Selected: " + parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void checkParameter(){
//        if (selectedRetailerIndex == 0){
//            DataManager.alertShow("Error!","Select Retailer", getContext());
//        }
        if (selectedProductIndex == 0){
            DataManager.alertShow("Error!","Select your Product", getContext());
        }
        else if (creditLimitET.getText().length() <= 0){
            DataManager.alertShow("Error!","Credit Limit field is Empty", getContext());
        }
        else if (dueAmountET.getText().length() <= 0){
            DataManager.alertShow("Error!","Due Balance field is Empty", getContext());
        }
        else if (blockAmountET.getText().length() <= 0){
            DataManager.alertShow("Error!","Block Amount field is Empty", getContext());
        }
//        else if (priceEditText.getText().length() <= 0){
//            DataManager.alertShow("Error!","Price per Unit field is Empty", getContext());
//        }
        else if (selectedModeIndex == 0){
            DataManager.alertShow("Error!","Select Delivery mode", getContext());
        }
        else if (selectedlocationIndex == 0){
            DataManager.alertShow("Error!","Select Location", getContext());
        }
        else if (nameET.getText().length() <= 0){
            DataManager.alertShow("Error!","Name field is Empty", getContext());
        }
        else if (contactNoET.getText().length() <= 0){
            DataManager.alertShow("Error!","Contact Number field is Empty", getContext());
        }
        else if (addressET.getText().length() <= 0){
            DataManager.alertShow("Error!","Address field is Empty", getContext());
        }
        else {
            sendDORequest();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    public static int math(float f) {
//        int c = (int) ((f) + 0.5f);
//        float n = f + 0.5f;
//        return (n - c) % 2 == 0 ? (int) f : c;
//    }


    private void sendDORequest(){

        final KProgressHUD hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30)
                .show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getActivity());
        final String accept = "application/json";
        final String token = "Bearer " + loggedInUser.accessToken;
        Log.d("response", token);
        Log.d("response", loggedInUser.userId.toString());

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormatter.setLenient(false);
        Date today = new Date();
        final String todays = dateFormatter.format(today);
        Log.d("Date", todays);

//        String do_number = null;
//        if (doNumberEditText.getText().length()>0){
//            do_number = doNumberEditText.getText().toString();
//        }
        Integer retailer_id = retailerId;
        Integer product_id = productId;
        String delivery_mode = modeDropdown.getSelectedItem().toString();
        String location = locationDropdown.getSelectedItem().toString();
        ArrayList name = new ArrayList();
        name.add(nameET.getText().toString());
        ArrayList address = new ArrayList();
        address.add(addressET.getText().toString());
        ArrayList contactNumber = new ArrayList();
        contactNumber.add(contactNoET.getText().toString());
        Number billBag = Integer.parseInt(billEditText.getText().toString());
        //Number actualBag = Integer.parseInt(bagQtyET.getText().toString());
        Number vechileCap = null;
        if (capacityEditText.getText().length() > 0){
            vechileCap = Integer.parseInt(capacityEditText.getText().toString());
        }
        String note = null;
        if (noteEditText.getText().length()>0){
            note = noteEditText.getText().toString();
        }
//        Integer unit_price = Integer.parseInt(priceEditText.getText().toString());

        Log.d("response", todays +" "+" "+retailer_id+ " "+product_id+" "+delivery_mode+" "+location+" "+name+" "+address+" "+contactNumber+" "+billBag+" "+" "+vechileCap+" "+note+" "+note);



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
        Call<DORequestResponse> call = userApiClient.sendDORequest(loggedInUser.userId,retailer_id,product_id,delivery_mode,location,name,address,contactNumber,billBag,vechileCap,note);


        call.enqueue(new Callback<DORequestResponse>() {
            @Override
            public void onResponse(Call<DORequestResponse> call, Response<DORequestResponse> response) {

                Log.d("response", String.valueOf(response.code()));
                if(response.isSuccessful()) {
                    //ArrayList arrayList = new ArrayList();
                    //if (DataManager.getDOOrderList(getContext()) != null) {
                        //arrayList = DataManager.getDOOrderList(getContext());

                    //}

                    DORequestResponse doResponse = response.body();
                    DORequest request = doResponse.getDoRequest();

                    //DOOrderContainer doOrder = new DOOrderContainer();
                    //doOrder.id = request.getId();
                    //doOrder.type = request.getType();
                    //doOrder.
                    //doNumberEditText.getText().clear();
                    //bagQtyET.getText().clear();
                    billEditText.getText().clear();
                    //priceEditText.getText().clear();
                    nameET.getText().clear();
                    addressET.getText().clear();
                    contactNoET.getText().clear();
                    hud.dismiss();
                    DataManager.alertShow("Congratulation!","Request Successfull",getContext());


                }else {

                    try {
                        hud.dismiss();
                        Log.d("response3", String.valueOf(response.errorBody()));
                        DataManager.alertShow("Error!", "Response Error code "+response.code(),getContext());

                    } catch (Exception e) {
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DORequestResponse> call, Throwable t) {

                hud.dismiss();
                Log.d("response5", t.getLocalizedMessage());
            }
        });
    }

    private void getDealerAddress(){

        hud.show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getActivity());
        final String accept = "application/json";
        final String token = "Bearer " + loggedInUser.accessToken;
        Log.d("response", token);
        Log.d("response", loggedInUser.userId.toString());


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
        Call<DealerAddressData> call = userApiClient.getAddress(loggedInUser.userId);


        call.enqueue(new Callback<DealerAddressData>() {
            @Override
            public void onResponse(Call<DealerAddressData> call, Response<DealerAddressData> response) {

                Log.d("response222", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    //Log.d("response", String.valueOf(response.body()));
                    //Log.d("response", String.valueOf(response.body().toString()));
                    //List<DeliveryMode> data = (List<DeliveryMode>) response.body();
                    DealerAddressData data = response.body();
                    DealerAddress dealerObj = data.getAddress();

                    //DeliveryModeContainer type = new DeliveryModeContainer();

                    dealer_name = dealerObj.getName();
                    dealer_no = dealerObj.getContactNumber();
                    dealer_address = dealerObj.getDetailAddress();

                    hud.dismiss();


                }else {

                    try {
                        hud.dismiss();
                        Log.d("response3", "post submitted to API.");

                    } catch (Exception e) {
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        //hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DealerAddressData> call, Throwable t) {

                //hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }


    private void getDealerInfo(){

        hud.show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getActivity());
        final String accept = "application/json";
        final String token = "Bearer " + loggedInUser.accessToken;
        Log.d("response", token);
        Log.d("response", loggedInUser.userId.toString());


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
        Call<DealerCreditList> call = userApiClient.getDealerCreditInfo(loggedInUser.userId);


        call.enqueue(new Callback<DealerCreditList>() {
            @Override
            public void onResponse(Call<DealerCreditList> call, Response<DealerCreditList> response) {

                Log.d("response222", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    //Log.d("response", String.valueOf(response.body()));
                    //Log.d("response", String.valueOf(response.body().toString()));
                    //List<DeliveryMode> data = (List<DeliveryMode>) response.body();
                    DealerCreditList dealerCreditList = response.body();
                    DealerCreditInfo dealerCreditInfo = dealerCreditList.getDealerCreditInfo();

                    //DeliveryModeContainer type = new DeliveryModeContainer();

                    Log.d("response credit", dealerCreditInfo.getCreditLimit());
                    if (dealerCreditInfo.getCreditLimit() != null){
                        creditLimitET.setText(dealerCreditInfo.getCreditLimit());
                    }
                    if(dealerCreditInfo.getDueBalance() != null){
                        dueAmountET.setText(dealerCreditInfo.getDueBalance());
                    }
                    if(dealerCreditInfo.getBlockAmount() != null){
                        blockAmountET.setText(dealerCreditInfo.getBlockAmount());
                    }


                    hud.dismiss();


                }else {

                    try {
                        hud.dismiss();
                        Log.d("response3", "post submitted to API.");

                    } catch (Exception e) {
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        //hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DealerCreditList> call, Throwable t) {

                //hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }


    private void getRetailerList(){

        final KProgressHUD hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30)
                .show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getActivity());
        final String accept = "application/json";
        final String token = "Bearer " + loggedInUser.accessToken;
        Log.d("response", token);
        Log.d("response", loggedInUser.userId.toString());


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
        Call<RetailerList> call = userApiClient.getRetailerList(loggedInUser.userId);


        call.enqueue(new Callback<RetailerList>() {
            @Override
            public void onResponse(Call<RetailerList> call, Response<RetailerList> response) {

                Log.d("response", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    Log.d("response", String.valueOf(response.body().getData()));
                    Log.d("response", response.body().getData().toString());

                    RetailerList retailerListObject = response.body();
                    List<Retailer> retailers = retailerListObject.getData();

                    if (retailers.size() > 0){
                        ArrayList retailerArrayList = new ArrayList();

                        for (Retailer retailer: retailers){

                            Log.d("response", retailer.getOwnerName());

                            RetailerContainer retailerContainer = new RetailerContainer();
                            retailerContainer.retailer_name = retailer.getOwnerName();
                            retailerContainer.retailer_id = retailer.getId();
                            retailerContainer.address = retailer.getAddress();
                            retailerContainer.phone = retailer.getPhone();
                            retailerArrayList.add(retailerContainer);
                            DataManager.setRetailerList(retailerArrayList, getContext());


                        }

                        retailerArray = DataManager.getRetailerList(getContext());


                        for (int i = 0; i<retailerArray.size(); i++){
                            RetailerContainer item = (RetailerContainer) retailerArray.get(i);
                            Log.d("response", item.retailer_name);


                            retailerList.add(item.retailer_name);
                        }



                        retailerAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<RetailerList> call, Throwable t) {

                hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }


    private void getProductList(){

//        final KProgressHUD hud = KProgressHUD.create(getActivity())
//                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
//                .setLabel("Please wait")
//                .setMaxProgress(30)
//                .show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getActivity());
        final String accept = "application/json";
        final String token = "Bearer " + loggedInUser.accessToken;
        Log.d("response", token);
        Log.d("response", loggedInUser.userId.toString());


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
        Call<ProductList> call = userApiClient.getProductList();


        call.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {

                if(response.isSuccessful()) {

                    //Log.d("response", String.valueOf(response.body().getProductList()));
                    //JsonObject data = response.body().getProductList();

                    ProductList productList = response.body();
                    List<ProductType> productTypeList = productList.getProductList();

                    ArrayList productArrayList = new ArrayList();

                    for (ProductType product: productTypeList){

                        ProductContainer proType = new ProductContainer();
                        proType.product_name = product.getName();
                        proType.product_id = product.getId();
                        productArrayList.add(proType);
                        DataManager.setProductItems(productArrayList, getContext());

                    }

                    productArray = DataManager.getProductItems(getContext());


                    for (int i = 0; i<productArray.size(); i++){
                        ProductContainer item = (ProductContainer) productArray.get(i);
                        productType.add(item.product_name);
                    }

                    typeAdapter.notifyDataSetChanged();

                    //hud.dismiss();


                }else {

                    try {
                        //hud.dismiss();
                        Log.d("response3", "post submitted to API.");

                    } catch (Exception e) {
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        //hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {

                //hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }

    private void getDeliveryModeList(){

//        final KProgressHUD hud = KProgressHUD.create(getActivity())
//                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
//                .setLabel("Please wait")
//                .setMaxProgress(30)
//                .show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getActivity());
        final String accept = "application/json";
        final String token = "Bearer " + loggedInUser.accessToken;
        Log.d("response", token);
        Log.d("response", loggedInUser.userId.toString());


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
        Call<DeliveryModeList> call = userApiClient.getDeliveryModeList();


        call.enqueue(new Callback<DeliveryModeList>() {
            @Override
            public void onResponse(Call<DeliveryModeList> call, Response<DeliveryModeList> response) {

                Log.d("response222", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    //Log.d("response", String.valueOf(response.body()));
                    //Log.d("response", String.valueOf(response.body().toString()));
                    //List<DeliveryMode> data = (List<DeliveryMode>) response.body();
                    DeliveryModeList deliveryModeList = response.body();
                    List<DeliveryMode> modeList = deliveryModeList.getData();

                    //DeliveryModeContainer type = new DeliveryModeContainer();
                    ArrayList arrayList = new ArrayList();

                    for (DeliveryMode mode: modeList){

                        Log.d("response", mode.getName());

                        DeliveryModeContainer type = new DeliveryModeContainer();
                        type.deliveryType = mode.getName();
                        arrayList.add(type);
                        DataManager.setDeliveryModeItems(arrayList, getContext());


                    }

                    ArrayList deliveryModeType = DataManager.getDeliveryModeItems(getContext());


                    for (int i = 0; i<deliveryModeType.size(); i++){
                        DeliveryModeContainer item = (DeliveryModeContainer) deliveryModeType.get(i);
                        Log.d("response", item.deliveryType);


                        deliveryType.add(item.deliveryType);
                    }


                    modeAdapter.notifyDataSetChanged();


                    //hud.dismiss();


                }else {

                    try {
                        //hud.dismiss();
                        Log.d("response3", "post submitted to API.");

                    } catch (Exception e) {
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        //hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeliveryModeList> call, Throwable t) {

                //hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }


}
