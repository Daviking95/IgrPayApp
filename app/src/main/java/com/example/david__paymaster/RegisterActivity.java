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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david__paymaster.igrpay.R;
import com.example.david__paymaster.model.User;
import com.example.david__paymaster.utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String SUCCESS_REGISTERED = "Agent Has Succesfully Registered";
    public static final String AGENT_USERNAME = "agent_username";
    public static final String AGENT_EMAIL = "agent_email";
    public static final String AGENT_ID = "agent_id";
    private View parent_view;
    TextInputEditText email, password, username;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView show;
    ProgressBar progressBar ;
    public static final String TAG = "Register Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        parent_view = findViewById(android.R.id.content);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        show = findViewById(R.id.show);
        String androidId = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                show.setText(androidId);

        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);

        ((View) findViewById(R.id.sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        ((View) findViewById(R.id.login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        if (mAuth.getCurrentUser() != null) {
//            FirebaseUser currentUser = mAuth.getCurrentUser();
//            startActivity(new Intent(getBaseContext(), DashboardActivity.class));
//        }else {
//            startActivity(new Intent(getBaseContext(), LoginActivity.class));
//        }
//    }

    private void registerUser(){
        final String emailInput = email.getText().toString().trim();
        final String passwordInput = password.getText().toString().trim();
        final String usernameInput = username.getText().toString().trim();
        final String androidId = show.getText().toString().trim();
//        final String androidId = Settings.Secure.getString(getContentResolver(),
//                Settings.Secure.ANDROID_ID);

        if (emailInput.isEmpty() || passwordInput.isEmpty() || usernameInput.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Input fields must not be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }else if(passwordInput.length() <= 6) {
            Toast.makeText(RegisterActivity.this, "Password length must be more than 6",
                    Toast.LENGTH_SHORT).show();
        }else {

//            allowReg();
            CollectionReference docRef = db.collection("igrpayweb").document("add_agents").collection("agent_id");
            docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String compareId = document.getData().get("agent_id").toString();
                            String compareEmail = document.getData().get("agent_email").toString();
                            if (androidId.equals(compareId)){
                                Toast.makeText(RegisterActivity.this, "This device has already been registered. Please Login",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                            }else{
                               allowReg();
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

    private void allowReg(){
        final String emailInput = email.getText().toString().trim();
        final String passwordInput = password.getText().toString().trim();
        final String usernameInput = username.getText().toString().trim();
        final String androidId = show.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Success", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            Map<String, Object> userD = new HashMap<>();
                            userD.put(AGENT_USERNAME, usernameInput);
                            userD.put(AGENT_ID, androidId);
                            userD.put(AGENT_EMAIL, emailInput);

                            // Add a new document with a generated ID
                            db.collection("igrpayweb/add_agents/agent_id")
                                    .add(userD)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
//                                                Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
//                                                startActivity(new Intent(getBaseContext(), ViewPayersActivity.class));
                                            Log.d("ID Saved", "DocumentSnapshot added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Error adding ID", "Error adding document", e);
                                        }
                                    });

                            Toast.makeText(RegisterActivity.this, "Registered Successfully. Please Login",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(), LoginActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Failed", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
