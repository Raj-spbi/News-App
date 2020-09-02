package com.aaratechnologies.lawtrend.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aaratechnologies.lawtrend.BuildConfig;
import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.activities.PdfViewActivity;
import com.aaratechnologies.lawtrend.activities.TotalNewsActivity;
import com.aaratechnologies.lawtrend.managers.VolleySingleton;
import com.aaratechnologies.lawtrend.managers.WebURLS;
import com.aaratechnologies.lawtrend.models.ModelData;
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


public class TrendingStoriesFragment extends Fragment {
    DatabaseManager databaseManager;
    RecyclerView listView;
    List<ModelData> modelData;

    int page=1;
    MKLoader progress;
    AdapterAllList adapterAllList;
    NestedScrollView nestedScrollView;

    public TrendingStoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending_stories, container, false);
        listView=view.findViewById(R.id.lv);
        databaseManager=new DatabaseManager(getContext());
        nestedScrollView=view.findViewById(R.id.nestedscroll);
        progress=view.findViewById(R.id.progress);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        modelData=new ArrayList<>();
        listView.setAdapter(adapterAllList);
        getData(page);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                    page++;
                    progress.setVisibility(View.VISIBLE);
                    getData(page);
                }
            }
        });
        return view;
    }
    private void getData(final int page) {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, WebURLS.Category_WISE_POST_PER_PAGE+"69&page="+page, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    progress.setVisibility(View.GONE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String date = jsonObject.getString("date");
                        JSONObject jsonObject1 = jsonObject.getJSONObject("title");
                        String title = jsonObject1.getString("rendered");

                        JSONObject jsonObject2 = jsonObject.getJSONObject("excerpt");
                        String shortcontent = jsonObject2.getString("rendered");
                        JSONObject jsonObject3 = jsonObject.getJSONObject("content");
                        String content = jsonObject3.getString("rendered");

                        JSONObject jsonObject4 = jsonObject.getJSONObject("better_featured_image");
                        String imageurl = jsonObject4.getString("source_url");
                        String sharelink=jsonObject.getString("link");
                        Log.d("responsesare", "onResponse: " + id + ", " + date + ", " + title + "," + imageurl);
                        modelData.add(new ModelData(title, content, imageurl, date, id, shortcontent,sharelink));

                    }
                } catch (JSONException e) {
                    progress.setVisibility(View.GONE);
                    Log.d("causeoferror", "onResponse: "+e.getMessage());
                    e.printStackTrace();

                }
                adapterAllList = new AdapterAllList(getContext(), modelData);
                listView.setAdapter(adapterAllList);
                adapterAllList.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);

