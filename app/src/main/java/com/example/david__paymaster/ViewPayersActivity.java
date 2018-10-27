package com.example.david__paymaster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david__paymaster.adapters.ViewPayersList;
import com.example.david__paymaster.data.DataGenerator;
import com.example.david__paymaster.igrpay.R;
import com.example.david__paymaster.model.People;
import com.example.david__paymaster.utils.Tools;
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ViewPayersActivity extends AppCompatActivity {

    private View parent_view;

    private RecyclerView recyclerView;
    private ViewPayersList mAdapter;
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private FirebaseFirestore firestoreDB;
    private String TAG = "ViewPayersActivity";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private Context ctx;
//    FirestoreRecyclerAdapter<People, OriginalViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        parent_view = findViewById(android.R.id.content);
        firestoreDB = FirebaseFirestore.getInstance();

        initToolbar();
        loadPayersList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), RegisterPayersActivity.class));
            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Your Tax Payers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
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

    private void initComponent() {
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
//
//        Query query = rootRef.collection("igrpayweb/add_tax_payers/tax_payers")
//                .orderBy("first_name", Query.Direction.ASCENDING);
//
//        List<People> items = DataGenerator.getPeopleData(this);
//        items.addAll(DataGenerator.getPeopleData(this));
//        items.addAll(DataGenerator.getPeopleData(this));
//
//        //set data and list adapter
//        mAdapter = new ViewPayersList(this, items);
//        recyclerView.setAdapter(mAdapter);
//
//        // on item list clicked
//        mAdapter.setOnItemClickListener(new ViewPayersList.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, People obj, int position) {
//                Snackbar.make(parent_view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });

    }

    private void loadPayersList() {
       final String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
       final String agentIdText = androidId.toString();
        firestoreDB.collection("igrpayweb/add_tax_payers/tax_payers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<People> peopleList = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {
                                String compareId = doc.getData().get("agent_id").toString();
                                String firstNameText = doc.getData().get("first_name").toString();
                                final StorageReference pathReference = storageRef.child("payersImages/" + agentIdText + "/" + firstNameText + "/");
                                if (androidId.equals(compareId)){
                                    People note = doc.toObject(People.class);
//                                    note.image = doc.getData().get("photo_url").toString();
//                                    note.imageDrw = getResources().getDrawable(note.image);
//                                    peopleList.addAll(doc.toObject(People.class));
//                                    note.imageDrw = ctx.getResources().getDrawable(doc.getData().get("first_name").toString());
//
//
                                    note.setImageUrl(doc.getData().get("photo_url").toString());
                                    note.setName(doc.getData().get("first_name").toString());
                                    note.setOccupation(doc.getData().get("occupation").toString());
                                    peopleList.add(note);
                                }

                            }

                            mAdapter = new ViewPayersList(getApplicationContext(), peopleList, firestoreDB);
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
