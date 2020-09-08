package com.aaratechnologies.lawtrend.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.aaratechnologies.lawtrend.menuwiseactivities.DatabaseManager;
import com.aaratechnologies.lawtrend.menuwiseactivities.SearchNewsActivity;
import com.aaratechnologies.lawtrend.models.ModelShowSearchData;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SeachedDataShowActivity extends AppCompatActivity {

    public  static final  String TAG="namesare";
    String url="";
    List<ModelShowSearchData> modelShowSearchData;
    RecyclerView recyclerView;
    String abcd="";

    DatabaseManager databaseManager;
    String shareableLink="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seached_data_show);
        databaseManager=new DatabaseManager(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" Complete News");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        modelShowSearchData=new ArrayList<>();

        Intent intent=getIntent();
        url=intent.getStringExtra("url");


        StringRequest stringRequest=new StringRequest(Request.Method.GET, WebURLS.Show_ParticularNews+url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String id=jsonObject.getString("id");
                    String date=jsonObject.getString("date");
//                    JSONObject jsonObject1=jsonObject.getJSONObject("guid");
                  shareableLink=jsonObject.getString("link");
                    Log.d(TAG, "onResponse: "+shareableLink);
                    JSONObject jsonObject2=jsonObject.getJSONObject("title");
                    String Title=jsonObject2.getString("rendered");
                    JSONObject jsonObject3=jsonObject.getJSONObject("content");
                    String Content=jsonObject3.getString("rendered");
                    JSONArray jsonArray=jsonObject.getJSONArray("categories");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject4=jsonArray.getJSONObject(i);
                     abcd=jsonObject4.getString("category_name");
                    }
                    JSONObject jsonObject5=jsonObject.getJSONObject("better_featured_image");
                    String imageUrl=jsonObject5.getString("source_url");
                    modelShowSearchData.add(new ModelShowSearchData(Title,Content,imageUrl,date,shareableLink,abcd));
                    SearchAdapterS searchAdapterS=new SearchAdapterS(getApplicationContext(),modelShowSearchData);
                    recyclerView.setAdapter(searchAdapterS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(SeachedDataShowActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

   public class SearchAdapterS extends RecyclerView.Adapter<SearchAdapterS.ExViewHolder>{

        Context context;
        List<ModelShowSearchData> modelData;

       public SearchAdapterS(Context context, List<ModelShowSearchData> modelData) {
           this.context = context;
           this.modelData = modelData;
       }

       @NonNull
       @Override
       public ExViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view= LayoutInflater.from(context).inflate(R.layout.custom_layout_for_search_show,parent,false);
           return new ExViewHolder(view);

       }

       @Override
       public void onBindViewHolder(@NonNull ExViewHolder holder, final int position) {
           holder.title.setText(Html.fromHtml(modelData.get(position).getTitle()));
           holder.content.setText(Html.fromHtml(modelData.get(position).getContent()));
           Glide.with(getApplicationContext()).load(modelData.get(position).getImageres()).into(holder.image);
           holder.date.setText(modelData.get(position).getTime());
//           holder.label.setText(modelData.get(position).get());
           holder.label.setVisibility(View.GONE);
//           Toast.makeText(context, ""+modelData.get(position).getId(), Toast.LENGTH_SHORT).show();
           holder.book.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String DATABASE_NAME = "BookmarkedNews";
                   String TABLE_NAME = "Bookmark";
                   Cursor cursor;
                   SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME,android.content.Context.MODE_PRIVATE ,null);
                   try {
                       String query="Select * From "+TABLE_NAME+" Where post_id="+modelData.get(position).getId();
                       cursor=db.rawQuery(query,null);
                       if (cursor.getCount()>0){
                           Toast.makeText(context, "Already Bookmarked", Toast.LENGTH_SHORT).show();
                       } else if(databaseManager.addData(modelData.get(position).getId(),modelData.get(position).getTitle(),modelData.get(position).getContent(),modelData.get(position).getImageres(),modelData.get(position).getTime())){
                           Toast.makeText(context, "Bookmarked Successfully", Toast.LENGTH_SHORT).show();
                       }else {
                           Toast.makeText(context, "Please try later", Toast.LENGTH_SHORT).show();
                       }
                   } catch (Exception e) {
//                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                       e.printStackTrace();
                       if(databaseManager.addData(modelData.get(position).getId(),modelData.get(position).getTitle(),modelData.get(position).getContent(),modelData.get(position).getImageres(),modelData.get(position).getTime())){
                           Toast.makeText(context, "Bookmarked Successfully", Toast.LENGTH_SHORT).show();
                       }
                   }
               }
           });
           holder.share.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent sendIntent = new Intent(Intent.ACTION_SEND);
                   sendIntent.setType("text/plain");
                   sendIntent.putExtra(Intent.EXTRA_TEXT,modelData.get(position).getId()+"  \nDownload Law Trend for more update \n "+"https://play.google.com/store/apps/details?id=" + context.getPackageName());
                   startActivity(sendIntent);
               }
           });

       }

       @Override
       public int getItemCount() {
           return modelData.size();
       }

       public class ExViewHolder extends RecyclerView.ViewHolder{

           TextView date,title,content,label;
           ImageView image,book,share;
           public ExViewHolder(@NonNull View itemView) {
                super(itemView);
                date=itemView.findViewById(R.id.date);
                title=itemView.findViewById(R.id.title);
                content=itemView.findViewById(R.id.content);
                label=itemView.findViewById(R.id.label);
                image=itemView.findViewById(R.id.image);
               book=itemView.findViewById(R.id.book);
               share=itemView.findViewById(R.id.share);

            }
        }
   }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), SearchNewsActivity.class);
        startActivity(intent);
        finish();
    }
}