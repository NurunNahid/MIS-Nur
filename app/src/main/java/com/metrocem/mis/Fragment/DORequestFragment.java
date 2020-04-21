package com.metrocem.mis.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.R;
import com.metrocem.mis.Model.CurrentUser;
import com.metrocem.mis.Model.DORequest;
import com.metrocem.mis.Model.DORequestResponse;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.DealerAddress;
import com.metrocem.mis.Model.DealerAddressData;
import com.metrocem.mis.Model.DeliveryMode;
import com.metrocem.mis.Container.DeliveryModeContainer;
import com.metrocem.mis.Model.DeliveryModeList;
import com.metrocem.mis.Container.ProductContainer;
import com.metrocem.mis.Model.ProductList;
import com.metrocem.mis.Model.ProductType;
import com.metrocem.mis.Model.Retailer;
import com.metrocem.mis.Container.RetailerContainer;
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
import com.metrocem.mis.Utilities.APIDataManager;
import com.metrocem.mis.Utilities.Dialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DORequestFragment extends Fragment implements View.OnClickListener {

    private Spinner dealerDropdown, retailerDropdown, typeDropdown, modeDropdown, locationDropdown;
    String[] items;
    private EditText creditLimitET, billEditText, dueAmountET,blockAmountET, nameET, addressET, contactNoET, capacityEditText, noteEditText, doNumberEditText, priceEditText;
    private ArrayAdapter<String> typeAdapter, modeAdapter, retailerAdapter;
    //ArrayList productTypes;
    private ArrayList<String> deliveryType, productType, retailerList;
    //List<DeliveryModeContainer> modelist;
    //SwipeRefreshLayout swipeRefresh;
    private TextView createDOBtn, connectionStatus;
    private Integer productId, retailerId;
    private String retailer_name, retailer_contact_no, retailer_address, dealer_name, dealer_no, dealer_address, productName;
    private Integer selectedRetailerIndex, selectedProductIndex, selectedModeIndex, selectedlocationIndex;
    private ArrayList productArray;
    private ArrayList retailerArray;
    private KProgressHUD hud;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_request, null);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.do_request);

        //MainActivity activity = (MainActivity) getContext();

        initView(view);

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());

        if (loggedInUser.role.toLowerCase().equals("dealer")){
            items = new String[]{loggedInUser.userName};

        }else {
            items = new String[]{"Dealer Name"};
        }

        if(CheckNetworkConnection.isNetworkAvailable(getContext())){
            getDealerInfo();
            getDealerAddress();
        }else{
            connectionStatus.setVisibility(View.VISIBLE);
            //Toast.makeText(getContext(),"Connection Error!", Toast.LENGTH_SHORT).show();
        }



        /*DataManager.removeRetailerList(getContext());
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

            if (CheckNetworkConnection.isNetworkAvailable(getContext())){
                getRetailerList();

            }else {
                //Toast.makeText(getContext(),"Connection Error!", Toast.LENGTH_SHORT).show();
            }
        }*/

        /*productArray = DataManager.getProductItems(getContext());
        //Log.d("product", String.valueOf(productList.size()));
        productType.clear();
        productType.add("--Select your Product--");
        if (productArray != null) {

            //getProductList();

            for (int i = 0; i < productArray.size(); i++) {
                ProductContainer product = (ProductContainer) productArray.get(i);
                productType.add(product.product_name);
            }
        }
        else {

            if (CheckNetworkConnection.isNetworkAvailable(getContext())){

                getProductList();

            }else {
                //Toast.makeText(getContext(),"Connection Error!", Toast.LENGTH_SHORT).show();
            }
        }*/

        if (!CheckNetworkConnection.isNetworkAvailable(getContext())){
            retailerList = APIDataManager.getRetailerFromLocal(getContext());
            productType = APIDataManager.getProductFromLocal(getContext());
            deliveryType = APIDataManager.getDeliveryModeFromLocal(getContext());
            productArray = DataManager.getProductItems(getContext());
            retailerArray = DataManager.getRetailerList(getContext());
        }else {
            getRetailerList();
            getProductList();
            getDeliveryModeList();
        }

        /*final ArrayList deliveryModeType = DataManager.getDeliveryModeItems(getContext());
        deliveryType.clear();
        deliveryType.add("--Select Delivery Mode--");
        if (deliveryModeType != null){
            //getDeliveryModeList();

            for (int i = 0; i<deliveryModeType.size(); i++){
                DeliveryModeContainer item = (DeliveryModeContainer) deliveryModeType.get(i);
                deliveryType.add(item.deliveryType);
            }

        }
//        else {
//            if (CheckNetworkConnection.isNetworkAvailable(getContext())){
//                getDeliveryModeList();
//
//            }else {
//                Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
//            }
//        }*/



        //final String[] retailerItems = new String[]{"Retailer Name"};


        //DOThread doThread = new DOThread();
        //doThread.start();

        initDealerDropdown();
        initRetailerDropdown();
        initProductDropdown();
        initDeliveryMode();
        initLocationDropdown();

        setSwipeRefresh();



