package com.metrocem.mis.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Container.ChallanContainer;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.R;
import com.metrocem.mis.Model.ChallanReceiveInfo;
import com.metrocem.mis.Model.DataManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChallanDetailActivity extends AppCompatActivity {

    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challan_detail);

        getSupportActionBar().setTitle("Challan Detail"); // for set actionbar title
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.drawable.toolbar_gradient)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.toolbar_gradient));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Intent mIntent = getIntent();
        //int intValue = mIntent.getIntExtra("SELECTED_CHALLAN", 0);
        //Log.d("response", String.valueOf(intValue));
        //ArrayList challanArray = DataManager.getChallanList(this);
        //Object order = doArray.get(intValue);
        //CurrentUser currentUser = DataManager.getCurrentUser(this);

        Bundle bundle = getIntent().getExtras();

        ChallanContainer challan = (ChallanContainer) bundle.getSerializable("Challan_Obj");
        Log.d("response challan", challan.doNumber);

        hud = KProgressHUD.create(ChallanDetailActivity.this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        //ChallanContainer challan = (ChallanContainer) challanArray.get(intValue);
        final TextView challan_Id_TV = findViewById(R.id.do_Id_TV);
        challan_Id_TV.setText(challan.id.toString());

        TextView employee_name_TV = findViewById(R.id.employee_name_TV);
        employee_name_TV.setText(challan.employeeName);

        TextView dealer_name_TV = findViewById(R.id.dealer_name_TV);
        dealer_name_TV.setText(challan.dealerName);

        TextView bag_qty_TV = findViewById(R.id.bag_qty_TV);
        bag_qty_TV.setText(challan.bagQuantity.toString());

        TextView delivery_address_TV = findViewById(R.id.delivery_address_TV);
        delivery_address_TV.setText(challan.deliveryAddress);

        TextView contact_no_TV = findViewById(R.id.contact_no_TV);
        contact_no_TV.setText(challan.dealerPhone);

        TextView unit_price_TV = findViewById(R.id.unit_price_TV);
        unit_price_TV.setText(challan.unitPrice.toString());

        TextView product_name_TV = findViewById(R.id.product_name_TV);
        product_name_TV.setText(challan.productName);

        TextView do_number_TV = findViewById(R.id.do_number_TV);
        do_number_TV.setText(challan.doNumber);

        TextView mode_TV = findViewById(R.id.mode_TV);
        mode_TV.setText(challan.deliveryMode);

        TextView processing_time_TV = findViewById(R.id.processing_time_TV);
        processing_time_TV.setText(challan.doProcessing);

        TextView allocated_time_TV = findViewById(R.id.allocated_time_TV);
        allocated_time_TV.setText(challan.doAllocating);

        CardView cardView = findViewById(R.id.save_card_view);
        TextView receiveBtn = findViewById(R.id.receiveBtn);
        if(challan.status.toLowerCase().equals("received") || challan.status.toLowerCase().equals("draft")){
            cardView.setVisibility(View.INVISIBLE);
        }
        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                receiveChallan(challan_Id_TV.getText().toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void receiveChallan(String challanId){


        hud.show();

        /*CurrentUser loggedInUser = DataManager.getCurrentUser(ChallanDetailActivity.this);
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
        Call<ChallanReceiveInfo> call = userApiClient.receiveChallan(Integer.valueOf(challanId));*/

        Call<ChallanReceiveInfo> call = MainActivity.apiClient.receiveChallan(Integer.valueOf(challanId));

        call.enqueue(new Callback<ChallanReceiveInfo>() {
            @Override
            public void onResponse(Call<ChallanReceiveInfo> call, Response<ChallanReceiveInfo> response) {

                Log.d("response", String.valueOf(response.code()));
                if(response.isSuccessful()) {
                    //ArrayList arrayList = new ArrayList();
                    //if (DataManager.getDOOrderList(getContext()) != null) {
                    //arrayList = DataManager.getDOOrderList(getContext());

                    //}

                    ChallanReceiveInfo challanResponse = response.body();

                    hud.dismiss();

                    alertShow("Message!",challanResponse.getMessage(),ChallanDetailActivity.this);


                }else {

                    try {
                        hud.dismiss();
                        Log.d("response3", String.valueOf(response.errorBody()));
                        DataManager.alertShow("Error!", "Response Error code "+response.code(),ChallanDetailActivity.this);

                    } catch (Exception e) {
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChallanReceiveInfo> call, Throwable t) {

                hud.dismiss();
                Log.d("response5", t.getLocalizedMessage());
            }
        });
    }

    public void alertShow(String title, String msgString, Context context){

        AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
        builderInner.setTitle(title);
        builderInner.setMessage(msgString);
        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        builderInner.show();
    }

}
