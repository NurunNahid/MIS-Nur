package com.metrocem.mismetrocem.SignIn;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.metrocem.mismetrocem.R;
import com.metrocem.mismetrocem.Subclasses.CurrentUser;
import com.metrocem.mismetrocem.Subclasses.DataManager;

public class SignInActivity extends AppCompatActivity {

    EditText signInEmail, signInPassword;
    CurrentUser loggedInUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide();

        signInEmail = findViewById(R.id.loginEmailET);
        signInPassword = findViewById(R.id.logInPasswordET);

        loggedInUser = new CurrentUser();


        TextView signInBtn = findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (signInEmail.getText().length() <= 0){
                    DataManager.alertShow("Error!","Email field is empty", SignInActivity.this);
                    //Toast.makeText(SignInActivity.this,"Email field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (signInPassword.getText().length() <= 0){
                    DataManager.alertShow("Error!","Input your password", SignInActivity.this);
                    //Toast.makeText(SignInActivity.this,"Input your password", Toast.LENGTH_SHORT).show();
                }else {
                    
                    signIn();
                }

            }
        });

    }

    public void signIn(){

        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFlag", true);
        editor.commit();

        loggedInUser.userName = signInEmail.getText().toString();
        if (loggedInUser.userName.equals("Admin") || loggedInUser.userName.equals("admin")){
            loggedInUser.role = "Dealer";
        }else {
            loggedInUser.role = "Employee";
        }

        DataManager.setCurrentUser(loggedInUser, SignInActivity.this);

        onBackPressed();


    }
}
