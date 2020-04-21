package com.metrocem.mis.SignInAndRegistration;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.R;
import com.metrocem.mis.Retrofit.ApiClient;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.DeviceRegistration;
import com.metrocem.mis.Utilities.Constants;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeviceRegistrationActivity extends AppCompatActivity {

    TextInputEditText name, identifier, type, os, version, deviceIdET;
    LinearLayout rLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_registration);

        getSupportActionBar().hide();


        rLayout = findViewById(R.id.rLayout);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.up_to_down_transition);
        rLayout.setAnimation(animation);

        ImageView crossBtn = findViewById(R.id.crossBtn);
        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String imei_no = DataManager.getUniqueIMEIId(this);

        name = findViewById(R.id.nameET);
        name.setText(Build.MODEL);
        identifier = findViewById(R.id.identifierET);
        identifier.setText(imei_no);
        type = findViewById(R.id.typeET);
        type.setText(Build.PRODUCT);
        os = findViewById(R.id.osET);
        os.setText(Build.MANUFACTURER);
        version = findViewById(R.id.versionET);
        version.setText(Build.VERSION.RELEASE);
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //String deviceId = DataManager.getToken(this);
        String uniqueID = UUID.randomUUID().toString();

        deviceIdET = findViewById(R.id.deviceIdET);
        //deviceIdET.setText(android.os.Build.SERIAL);
        deviceIdET.setText(deviceId);



        TextView registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().length() <= 0){
                    DataManager.onlyAlertShow("Input Device Name", DeviceRegistrationActivity.this);
                    //Toast.makeText(SignUpActivity.this,"Input your Name", Toast.LENGTH_SHORT).show();
                }
                else if (identifier.getText().length() <= 0){
                    DataManager.onlyAlertShow("Input Device Identifier", DeviceRegistrationActivity.this);
                    //Toast.makeText(SignUpActivity.this,"Input your Company Name", Toast.LENGTH_SHORT).show();
                }

                else if (type.getText().length() <= 0){
                    DataManager.onlyAlertShow("Type field is empty", DeviceRegistrationActivity.this);
                    //Toast.makeText(SignUpActivity.this,"Email field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (os.getText().length() <= 0){
                    DataManager.onlyAlertShow("OS field is empty", DeviceRegistrationActivity.this);
                    //Toast.makeText(SignUpActivity.this,"Password field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (version.getText().length() <= 0){
                    DataManager.onlyAlertShow("Input your Device Version", DeviceRegistrationActivity.this);
                    //Toast.makeText(SignUpActivity.this,"Input your Mobile Number", Toast.LENGTH_SHORT).show();
                }

                else {
                    if (isNetworkAvailable()){

                        registerDevice();

                    }else {
                        Toast.makeText(DeviceRegistrationActivity.this,"Connection Error!", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }


        return false;
    }

    private void registerDevice(){

        final KProgressHUD hud = KProgressHUD.create(DeviceRegistrationActivity.this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30)
                .show();


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS);


        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create());

        //Retrofit retrofit = builder.build();
        Retrofit retrofit = builder.client(httpClient.build()).build();
        ApiClient userApiClient = retrofit.create(ApiClient.class);
        Call<DeviceRegistration> call = userApiClient.registerDevice(name.getText().toString(),deviceIdET.getText().toString(), identifier.getText().toString(),type.getText().toString(),os.getText().toString(),version.getText().toString());


        call.enqueue(new Callback<DeviceRegistration>() {
            @Override
            public void onResponse(Call<DeviceRegistration> call, Response<DeviceRegistration> response) {

                Log.d("response", String.valueOf(response.code()));

                if(response.isSuccessful()) {

                    hud.dismiss();
                    alertShow("Congratulation","Registration Successful.");

                    //onBackPressed();

                }else {

                    try {
                        hud.dismiss();
                        alertShow("Error!","This Device is already Registered");


                    } catch (Exception e) {
                        Toast.makeText(DeviceRegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        //Log.d("Message4", "post submitted to API." + e.getMessage().toString());
                        hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeviceRegistration> call, Throwable t) {

                Log.d("error", t.getLocalizedMessage());
                hud.dismiss();
                DataManager.alertShow("Error!","Device Registered. \nYou don't need to register again. Thank you.",DeviceRegistrationActivity.this);
            }
        });
    }

    public void alertShow(String title, String msgString){

        AlertDialog.Builder builderInner = new AlertDialog.Builder(DeviceRegistrationActivity.this);
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
