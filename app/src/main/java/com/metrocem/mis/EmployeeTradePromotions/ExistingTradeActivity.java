package com.metrocem.mis.EmployeeTradePromotions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.R;
import com.metrocem.mis.Model.CurrentUser;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.Promotion;
import com.metrocem.mis.Model.PromotionList;
import com.metrocem.mis.Subclasses.CheckNetworkConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExistingTradeActivity extends AppCompatActivity {

    GridView offerGrid;
    ArrayList promotionArrayList;
    KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_trade);

        CurrentUser currentUser = DataManager.getCurrentUser(this);

        getSupportActionBar().setTitle("Existing Trade Promotions"); // for set actionbar title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.toolbar_gradient));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30)
                .show();

        promotionArrayList = new ArrayList();

        offerGrid = findViewById(R.id.gridView);
        offerGrid.setAdapter(new OfferGridAdapter(this, promotionArrayList));
        offerGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int     position, long id) {

            }
        });

        if(CheckNetworkConnection.isNetworkAvailable(this)){
            getPromotionList();
        }else {
            Toast.makeText(this,"Connection Error!", Toast.LENGTH_SHORT).show();
        }


//        ListView tradePromotionListView = findViewById(R.id.tradePromotionListView);
//
//        TradePromotionAdapter adapter = new TradePromotionAdapter(this);
//        tradePromotionListView.setAdapter(adapter);
//        tradePromotionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("position = ", String.valueOf(position));
//
//                //Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
//                //getActivity().startActivity(intent);
//
//            }
//        });
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

                    //ArrayList promotionArrayList = new ArrayList();

                    for (Promotion promotion: promotions){

                        Log.d("response", promotion.getType());

                        if (promotion.getType().equals("trade_promotions")){
                            promotionArrayList.add(promotion);

                        }

                    }

                    offerGrid.invalidateViews();

                    kProgressHUD.dismiss();


                    //hud.dismiss();


                }else {

                    try {
                        kProgressHUD.dismiss();
                        Log.d("response3", "post submitted to API.");

                    } catch (Exception e) {
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        //hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<PromotionList> call, Throwable t) {

                kProgressHUD.dismiss();
                Log.d("Exception call", t.getLocalizedMessage());
            }
        });
    }

}
