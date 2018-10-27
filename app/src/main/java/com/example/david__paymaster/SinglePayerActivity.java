package com.example.david__paymaster;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david__paymaster.adapters.ViewPayersList;
import com.example.david__paymaster.igrpay.R;
import com.example.david__paymaster.model.People;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SinglePayerActivity extends AppCompatActivity {

    TextView firstName, email;
    private FirebaseFirestore firestoreDB;
    private String TAG = "SinglePayerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_payer);
        firestoreDB = FirebaseFirestore.getInstance();

        initToolbar();

        firstName = findViewById(R.id.fullname);
        email = findViewById(R.id.email);

        loadSinglePayer();
    }
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_basic, menu);
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
    private void loadSinglePayer() {
        final String TempHolder = getIntent().getStringExtra("Name");
        firstName.setText(TempHolder);
        final String firstNameString = firstName.toString();
        firestoreDB.collection("igrpayweb/add_tax_payers/tax_payers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<People> peopleList = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {
                                String compareFirstName = doc.getData().get("first_name").toString();
                                if (TempHolder.equals(compareFirstName)){
//                                    People note = doc.toObject(People.class);
                                    email.setText(doc.getData().get("email").toString());
//                                    note.set
//                                    note.setOccupation(doc.getData().get("occupation").toString());
//                                    peopleList.add(note);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
