package com.aaratechnologies.lawtrend.activities;

import android.content.Intent;
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

import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.menuwiseactivities.BookmarkActivity;
import com.aaratechnologies.lawtrend.menuwiseactivities.DatabaseManager;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CompleteNewsBookmarked extends AppCompatActivity {
    ImageView image,bookmark,share,delete;
    TextView title,content,time;
    int start=0,last=0;
    LinearLayout ll;
    String convTime="",Timenew="",key="";
    String shareLink="";
    String past,id="",post_id="";
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_news_bookmarked);
        databaseManager = new DatabaseManager(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" Complete News");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        image = findViewById(R.id.image);
        delete = findViewById(R.id.delete);
        bookmark = findViewById(R.id.bookmark);
        share = findViewById(R.id.share);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        time = findViewById(R.id.time);
        ll = findViewById(R.id.ll);
        final Intent intent = getIntent();

        final String Title = intent.getStringExtra("title");
        String Content = intent.getStringExtra("content");
        Timenew = intent.getStringExtra("time");
   id = intent.getStringExtra("id");
        String Image = intent.getStringExtra("image");
        shareLink = intent.getStringExtra("share");
        post_id=intent.getStringExtra("posId");
//        Toast.makeText(this, ""+post_id, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "" + id, Toast.LENGTH_SHORT).show();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final Date dateObj;
        try {
            dateObj = sdf.parse(Timenew);
            SimpleDateFormat formated = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss a");
            past = formated.format(dateObj);
            System.out.println(past);
//            Toast.makeText(this, "" + past, Toast.LENGTH_SHORT).show();

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
        } else if (Content.contains("https://lawtrend.in/wp-content/uploads/")) {
            start = Content.indexOf("https://lawtrend.in/wp-content/uploads/");
// Log.d("indexesare", "Index Start: "+start);
            last = Content.indexOf(".pdf");
// Log.d("indexesare", "Index Last: "+last);

            if ((start != (-1)) && (last != (-1))) {
                last = last + 4;
                key = Content.substring(start, last);
// Log.d("indexesarea", "Index Last: "+key);
            }
        } else {
            key = "";
        }
// Log.d("totalsrc", "Index Key: "+Title+" , "+key);
        time.setText(past);
//                dateTimeinNewFormat();

// Toast.makeText(this, ""+PDF, Toast.LENGTH_SHORT).show();
        title.setText(Html.fromHtml(Title));
        content.setText(Html.fromHtml(Content));
        ll.setVisibility(View.GONE);
// time.setText(Timenew);
        Glide.with(getApplicationContext()).load(Image).into(image);

        if (key.equals("")) {
            ll.setVisibility(View.GONE);
        } else {
            ll.setVisibility(View.VISIBLE);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (key.contains("https://drive")) {
                        Intent intent = new Intent(getApplicationContext(), PdfViewActivity.class);
                        intent.putExtra("pdf", key);
                        startActivity(intent);
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(key));
                        startActivity(browserIntent);
                    }
                }
            });
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (databaseManager.deleteData(Integer.parseInt(id))){
                    Toast.makeText(CompleteNewsBookmarked.this, "Removed From Bookmark", Toast.LENGTH_SHORT).show();
                    Intent intent1=new Intent(getApplicationContext(),BookmarkActivity.class);
                    startActivity(intent1);
                    finish();
                }else {
                    Toast.makeText(CompleteNewsBookmarked.this, "Please Try Later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
            @Override
            public void onBackPressed() {
                Intent intent=new Intent(getApplicationContext(), BookmarkActivity.class);
                startActivity(intent);
                finish();
            }
        }

