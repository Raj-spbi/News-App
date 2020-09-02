package com.aaratechnologies.lawtrend.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.activities.MainActivity;
import com.aaratechnologies.lawtrend.activities.SeachedDataShowActivity;
import com.aaratechnologies.lawtrend.managers.VolleySingleton;
import com.aaratechnologies.lawtrend.managers.WebURLS;
import com.aaratechnologies.lawtrend.models.ModelMenus;
import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {


    EditText key;
    ImageView search,delete,back_img;
    RecyclerView recyclerView;
    MKLoader mkLoader;
    List<ModelMenus> modelMenusList;
    SearchAdapter searchAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);
key=view.findViewById(R.id.key);
back_img=view.findViewById(R.id.back_img);
search=view.findViewById(R.id.search);
delete=view.findViewById(R.id.delete);
mkLoader=view.findViewById(R.id.progress);
        recyclerView=view.findViewById(R.id.recyclerView);
        delete.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setVisibility(View.GONE);
        modelMenusList=new ArrayList<>();
        mkLoader.setVisibility(View.GONE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (key.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Can't search", Toast.LENGTH_SHORT).show();
                }else{

                    search.setVisibility(View.GONE);
                    delete.setVisibility(View.VISIBLE);
                    LoadData(key.getText().toString());
                    recyclerView.setVisibility(View.VISIBLE);
                    search.setVisibility(View.GONE);
                    delete.setVisibility(View.VISIBLE);
                }
            }
        });
        key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                modelMenusList.clear();
                recyclerView.setVisibility(View.GONE);
                mkLoader.setVisibility(View.VISIBLE);
                LoadData(key.getText().toString().trim());
//                searchAdapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                mkLoader.setVisibility(View.VISIBLE);
                search.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (key.getText().toString().isEmpty()){
                    modelMenusList.clear();
                    recyclerView.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelMenusList.clear();
                delete.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                mkLoader.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                key.setText("");
            }
        });
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        return view;
    }

    private void LoadData(String key) {
        mkLoader.setVisibility(View.VISIBLE);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, WebURLS.Search_DATA + key, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    mkLoader.setVisibility(View.GONE);
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        JSONObject jsonObject5=jsonObject.getJSONObject("title");
                        String title=jsonObject5.getString("rendered");
                      JSONObject jsonObject1=jsonObject.getJSONObject("_links");
                      JSONArray jsonArray1=jsonObject1.getJSONArray("self");
                      JSONObject jsonObject2=jsonArray1.getJSONObject(0);
                      String apip_url=jsonObject2.getString("href");
                        Log.d("searched", "onResponse: "+title+", "+apip_url);
                        modelMenusList.add(new ModelMenus(title,apip_url));
                    }
                     searchAdapter=new SearchAdapter(getActivity(),modelMenusList);
                    recyclerView.setAdapter(searchAdapter);
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mkLoader.setVisibility(View.GONE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getContext(),"Time Out"+error.networkResponse.statusCode,
//                            Toast.LENGTH_LONG).show();
                    final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setMessage("Network Error");
                    builder.setTitle("Alert !");
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                        getData(page);
                        }
                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                } else if (error instanceof AuthFailureError) {

                    error.printStackTrace();
//                    Toast.makeText(getContext(), "Login Again"+error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError ||error instanceof ClientError) {

                    error.printStackTrace();
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Weak Network", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    error.printStackTrace();
                }
            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
    public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ExViewHolder>{
        Context context;
        List<ModelMenus> modelMenus;

        public SearchAdapter(Context context, List<ModelMenus> modelMenus) {
            this.context = context;
            this.modelMenus = modelMenus;
        }

        @NonNull
        @Override
        public ExViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(context).inflate(R.layout.custom_statewise_pdf,parent,false);

            return new ExViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExViewHolder holder, final int position) {
            holder.menu.setText(Html.fromHtml(modelMenus.get(position).getMenu()));
            String urlLink=modelMenus.get(position).getObject_id();
            holder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("aaaa", "onClick: "+modelMenus.get(position).getObject_id());
                    Intent intent=new Intent(getActivity(), SeachedDataShowActivity.class);
                    intent.putExtra("url",modelMenus.get(position).getObject_id());
                    context.startActivity(intent);
//                    Toast.makeText(context, ""+modelMenus.get(position).getObject_id(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return modelMenus.size();
        }

        public class ExViewHolder extends RecyclerView.ViewHolder{

            TextView menu;
            LinearLayout cover;
            public ExViewHolder(@NonNull View itemView) {
                super(itemView);
                menu=itemView.findViewById(R.id.menu);
                cover=itemView.findViewById(R.id.cover);

            }
        }
    }
}