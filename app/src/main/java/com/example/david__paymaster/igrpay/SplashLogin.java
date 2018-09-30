package com.example.david__paymaster.igrpay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SplashLogin extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout rellay2, rellay3;
    private EditText userName, passWord;
    private Button loginButton, forgotButton;
    private ProgressDialog progressLoading;

    private FirebaseAuth firebaseAuth;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay2.setVisibility(View.VISIBLE);
            rellay3.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

//        Initialize Firebase Auth Object
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
//            profile activity here
            finish();
            Toast.makeText(SplashLogin.this, "You are registered successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

//        ADD ProgressDialog
        progressLoading = new ProgressDialog(this);

        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        rellay3 = (RelativeLayout) findViewById(R.id.rellay3);

        handler.postDelayed(runnable, 2000);

//        CONNECTING WIDGETS TO VIEWS
        userName = (EditText) findViewById(R.id.user);
        passWord = (EditText) findViewById(R.id.password);

        loginButton = (Button) findViewById(R.id.login_button);
        forgotButton = (Button) findViewById(R.id.forgotButton);

//        SET EVENTLISTENER FOR LOGIN BUTTON
        loginButton.setOnClickListener(this);
        forgotButton.setOnClickListener(this);
    }


//    REGISTER METHOD
    private void registerAgent(){
        String username = userName.getText().toString().trim();
        String password = passWord.getText().toString().trim();

        //        VALIDATION ERROR
        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();

            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();

            return;
        }

        String generateEmail = username + "@igrpay.com";

        //          VALIDATION SUCCESS
        progressLoading.setMessage("Registering....");
        progressLoading.show();

//        Create User
        firebaseAuth.createUserWithEmailAndPassword(generateEmail,password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SplashLogin.this, "You are registered successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SplashLogin.this, "Could not register. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        firebaseAuth.signInWithCredential()
    }


//    LOGIN FUNCTION
    private void loginAgent(){
        String username = userName.getText().toString().trim();
        String password = passWord.getText().toString().trim();

        //        VALIDATION ERROR
        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please enter correct username", Toast.LENGTH_SHORT).show();

            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter correct password", Toast.LENGTH_SHORT).show();

            return;
        }

        final String generateEmail = username + "@gmail.com";

        //          VALIDATION SUCCESS
        progressLoading.setMessage("Logging in....");
        progressLoading.show();

        firebaseAuth.signInWithEmailAndPassword(generateEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressLoading.dismiss();
                        if (task.isSuccessful()){
                            finish();
                            Toast.makeText(SplashLogin.this, "You are logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            Toast.makeText(SplashLogin.this, "Could not log in. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            registerAgent();
        }
        if (v == forgotButton){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
