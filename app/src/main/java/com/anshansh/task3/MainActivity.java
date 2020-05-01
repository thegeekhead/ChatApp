package com.anshansh.task3;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

//import static com.anshansh.task3.R.id.app_bar_layout;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private  TabsAccessAdapter tabsAccessAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ATGChatApp");

        viewPager = (ViewPager) findViewById(R.id.app_view_pager);
        tabsAccessAdapter = new TabsAccessAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAccessAdapter);


        tabLayout = (TabLayout) findViewById(R.id.app_tab);
        tabLayout.setupWithViewPager(viewPager);

    }
}
