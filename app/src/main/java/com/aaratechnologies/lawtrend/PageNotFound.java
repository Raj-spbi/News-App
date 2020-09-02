package com.aaratechnologies.lawtrend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PageNotFound extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_not_found);
        btn=findViewById(R.id.reload);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), HostScreen.class);
                startActivity(i);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}