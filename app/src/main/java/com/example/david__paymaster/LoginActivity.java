package com.example.david__paymaster;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david__paymaster.igrpay.R;
import com.example.david__paymaster.utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private View parent_view;
    TextInputEditText email, password;
    private FirebaseAuth mAuth;
    ProgressBar progressBar ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String TAG = "Login Activity";
    LinearLayout sign;
    CollectionReference docRef = db.collection("igrpayweb").document("add_agents").collection("agent_id");

//        @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        if (mAuth.getCurrentUser() != null) {
////            FirebaseUser currentUser = mAuth.getCurrentUser();
//            startActivity(new Intent(getBaseContext(), DashboardActivity.class));
//        }else {
//            startActivity(new Intent(getBaseContext(), LoginActivity.class));
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        parent_view = findViewById(android.R.id.content);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        sign = findViewById(R.id.signLay);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);


        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);

        ((View) findViewById(R.id.forgot_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "Forgot Password", Snackbar.LENGTH_SHORT).show();
            }
        });
        ((View) findViewById(R.id.sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), RegisterActivity.class));
            }
        });
        ((View) findViewById(R.id.login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        final String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        // Check if user is signed in (non-null) and update UI accordingly.

        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String compareId = document.getData().get("agent_id").toString();
                        if (androidId.equals(compareId)){
                            sign.setVisibility(View.GONE);
//                            startActivity(new Intent(getBaseContext(), LoginActivity.class));
                            return;
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getBaseContext(), DashboardActivity.class));
//        }else {
//            startActivity(new Intent(getBaseContext(), LoginActivity.class));
//        }
//
//    }

    private void loginUser(){
        final String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        final String emailInput = email.getText().toString().trim();
        final String passwordInput = password.getText().toString().trim();

        if (emailInput.isEmpty() || passwordInput.isEmpty()){
            Toast.makeText(LoginActivity.this, "Input fields must not be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }else {
            progressBar.setVisibility(View.VISIBLE);

//            signOperations(emailInput, passwordInput);

            docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String compareId = document.getData().get("agent_id").toString();
                            String compareEmail = document.getData().get("agent_email").toString();
                            if ((androidId.equals(compareId)) && (emailInput.equals(compareEmail))){
                                signOperations(emailInput, passwordInput);
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });


        }

    }

    private void signOperations(String emailInp, String passwordInp){
        mAuth.signInWithEmailAndPassword(emailInp, passwordInp)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign In Sucessful", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login Successful.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(), DashboardActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sign In Failed", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(), LoginActivity.class));
                        }

                        // ...
                    }
                });
    }

}
