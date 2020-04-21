package com.metrocem.mismetrocem.CommissionAndIncentive;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.metrocem.mismetrocem.Container.DealerListContainer;
import com.metrocem.mismetrocem.R;
import com.metrocem.mismetrocem.SignIn.ApiClient;
import com.metrocem.mismetrocem.Subclasses.CurrentUser;
import com.metrocem.mismetrocem.Subclasses.DataManager;
import com.metrocem.mismetrocem.Container.ProductContainer;
import com.metrocem.mismetrocem.Subclasses.Promotion;
import com.metrocem.mismetrocem.Subclasses.PromotionList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommissionIncentiveFragment extends Fragment {

    TextView commisionValue, incentiveValue;
    Spinner promotionDropdown;
    Integer dropdownValue;
    ArrayList<String> promotionOfferList;
    ArrayAdapter<String> promotionAdapter;
    ArrayList promotionArray;
    Integer selectedPromotionIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_commission_incentive,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.commission_incentive);

        //hideSoftKeyboard(getActivity());

        commisionValue = view.findViewById(R.id.commisionValue);
        incentiveValue = view.findViewById(R.id.incentiveValue);
        promotionOfferList = new ArrayList<>();

        promotionArray = DataManager.getPromotionList(getContext());
        promotionOfferList.add("Select Promotion");
        if (promotionArray != null){

            getPromotionList();

//            for (int i = 0; i<promotionList.size(); i++){
//                ProductContainer product = (ProductContainer) promotionList.get(i);
//                promotionOfferList.add(product.product_name);
//            }

        }else {

            Log.d("hello", "call");

            getPromotionList();
        }

//        dropdown = view.findViewById(R.id.spinner1);
//        final String[] items = new String[]{"5", "7", "10"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
//        dropdown.setAdapter(adapter);

//        promotionDropdown = view.findViewById(R.id.promotionSpinner);
//        //final String[] typeItems = new String[]{"Product Type"};
//        promotionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, dealerList);
//        //typeAdapter = new ArrayAdapter<>(getContext(),  android.R.layout.simple_spinner_dropdown_item, productTypes);
//        promotionDropdown.setAdapter(promotionAdapter);

        promotionDropdown = view.findViewById(R.id.promotionSpinner);
        promotionAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, promotionOfferList);
        promotionDropdown.setAdapter(promotionAdapter);


        EditText commisionCalculatorET = view.findViewById(R.id.commisionCalculatorET);
        commisionCalculatorET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editTValue) {
                //SaleItemActivity.this.saleAdapter.filterItem(s);
                //saleItemListView.invalidateViews();

                commisionCalculator(editTValue);
            }
        });

        final EditText incentiveCalculatorET = view.findViewById(R.id.incentiveCalculatorET);
        incentiveCalculatorET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editTValue) {
                //SaleItemActivity.this.saleAdapter.filterItem(s);
                //saleItemListView.invalidateViews();

                if (selectedPromotionIndex>0){
                    incentiveCalculator(editTValue);
                }
            }
        });




        promotionDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                selectedPromotionIndex = position;

                if(position>0){

                    PromotionContainer promotion = (PromotionContainer) promotionArray.get(position-1);

                    dropdownValue = promotion.sales_qty;
                    Integer editTextValue = Integer.valueOf(incentiveCalculatorET.getText().toString());
                    if (incentiveCalculatorET.getText().length() > 0){
                        Integer value = dropdownValue*editTextValue;
                        incentiveValue.setText(value.toString());
                    }else {
                        Toast.makeText(getActivity(),"Empty Incentive field", Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    public void commisionCalculator(Editable editTValue){

        String value = editTValue.toString();
        if (value.length() > 0){
            if (Integer.parseInt(value) >= 20000){

                Integer commvalue = (Integer.parseInt(String.valueOf(value))*31);
                commisionValue.setText(commvalue.toString());
            }
            else if (Integer.parseInt(value) >= 15000 && Integer.parseInt(String.valueOf(value)) < 20000){
                Integer commvalue = (Integer.parseInt(value)*29);
                commisionValue.setText(commvalue.toString());
            }
            else if (Integer.parseInt(value) >= 10000 && Integer.parseInt(String.valueOf(value)) < 15000){
                Integer commvalue = (Integer.parseInt(value)*26);
                commisionValue.setText(commvalue.toString());
            }
            else if (Integer.parseInt(value) >= 5000 && Integer.parseInt(String.valueOf(value)) < 10000){
                Integer commvalue = (Integer.parseInt(value)*23);
                commisionValue.setText(commvalue.toString());
            }
            else if (Integer.parseInt(value) >= 100 && Integer.parseInt(String.valueOf(value)) < 5000){
                Integer commvalue = (Integer.parseInt(value)*19);
                commisionValue.setText(commvalue.toString());
            }
            else if (Integer.parseInt(value) < 100){
                Integer commvalue = (Integer.parseInt(value)*0);
                commisionValue.setText(commvalue.toString());
            }
        }

    }

    public void incentiveCalculator(Editable editTValue){

        String value = editTValue.toString();

        if (value.length() > 0) {
            Integer incentive = Integer.parseInt(value)*dropdownValue;
            incentiveValue.setText(incentive.toString());
        }else {
            incentiveValue.setText("0");
        }


    }

    private void getPromotionList(){

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
        Call<PromotionList> call = userApiClient.getPromotionList();


        call.enqueue(new Callback<PromotionList>() {
            @Override
            public void onResponse(Call<PromotionList> call, Response<PromotionList> response) {

                Log.d("response", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    Log.d("response2", String.valueOf(response.body()));
                    //Log.d("response", String.valueOf(response.body().getProductList()));
                    //JsonObject data = response.body().getProductList();

                    PromotionList promotionList = response.body();
                    List<Promotion> promotions = promotionList.getData();

                    ArrayList promotionArrayList = new ArrayList();

                    for (Promotion promotion: promotions){

                        Log.d("response", promotion.getName());

                        PromotionContainer promotionData = new PromotionContainer();
                        promotionData.promotion_name = promotion.getName();
                        promotionData.sales_qty = promotion.getSalesQty();
                        promotionArrayList.add(promotionData);
                        DataManager.setPromotionList(promotionArrayList, getContext());


                    }


                    promotionArray = DataManager.getPromotionList(getContext());

                    if (promotionArray != null){
                        for (int i = 0; i<promotionArray.size(); i++){
                            PromotionContainer promotion = (PromotionContainer) promotionArray.get(i);
                            Log.d("response", promotion.promotion_name);
                            promotionOfferList.add(promotion.promotion_name);
                        }
                    }

                    promotionAdapter.notifyDataSetChanged();


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
            public void onFailure(Call<PromotionList> call, Throwable t) {

                //hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


}
