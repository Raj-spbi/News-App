package com.aaratechnologies.lawtrend.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.activities.ApplyForInternShipBottomSheet;
import com.aaratechnologies.lawtrend.models.ModelData;

import java.util.Calendar;
import java.util.List;


public class OnlineInternshipFragment extends Fragment {



    Button applybtn;
    public OnlineInternshipFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_online_internship, container, false);

        applybtn=view.findViewById(R.id.applybtn);
        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplyForInternShipBottomSheet applyForInternShipBottomSheet=new ApplyForInternShipBottomSheet();
                applyForInternShipBottomSheet.show(getFragmentManager(),applyForInternShipBottomSheet.getTag());
            }
        });
        return view;

    }
}