package com.aaratechnologies.lawtrend.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aaratechnologies.lawtrend.BuildConfig;
import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.menuwiseactivities.DatabaseManager;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TotalNewsActivity extends AppCompatActivity {

    ImageView image,bookmark,share;
    TextView title,content,time;
    int start=0,last=0;
    LinearLayout ll;
    String convTime="",Timenew="",key="";
    String shareLink="";
    String past;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" Complete News");
        databaseManager=new DatabaseManager(getApplicationContext());
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

        image=findViewById(R.id.image);
        bookmark=findViewById(R.id.bookmark);
        share=findViewById(R.id.share);
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        time=findViewById(R.id.time);
        ll=findViewById(R.id.ll);
        Intent intent=getIntent();
        final String Title=intent.getStringExtra("title");
        final String Content=intent.getStringExtra("content");
        Timenew=intent.getStringExtra("time");
        final String Id=intent.getStringExtra("id");
        final String Image=intent.getStringExtra("image");
        shareLink=intent.getStringExtra("share");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            final Date dateObj;
            try {
                dateObj = sdf.parse(Timenew);
                SimpleDateFormat formated = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss a");
                past=formated.format(dateObj);
//                System.out.println(past);
//                Toast.makeText(this, ""+past, Toast.LENGTH_SHORT).show();

            } catch (ParseException e) {
                e.printStackTrace();
            }
// PDF=intent.getStringExtra("pdf");

        if (Content.contains("<iframe src=\"")) {
            start = Content.indexOf("https://drive");
            last = Content.indexOf("preview");
            Log.d("abcdef", "onBindViewHolder: " + start + ", " + last);
            last = last + 7;
            key = Content.substring(start, last);
            Log.d("datata", "getView: " + key);
        }
        else if (Content.contains("https://lawtrend.in/wp-content/uploads/")){
            start=Content.indexOf("https://lawtrend.in/wp-content/uploads/");
// Log.d("indexesare", "Index Start: "+start);
            last=Content.indexOf(".pdf");
// Log.d("indexesare", "Index Last: "+last);

            if ((start!=(-1))&&(last!=(-1))){
                last=last+4;
                key=Content.substring(start,last);
// Log.d("indexesarea", "Index Last: "+key);
            }
        } else {
            key="";
        }
// Log.d("totalsrc", "Index Key: "+Title+" , "+key);

//        dateTimeinNewFormat();
        time.setText(past);

// Toast.makeText(this, ""+PDF, Toast.LENGTH_SHORT).show();
        title.setText(Html.fromHtml(Title));
        content.setText(Html.fromHtml(Content));
        ll.setVisibility(View.GONE);
// time.setText(Timenew);
        Glide.with(getApplicationContext()).load(Image).into(image);

        if (key.equals("")){
            ll.setVisibility(View.GONE);
        } else {
            ll.setVisibility(View.VISIBLE);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (key.contains("https://drive")){
                    Intent intent = new Intent(getApplicationContext(), PdfViewActivity.class);
                    intent.putExtra("pdf", key);
                    startActivity(intent);} else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(key));
                        startActivity(browserIntent);
                    }
                }
            });
        }

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String DATABASE_NAME = "BookmarkedNews";
                String TABLE_NAME = "Bookmark";
                Cursor cursor;
                SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME,android.content.Context.MODE_PRIVATE ,null);
                try {
                    String query="Select * From "+TABLE_NAME+" Where post_id="+Id;
                    cursor=db.rawQuery(query,null);
                    if (cursor.getCount()>0){
                        Toast.makeText(getApplicationContext(), "Already Bookmarked", Toast.LENGTH_SHORT).show();
                    } else if(databaseManager.addData(Id,Title,Content,Image,Timenew)){
                        Toast.makeText(getApplicationContext(), "Bookmarked Successfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Please try later", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    if(databaseManager.addData(Id,Title,Content,Image,Timenew)){
                        Toast.makeText(getApplicationContext(), "Bookmarked Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareLink+" \nDownload Law Trend for more update \n "+ BuildConfig.APPLICATION_ID);
// +"https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                startActivity(sendIntent);
            }
        });
    }

//    private void dateTimeinNewFormat() {
//        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//// This line converts the given date into UTC time zone
//        try {
//            Date pastpast=sdf.parse(Timenew);
//            Date now=new Date();
////            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
////            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
////            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
////            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
//            if (seconds == 0) {
//                time.setText("just now");
//            } else if (seconds < 60 && seconds > 0) {
//                time.setText(seconds + " seconds ago");
//            } else if (minutes < 60) {
//                time.setText(minutes + " minutes ago");
//            } else if (hours < 24) {
//                time.setText(hours + " hours ago");
//            } else if (days >= 7) {
//                if (days > 360) {
//                    convTime = (days / 360) + " years ago ";
//                    time.setText(convTime);
//                } else if (days > 30) {
//                    convTime = (days / 30) + " months ago ";
//                    time.setText(convTime);
//                } else {
//                    convTime = (days / 7) + " week ago";
//                    time.setText(convTime);
//                }
//            } else if (days < 7) {
//                convTime = days + " days ago";
//                time.setText(convTime);
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}