//        ((MainActivity)getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // your method
//                Log.d("response fragment","call");
//
//            }
//        });




    }



    private void initView(View view){

        productArray = new ArrayList();
        retailerArray = new ArrayList();
        productType = new ArrayList<>();
        retailerList = new ArrayList<>();
        deliveryType = new ArrayList<>();
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        connectionStatus = view.findViewById(R.id.networkStatus);

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
        //doNumberEditText = view.findViewById(R.id.doNumberEditText);
        //priceEditText = view.findViewById(R.id.priceEditText);

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        billEditText = view.findViewById(R.id.billEditText);
        dealerDropdown = view.findViewById(R.id.spinner1);
        retailerDropdown = view.findViewById(R.id.retailerSpinner);
        locationDropdown = view.findViewById(R.id.locationSpinner);
        modeDropdown = view.findViewById(R.id.deliverySpinner);
        typeDropdown = view.findViewById(R.id.productSpinner);

        createDOBtn = view.findViewById(R.id.createDOBtn);
        createDOBtn.setOnClickListener(this);


    }

    class DOThread extends Thread{
        @Override
        public void run() {
            initRetailerDropdown();
            initProductDropdown();
            initDeliveryMode();
            initLocationDropdown();
        }
    }

    private void initDealerDropdown() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, items);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        dealerDropdown.setAdapter(adapter);
    }

    private void initRetailerDropdown(){
        retailerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, retailerList);
        retailerDropdown.setAdapter(retailerAdapter);
        retailerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                //selectedRetailerIndex = position;
                locationDropdown.setSelection(0);

//                if (position>0){
//                    RetailerContainer retailer = (RetailerContainer) retailerArray.get(position-1);
//                    Log.d("value", String.valueOf(retailer.address));
//                    retailerId = retailer.retailer_id;
//                    retailer_name = retailer.retailer_name;
//                    retailer_contact_no = retailer.phone;
//                    retailer_address = retailer.address;
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void initProductDropdown(){
        typeAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, productType);
        typeDropdown.setAdapter(typeAdapter);
        /*typeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });*/
    }

    private void initDeliveryMode(){
        modeAdapter = new ArrayAdapter<String>(getContext(),  R.layout.spinner_item, deliveryType);
        modeDropdown.setAdapter(modeAdapter);
        /*modeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedModeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });*/
    }

    private void initLocationDropdown(){
        final String[] locationItems = new String[]{"--Select Location--","Shop","Site"};
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, locationItems);
        locationDropdown.setAdapter(locationAdapter);
        locationDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //selectedlocationIndex = position;

                if (locationItems[position].equals("Shop")){
                    if (retailerDropdown.getSelectedItemPosition() != 0){
                        RetailerContainer retailer = (RetailerContainer) retailerArray.get(position-1);

                        nameET.setText(retailer.retailer_name);
                        addressET.setText(retailer.address);
                        contactNoET.setText(retailer.phone);
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
    }

    private void setSwipeRefresh(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
                if (CheckNetworkConnection.isNetworkAvailable(getContext())){
                    getDealerInfo();
                    getDealerAddress();
                    getProductList();
                    getRetailerList();
                    getDeliveryModeList();
                }else {
                    //Toast.makeText(getContext(),"Connection Error!", Toast.LENGTH_SHORT).show();
                    connectionStatus.setVisibility(View.VISIBLE);
                    if(mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }


            }
        });
    }


