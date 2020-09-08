package com.aaratechnologies.lawtrend.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.fragments.ColumnsFragment;
import com.aaratechnologies.lawtrend.fragments.CourtUpdatesFragment;
import com.aaratechnologies.lawtrend.fragments.JudgementsFragment;
import com.aaratechnologies.lawtrend.fragments.OnlineInternshipFragment;
import com.aaratechnologies.lawtrend.fragments.TrendingStoriesFragment;
import com.aaratechnologies.lawtrend.fragments.UserPanelFragment;
import com.aaratechnologies.lawtrend.managers.VolleySingleton;
import com.aaratechnologies.lawtrend.managers.WebURLS;
import com.aaratechnologies.lawtrend.menuwiseactivities.AboutUsActivity;
import com.aaratechnologies.lawtrend.menuwiseactivities.BareAndActsActivity;
import com.aaratechnologies.lawtrend.menuwiseactivities.BookmarkActivity;
import com.aaratechnologies.lawtrend.menuwiseactivities.ContactUsActivity;
import com.aaratechnologies.lawtrend.menuwiseactivities.MasterActivity;
import com.aaratechnologies.lawtrend.menuwiseactivities.OnlineInternshipActivity;
import com.aaratechnologies.lawtrend.menuwiseactivities.SearchNewsActivity;
import com.aaratechnologies.lawtrend.models.ModelMenus;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.skydoves.elasticviews.ElasticCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawer;
    NavigationView navigationView;
    TabLayout tabLayout;
    ViewPager viewPager;
    RelativeLayout container;
//    public static final String DATABASE_NAME="CARTDATABASE1";
//    public static final String BOOKMARKED="Bookmarked";
    SQLiteDatabase mDatabase;

    ListView listView;
    List<ModelMenus> modelMenusList;

    TextView aara;
    ImageView fb,insta,twit,tele,youtu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        createCartDatabaseAndTable();
        fb=findViewById(R.id.fb);
        insta=findViewById(R.id.insta);
        twit=findViewById(R.id.twit);
        tele=findViewById(R.id.tele);
        youtu=findViewById(R.id.youtu);
        aara=findViewById(R.id.aara);

        if (!isInternetConnected()){
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Alert !");
            builder.setCancelable(false);
            builder.setMessage("Please Check your Internet Connectivity");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        } else {

//            AppUpdateChecker appUpdateChecker=new AppUpdateChecker(this);  //pass the activity in constructure
//            appUpdateChecker.checkForUpdate(false); //mannual check false here

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String newToken = instanceIdResult.getToken();
                    Log.d("newToken","Toaken is:   "+newToken);
//                    Toast.makeText(MainActivity.this, ""+newToken, Toast.LENGTH_SHORT).show();
                }
            });
            FirebaseMessaging.getInstance().subscribeToTopic("notification")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = getString(R.string.msg_subscribed);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.msg_subscribe_failed);
                            }
                            Log.d("TAG", msg);
