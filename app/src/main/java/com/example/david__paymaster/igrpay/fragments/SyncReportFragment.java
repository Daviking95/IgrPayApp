package com.example.david__paymaster.igrpay.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.david__paymaster.igrpay.R;
import com.example.david__paymaster.igrpay.constants.NavigationDrawerConstants;

/**
 * Created by tobiw on 01-Oct-18.
 */

public class SyncReportFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(NavigationDrawerConstants.TAG_SYNC_REPORT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sync_report, container, false);
    }

}