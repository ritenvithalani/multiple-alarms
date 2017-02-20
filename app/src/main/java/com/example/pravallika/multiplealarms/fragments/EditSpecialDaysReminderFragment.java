package com.example.pravallika.multiplealarms.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pravallika.multiplealarms.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditSpecialDaysReminderFragment extends Fragment {


    public EditSpecialDaysReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_spl_days_reminder_form, container, false);
        return rootView;
    }

}