//                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();

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
                        getData(page);
                        }
                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                } else if (error instanceof AuthFailureError) {

                    error.printStackTrace();
//                    Toast.makeText(getContext(), "Login Again"+error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError ||error instanceof ClientError) {

                    error.printStackTrace();
                    Toast.makeText(getContext(), "No More Data Found", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    error.printStackTrace();
//                    Toast.makeText(getContext(), ""+error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    error.printStackTrace();
                }

            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private class AdapterAllList extends RecyclerView.Adapter{
        String key=""; String convTime=null;
        Context context;
        List<ModelData> modelData;

        public AdapterAllList(Context context, List<ModelData> modelData) {
            this.context = context;
            this.modelData = modelData;
        }
        @Override
        public int getItemViewType(int position) {
            if (position==0){
                return 0;
            }
            return 1;
        }



        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater=LayoutInflater.from(context);
            View view;
            if (viewType==0){
                view=layoutInflater.inflate(R.layout.custom_layout,parent,false);
                return new ViewHolderOne(view);
            }
            view=layoutInflater.inflate(R.layout.custom_differ_layout,parent,false);
            return new ViewHolderTwo(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            if (position==0){

               ViewHolderOne viewHolderOne=(ViewHolderOne) holder;
                viewHolderOne.title.setText(Html.fromHtml(modelData.get(position).getTitle()));
                viewHolderOne.content.setText(Html.fromHtml(modelData.get(position).getContent()));
                viewHolderOne.time.setText(modelData.get(position).getTime());
                Glide.with(getActivity()).load(modelData.get(position).getImageres()).into(viewHolderOne.image);
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                // This line converts the given date into UTC time zone
                try {
                    Date past=sdf.parse(modelData.get(position).getTime());
                    Date now=new Date();
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
                    long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
                    long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
                    if (seconds == 0) {
                        viewHolderOne.time.setText("just now");
                    } else if (seconds < 60 && seconds > 0) {
                        viewHolderOne.time.setText(seconds + " seconds ago");
                    } else if (minutes < 60) {
                        viewHolderOne.time.setText(minutes + " minutes ago");
                    } else if (hours < 24) {
                        viewHolderOne.time.setText(hours + " hours ago");
                    } else if (days >= 7) {
                        if (days > 360) {
                            convTime = (days / 360) + " years ago ";
                            viewHolderOne.time.setText(convTime);
                        } else if (days > 30) {
                            convTime = (days / 30) + " months ago ";
                            viewHolderOne.time.setText(convTime);
                        } else {
                            convTime = (days / 7) + " week ago";
                            viewHolderOne.time.setText(convTime);
                        }
                    } else if (days < 7) {
                        convTime = days + " days ago";
                        viewHolderOne.time.setText(convTime);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

//            final Date dateObj;
//            try {
//                dateObj = sdf.parse(modelData.get(i).getTime());
//                SimpleDateFormat formated = new SimpleDateFormat("dd/MM/yyyy KK:mm:ss a");0
//                String past=formated.format(dateObj);
//                System.out.println(newdate);
//                time.setText(newdate);

//            } catch (ParseException e) {
//                e.printStackTrace();
//            }


                String src = modelData.get(position).getContent();
                if (src.contains("<iframe src=\"")) {
                    final int start = src.indexOf("https://drive");
                    int last = src.indexOf("preview");
                    Log.d("abcdef", "onBindViewHolder: "+start+", "+last);
                    last=last+7;
                    key = src.substring(start, last);
                    Log.d("datata", "getView: " + key);
                    viewHolderOne.ll.setVisibility(View.VISIBLE);
                    viewHolderOne.ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, PdfViewActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("pdf", key);
                            context.startActivity(intent);
                        }
                    });
                } else {
                    viewHolderOne.ll.setVisibility(View.GONE);
                }
                viewHolderOne.cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent=new Intent(getActivity(), TotalNewsActivity.class);
                        intent.putExtra("image",modelData.get(position).getImageres());
                        intent.putExtra("title",modelData.get(position).getTitle());
                        intent.putExtra("content",modelData.get(position).getContent());
                        intent.putExtra("time",modelData.get(position).getTime());
                        intent.putExtra("id",modelData.get(position).getId());
                        intent.putExtra("pdf",key);
                        intent.putExtra("share",modelData.get(position).getSharelink());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                viewHolderOne.book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String DATABASE_NAME = "BookmarkedNews";
                        String TABLE_NAME = "Bookmark";
                        Cursor cursor;
                        SQLiteDatabase db = getActivity().openOrCreateDatabase(DATABASE_NAME,android.content.Context.MODE_PRIVATE ,null);
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
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            if(databaseManager.addData(modelData.get(position).getId(),modelData.get(position).getTitle(),modelData.get(position).getContent(),modelData.get(position).getImageres(),modelData.get(position).getTime())){
                                Toast.makeText(context, "Bookmarked Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                viewHolderOne.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, modelData.get(position).getSharelink()+"  \nDownload Law Trend for more update \n "+"https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                        startActivity(sendIntent);
                    }
                });


            } else {
                ViewHolderTwo viewHolderTwo=(ViewHolderTwo) holder;
                viewHolderTwo.title.setText(Html.fromHtml(modelData.get(position).getTitle()));
                viewHolderTwo.content.setText(Html.fromHtml(modelData.get(position).getContent()));
                viewHolderTwo.time.setText(modelData.get(position).getTime());
                Glide.with(getActivity()).load(modelData.get(position).getImageres()).into(viewHolderTwo.image);
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                // This line converts the given date into UTC time zone
                try {
                    Date past=sdf.parse(modelData.get(position).getTime());
                    Date now=new Date();
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
                    long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
                    long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
                    if (seconds == 0) {
                        viewHolderTwo.time.setText("just now");
                    } else if (seconds < 60 && seconds > 0) {
                        viewHolderTwo.time.setText(seconds + " seconds ago");
                    } else if (minutes < 60) {
                        viewHolderTwo.time.setText(minutes + " minutes ago");
                    } else if (hours < 24) {
                        viewHolderTwo.time.setText(hours + " hours ago");
                    } else if (days >= 7) {
                        if (days > 360) {
                            convTime = (days / 360) + " years ago ";
                            viewHolderTwo.time.setText(convTime);
                        } else if (days > 30) {
                            convTime = (days / 30) + " months ago ";
                            viewHolderTwo.time.setText(convTime);
                        } else {
                            convTime = (days / 7) + " week ago";
                            viewHolderTwo.time.setText(convTime);
                        }
                    } else if (days < 7) {
                        convTime = days + " days ago";
                        viewHolderTwo.time.setText(convTime);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String src = modelData.get(position).getContent();
                if (src.contains("<iframe src=\"")) {
                    final int start = src.indexOf("https://drive");
                    int last = src.indexOf("preview");
                    Log.d("abcdef", "onBindViewHolder: "+start+", "+last);
                    last=last+7;
                    key = src.substring(start, last);
                }
                viewHolderTwo.cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity(), TotalNewsActivity.class);
                        intent.putExtra("image",modelData.get(position).getImageres());
                        intent.putExtra("title",modelData.get(position).getTitle());
                        intent.putExtra("content",modelData.get(position).getContent());
                        intent.putExtra("time",modelData.get(position).getTime());
                        intent.putExtra("id",modelData.get(position).getId());
                        intent.putExtra("pdf",key);
                        intent.putExtra("share",modelData.get(position).getSharelink());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                viewHolderTwo.book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String DATABASE_NAME = "BookmarkedNews";
                        String TABLE_NAME = "Bookmark";
                        Cursor cursor;
                        SQLiteDatabase db = getActivity().openOrCreateDatabase(DATABASE_NAME,android.content.Context.MODE_PRIVATE ,null);
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
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            if(databaseManager.addData(modelData.get(position).getId(),modelData.get(position).getTitle(),modelData.get(position).getContent(),modelData.get(position).getImageres(),modelData.get(position).getTime())){
                                Toast.makeText(context, "Bookmarked Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                viewHolderTwo.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, modelData.get(position).getSharelink()+"  \nDownload Law Trend for more update \n "+"https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                        startActivity(sendIntent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return modelData.size();
        }

        class ViewHolderOne extends RecyclerView.ViewHolder{


            ImageView image;
            TextView time,title,content;
            CardView cover;
            LinearLayout ll;
            ImageView book,share;


            public ViewHolderOne(@NonNull View itemView) {
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
        class ViewHolderTwo extends RecyclerView.ViewHolder{
            ImageView image;
            TextView time,title,content;
            CardView cover;
            LinearLayout ll;
            ImageView book,share;

            public ViewHolderTwo(@NonNull View itemView) {
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

}