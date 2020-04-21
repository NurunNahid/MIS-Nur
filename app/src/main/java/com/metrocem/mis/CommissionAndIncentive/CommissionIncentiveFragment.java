package com.metrocem.mis.CommissionAndIncentive;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.R;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.Promotion;
import com.metrocem.mis.Model.PromotionList;
import com.metrocem.mis.Subclasses.CalculateCommission;
import com.metrocem.mis.Subclasses.CheckNetworkConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommissionIncentiveFragment extends Fragment {

    private TextView commisionValue, incentiveValue;
    //private Spinner promotionDropdown;
    private Integer dropdownValue;
    private ArrayList<String> promotionOfferList;
    private ArrayAdapter<String> promotionAdapter;
    private ArrayList promotionArray;
    private Integer selectedPromotionIndex;
    private KProgressHUD kProgressHUD;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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

        kProgressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        commisionValue = view.findViewById(R.id.commisionValue);
        incentiveValue = view.findViewById(R.id.incentiveValue);
        promotionOfferList = new ArrayList<>();

        promotionArray = DataManager.getPromotionList(getContext());
        promotionOfferList.add("Select Promotion");

        if (CheckNetworkConnection.isNetworkAvailable(getContext())){
            getPromotionList();

        }else {
            //connectionStatus.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(),"Connection Error!", Toast.LENGTH_SHORT).show();
        }


        Spinner promotionDropdown = view.findViewById(R.id.promotionSpinner);
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

                commissionCalculator(editTValue);
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

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
                if (CheckNetworkConnection.isNetworkAvailable(getContext())){
                    getPromotionList();
                }else {
                    Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }


            }
        });

    }

    private void commissionCalculator(Editable editTValue){

        String value = editTValue.toString();
        if (value.length() > 0){

            Integer incentiveValue = CalculateCommission.calculateCommissionAndIncentive(value);
            String commValue = incentiveValue.toString();
            commisionValue.setText(commValue);

        }

    }


    private void incentiveCalculator(Editable editTValue){

        String value = editTValue.toString();

        if (value.length() > 0) {
            Integer incentive = Integer.parseInt(value)*dropdownValue;
            incentiveValue.setText(incentive.toString());
        }else {
            incentiveValue.setText("0");
        }


    }

    private void getPromotionList(){

        kProgressHUD.show();

        Call<PromotionList> call = MainActivity.apiClient.getPromotionList();

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

                        //Log.d("response", promotion.getName());
                        if (promotion.getType().equals("surprise_offer")){
                            PromotionContainer promotionData = new PromotionContainer();
                            promotionData.promotion_name = promotion.getName();
                            promotionData.sales_qty = promotion.getSalesQty();
                            promotionArrayList.add(promotionData);
                            DataManager.setPromotionList(promotionArrayList, getContext());
                        }


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


                    kProgressHUD.dismiss();
                    mSwipeRefreshLayout.setRefreshing(false);


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
