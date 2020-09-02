package com.aaratechnologies.lawtrend.menuwiseactivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.managers.VolleySingleton;
import com.aaratechnologies.lawtrend.managers.WebURLS;
import com.aaratechnologies.lawtrend.models.ModelMenus;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StateWisePdf extends AppCompatActivity {
    RecyclerView recyc;
    List<ModelMenus> modelData;
    String obj_id="";
    MKLoader progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_wise_pdf);progress=findViewById(R.id.progress);
        final Intent intent=getIntent();
        String Title=intent.getStringExtra("state");
        progress=findViewById(R.id.progress);
        obj_id=intent.getStringExtra("cat_id");
//        Log.d("stats", "onCreate: "+Title+", "+obj_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), BareAndActsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        recyc=findViewById(R.id.recyc);
        recyc.setHasFixedSize(true);
        recyc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        modelData=new ArrayList<>();
        getData();

    }

    private void getData() {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, WebURLS.StateWiseCategory+obj_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progress.setVisibility(View.GONE);
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("records");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String title=jsonObject1.getString("title");
                        String filename=jsonObject1.getString("file_name");
                        modelData.add(new ModelMenus(title,filename));
                    }
                    AdapterStateWisePdf adapterStateWisePdf=new AdapterStateWisePdf(getApplicationContext(),modelData);
                    recyc.setAdapter(adapterStateWisePdf);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
                error.printStackTrace();
                final AlertDialog.Builder builder=new AlertDialog.Builder(StateWisePdf.this);
                builder.setMessage("Network Error");
                builder.setTitle("Alert !");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getData();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();

            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private class AdapterStateWisePdf extends RecyclerView.Adapter<AdapterStateWisePdf.ExViewHolder>{
        Context context;
        List<ModelMenus> modelMenus;

        public AdapterStateWisePdf(Context context, List<ModelMenus> modelMenus) {
            this.context = context;
            this.modelMenus = modelMenus;
        }

        @NonNull
        @Override
        public ExViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(context).inflate(R.layout.custom_statewise_pdf,parent,false);
         return new ExViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExViewHolder holder, final int position) {

//            if (modelData.get(position).getMenu().equalsIgnoreCase("Central Bare Acts")){
//                holder.cover.setVisibility(View.GONE);
//            }else {
                holder.cover.setVisibility(View.VISIBLE);
                holder.textView.setText(modelMenus.get(position).getMenu());
                holder.cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    if (modelMenus.get(position).getObject_id().isEmpty()){
                        Toast.makeText(context, "Sorry Link is not Available", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lawtrend.in/" + modelMenus.get(position).getObject_id()));
                        startActivity(browserIntent);
                    }
                    }
                });
//            }
        }

        @Override
        public int getItemCount() {
            return modelMenus.size();
        }

        public class ExViewHolder extends RecyclerView.ViewHolder{

            TextView textView;
            LinearLayout cover;
            public ExViewHolder(@NonNull View itemView) {
                super(itemView);
                textView=itemView.findViewById(R.id.menu);
                cover=itemView.findViewById(R.id.cover);
            }
        }
    }
}