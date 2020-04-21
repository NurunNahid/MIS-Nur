package com.metrocem.mis.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.metrocem.mis.Container.DealerListContainer;
import com.metrocem.mis.Container.RetailerContainer;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.R;
import com.metrocem.mis.Model.CurrentUser;
import com.metrocem.mis.Model.DORequest;
import com.metrocem.mis.Model.DORequestResponse;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.Dealer;
import com.metrocem.mis.Model.DealerList;
import com.metrocem.mis.Model.DeliveryMode;
import com.metrocem.mis.Container.DeliveryModeContainer;
import com.metrocem.mis.Model.DeliveryModeList;
import com.metrocem.mis.Container.ProductContainer;
import com.metrocem.mis.Model.ProductList;
import com.metrocem.mis.Model.ProductType;
import com.metrocem.mis.Model.Retailer;
import com.metrocem.mis.Model.RetailerList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.metrocem.mis.EmployeeDO.DealerCreditInfo;
import com.metrocem.mis.EmployeeDO.DealerCreditList;
import com.metrocem.mis.Subclasses.CheckNetworkConnection;
import com.metrocem.mis.Utilities.Dialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeDOFragment extends Fragment {

    private Spinner dealerDropdown, retailerDropdown, typeDropdown, modeDropdown, locationDropdown;
    String[] items;
    private EditText creditLimitET, bagQtyET, billEditText, dueAmountET,blockAmountET, nameET, addressET, contactNoET, capacityEditText, noteEditText, doNumberEditText, priceEditText;
    private ArrayAdapter<String> typeAdapter, modeAdapter, dealerAdapter, retailerAdapter;
    //ArrayList productTypes;
    private ArrayList<String> deliveryType, productType, dealerList, retailerList;
    //List<DeliveryModeContainer> modelist;
    //SwipeRefreshLayout swipeRefresh;
    private TextView createDOBtn;
    private Integer productId, retailerId, dealerId;
    private String retailer_name, retailer_contact_no, retailer_address, dealer_name, dealer_no, dealer_address, productName;
    private Integer selectedRetailerIndex, selectedProductIndex, selectedModeIndex, selectedlocationIndex, selectedDealerIndex;
    private ArrayList productArray, dealerArray, retailerArray;
    private KProgressHUD hud;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.employee_do_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.do_request);

        productType = new ArrayList<>();
        dealerList = new ArrayList<>();
        deliveryType = new ArrayList<>();
        retailerList = new ArrayList<>();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());
        creditLimitET = view.findViewById(R.id.creditEditText);
        dueAmountET = view.findViewById(R.id.dueEditText);
        blockAmountET = view.findViewById(R.id.blockAmountEditText);
        creditLimitET.setText("0");
        dueAmountET.setText("0");
        blockAmountET.setText("0");

        //bagQtyET = view.findViewById(R.id.bagQtyEditText);
        nameET = view.findViewById(R.id.nameEditText);
        addressET = view.findViewById(R.id.addressEditText);
        contactNoET = view.findViewById(R.id.contactNoEditText);
        capacityEditText = view.findViewById(R.id.capacityEditText);
        noteEditText = view.findViewById(R.id.noteEditText);

        //getDealerList();
        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        dealerArray = DataManager.getDealerList(getContext());
        //Log.d("product", String.valueOf(productList.size()));
        //productType.clear();
        dealerList.add("--Select your Dealer--");
        if (dealerArray != null){

            getDealerList();

//            Log.d("response", String.valueOf(dealerArray.size()));
//            for (int i = 0; i<dealerArray.size(); i++){
//                DealerListContainer dealer = (DealerListContainer) dealerArray.get(i);
//                dealerList.add(dealer.organization+" - "+dealer.dealer_name);
//            }

        }else {

            if (CheckNetworkConnection.isNetworkAvailable(getContext())){

                getDealerList();

            }else {
                Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
            }
        }

        productArray = DataManager.getProductItems(getContext());
        //Log.d("product", String.valueOf(productList.size()));
        //productType.clear();
        productType.add("--Select your Product--");
        if (productArray != null){

            getProductList();

//            for (int i = 0; i<productArray.size(); i++){
//                ProductContainer product = (ProductContainer) productArray.get(i);
//                productType.add(product.product_name);
//            }

        }else {

            if (CheckNetworkConnection.isNetworkAvailable(getContext())){

                getProductList();

            }else {
                Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
            }
        }

        final ArrayList deliveryModeType = DataManager.getDeliveryModeItems(getContext());
        //deliveryType.clear();
        deliveryType.add("--Select Delivery Mode--");
        if (deliveryModeType != null){

            getDeliveryModeList();

//            for (int i = 0; i<deliveryModeType.size(); i++){
//                DeliveryModeContainer item = (DeliveryModeContainer) deliveryModeType.get(i);
//                deliveryType.add(item.deliveryType);
//            }

        }else {
            if (CheckNetworkConnection.isNetworkAvailable(getContext())){
                getDeliveryModeList();

            }else {
                Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
            }
        }

        dealerDropdown = view.findViewById(R.id.dealerSpinner);
        //final String[] typeItems = new String[]{"Product Type"};
        dealerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dealerList);
        //typeAdapter = new ArrayAdapter<>(getContext(),  android.R.layout.simple_spinner_dropdown_item, productTypes);
        dealerDropdown.setAdapter(dealerAdapter);
        dealerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedDealerIndex = position;
                locationDropdown.setSelection(0);

                if(position>0){
                    DealerListContainer dealer = (DealerListContainer) dealerArray.get(position-1);
                    //Log.d("response", String.valueOf(product.product_id));
                    dealerId = dealer.dealer_id;
                    dealer_name = dealer.dealer_name;
                    dealer_no = dealer.phone;
                    dealer_address = dealer.address;

                    //Integer dealerId = dealer.dealer_id;
                    getRetailerList(dealerId);
                    getDealerInfo(dealerId);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        retailerList.add("--Select Retailer--");
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

                selectedProductIndex = position;
                if (position>0){
                    ProductContainer product = (ProductContainer) productArray.get(position-1);
                    productId = product.product_id;
                    productName = product.product_name;
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
                    }else if(selectedDealerIndex != 0){
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
//                    bagQtyET.setText(actualBag.toString());
//
//
//                }else{
//                    bagQtyET.setText("0");
//                }
//
//            }
//        });

        createDOBtn = view.findViewById(R.id.createDOBtn);
        createDOBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkConnection.isNetworkAvailable(getContext())){
                    checkParameter();
                }else {
                    Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static int math(float f) {
        int c = (int) ((f) + 0.5f);
        float n = f + 0.5f;
        return (n - c) % 2 == 0 ? (int) f : c;
    }

    public void checkParameter(){
        if (selectedDealerIndex == 0){
            DataManager.alertShow("Error!","Select Dealer", getContext());
        }
        else if (selectedProductIndex == 0){
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
        else if (billEditText.getText().length() <= 0){
            DataManager.alertShow("Error!","Bag Qty field is Empty", getContext());
        }
        else {
            sendDORequest();
        }
    }

    private void sendDORequest(){


        hud.show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getActivity());


        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormatter.setLenient(false);
        Date today = new Date();
        final String todays = dateFormatter.format(today);

        Integer retailer_id = null;
        if (selectedRetailerIndex != 0){
            retailer_id = retailerId;
        }
        Integer product_id = productId;
        String delivery_mode = modeDropdown.getSelectedItem().toString();
        String location = locationDropdown.getSelectedItem().toString().toLowerCase();
        ArrayList<String> name = new ArrayList<String>();
        name.add(nameET.getText().toString());
        ArrayList<String> address = new ArrayList<String>();
        address.add(addressET.getText().toString());
        ArrayList<String> contactNumber = new ArrayList<String>();
        contactNumber.add(contactNoET.getText().toString());
        Number billBag = Integer.parseInt(billEditText.getText().toString());
        //Number actualBag = 0;
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



//        String API_BASE_URL = "http://mis.nurtech.xyz/api/v1/";
//
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
//        Call<DORequestResponse> call = userApiClient.sendDORequest(dealerId,retailer_id,product_id,delivery_mode,location,name,address,contactNumber,billBag,vechileCap,note);

        Call<DORequestResponse> call = MainActivity.apiClient.sendDORequest(dealerId,retailer_id,product_id,delivery_mode,location,name,address,contactNumber,billBag,vechileCap,note);

        call.enqueue(new Callback<DORequestResponse>() {
            @Override
            public void onResponse(Call<DORequestResponse> call, Response<DORequestResponse> response) {

                Log.d("response", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    DORequestResponse doResponse = response.body();
                    DORequest request = doResponse.getDoRequest();

                    billEditText.getText().clear();
                    //priceEditText.getText().clear();
                    nameET.getText().clear();
                    addressET.getText().clear();
                    contactNoET.getText().clear();
                    hud.dismiss();
                    //DataManager.alertShow("Congratulation!","Request Successfull",getContext());

                    Dialog.showDORequestSuccessfulDialog(request.getDoNumber(),productName, request.getActualBagQty(),request.getCreatedAt(), getContext());

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

    public void getDealerList(){
        final KProgressHUD hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30)
                .show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getActivity());

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

                            //Log.d("response", dealer.getName());

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


                        if(dealerArray != null){
                            for (int i = 0; i<dealerArray.size(); i++){
                                DealerListContainer item = (DealerListContainer) dealerArray.get(i);
                                dealerList.add(item.organization+" - "+item.dealer_name);
                            }
                        }




                        dealerAdapter.notifyDataSetChanged();
                    }


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

    private void getRetailerList(Integer dealer_Id){

        final KProgressHUD hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30)
                .show();

        Call<RetailerList> call = MainActivity.apiClient.getRetailerList(dealer_Id);


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
                        ArrayList<RetailerContainer> retailerArrayList = new ArrayList<RetailerContainer>();

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

                        if (retailerArray != null){
                            for (int i = 0; i<retailerArray.size(); i++){
                                RetailerContainer item = (RetailerContainer) retailerArray.get(i);
                                retailerList.add(item.retailer_name);
                            }
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


        Call<ProductList> call = MainActivity.apiClient.getProductList();

        call.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {

                if(response.isSuccessful()) {

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


                    if(productArray != null){
                        for (int i = 0; i<productArray.size(); i++){
                            ProductContainer item = (ProductContainer) productArray.get(i);
                            productType.add(item.product_name);
                        }
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


        Call<DeliveryModeList> call = MainActivity.apiClient.getDeliveryModeList();

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

                        DeliveryModeContainer type = new DeliveryModeContainer();
                        type.deliveryType = mode.getName();
                        arrayList.add(type);
                        DataManager.setDeliveryModeItems(arrayList, getContext());


                    }

                    ArrayList deliveryModeType = DataManager.getDeliveryModeItems(getContext());


                    if (deliveryModeType != null){
                        for (int i = 0; i<deliveryModeType.size(); i++){

                            DeliveryModeContainer item = (DeliveryModeContainer) deliveryModeType.get(i);
                            deliveryType.add(item.deliveryType);
                        }
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

    private void getDealerInfo(Integer dealer_Id){

        Call<DealerCreditList> call = MainActivity.apiClient.getDealerCreditInfo(dealer_Id);

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
                        //hud.dismiss();
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


}
