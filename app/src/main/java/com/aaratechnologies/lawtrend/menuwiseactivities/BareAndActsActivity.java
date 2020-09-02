package com.aaratechnologies.lawtrend.menuwiseactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.activities.MainActivity;
import com.aaratechnologies.lawtrend.fragments.AllStatesFragment;
import com.aaratechnologies.lawtrend.fragments.CentralBareActsNewFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class BareAndActsActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bare_and_acts);

        Intent intent=getIntent();
        String Title=intent.getStringExtra("title");
//        obj_id=intent.getStringExtra("obj_id");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tabLayout = findViewById(R.id.tab_layout);
         viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new CentralBareActsNewFragment(), "Central");
        viewPagerAdapter.addFragment(new AllStatesFragment(), "States");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bare_acts, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.search:
//                tabLayout.setVisibility(View.GONE);
//                viewPager.setVisibility(View.GONE);
//                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),SearchActivityForBareAct.class);
                startActivity(intent);
                finish();
//                FragmentManager fragmentManagersearch=getSupportFragmentManager();
//                FragmentTransaction fragmentTransactionsearch=fragmentManagersearch.beginTransaction();
//                fragmentTransactionsearch.replace(R.id.container,new SearchFragment());
//
//                fragmentTransactionsearch.commit();

                break;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }


}