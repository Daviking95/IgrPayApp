package com.example.david__paymaster.igrpay.fragments;

<<<<<<< HEAD
import android.os.Bundle;
import android.support.v4.app.Fragment;
=======
import android.support.v4.app.Fragment;
import android.os.Bundle;
>>>>>>> Update UI and save to Firebase
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.david__paymaster.igrpay.R;
import com.example.david__paymaster.igrpay.constants.NavigationDrawerConstants;


public class SettingsFragment extends Fragment {

<<<<<<< HEAD


=======
>>>>>>> Update UI and save to Firebase
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(NavigationDrawerConstants.TAG_SETTINGS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

}
