package com.aaratechnologies.lawtrend.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaratechnologies.lawtrend.BuildConfig;
import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.fragments.UserPanelFragment;
import com.aaratechnologies.lawtrend.managers.VolleySingleton;
import com.aaratechnologies.lawtrend.managers.WebURLS;
import com.aaratechnologies.lawtrend.menuwiseactivities.BookmarkActivity;
import com.aaratechnologies.lawtrend.menuwiseactivities.DatabaseManager;
import com.aaratechnologies.lawtrend.menuwiseactivities.MasterActivity;
import com.aaratechnologies.lawtrend.models.ModelData;
import com.aaratechnologies.lawtrend.models.ModelDataWithLabel;
import com.aaratechnologies.lawtrend.models.ModelShowSearchData;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class NotificationShowActivity extends AppCompatActivity {
    public  static final  String TAG="namesare";
    String url="";
    RecyclerView recyclerView;
    String abcd="";

    TextView title,content,time;
    ImageView image,book,share;
    LinearLayout ll;
    MKLoader progress;
    DatabaseManager databaseManager;
    LinearLayout containerView;
    String shareableLink="";
    int start=0,last=0;
    String key=""; String convTime=null;
    String Title="",Content="",ShareLink="",Date="",Url="";
    int newId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_show);
        databaseManager=new DatabaseManager(getApplicationContext());
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        ll=findViewById(R.id.ll);
        ll.setVisibility(View.GONE);
        image=findViewById(R.id.image);
        book=findViewById(R.id.book);
        share=findViewById(R.id.share);
        time=findViewById(R.id.time);
        progress=findViewById(R.id.progress);
        containerView=findViewById(R.id.containerView);
        containerView.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" Notification News");
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

        getData();
    }

    private void getData() {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, WebURLS.ALL_POSTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0;i<1;i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        Date=jsonObject.getString("date");//true
                        newId= Integer.parseInt(jsonObject.getString("id"));//true
                        final JSONObject jsonObject1=jsonObject.getJSONObject("title");
                        Title=jsonObject1.getString("rendered");
                        JSONObject jsonObject2=jsonObject.getJSONObject("content");
                        Content=jsonObject2.getString("rendered");
                        JSONObject jsonObject3=jsonObject.getJSONObject("better_featured_image");
                        //inner object in featured image
                        Url=jsonObject3.getString("source_url");
                        JSONObject jsonObject4=jsonObject.getJSONObject("excerpt");
                        String LowContent=jsonObject4.getString("rendered");
                        JSONArray jsonArray1=jsonObject.getJSONArray("categories");
//                        JSONObject jsonObject5=jsonArray1.getJSONObject(0);
//                        String labelname=jsonObject5.getString("category_name");

                        ShareLink=jsonObject.getString("link");
//
                        share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                                sendIntent.setType("text/plain");
                                sendIntent.putExtra(Intent.EXTRA_TEXT, ShareLink +"  \nDownload Law Trend for more update \n "+"https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                                startActivity(sendIntent);
                            }
                        });

                        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        // This line converts the given date into UTC time zone
                        try {
                            Date past=sdf.parse(Date);
                            Date now=new Date();
                            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
                            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
                            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
                            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
                            if (seconds == 0) {
                                time.setText("just now");
                            } else if (seconds < 60 && seconds > 0) {
                                time.setText(seconds + " seconds ago");
                            } else if (minutes < 60) {
                                time.setText(minutes + " minutes ago");
                            } else if (hours < 24) {
                                time.setText(hours + " hours ago");
                            } else if (days >= 7) {
                                if (days > 360) {
                                    convTime = (days / 360) + " years ago ";
                                    time.setText(convTime);
                                } else if (days > 30) {
                                    convTime = (days / 30) + " months ago ";
                                    time.setText(convTime);
                                } else {
                                    convTime = (days / 7) + " week ago";
                                    time.setText(convTime);
                                }
                            } else if (days < 7) {
                                convTime = days + " days ago";
                                time.setText(convTime);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        title.setText(Html.fromHtml(jsonObject1.getString("rendered")));
                        Glide.with(getApplicationContext()).load(Url).into(image);
                        content.setText(Html.fromHtml(jsonObject2.getString("rendered")));

                        book.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String DATABASE_NAME = "BookmarkedNews";
                                String TABLE_NAME = "Bookmark";
                                Cursor cursor;
                                SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME,android.content.Context.MODE_PRIVATE ,null);
                                String query="Select * From "+TABLE_NAME+" Where post_id="+newId;
                                cursor=db.rawQuery(query,null);
                                if (cursor.getCount()>0){
                                    Toast.makeText(getApplicationContext(), "Already Bookmarked", Toast.LENGTH_SHORT).show();
                                } else if(databaseManager.addData(String.valueOf(newId),Title,Content,Url,Date)){
                                    Toast.makeText(getApplicationContext(), "Bookmarked Successfully", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), "Please try later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        String src = jsonObject2.getString("rendered");

                        if (src.contains("<iframe src=\"")) {
                            start = src.indexOf("https://drive");
                            last = src.indexOf("preview");
                            Log.d("indexesare", "onBindViewHolder: "+start+", "+last);
                            last=last+7;
                            key = src.substring(start, last);

                            ll.setVisibility(View.VISIBLE);
                            ll.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), PdfViewActivity.class);
                                  intent.putExtra("pdf", key);
                                    startActivity(intent);
                                }
                            });

                        } else if (src.contains("https://lawtrend.in/wp-content/uploads/")){
                            start=src.indexOf("https://lawtrend.in/wp-content/uploads/");
                            Log.d("indexesare", "Index Start: "+start);
                            last=src.indexOf(".pdf");
                            Log.d("indexesare", "Index Last: "+last);

                            if ((start!=(-1))&&(last!=(-1))){
                                last=last+4;
                                key=src.substring(start,last);
//                                Log.d("indexesarea", "Index Last: "+key);
                                ll.setVisibility(View.VISIBLE);
                                ll.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(key));
                                        startActivity(browserIntent);
                                    }
                                });
                            }                    }
                        else {
                            ll.setVisibility(View.GONE);
                            key="";
                        }


