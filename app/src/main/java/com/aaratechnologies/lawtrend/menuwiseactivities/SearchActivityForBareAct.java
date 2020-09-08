package com.aaratechnologies.lawtrend.menuwiseactivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.managers.VolleySingleton;
import com.aaratechnologies.lawtrend.managers.WebURLS;
import com.aaratechnologies.lawtrend.models.ExampleAdapter;
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

public class SearchActivityForBareAct extends AppCompatActivity {
    private ExampleAdapter adapter;
    private List<ModelMenus> exampleList;
    MKLoader progress;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_bare);
        progress=findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        fillExampleList();
         recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(exampleList,getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
//        setUpRecyclerView();
}
    private void fillExampleList() {
        exampleList = new ArrayList<>();
        getData();

    }

    private void getData() {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, WebURLS.AllPDF_Single_APi , new Response.Listener<String>() {
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
                        exampleList.add(new ModelMenus(title,filename));
                    }
                    adapter=new ExampleAdapter(exampleList,getApplicationContext());
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),BareAndActsActivity.class);
        startActivity(intent);
        finish();
    }
}