//                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

            listView = findViewById(R.id.listview);
            container = (RelativeLayout) findViewById(R.id.container);
            Toolbar myToolBar = (Toolbar) findViewById(R.id.toolbar);
            if (myToolBar != null) {
                setSupportActionBar(myToolBar);
                getSupportActionBar().setLogo(R.drawable.lg1);
                // myToolBar.setTitle("Dashboard");
                // requestWindowFeature(Window.FEATURE_NO_TITLE);
                myToolBar.hideOverflowMenu();
            }

            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/lawtrend.in/"));
                    startActivity(browserIntent);
                }
            });
            twit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/law_trend/"));
                    startActivity(browserIntent);
                }
            });

            insta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/lawtrend.in/"));
                    startActivity(browserIntent);
                }
            });

            youtu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCnZ9vnXxrQiyxihaA72qc-Q"));
                    startActivity(browserIntent);
                }
            });

            tele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/joinchat/AAAAAFSHPo_-68LPRnsVpg"));
                    startActivity(browserIntent);
                }
            });
            aara.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.aaratechnologies.com/"));
                    startActivity(browserIntent);
                }
            });


            //For drawer
            navigationView = findViewById(R.id.navigation_view);

            drawer = findViewById(R.id.drawer);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolBar, R.string.open, R.string.close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = findViewById(R.id.navigation_view);
            myToolBar.setNavigationIcon(R.drawable.menu_ic);
            tabLayout = findViewById(R.id.tab_layout);
            viewPager = findViewById(R.id.view_pager);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addFragment(new UserPanelFragment(), "All News");
            viewPagerAdapter.addFragment(new TrendingStoriesFragment(), "Trending Stories");
            viewPagerAdapter.addFragment(new CourtUpdatesFragment(), "Court Updates");
            viewPagerAdapter.addFragment(new JudgementsFragment(), "Judgements");
            viewPagerAdapter.addFragment(new OnlineInternshipFragment(), "Online Internship");
            viewPagerAdapter.addFragment(new ColumnsFragment(), "Columns");
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);



            modelMenusList = new ArrayList<>();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, WebURLS.ALL_MENUS_LIST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Log.d("allmenus", "All menus: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String menuname = jsonObject1.getString("title");
                            String id = jsonObject1.getString("id");
                            String object_id = jsonObject1.getString("object_id");
                            Log.d("allmenus", "All menus: " + id + " " + menuname + " Object Id: " + object_id);
                            modelMenusList.add(new ModelMenus(menuname,object_id));

                        }
                        AdapterMenus adapterMenus = new AdapterMenus(getApplicationContext(), modelMenusList);
                        listView.setAdapter(adapterMenus);

                    } catch (JSONException e) {
                        Log.d("exce", "onResponse: "+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


        }

    }
    public boolean isInternetConnected() {
        // At activity startup we manually check the internet status and change
        // the text status
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_dashboard_corner_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.bookmark:
//                Toast.makeText(this, "Bookmarked News", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.search:
                Intent intent1=new Intent(getApplicationContext(), SearchNewsActivity.class);
                startActivity(intent1);
                finish();
//                tabLayout.setVisibility(View.GONE);
//                viewPager.setVisibility(View.GONE);
//               FragmentManager fragmentManagersearch=getSupportFragmentManager();
//                FragmentTransaction fragmentTransactionsearch=fragmentManagersearch.beginTransaction();
//                fragmentTransactionsearch.replace(R.id.container,new SearchFragment());
//                fragmentTransactionsearch.commit();

                break;
            case R.id.notification:
                Intent intent2 = new Intent(getApplicationContext(), NotificationShowActivity.class);
                startActivity(intent2);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.drawable.ic_baseline_exit_to_app_24);
            builder.setMessage("Do you want to Exit");
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private class AdapterMenus extends BaseAdapter{

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
            menu.setText(modelMenus.get(i).getMenu());
            allmenus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (modelMenus.get(i).getMenu().equalsIgnoreCase("About Us")){
                        drawer.closeDrawer(GravityCompat.START);
                        Intent intent=new Intent(getApplicationContext(), AboutUsActivity.class);
                        startActivity(intent);
                        finish();
                    }else if (modelMenus.get(i).getMenu().equalsIgnoreCase("Contact Us")){
                        drawer.closeDrawer(GravityCompat.START);
                        Intent intent=new Intent(getApplicationContext(), ContactUsActivity.class);
                        intent.putExtra("title",modelMenus.get(i).getMenu());
                        startActivity(intent);
                        finish();
                    }else if (modelMenus.get(i).getMenu().equalsIgnoreCase("Home")){
                        drawer.closeDrawer(GravityCompat.START);
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("title",modelMenus.get(i).getMenu());
                        startActivity(intent);
                        finish();

                    } else if (modelMenus.get(i).getMenu().equalsIgnoreCase("Bare Acts and Rules")){
                        drawer.closeDrawer(GravityCompat.START);
                        Intent intent=new Intent(getApplicationContext(), BareAndActsActivity.class);
                        intent.putExtra("title",modelMenus.get(i).getMenu());
                        startActivity(intent);
                        finish();
                    } else if (modelMenus.get(i).getMenu().equalsIgnoreCase("Online Internship")){
                        drawer.closeDrawer(GravityCompat.START);
                        Intent intent=new Intent(getApplicationContext(), OnlineInternshipActivity.class);
                        intent.putExtra("title",modelMenus.get(i).getMenu());
                        startActivity(intent);
                        finish();
                    } else {
                    drawer.closeDrawer(GravityCompat.START);
                        drawer.closeDrawer(GravityCompat.START);
                        Intent intent=new Intent(getApplicationContext(), MasterActivity.class);
                        intent.putExtra("title",modelMenus.get(i).getMenu());
                        intent.putExtra("obj_id",modelMenus.get(i).getObject_id());
                        startActivity(intent);
                        finish();
//                
                }
                }
            });
            return view;
        }
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        private void addFragment(Fragment fragment,String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
//    private void createCartDatabaseAndTable() {
//        mDatabase=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
//        String createTableQuery="Create Table if not exists "+BOOKMARKED+"(" +
//                "id Integer primary key autoincrement," +
//                "post_id text not null," +
//                "image_res text not null," +
//                " time text not null," +
//                " title text not null," +
//                " content text not null)";
//        mDatabase.execSQL(createTableQuery);
//        Log.d("Tablecreated", "Tablecreated: ");
//
//    }


}









