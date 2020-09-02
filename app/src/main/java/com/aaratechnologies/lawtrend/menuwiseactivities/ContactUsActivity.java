package com.aaratechnologies.lawtrend.menuwiseactivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.activities.MainActivity;
import com.aaratechnologies.lawtrend.managers.VolleySingleton;
import com.aaratechnologies.lawtrend.managers.WebURLS;
import com.aaratechnologies.lawtrend.models.ModelData;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactUsActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE =99 ;

    RelativeLayout callsend,whatsappsend;
    LinearLayout emailsend;

    EditText name,email,subject,message;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        name = findViewById(R.id.name);
        callsend = findViewById(R.id.callsend);
        whatsappsend = findViewById(R.id.whatsappsend);
        emailsend = findViewById(R.id.emailsend);
        email = findViewById(R.id.email);
        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()) {
                    name.setError("Empty");
                    name.requestFocus();
                    return;
                }
                if (email.getText().toString().isEmpty()) {
                    email.setError("Empty");
                    email.requestFocus();
                    return;
                }

                if (subject.getText().toString().isEmpty()) {
                    subject.setError("Empty");
                    subject.requestFocus();
                    return;
                }
                if (message.getText().toString().isEmpty()) {
                    message.setError("Empty");
                    message.requestFocus();
                    return;
                }
                sendDataToDatabase();
            }
        });

        emailsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to send Email
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+"contact@lawtrend.in"));
                startActivity(intent);

            }
        });
        whatsappsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to send Whatsapp
                String url = "https://api.whatsapp.com/send?phone="+"9839773999";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        callsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to send Call
//                Intent  Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:"+"7007231238"));//change the number
//                startActivity(callIntent);

                String number = ("tel:" + "7007231238");
                Intent mIntent = new Intent(Intent.ACTION_CALL);
                mIntent.setData(Uri.parse(number));
// Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ContactUsActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(mIntent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendDataToDatabase() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebURLS.Contact_US_Form, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("contactres", "onResponse: "+response);
//                Toast.makeText(ContactUsActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message1=jsonObject.getString("message");
                    Toast.makeText(ContactUsActivity.this, ""+message1, Toast.LENGTH_SHORT).show();
                    name.setText("");
                    email.setText("");
                    subject.setText("");
                    message.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ContactUsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("your-name",name.getText().toString().trim());
                map.put("your-email",email.getText().toString().trim());
                map.put("your-subject",subject.getText().toString().trim());
                map.put("your-message",message.getText().toString().trim());
                return map;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}