package com.aaratechnologies.lawtrend.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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


public class CentralBareActsNewFragment extends Fragment {

    RecyclerView recyclerView;
    List<ModelMenus> modelMenus;
    MKLoader progress;
    AdapterBareActRules adapterStateWisePdf;

    public CentralBareActsNewFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_central_bare_acts_new, container, false);

        progress=view.findViewById(R.id.progress);
        recyclerView=view.findViewById(R.id.recyc);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        modelMenus=new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, WebURLS.StateWiseCategory+"3", new Response.Listener<String>() {
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
                        modelMenus.add(new ModelMenus(title,filename));
                    }
                    AdapterBareActRules adapterBareActRules=new AdapterBareActRules(getActivity(),modelMenus);
                    recyclerView.setAdapter(adapterBareActRules);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

        return view;
    }
    public class AdapterBareActRules extends RecyclerView.Adapter<AdapterBareActRules.ExViewHolder> {
        Context context;
        List<ModelMenus> modelMenusList;

        public AdapterBareActRules(Context context, List<ModelMenus> modelMenus) {
            this.context = context;
            this.modelMenusList = modelMenus;
        }

        @NonNull
        @Override
        public AdapterBareActRules.ExViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(context).inflate(R.layout.custom_statewise_pdf,parent,false);
            return new AdapterBareActRules.ExViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterBareActRules.ExViewHolder holder, final int position) {

            Log.d("menusare", "onBindViewHolder: "+modelMenusList.get(position).getMenu());
            holder.textView.setText(modelMenusList.get(position).getMenu());
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

        }

        @Override
        public int getItemCount() {
            return modelMenusList.size();
        }

//        public void filterList(List<ModelMenus> arrayList) {
//            this.modelMenusList=arrayList;
//            adapterStateWisePdf.notifyDataSetChanged();
//
//        }

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