//    ElasticCardView home,aboutus,trendingstories,courtupdates,columns,
//            bareacts,share,judgements,onlineinternship,contactus,central,state;
//    ImageView toggleorigin,togglechange;

//Nav Menus
//        home = findViewById(R.id.home);
//        aboutus = findViewById(R.id.aboutus);
//        trendingstories = findViewById(R.id.trendingstories);
//        courtupdates = findViewById(R.id.courtupdates);
//        columns = findViewById(R.id.columns);
//        contactus = findViewById(R.id.contactus);
//        bareacts = findViewById(R.id.bareacts);
//        share = findViewById(R.id.share);
//        judgements = findViewById(R.id.judgements);
//        onlineinternship = findViewById(R.id.onlineinternship);
//        central = findViewById(R.id.central);
//        state = findViewById(R.id.state);
//        toggleorigin = findViewById(R.id.toggleorigin);
//        togglechange = findViewById(R.id.togglechange);
//        cover = findViewById(R.id.cover);





//        bareacts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (a==0) {
//                    toggleorigin.setVisibility(View.GONE);
//                    togglechange.setVisibility(View.VISIBLE);
//                    cover.setVisibility(View.VISIBLE);
//                    a = 1;
//                    return;
//                }
//                if (a==1){
//                    toggleorigin.setVisibility(View.VISIBLE);
//                    togglechange.setVisibility(View.GONE);
//                    cover.setVisibility(View.GONE);
//                    a = 0;
//                    return;
//                }
//            }
//        });
//


//share.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        Intent sendIntent = new Intent(Intent.ACTION_SEND);
//        sendIntent.setType("text/plain");
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(sendIntent);
//    }
//});
//
//home.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        drawer.closeDrawer(GravityCompat.START);
//        FragmentManager fragmentManager1=getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction1=fragmentManager1.beginTransaction();
//        fragmentTransaction1.replace(R.id.container,new UserPanelFragment());
//        fragmentTransaction1.commit();
//    }
//});
//        aboutus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.closeDrawer(GravityCompat.START);
//                FragmentManager fragmentManager1=getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction1=fragmentManager1.beginTransaction();
//                fragmentTransaction1.replace(R.id.container,new AboutUsFragment());
//                fragmentTransaction1.commit();
//            }
//        });
//        trendingstories.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.closeDrawer(GravityCompat.START);
//                FragmentManager fragmentManager1=getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction1=fragmentManager1.beginTransaction();
//                fragmentTransaction1.replace(R.id.container,new TrendingStoriesFragment());
//                fragmentTransaction1.commit();
//            }
//        });
//        courtupdates.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.closeDrawer(GravityCompat.START);
//                FragmentManager fragmentManager1=getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction1=fragmentManager1.beginTransaction();
//                fragmentTransaction1.replace(R.id.container,new CourtUpdatesFragment());
//                fragmentTransaction1.commit();
//            }
//        });
//        columns.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.closeDrawer(GravityCompat.START);
//                FragmentManager fragmentManager1=getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction1=fragmentManager1.beginTransaction();
//                fragmentTransaction1.replace(R.id.container,new ColumnsFragment());
//                fragmentTransaction1.commit();
//            }
//        });
//        judgements.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.closeDrawer(GravityCompat.START);
//                FragmentManager fragmentManager1=getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction1=fragmentManager1.beginTransaction();
//                fragmentTransaction1.replace(R.id.container,new JudgementsFragment());
//                fragmentTransaction1.commit();
//            }
//        });
//        onlineinternship.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.closeDrawer(GravityCompat.START);
//                FragmentManager fragmentManager1=getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction1=fragmentManager1.beginTransaction();
//                fragmentTransaction1.replace(R.id.container,new OnlineInternshipFragment());
//                fragmentTransaction1.commit();
//            }
//        });
//        contactus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.closeDrawer(GravityCompat.START);
//                FragmentManager fragmentManager1=getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction1=fragmentManager1.beginTransaction();
//                fragmentTransaction1.replace(R.id.container,new ContactUsFragment());
//                fragmentTransaction1.commit();
//            }
//        });