//                        modelShowSearchData.add(new ModelShowSearchData(jsonObject1.getString("rendered"),jsonObject2.getString("rendered"),"",date,id,""));
                    }

                    progress.setVisibility(View.GONE);
                    containerView.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                searchAdapterS=new SearchAdapterS(getApplicationContext(),modelShowSearchData);
//                recyclerView.setAdapter(searchAdapterS);
//                searchAdapterS.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(NotificationShowActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
               // Toast.makeText(NotificationShowActivity.this, "", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                getData();
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}




//public class SearchAdapterS extends RecyclerView.Adapter<SearchAdapterS.ExViewHolder>{
//
//    Context context;
//    List<ModelShowSearchData> modelData;
//
//    public SearchAdapterS(Context context, List<ModelShowSearchData> modelData) {
//        this.context = context;
//        this.modelData = modelData;
//    }
//
//    @NonNull
//    @Override
//    public SearchAdapterS.ExViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view= LayoutInflater.from(context).inflate(R.layout.layout_for_noti,parent,false);
//        return new SearchAdapterS.ExViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SearchAdapterS.ExViewHolder holder, final int position) {
//        if (position==0){
//            holder.title.setText(Html.fromHtml(modelData.get(position).getTitle()));
//            holder.content.setText(Html.fromHtml(modelData.get(position).getContent()));
//            Glide.with(getApplicationContext()).load(modelData.get(position).getImageres()).into(holder.image);
//            holder.date.setText(modelData.get(position).getTime());
////           holder.label.setText(modelData.get(position).get());
////              holder.label.setVisibility(View.GONE);
////           Toast.makeText(context, ""+modelData.get(position).getId(), Toast.LENGTH_SHORT).show();
//            holder.book.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String DATABASE_NAME = "BookmarkedNews";
//                    String TABLE_NAME = "Bookmark";
//                    Cursor cursor;
//                    SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME,android.content.Context.MODE_PRIVATE ,null);
//                    String query="Select * From "+TABLE_NAME+" Where post_id="+modelData.get(position).getId();
//                    cursor=db.rawQuery(query,null);
//                    if (cursor.getCount()>0){
//                        Toast.makeText(context, "Already Bookmarked", Toast.LENGTH_SHORT).show();
//                    } else if(databaseManager.addData(modelData.get(position).getId(),modelData.get(position).getTitle(),modelData.get(position).getContent(),modelData.get(position).getImageres(),modelData.get(position).getTime())){
//                        Toast.makeText(context, "Bookmarked Successfully", Toast.LENGTH_SHORT).show();
//                    }else {
//                        Toast.makeText(context, "Please try later", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//            holder.share.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
//                    sendIntent.setType("text/plain");
//                    sendIntent.putExtra(Intent.EXTRA_TEXT,modelData.get(position).getId()+"  \nDownload Law Trend for more update \n "+"https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
//                    startActivity(sendIntent);
//                }
//            });
//        } else {
//            holder.container.setVisibility(View.GONE);
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return modelData.size();
//    }
//
//    public class ExViewHolder extends RecyclerView.ViewHolder{
//
//        TextView date,title,content,label;
//        ImageView image,book,share;
//        LinearLayout container;
//        public ExViewHolder(@NonNull View itemView) {
//            super(itemView);
//            date=itemView.findViewById(R.id.time);
//            title=itemView.findViewById(R.id.title);
//            content=itemView.findViewById(R.id.content);
////                label=itemView.findViewById(R.id.label);
//            image=itemView.findViewById(R.id.image);
//            book=itemView.findViewById(R.id.book);
//            share=itemView.findViewById(R.id.share);
//            container=itemView.findViewById(R.id.container);
//
//        }
//    }
//}