//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
//        switch (parent.getId()){
//            case R.id.retailerSpinner:
//                locationDropdown.setSelection(0);
//                Toast.makeText(getContext(), "Option Selected: " + parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.deliverySpinner:
//                Toast.makeText(getContext(), "Alarm Selected: " + parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.locationSpinner:
//                Toast.makeText(getContext(), "Option Selected: " + parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

    private void checkParameter(){

        if (typeDropdown.getSelectedItemPosition() == 0){
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
        else if (modeDropdown.getSelectedItemPosition() == 0){
            DataManager.alertShow("Error!","Select Delivery mode", getContext());
        }
        else if (locationDropdown.getSelectedItemPosition() == 0){
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
        }else if (billEditText.getText().length() <= 0){
            DataManager.alertShow("Error!","Billed Qty field is Empty", getContext());
        }
        else {
            sendDORequest();
        }
    }


//    public static int math(float f) {
//        int c = (int) ((f) + 0.5f);
//        float n = f + 0.5f;
//        return (n - c) % 2 == 0 ? (int) f : c;
//    }


    private void sendDORequest(){

        final KProgressHUD hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30)
                .show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());

        //DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        //dateFormatter.setLenient(false);
        //Date today = new Date();
        //final String todays = dateFormatter.format(today);

        Integer retailer_id = retailerId;

        final ProductContainer product = (ProductContainer) productArray.get(typeDropdown.getSelectedItemPosition()-1);
        Integer product_id = product.product_id;

        String delivery_mode = modeDropdown.getSelectedItem().toString();
        String location = locationDropdown.getSelectedItem().toString();
        ArrayList<String> name = new ArrayList<>();
        name.add(nameET.getText().toString());
        ArrayList<String> address = new ArrayList<>();
        address.add(addressET.getText().toString());
        ArrayList<String> contactNumber = new ArrayList<>();
        contactNumber.add(contactNoET.getText().toString());
        final Number billBag = Integer.parseInt(billEditText.getText().toString());
        //Number actualBag = Integer.parseInt(bagQtyET.getText().toString());
        Number vechileCap = null;
        if (capacityEditText.getText().length() > 0){
            vechileCap = Integer.parseInt(capacityEditText.getText().toString());
        }
        String note = null;
        if (noteEditText.getText().length()>0){
            note = noteEditText.getText().toString();
        }

        //Log.d("response", todays +" "+" "+retailer_id+ " "+product_id+" "+delivery_mode+" "+location+" "+name+" "+address+" "+contactNumber+" "+billBag+" "+" "+vechileCap+" "+note+" "+note);

        Call<DORequestResponse> call = MainActivity.apiClient.sendDORequest(loggedInUser.userId,retailer_id,product_id,delivery_mode,location,name,address,contactNumber,billBag,vechileCap,note);

        call.enqueue(new Callback<DORequestResponse>() {
            @Override
            public void onResponse(Call<DORequestResponse> call, Response<DORequestResponse> response) {

                Log.d("response", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    Log.d("response", String.valueOf(response.body()));

                    DORequestResponse doResponse = response.body();
                    DORequest request = doResponse.getDoRequest();

                    Log.d("response 2", request.getDoNumber());

                    billEditText.getText().clear();
                    //priceEditText.getText().clear();
                    nameET.getText().clear();
                    addressET.getText().clear();
                    contactNoET.getText().clear();
                    hud.dismiss();

                    Dialog.showDORequestSuccessfulDialog(request.getDoNumber(),product.product_name, request.getActualBagQty(),request.getCreatedAt(), getContext());
                    //DataManager.alertShow("Congratulation!","Request Successfull",getContext());


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

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());

        Call<DealerAddressData> call = MainActivity.apiClient.getAddress(loggedInUser.userId);

        call.enqueue(new Callback<DealerAddressData>() {
            @Override
            public void onResponse(Call<DealerAddressData> call, Response<DealerAddressData> response) {

                Log.d("response222", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    DealerAddressData data = response.body();
                    DealerAddress dealerObj = data.getAddress();

                    //DeliveryModeContainer type = new DeliveryModeContainer();

                    dealer_name = dealerObj.getName();
                    dealer_no = dealerObj.getContactNumber();
                    dealer_address = dealerObj.getDetailAddress();

                    hud.dismiss();
                    mSwipeRefreshLayout.setRefreshing(false);


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

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());

        Call<DealerCreditList> call = MainActivity.apiClient.getDealerCreditInfo(loggedInUser.userId);

        call.enqueue(new Callback<DealerCreditList>() {
            @Override
            public void onResponse(Call<DealerCreditList> call, Response<DealerCreditList> response) {

                if(response.isSuccessful()) {

                    DealerCreditList dealerCreditList = response.body();
                    DealerCreditInfo dealerCreditInfo = dealerCreditList.getDealerCreditInfo();

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
                    mSwipeRefreshLayout.setRefreshing(false);


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

        hud.show();

        DataManager.removeRetailerList(getContext());
        if(retailerArray != null){
            retailerArray.clear();
        }
        retailerList.clear();
        retailerList.add("--Select Retailer--");

        CurrentUser loggedInUser = DataManager.getCurrentUser(getContext());

        Call<RetailerList> call = MainActivity.apiClient.getRetailerList(loggedInUser.userId);

        call.enqueue(new Callback<RetailerList>() {
            @Override
            public void onResponse(Call<RetailerList> call, Response<RetailerList> response) {

                if(response.isSuccessful()) {

                    RetailerList retailerListObject = response.body();
                    List<Retailer> retailers = retailerListObject.getData();

                    if (retailers.size() > 0){
                        ArrayList<RetailerContainer> retailerArrayList = new ArrayList<RetailerContainer>();

                        for (Retailer retailer: retailers){

                            RetailerContainer retailerContainer = new RetailerContainer();
                            retailerContainer.retailer_name = retailer.getOwnerName();
                            retailerContainer.retailer_id = retailer.getId();
                            retailerContainer.address = retailer.getAddress();
                            retailerContainer.phone = retailer.getPhone();
                            retailerArrayList.add(retailerContainer);
                            DataManager.setRetailerList(retailerArrayList, getContext());


                        }

                        retailerArray = DataManager.getRetailerList(getContext());

                        if(retailerArray != null){
                            for (int i = 0; i<retailerArray.size(); i++){
                                RetailerContainer item = (RetailerContainer) retailerArray.get(i);
                                retailerList.add(item.retailer_name);
                            }
                        }

                        retailerAdapter.notifyDataSetChanged();
                    }


                    hud.dismiss();
                    mSwipeRefreshLayout.setRefreshing(false);


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

        DataManager.removeProductItems(getContext());
        if(productArray != null){
            productArray.clear();
        }
        productType.clear();
        productType.add("--Select your Product--");

        Call<ProductList> call = MainActivity.apiClient.getProductList();

        call.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {

                if(response.isSuccessful()) {

                    //Log.d("response", String.valueOf(response.body().getProductList()));
                    //JsonObject data = response.body().getProductList();

                    ProductList productList = response.body();
                    List<ProductType> productTypeList = productList.getProductList();

                    if(productTypeList.size() != 0){

                        //ArrayList productArrayList = new ArrayList();

                        for (ProductType product: productTypeList){

                            //Log.d("product", String.valueOf(product.getName()));

                            productType.add(product.getName());

                            ProductContainer proType = new ProductContainer();
                            proType.product_name = product.getName();
                            proType.product_id = product.getId();
                            productArray.add(proType);
                            DataManager.setProductItems(productArray, getContext());

                        }

//                        productArray = DataManager.getProductItems(getContext());
//
//                        if (productArray != null){
//                            for (int i = 0; i<productArray.size(); i++){
//                                ProductContainer item = (ProductContainer) productArray.get(i);
//                                productType.add(item.product_name);
//                            }
//                        }


                    }


                    typeAdapter.notifyDataSetChanged();

                    mSwipeRefreshLayout.setRefreshing(false);
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


        deliveryType.clear();
        deliveryType.add("--Select Delivery Mode--");

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

                        Log.d("response", mode.getName());

                        DeliveryModeContainer type = new DeliveryModeContainer();
                        type.deliveryType = mode.getName();
                        arrayList.add(type);
                        DataManager.setDeliveryModeItems(arrayList, getContext());


                    }

                    ArrayList deliveryModeType = DataManager.getDeliveryModeItems(getContext());

                    if(deliveryModeType != null){
                        for (int i = 0; i<deliveryModeType.size(); i++){
                            DeliveryModeContainer item = (DeliveryModeContainer) deliveryModeType.get(i);
                            deliveryType.add(item.deliveryType);
                        }
                    }

                    modeAdapter.notifyDataSetChanged();

                    mSwipeRefreshLayout.setRefreshing(false);
                    connectionStatus.setVisibility(View.GONE);

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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.createDOBtn:
                if (CheckNetworkConnection.isNetworkAvailable(getContext())){
                    checkParameter();
                    connectionStatus.setVisibility(View.GONE);
                }else {
                    connectionStatus.setVisibility(View.VISIBLE);
                    //Toast.makeText(getContext(),"Connection Error!", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }


    }
}
