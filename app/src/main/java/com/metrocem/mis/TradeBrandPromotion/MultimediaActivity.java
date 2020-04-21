package com.metrocem.mis.TradeBrandPromotion;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MultimediaActivity extends AppCompatActivity {

    GridView myGrid;
    ArrayList multimediaArrayList;
    KProgressHUD kProgressHUD;
    Adapter adapter;
    //GridAdapter.ImageViewClickListener mImageViewClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocated_do);

        getSupportActionBar().setTitle("Multimedia"); // for set actionbar title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.toolbar_gradient));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kProgressHUD = KProgressHUD.create(MultimediaActivity.this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);

        multimediaArrayList = new ArrayList();
        myGrid = findViewById(R.id.gridView);
        //adapter = new GridAdapter(this,multimediaArrayList);
        //myGrid.setAdapter((ListAdapter) adapter);
        myGrid.setAdapter(new GridAdapter(this, multimediaArrayList));
        //((GridAdapter) adapter).setImageViewClickListener(this);

        if (isNetworkAvailable()){
            getMultimediaList();

        }else {
            //connectionStatus.setVisibility(View.VISIBLE);
            Toast.makeText(this,"Connection Error!", Toast.LENGTH_SHORT).show();
        }
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    private void getMultimediaList(){


        kProgressHUD.show();
        /*CurrentUser loggedInUser = DataManager.getCurrentUser(MultimediaActivity.this);
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
        Call<MultimediaList> call = userApiClient.getMultimediaList();*/

        Call<MultimediaList> call = MainActivity.apiClient.getMultimediaList();

        call.enqueue(new Callback<MultimediaList>() {
            @Override
            public void onResponse(Call<MultimediaList> call, Response<MultimediaList> response) {

                if(response.isSuccessful()) {

                    //Log.d("response", String.valueOf(response.body().getProductList()));
                    //JsonObject data = response.body().getProductList();

                    MultimediaList multimediaList = response.body();
                    List<MultimediaInfo> multimediaInfos = multimediaList.getMultimediaList();

                    //multimediaArrayList = new ArrayList();

                    for (MultimediaInfo multimediaInfo: multimediaInfos){

                        multimediaArrayList.add(multimediaInfo);

//                        MultimediaContainer info = new MultimediaContainer();
//                        info.contentType = multimediaInfo.getContent_type();
//                        info.title = multimediaInfo.getTitle();
//                        info.description = multimediaInfo.getDescription();
//                        info.path = multimediaInfo.getPath();
//                        info.createdDate = multimediaInfo.getCreated_at();

                        //DataManager.setProductItems(productArrayList, getContext());

                    }

//                    productArray = DataManager.getProductItems(getContext());
//
//
//                    for (int i = 0; i<productArray.size(); i++){
//                        ProductContainer item = (ProductContainer) productArray.get(i);
//                        productType.add(item.product_name);
//                    }

                    Log.d("response", String.valueOf(multimediaArrayList));

                    myGrid.invalidateViews();

                    kProgressHUD.dismiss();


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
            public void onFailure(Call<MultimediaList> call, Throwable t) {

                //hud.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }


}
