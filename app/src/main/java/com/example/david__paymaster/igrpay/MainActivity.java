package com.example.david__paymaster.igrpay;

<<<<<<< HEAD
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
=======
//import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
>>>>>>> Update UI and save to Firebase
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
<<<<<<< HEAD
import android.widget.ImageView;
import android.widget.Toast;

import com.example.david__paymaster.igrpay.constants.NavigationDrawerConstants;
import com.example.david__paymaster.igrpay.fragments.GalleryFragment;
import com.example.david__paymaster.igrpay.fragments.HomeFragment;
=======
import android.widget.Toast;

import com.example.david__paymaster.igrpay.fragments.AddTaxPayersFragment;
import com.example.david__paymaster.igrpay.fragments.DueListFragment;
import com.example.david__paymaster.igrpay.fragments.SettingsFragment;
import com.example.david__paymaster.igrpay.fragments.ViewTaxPayersFragment;
import com.example.david__paymaster.igrpay.fragments.HomeFragment;
import com.example.david__paymaster.igrpay.fragments.PayTaxFragment;
import com.example.david__paymaster.igrpay.fragments.SyncReportFragment;
import com.example.david__paymaster.igrpay.fragments.VideosFragment;
>>>>>>> Update UI and save to Firebase
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, SplashLogin.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

<<<<<<< HEAD
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

=======
>>>>>>> Update UI and save to Firebase
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        // Loading profile image
//        ImageView profileImage = navHeader.findViewById(R.id.profileImage);
//        Glide.with(this).load(NavigationDrawerConstants.PROFILE_URL)
//                .apply(RequestOptions.circleCropTransform())
//                .thumbnail(0.5f)
//                .into(profileImage);
//        //Loading backgrounf image
//        ImageView navBackground = navHeader.findViewById(R.id.img_header_bg);
//        Glide.with(this).load(NavigationDrawerConstants.BACKGROUND_URL)
//                .thumbnail(0.5f)
//                .into(navBackground);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
<<<<<<< HEAD
=======

        Fragment homeFragment = new HomeFragment();
        showFragment(homeFragment);
>>>>>>> Update UI and save to Firebase
    }

    private void logoutAgent(){
        Toast.makeText(MainActivity.this, "You have logged out", Toast.LENGTH_SHORT).show();
        firebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), SplashLogin.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
<<<<<<< HEAD
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
=======
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                //that means your stack is empty and you want to close activity
                finish();
            } else {
                // pop the backstack here
                getSupportFragmentManager().popBackStackImmediate();
            }
>>>>>>> Update UI and save to Firebase
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
<<<<<<< HEAD
=======

            Fragment fragment = new SettingsFragment();
            showFragment(fragment);
            
>>>>>>> Update UI and save to Firebase
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
<<<<<<< HEAD
        Fragment fragment = null;

        if (id == R.id.add_tax_payer) {
            fragment = new HomeFragment();

//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//// Replace whatever is in the fragment_container view with this fragment,
//// and add the transaction to the back stack
//            transaction.replace(R.id.fragment_container, newFragment);
//            transaction.addToBackStack(null);
//
//// Commit the transaction
//            transaction.commit();
        } else if (id == R.id.view_payers) {
            fragment = new GalleryFragment();
        } else if (id == R.id.pay_tax) {

        } else if (id == R.id.sync_report) {

        } else if (id == R.id.nav_share) {
=======
        Fragment fragment;

        if (id == R.id.home) {
            fragment = new HomeFragment();
            showFragment(fragment);

        } else if (id == R.id.add_tax_payer) {
            fragment = new AddTaxPayersFragment();
            showFragment(fragment);

        } else if (id == R.id.view_payers) {
            fragment = new ViewTaxPayersFragment();
            showFragment(fragment);

        } else if (id == R.id.collect_payment) {
            fragment = new PayTaxFragment();
            showFragment(fragment);

        } else if (id == R.id.due_list) {
            fragment = new DueListFragment();
            showFragment(fragment);

//        } else if (id == R.id.pay_tax) {
//            fragment = new PayTaxFragment();
//            showFragment(fragment);

        } else if (id == R.id.sync_report) {
            fragment = new SyncReportFragment();
            showFragment(fragment);

        } else if (id == R.id.end_of_day) {
            fragment = new SyncReportFragment();
            showFragment(fragment);

        } else if (id == R.id.profile) {
            fragment = new SettingsFragment();
            showFragment(fragment);
>>>>>>> Update UI and save to Firebase

        } else if (id == R.id.logout) {
//            Toast.makeText(MainActivity.this, "Hey", Toast.LENGTH_SHORT).show();
            logoutAgent();
        }

<<<<<<< HEAD
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();
        }

=======
>>>>>>> Update UI and save to Firebase
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

<<<<<<< HEAD
=======
    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
        .commit();
    }

>>>>>>> Update UI and save to Firebase
//    public void setFragment(Fragment fragment){
//        if(fragment!=null){
//            FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_main,fragment);
//            ft.commit();
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
////        return true;
//    }


}
