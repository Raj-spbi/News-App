package com.aaratechnologies.lawtrend.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.managers.VolleySingleton;
import com.aaratechnologies.lawtrend.managers.WebURLS;
import com.aaratechnologies.lawtrend.menuwiseactivities.StateWisePdf;
import com.aaratechnologies.lawtrend.models.ModelMenus;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.skydoves.elasticviews.ElasticCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AllStatesFragment extends Fragment {


    ListView listView;
    List<ModelMenus> modelMenus;
    public AllStatesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_states, container, false);

        listView=view.findViewById(R.id.listview);
        modelMenus=new ArrayList<>();

        LoadStates();
        return view;
    }

    private void LoadStates() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebURLS.All_States, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                    Log.d("allstates", "All menus: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("records");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String cat_id = jsonObject1.getString("id");

                        String statename = jsonObject1.getString("title");
                        modelMenus.add(new ModelMenus(statename,cat_id));
                    }
                AdapterMenus adapterMenus=new AdapterMenus(getContext(),modelMenus);
                    listView.setAdapter(adapterMenus);
//
                } catch (JSONException e) {
                    Log.d("exce", "onResponse: "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }
    private class AdapterMenus extends BaseAdapter {

        Context context;
        List<ModelMenus> modelMenus;

        public AdapterMenus(Context context, List<ModelMenus> modelMenus) {
            this.context = context;
            this.modelMenus = modelMenus;
        }

        @Override
        public int getCount() {
            return modelMenus.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view=LayoutInflater.from(context).inflate(R.layout.custom_menus,viewGroup,false);
            ElasticCardView allmenus=view.findViewById(R.id.allmenus);
            TextView menu=view.findViewById(R.id.menu);
            Log.d("menusname", "getView: "+modelMenus.get(i).getMenu());
         if (modelMenus.get(i).getMenu().equalsIgnoreCase("Central Bare Acts")){
             menu.setVisibility(View.GONE);
         } else {
             menu.setText(modelMenus.get(i).getMenu());
             allmenus.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Intent intent = new Intent(getActivity(), StateWisePdf.class);
                     intent.putExtra("state", modelMenus.get(i).getMenu());
                     intent.putExtra("cat_id", modelMenus.get(i).getObject_id());
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                     startActivity(intent);
//                    Toast.makeText(getContext(), ""+modelMenus.get(i).getMenu(), Toast.LENGTH_SHORT).show();
                 }
             });
         }
            return view;
        }
    }


}