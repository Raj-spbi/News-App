package com.aaratechnologies.lawtrend.menuwiseactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.activities.ApplyForInternShipBottomSheet;
import com.aaratechnologies.lawtrend.activities.MainActivity;

public class OnlineInternshipActivity extends AppCompatActivity {

    Button applybtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_internship);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Online Internship");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        applybtn=findViewById(R.id.applybtn);
        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplyForInternShipBottomSheet applyForInternShipBottomSheet=new ApplyForInternShipBottomSheet();
                applyForInternShipBottomSheet.show(getSupportFragmentManager(),"Online Internship");
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}