package com.example.david__paymaster;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.david__paymaster.adapters.AdapterEOD;
import com.example.david__paymaster.adapters.ViewPayersList;
import com.example.david__paymaster.data.DataGenerator;
import com.example.david__paymaster.igrpay.R;
import com.example.david__paymaster.model.EODModel;
import com.example.david__paymaster.model.People;
import com.example.david__paymaster.utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EODActivity extends AppCompatActivity {

    private View parent_view;
    private RecyclerView recyclerView;
    private AdapterEOD mAdapter;
    Calendar c = Calendar.getInstance();
    public static final String TAG = "EOD Activity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    CollectionReference docRef = db.collection("igrpayweb").document("collect_tax").collection("payers_tax");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eod);
        parent_view = findViewById(android.R.id.content);

        initToolbar();
//        initComponent();
        loadEOD();

        final String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    private void initToolbar() {
        String _currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("End Of Day Data for " + _currentDateString);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private void initComponent() {
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
//
//        List<EODModel> items = DataGenerator.getEODData(this);
//        items.addAll(DataGenerator.getEODData(this));
//        items.addAll(DataGenerator.getEODData(this));
//
//        //set data and list adapter
//        mAdapter = new AdapterEOD(this, items);
//        recyclerView.setAdapter(mAdapter);
//
//        // on item list clicked
//        mAdapter.setOnItemClickListener(new AdapterEOD.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, EODModel obj, int position) {
//                Snackbar.make(parent_view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
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

    private void loadEOD() {
        final String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        final String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        db.collection("igrpayweb/collect_tax/payers_tax")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<EODModel> peopleList = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {
                                String compareId = doc.getData().get("agent_id").toString();
                                String _date_of_transaction = doc.getData().get("date_of_transaction").toString();
                                if (androidId.equals(compareId)){
                                    if (currentDateString.equals(_date_of_transaction)){
                                        EODModel note = doc.toObject(EODModel.class);
                                        note.setName(doc.getData().get("first_name").toString());
                                        note.setAmount(doc.getData().get("amount").toString());
                                        peopleList.add(note);
                                    }

                                }

                            }

                            mAdapter = new AdapterEOD(getApplicationContext(), peopleList, db);
                            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
