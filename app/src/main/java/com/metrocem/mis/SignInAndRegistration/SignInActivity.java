package com.metrocem.mis.SignIn;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.metrocem.mis.Firebase.FirebaseTokenResponse;
import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.R;
import com.metrocem.mis.Subclasses.CurrentUser;
import com.metrocem.mis.Subclasses.DataManager;
import com.metrocem.mis.Subclasses.DeviceRegistration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    EditText signInEmail, signInPassword;
    CurrentUser loggedInUser;
    String imei_no, deviceId;
    TextView identifierET;
    KProgressHUD hud;
    //String API_BASE_URL = "http://mis.nurtech.xyz/api/v1/";
    String API_BASE_URL = "http://misstage.nurtech.xyz/api/v1/";

    @SuppressLint({"MissingPermission", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide();

        signInEmail = findViewById(R.id.loginEmailET);
        signInPassword = findViewById(R.id.logInPasswordET);

        final ImageView logoImage = findViewById(R.id.logoImage);

        TextView registerBtn = findViewById(R.id.deviceRegistrationTV);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, DeviceRegistrationActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SignInActivity.this,logoImage, ViewCompat.getTransitionName(logoImage));
                SignInActivity.this.startActivity(intent, options.toBundle());
            }
        });




        identifierET = findViewById(R.id.identifierET);
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        hud = KProgressHUD.create(SignInActivity.this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(30);


        CheckBox signInCheckbox = findViewById(R.id.signInCheckbox);

        signInCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
                SharedPreferences sharedPreferences = getSharedPreferences("SIGN_IN", Context.MODE_PRIVATE);

                if(cb.isChecked()){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("KEEP_SIGN_IN", true);
                    editor.apply();
                }else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("KEEP_SIGN_IN", true);
                    editor.apply();
                }
            }
        });

        TextView signInBtn = findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (signInEmail.getText().length() <= 0){
                    DataManager.alertShow("Error!","Email field is empty", SignInActivity.this);
                }
                else if (signInPassword.getText().length() <= 0){
                    DataManager.alertShow("Error!","Input your password", SignInActivity.this);
                }else {
                    if (isNetworkAvailable()){
                        String imei_no = DataManager.getUniqueIMEIId(getApplicationContext());
                        //identifierET.getText().length()
                        if (imei_no.length() != 0){
                            Log.d("response phone", String.valueOf(identifierET.getText()));

                            //checkDeviceExistence(identifierET.getText().toString());
                            checkDeviceExistence(imei_no);


                        }else {
                            DataManager.onlyAlertShow("App need to Permission Phone State for get IMEI. Please turn on Phone State for this app to login.",SignInActivity.this);
                        }

                        //signIn();

                    }else {
                        Toast.makeText(SignInActivity.this,"Connection Error!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            moveTaskToBack(true);
        }

        return true;

        // your other related codes
    }


    @Override
    public void onResume() {
        //will be executed onResume
        super.onResume();

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                imei_no = DataManager.getUniqueIMEIId(this);
                identifierET.setText(imei_no);

            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            imei_no = DataManager.getUniqueIMEIId(this);
            Log.v("TAG","Permission is granted");
        }

    }
    public void checkDeviceExistence(final String imeiNo){


        hud.show();

        //ApiClient service = RetrofitInstance.getRetrofitInstance().create(ApiClient.class);

        /*Call the method with parameter in the interface to get the employee data*/
        //Call<DeviceRegistration> call = service.checkDeviceExistence("1211");

        //Gson gson = new GsonBuilder()
        //        .setLenient()
        //        .create();

        //String API_BASE_URL = "http://mis.nurtech.xyz/api/v1/";


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS);
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());

        //Retrofit retrofit = builder.build();
        Retrofit retrofit = builder.client(httpClient.build()).build();
        ApiClient userApiClient = retrofit.create(ApiClient.class);
        Call<DeviceRegistration> call = userApiClient.checkDeviceExistence(imei_no);
        /*Log the URL called*/
        Log.wtf("response url", call.request().url() + "");

        call.enqueue(new Callback<DeviceRegistration>() {
            @Override
            public void onResponse(Call<DeviceRegistration> call, Response<DeviceRegistration> response) {
                //generateEmployeeList(response.body().getEmployeeArrayList());
                if(response.isSuccessful()) {

                    if (response.body().getData() != null){
                        signIn(imeiNo);
                    }else {
                        hud.dismiss();
                        DataManager.onlyAlertShow("Your Device is not Registerd!",SignInActivity.this);
                    }

                }else {

                    try {
                        hud.dismiss();

                    } catch (Exception e) {
                        Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        hud.dismiss();
                    }
                }

            }

            @Override
            public void onFailure(Call<DeviceRegistration> call, Throwable t) {

                hud.dismiss();

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


    public void signIn(String imeiNo){

        sendNetworkRequest(imeiNo,deviceId,signInEmail.getText().toString(),signInPassword.getText().toString());

    }

    private void sendNetworkRequest(String identifier,String deviceIdentifier, String email, String password){

        hud.show();

        //String API_BASE_URL = "http://mis.nurtech.xyz/api/v1/";

        //String API_BASE_URL = "http://misstage.nurtech.xyz/api/v1/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60,TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());

        //Retrofit retrofit = builder.build();
        Retrofit retrofit = builder.client(httpClient.build()).build();
        ApiClient userApiClient = retrofit.create(ApiClient.class);
        Log.d("response data",identifier+" "+deviceIdentifier+" "+email+" "+password);
        Call<LoginResponse> call = userApiClient.userLogin(identifier,deviceIdentifier,email,password);


        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(response.isSuccessful()) {

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("dashboard", true);
                    editor.commit();


                    loggedInUser = new CurrentUser();
                    loggedInUser.email = response.body().getData().getEmail();
                    loggedInUser.phone = response.body().getData().getPhone();
                    loggedInUser.photo = response.body().getData().getUser().getPhoto();
                    loggedInUser.role = response.body().getData().getUserType();
                    loggedInUser.accessToken = response.body().getToken().getAccessToken();
                    loggedInUser.userId = response.body().getData().getUser().getId();
                    loggedInUser.userName = response.body().getData().getUser().getName();
                    loggedInUser.creditLimit = response.body().getData().getUser().getCreditLimit();
                    DataManager.setCurrentUser(loggedInUser, SignInActivity.this);
                    hud.dismiss();

                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (task.isSuccessful()) {
                                        String firebaseToken = task.getResult().getToken();
                                        Log.d("response success = ", firebaseToken);
                                        Log.d("response success = ", loggedInUser.accessToken);

                                        updateFirebaseTokenToServer(firebaseToken, loggedInUser.accessToken);
                                    } else {
                                        Log.w("Response Failed", "getInstanceId failed", task.getException());
                                    }
                                }
                            });



                    //onBackPressed();

                }else {

                    try {
                        hud.dismiss();

                    } catch (Exception e) {
                        Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                hud.dismiss();
                Log.d("response",t.getLocalizedMessage());
                DataManager.alertShow("Error!","The username or password you have entered is not correct.", SignInActivity.this);

            }
        });
    }

    public void updateFirebaseTokenToServer(String firebaseToken, final String accessToken){

        //Log.d("access", accessToken);
        //Log.d("access token", firebaseToken);

        //String API_BASE_URL = "http://mis.nurtech.xyz/api/v1/";


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer "+ accessToken)
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
        Call<FirebaseTokenResponse> call = userApiClient.updateFirebaseToken(firebaseToken);


        call.enqueue(new Callback<FirebaseTokenResponse>() {
            @Override
            public void onResponse(Call<FirebaseTokenResponse> call, Response<FirebaseTokenResponse> response) {

                Log.d("response", String.valueOf(response.code()));

                if(response.isSuccessful()) {

                    FirebaseTokenResponse token = response.body();

                    if (token.getStatus().toLowerCase().equals("success")){
                        hud.dismiss();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        SignInActivity.this.startActivity(intent);
                        finish();
                    }

                }else {

                    try {
                        hud.dismiss();
                        Log.d("response3", String.valueOf(response.errorBody()));
                        DataManager.alertShow("Error!", "Response Error code "+response.code(),SignInActivity.this);

                    } catch (Exception e) {
                        Log.d("response4", "post submitted to API." + e.getMessage().toString());
                        hud.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<FirebaseTokenResponse> call, Throwable t) {

                hud.dismiss();
                Log.d("response5", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
