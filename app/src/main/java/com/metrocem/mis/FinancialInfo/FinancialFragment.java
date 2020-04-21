package com.metrocem.mis;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.Model.Collection;
import com.metrocem.mis.Model.CollectionAmount;
import com.metrocem.mis.Model.CurrentUser;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.Due;
import com.metrocem.mis.Model.DueAmount;
import com.metrocem.mis.Subclasses.CheckNetworkConnection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinancialFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener

    {

    private String[] monthNameArray;
    private TextView btnView, startDate = null, endDate = null;
    private CurrentUser currentUser;
    private KProgressHUD hud;
    private String btnTitle;
    //MaterialDatePicker datePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_financial, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.financial_info);
        currentUser = DataManager.getCurrentUser(getContext());
        startDate = view.findViewById(R.id.startDateTV);
        endDate = view.findViewById(R.id.endDateTV);

        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        TextView creditLimitTV = view.findViewById(R.id.creditLimit);
        String creditLimit = "৳"+currentUser.creditLimit;
        creditLimitTV.setText(creditLimit);

        TextView creditLimitBtn = view.findViewById(R.id.creditLimitBtn);
        creditLimitBtn.setOnClickListener(this);

        TextView dsoBtn = view.findViewById(R.id.dsoBtn);
        dsoBtn.setOnClickListener(this);

        TextView collectionReportBtn = view.findViewById(R.id.collectionReportBtn);
        collectionReportBtn.setOnClickListener(this);

        //String dueBalance = "৳"+currentUser.dueAmount;
        //TextView dueBalanceTV = view.findViewById(R.id.dueAmountTV);
        //dueBalanceTV.setText(dueBalance());

        TextView dueBalanceBtn = view.findViewById(R.id.dueBalanceBtn);
        dueBalanceBtn.setOnClickListener(this);


        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.getDefault());
        String thisM = sdf.format(c.getTime());

        c.add(Calendar.MONTH, -1);
        String previousM = sdf.format(c.getTime());

        monthNameArray = new String[]{"Today","Yesterday","Last 7 days",thisM,previousM,"Last 6 months"};

        //btnView = view.findViewById(R.id.btnView);
        //btnView.setOnClickListener(this);

        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);


        //MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        //builder.setTitleText("Select Start Date");
        //datePicker = builder.build();
    }


    private void getCollectionAmount(){

        hud.show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getActivity());

        String fromDate;
        String toDate;
        if (startDate.getText().toString().equals("Start Date") || endDate.getText().toString().equals("End Date")){
            fromDate = null;
            toDate = null;
        }else{
            fromDate = startDate.getText().toString();
            toDate = endDate.getText().toString();
        }
        Log.d("response", startDate.getText().toString());


        Call<Collection> call = MainActivity.apiClient.getCollectionAmount(loggedInUser.userId, fromDate,toDate);

        call.enqueue(new Callback<Collection>() {
            @Override
            public void onResponse(Call<Collection> call, Response<Collection> response) {

                Log.d("response", String.valueOf(response.code()));

                if(response.isSuccessful()) {

                    Collection collection = response.body();
                    CollectionAmount collectionAmount = collection.getData();

                    Integer total_amount = collectionAmount.getTotal();

                    String title = "Collection Report";
                    String msg = "Total Collection Received BDT "+total_amount+".";
                    DataManager.alertShow(title, msg, getActivity());

                    hud.dismiss();

                }else {
                    try {
                        hud.dismiss();
                    } catch (Exception e) {
                        hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Collection> call, Throwable t) {

                hud.dismiss();
            }
        });
    }

    private void getDueAmount(){

        hud.show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(getActivity());
//        final String accept = "application/json";
//        final String token = "Bearer " + loggedInUser.accessToken;
//        Log.d("response", token);
//        Log.d("response", loggedInUser.userId.toString());
//
//
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
//        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());
//
//        //Retrofit retrofit = builder.build();
//        Retrofit retrofit = builder.client(httpClient.build()).build();
//        ApiClient userApiClient = retrofit.create(ApiClient.class);
//        Call<Due> call = userApiClient.getDueAmount(loggedInUser.userId);

        Call<Due> call = MainActivity.apiClient.getDueAmount(loggedInUser.userId);

        call.enqueue(new Callback<Due>() {
            @Override
            public void onResponse(Call<Due> call, Response<Due> response) {

                Log.d("response", String.valueOf(response.code()));
                if(response.isSuccessful()) {

                    Log.d("response", String.valueOf(response.body().getData()));
                    Log.d("response", response.body().getData().toString());
                    Due due = response.body();
                    DueAmount collectionAmount = due.getData();
                    Integer total_amount = collectionAmount.getTotal();

                    String title = "Due Balance";
                    String msg = "Your last due balance is BDT "+total_amount+".";

                    hud.dismiss();

                    DataManager.alertShow(title, msg, getActivity());



                }else {

                    try {
                        hud.dismiss();
                        Log.d("response3", "post submitted to API." + response.errorBody());

                    } catch (Exception e) {
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Due> call, Throwable t) {

                hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }

    public void optionsBtnClick() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose your option");

        // add a list
        builder.setItems(monthNameArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        btnView.setText(monthNameArray[which]);
                        break;
                    case 1:
                        btnView.setText(monthNameArray[which]);
                        break;
                    case 2:
                        btnView.setText(monthNameArray[which]);
                        break;
                    case 3:
                        btnView.setText(monthNameArray[which]);
                        //getMonthData(monthNameArray[which]);
                        break;
                    case 4:
                        btnView.setText(monthNameArray[which]);
                        //getMonthData(monthNameArray[which]);
                        break;
                    case 5:
                        btnView.setText(monthNameArray[which]);
                        break;


                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.creditLimitBtn:
                String title = "Credit Limit";
                String msg = "Your current Credit Limit is BDT " + currentUser.creditLimit +".";
                DataManager.alertShow(title, msg, getActivity());
                break;

            case R.id.dsoBtn:
                String dso_title = "DSO";
                String dso_msg = "Your last 30 days DSO is 35 days.";
                DataManager.alertShow(dso_title, dso_msg, getActivity());
                break;
            case R.id.collectionReportBtn:
                if (CheckNetworkConnection.isNetworkAvailable(getContext())){
                    getCollectionAmount();

                }else {
                    Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.dueBalanceBtn:
                if (CheckNetworkConnection.isNetworkAvailable(getContext())){
                    getDueAmount();

                }else {
                    Toast.makeText(getActivity(),"Connection Error!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.startDateTV:
                btnTitle = "start";
                showDatePickerDialog();
                break;
            case R.id.endDateTV:
                btnTitle = "end";
                showDatePickerDialog();
                break;

        }
    }

    private void showDatePickerDialog(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)

                );
        datePickerDialog.show();
    }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            String month1 = String.valueOf(month);
            String day1 = String.valueOf(day);
            if(month < 10){
                month1 = "0"+month;
            }
            if(day < 10){
                day1 = "0"+day;

            }
            String date = year + "-" + month1 + "-" + day1;

            if(btnTitle.equals("start")){
                startDate.setText(date);
            }else {
                endDate.setText(date);
            }
            Log.d("response Date", "onDateSe" + date);
        }
    }
