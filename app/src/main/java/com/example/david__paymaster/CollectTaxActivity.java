package com.example.david__paymaster;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david__paymaster.igrpay.R;
import com.example.david__paymaster.model.People;
import com.example.david__paymaster.utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectTaxActivity extends AppCompatActivity {

    EditText payerName, phone, amount;
    Button showDetails, payment;
    TextView occupation, test;
    TextView amt, fnt, mnt, lnt, adt, emt, gnt, pnt, agt, cdt;
    private FirebaseAuth mAuth;
    public static final String TAG = "CollectTax Activity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Date currentTime;
    CollectionReference docRef = db.collection("igrpayweb").document("add_tax_payers").collection("tax_payers");
    List<People> peopleList = new ArrayList<>();
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_tax);
//        initToolbar();

        payerName = (EditText) findViewById(R.id.tax_payer_name);
        phone = (EditText) findViewById(R.id.phone_no);
        amount = (EditText) findViewById(R.id.amount);
        showDetails = (Button) findViewById(R.id.show_payer);
        payment = (Button) findViewById(R.id.make_payment);
        occupation = (TextView) findViewById(R.id.occupation);

        amt = (TextView) findViewById(R.id.amountText);
        fnt = (TextView) findViewById(R.id.fnText);
        mnt = (TextView) findViewById(R.id.mnText);
        lnt = (TextView) findViewById(R.id.lnText);
        adt = (TextView) findViewById(R.id.addressText);
        emt = (TextView) findViewById(R.id.emailText);
        gnt = (TextView) findViewById(R.id.genderText);
        pnt = (TextView) findViewById(R.id.phoneText);
        agt = (TextView) findViewById(R.id.agentText);
        cdt = (TextView) findViewById(R.id.dotText);

        final String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        c = Calendar.getInstance();
        final String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String payerText = payerName.getText().toString();
                final String phoneNo = phone.getText().toString();

                docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String compareName = document.getData().get("first_name").toString();
                                String compareNo = document.getData().get("phone").toString();
                                String compareID = document.getData().get("agent_id").toString();
//                                test.setText(androidId);
//                                if (compareID == androidId){

                                    if (payerText.equals(compareName) && phoneNo.equals(compareNo)){

                                        StringBuilder fields = new StringBuilder("");
                                        fields.append("Name : ").append(document.getData().get("first_name"));
                                        occupation.setText(fields.toString());
                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                        payment.setEnabled(true);
                                        amount.setEnabled(true);

                                    }
//                                else {
//                                    Toast.makeText(CollectTaxActivity.this, "No such account exists", Toast.LENGTH_LONG).show();
//                                }
//                                }else {
//                                    Toast.makeText(CollectTaxActivity.this, "No such account exists", Toast.LENGTH_LONG).show();
//                                }

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
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(true)
                        .build();
                db.setFirestoreSettings(settings);
                final String _phoneNo = phone.getText().toString();
                final String _payerText = payerName.getText().toString();

                if ((amount.getText().toString()).isEmpty()){
                    Snackbar.make(v, "Please enter an amount", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else {
                    docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            c = Calendar.getInstance();
                            String _currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
//                                    if (payerText.equals(document.getData().get("first_name").toString()) && phoneNo.equals(document.getData().get("phone").toString())){
//                                        amt.setText(amount.getText().toString());
                                        String _amtt = amount.getText().toString();
                                        String _fnt = document.getData().get("first_name").toString();
                                        String _mnt = document.getData().get("middle_name").toString();
                                        String _lnt = document.getData().get("last_name").toString();
                                        String _adt = document.getData().get("address").toString();
                                        String _emt = document.getData().get("email").toString();
                                        String _gnt = document.getData().get("gender").toString();
                                        String _phone = document.getData().get("phone").toString();
                                        String _agt = document.getData().get("agent_id").toString();
                                        cdt.setText(_currentDateString);
//                                    }
                                    if ((_phoneNo.equals(_phone)) && (_payerText.equals(_fnt))){
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("amount", _amtt);
                                        user.put("first_name", _fnt);
                                        user.put("middle_name", _mnt);
                                        user.put("last_name", _lnt);
                                        user.put("address", _adt);
                                        user.put("email", _emt);
                                        user.put("gender", _gnt);
                                        user.put("phone", _phone);
                                        user.put("agent_id", _agt);
                                        user.put("date_of_transaction", _currentDateString);

                                        // Add a new document with a generated ID
                                        db.collection("igrpayweb/collect_tax/payers_tax")
                                                .add(user)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(CollectTaxActivity.this, "Success", Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(getBaseContext(), EODActivity.class));
                                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding document", e);
                                                    }
                                                });
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }
            }
        });
    }

    private void addTax(){
        final String payerText = payerName.getText().toString();
        final String phoneNo = phone.getText().toString();

        if ((fnt.equals(payerText)) && (pnt.equals(phoneNo))){
            Map<String, Object> user = new HashMap<>();
            user.put("amount", amt);
            user.put("first_name", fnt);
            user.put("middle_name", mnt);
            user.put("last_name", lnt);
            user.put("address", adt);
            user.put("email", emt);
            user.put("gender", gnt);
            user.put("phone", pnt);
            user.put("agent_id", agt);
            user.put("date_of_transaction", cdt);

            // Add a new document with a generated ID
            db.collection("igrpayweb/collect_tax/payers_tax")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(CollectTaxActivity.this, "Success", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getBaseContext(), EODActivity.class));
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Collect Tax");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.pink_900);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

//    private void getDataFromServer(){
//        final String androidId = Settings.Secure.getString(getContentResolver(),
//                Settings.Secure.ANDROID_ID);
//
//        CollectionReference docRef = db.collection("igrpayweb").document("add_tax_payers").collection("tax_payers");
//        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot document : task.getResult()) {
//                        String compareId = document.getData().get("Agent_ID").toString();
//                        if (androidId.equals(compareId)){
//                            StringBuilder fields = new StringBuilder("");
//                            fields.append("Welcome, Agent ").append(document.getData().get("Agent_Username"));
//                            welcomeAgent.setText(fields.toString());
//                            Log.d(TAG, document.getId() + " => " + document.getData());
//                        }
//
//                    }
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//            }
//        });
//    }

}
