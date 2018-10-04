package com.example.david__paymaster.igrpay.fragments;

<<<<<<< HEAD
import android.os.Bundle;
import android.support.v4.app.Fragment;
=======
import android.support.v4.app.Fragment;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
>>>>>>> Update UI and save to Firebase
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.david__paymaster.igrpay.R;
import com.example.david__paymaster.igrpay.constants.NavigationDrawerConstants;
<<<<<<< HEAD
import com.example.david__paymaster.igrpay.constants.*;


public class HomeFragment extends Fragment {


=======


public class HomeFragment extends Fragment {
    private CardView addTaxPayerCardView;
    private CardView viewTaxPayersCardView, collectPaymentCardView, dueListCardView, syncReportCardView, endOfDayCardView;
>>>>>>> Update UI and save to Firebase

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(NavigationDrawerConstants.TAG_HOME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
<<<<<<< HEAD
        return inflater.inflate(R.layout.fragment_home, container, false);
=======
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        setUpViews(view);
        setUpListeners();

        return view;
    }

    private void setUpViews(View view){
        addTaxPayerCardView = (CardView) view.findViewById(R.id.addTaxPayerCardView);
        viewTaxPayersCardView = (CardView) view.findViewById(R.id.viewTaxPayersView);
        collectPaymentCardView = (CardView) view.findViewById(R.id.collectPaymentsView);
        syncReportCardView = (CardView) view.findViewById(R.id.syncReportView);
        endOfDayCardView = (CardView) view.findViewById(R.id.endOfDayView);

    }

    private void setUpListeners(){
        addTaxPayerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddTaxPayersFragment();
                showFragment(fragment);
            }
        });

        viewTaxPayersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ViewTaxPayersFragment();
                showFragment(fragment);
            }
        });

        collectPaymentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PayTaxFragment();
                showFragment(fragment);
            }
        });

        syncReportCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SyncReportFragment();
                showFragment(fragment);
            }
        });

        endOfDayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EndOfDayFragment();
                showFragment(fragment);
            }
        });
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
>>>>>>> Update UI and save to Firebase
    }

}