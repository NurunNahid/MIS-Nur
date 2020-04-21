package com.metrocem.mis.OrderInformation;

import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.ReuestedDOInfo.RequestedOrderActivity;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.R;
import com.metrocem.mis.Model.CurrentUser;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.Order;
import com.metrocem.mis.Model.OrderList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChallanInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
    }

    @Override
    public void onResume() {
        //will be executed onResume
        super.onResume();

        getSupportActionBar().setTitle("Challan History"); // for set actionbar title
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorGreen)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.toolbar_gradient));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout requestedDOHistory = findViewById(R.id.requestedDOHistory);
        requestedDOHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChallanInfoActivity.this, RequestedOrderActivity.class);
                intent.putExtra("CHALLAN_STATUS", "Draft");
                startActivity(intent);

            }
        });

        LinearLayout processedDOHistory = findViewById(R.id.processedDOHistory);
        processedDOHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChallanInfoActivity.this, RequestedOrderActivity.class);
                intent.putExtra("CHALLAN_STATUS", "In Transit");
                startActivity(intent);

            }
        });

        LinearLayout allocatedDOHistory = findViewById(R.id.allocatedDOHistory);
        allocatedDOHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChallanInfoActivity.this, RequestedOrderActivity.class);
                intent.putExtra("CHALLAN_STATUS", "Received");
                startActivity(intent);

            }
        });

        //getOrderList();


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

    private void getOrderList(){

        final KProgressHUD hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30)
                .show();

        CurrentUser loggedInUser = DataManager.getCurrentUser(this);
        /*final String accept = "application/json";
        final String token = "Bearer " + loggedInUser.accessToken;
        Log.d("response", token);
        Log.d("response", loggedInUser.id.toString());

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
        Call<OrderList> call = userApiClient.getOrderList(loggedInUser.id);*/

        Call<OrderList> call = MainActivity.apiClient.getOrderList(loggedInUser.id);

        call.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {

                Log.d("response", String.valueOf(response.code()));
                if(response.isSuccessful()) {


                    Log.d("response", String.valueOf(response.body().toString()));
                    //List<DeliveryMode> data = (List<DeliveryMode>) response.body();
                    OrderList orderList = response.body();
                    List<Order> order = orderList.getData();


                    //String name = order.get(1).getProductName();
                    Log.d("response", order.toString());
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
            public void onFailure(Call<OrderList> call, Throwable t) {

                hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }

}
