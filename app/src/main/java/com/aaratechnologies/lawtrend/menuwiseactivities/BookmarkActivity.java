package com.aaratechnologies.lawtrend.menuwiseactivities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.activities.CompleteNewsBookmarked;
import com.aaratechnologies.lawtrend.activities.MainActivity;
import com.aaratechnologies.lawtrend.models.ModelData;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class BookmarkActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ModelData> modelData;
    DatabaseManager databaseManager;
    LinearLayout second_cover;
    Button btn_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" Bookmarks");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        databaseManager=new DatabaseManager(getApplicationContext());
        recyclerView = findViewById(R.id.lv);
        second_cover = findViewById(R.id.second_cover);
        btn_continue = findViewById(R.id.btn_continue);
        second_cover.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        modelData = new ArrayList<>();
       loadDataFromDatabase();
       if (modelData.size()<1){
//           Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show();
           recyclerView.setVisibility(View.GONE);
           second_cover.setVisibility(View.VISIBLE);
       }else {
           second_cover.setVisibility(View.GONE);
           recyclerView.setVisibility(View.VISIBLE);
       }

       btn_continue.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(getApplicationContext(),MainActivity.class);
               startActivity(intent);
               finish();
           }
       });
    }

    private void loadDataFromDatabase() {
            //we are here using the DatabaseManager instance to get all employees
            Cursor cursor = databaseManager.getAllData();

            if (cursor.moveToFirst()) {
                do {
                    modelData.add(new ModelData(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            ""
                    ));
                } while (cursor.moveToNext());
        AdapterBookmark adapter = new AdapterBookmark(this,modelData);
            recyclerView.setAdapter(adapter);
        }
    }
    private class AdapterBookmark extends RecyclerView.Adapter<AdapterBookmark.ExViewHolder> {

        Context context;
        List<ModelData> modelDatas;
        int start=0,last=0;
        String key=""; String convTime=null;


        public AdapterBookmark(Context context, List<ModelData> modelDatas) {
            this.context = context;
            this.modelDatas = modelDatas;
        }

        @NonNull
        @Override
        public ExViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(context).inflate(R.layout.custom_bookmark_layout,parent,false);
            return new ExViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExViewHolder holder, final int position) {



            holder.title.setText(Html.fromHtml(modelData.get(position).getImageres()));
            holder.content.setText(Html.fromHtml(modelData.get(position).getTime()));
            holder.time.setText(modelData.get(position).getShortContent());

            Glide.with(getApplicationContext()).load(modelData.get(position).getId()).into(holder.image);
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            // This line converts the given date into UTC time zone
            try {
                Date past=sdf.parse(modelData.get(position).getShortContent());
                Date now=new Date();
                long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
                long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
                long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
                long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
                if (seconds == 0) {
                    holder.time.setText("just now");
                } else if (seconds < 60 && seconds > 0) {
                    holder.time.setText(seconds + " seconds ago");
                } else if (minutes < 60) {
                    holder.time.setText(minutes + " minutes ago");
                } else if (hours < 24) {
                    holder.time.setText(hours + " hours ago");
                } else if (days >= 7) {
                    if (days > 360) {
                        convTime = (days / 360) + " years ago ";
                        holder.time.setText(convTime);
                    } else if (days > 30) {
                        convTime = (days / 30) + " months ago ";
                        holder.time.setText(convTime);
                    } else {
                        convTime = (days / 7) + " week ago";
                        holder.time.setText(convTime);
                    }
                } else if (days < 7) {
                    convTime = days + " days ago";
                    holder.time.setText(convTime);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String src = modelData.get(position).getTime();
            if (src.contains("<iframe src=\"")) {
                final int start = src.indexOf("https://drive");
                int last = src.indexOf("preview");
                Log.d("indexesare", "onBindViewHolder: "+start+", "+last);
                last=last+7;
                key = src.substring(start, last);
                Log.d("indexesarea", "onBindViewHolder: "+key);
            } else if (src.contains("https://lawtrend.in/wp-content/uploads/")){
                start=src.indexOf("https://lawtrend.in/wp-content/uploads/");
                Log.d("indexesare", "Index Start: "+start);
                last=src.indexOf(".pdf");
                Log.d("indexesare", "Index Last: "+last);
                if ((start!=(-1))&&(last!=(-1))){
                    last=last+4;
                    key=src.substring(start,last);
                    Log.d("indexesarea", "Index Last: "+key);
                }
            }
            else {
                key="";
            }
//                else {
//                    viewHolderTwo.ll.setVisibility(View.GONE);
//                }

            holder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//            getTime:-- content
//            getShortContent :-date
//            getTitle:-SQL Serial id
                    //getContent:- getPOSTId=
                    //getId:- Image Res
                    //getSharLink: ""
                    //getImageres  :Title
                    Log.d("datatosend", "onClick:  Id"+modelData.get(position).getTitle()+", POST Id"+modelData.get(position).getContent());

                    Intent intent=new Intent(getApplicationContext(), CompleteNewsBookmarked.class);
                    intent.putExtra("image",modelData.get(position).getId());
                    intent.putExtra("title",modelData.get(position).getImageres());
                    intent.putExtra("content",modelData.get(position).getTime());
                    intent.putExtra("time",modelData.get(position).getShortContent());
                    intent.putExtra("id",modelData.get(position).getTitle());
                    intent.putExtra("posId",modelData.get(position).getContent());

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return modelDatas.size();
        }

        public class ExViewHolder extends RecyclerView.ViewHolder{
            ImageView image;
            TextView time,title,content;
            CardView cover;
            LinearLayout ll; ImageView book,share;
            public ExViewHolder(@NonNull View itemView) {
                super(itemView);
                title=itemView.findViewById(R.id.title);
                content=itemView.findViewById(R.id.webView);
                time=itemView.findViewById(R.id.time);
                image=itemView.findViewById(R.id.image);
                cover=itemView.findViewById(R.id.cover);
                ll=itemView.findViewById(R.id.ll);
                book=itemView.findViewById(R.id.book);
                share=itemView.findViewById(R.id.share);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}