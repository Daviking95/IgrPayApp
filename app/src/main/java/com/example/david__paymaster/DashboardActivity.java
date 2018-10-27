package com.example.david__paymaster;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david__paymaster.igrpay.R;
import com.example.david__paymaster.utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class DashboardActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Toolbar toolbar;
    private View parent_view;
    private FirebaseAuth mAuth;
    public static final String TAG = "Dashboard Activity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView mName, welcomeAgent;
    private TextView mEmail;

    private String getName;
    private String getEmail;
    private Uri photoUrl;
    private ImageView mPic;
    NavigationView mNavigationView;

        @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() != null) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
//            startActivity(new Intent(getBaseContext(), DashboardActivity.class));

        }else {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        parent_view = findViewById(android.R.id.content);

        mAuth = FirebaseAuth.getInstance();
        initToolbar();
        initNavigationMenu();
        welcomeAgent = findViewById(R.id.welcome_agent);
//        String name = currentUser.getDisplayName();
//        String email = currentUser.getEmail();

        getDataFromServer();

        ((View) findViewById(R.id.register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), RegisterPayersActivity.class));
            }
        });
        ((View) findViewById(R.id.collect)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), CollectTaxActivity.class));
            }
        });
        ((View) findViewById(R.id.view_tax_payers)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ViewPayersActivity.class));
            }
        });
        ((View) findViewById(R.id.view_eod)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), EODActivity.class));
            }
        });
        ((View) findViewById(R.id.profile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), RegisterActivity.class));
            }
        });
        ((View) findViewById(R.id.settings)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), DashboardActivity.class));
            }
        });

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("IGRPAY Tax Collection App");
        Tools.setSystemBarColor(this);
    }

    private void initNavigationMenu() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mName = (TextView) nav_view.getHeaderView(0).findViewById(R.id.navUsername);
        mEmail = (TextView) nav_view.getHeaderView(0).findViewById(R.id.navEmail);
//        userName.setText(new UserPreference().getUser(DashboardActivity.this).userName);
        getCurrentinfo();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_1){
                    startActivity(new Intent(getBaseContext(), RegisterPayersActivity.class));
                }else if (id == R.id.nav_3){
                    startActivity(new Intent(getBaseContext(), CollectTaxActivity.class));
                }else if (id == R.id.nav_4){
                    startActivity(new Intent(getBaseContext(), ViewPayersActivity.class));
                }else if (id == R.id.nav_5){
                    startActivity(new Intent(getBaseContext(), EODActivity.class));
                }else if (id == R.id.nav_11){
                    startActivity(new Intent(getBaseContext(), RegisterPayersActivity.class));
                }else if (id == R.id.nav_12){
                    startActivity(new Intent(getBaseContext(), RegisterPayersActivity.class));
                }else if (id == R.id.nav_13){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                }
                drawer.closeDrawers();
                return true;
            }
        });

        // open drawer at start
//        drawer.openDrawer(GravityCompat.START);
    }

    private void getCurrentinfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                getName = profile.getDisplayName();
                getEmail = profile.getEmail();
//                photoUrl = profile.getPhotoUrl();
                mName.setText(getName);
                mEmail.setText(getEmail);

//                Picasso.with(getApplicationContext())
//                        .load(photoUrl.toString())
//                        .placeholder(R.drawable.logo)
//                        .resize(100, 100)
//                        .transform(new CircleTransform())
//                        .centerCrop()
//                        .into(mPic);


            };
        }
    }

    private void getDataFromServer(){
            final String androidId = Settings.Secure.getString(getContentResolver(),
            Settings.Secure.ANDROID_ID);

        CollectionReference docRef = db.collection("igrpayweb").document("add_agents").collection("agent_id");
            docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String compareId = document.getData().get("agent_id").toString();
                            if (androidId.equals(compareId)){
                                StringBuilder fields = new StringBuilder("");
                                fields.append("Welcome, Agent ").append(document.getData().get("agent_username"));
                                welcomeAgent.setText(fields.toString());
                                Log.d(TAG, document.getId() + " => " + document.getData());
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
