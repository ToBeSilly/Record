package com.example.record;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends FragmentActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPager();
        //添加按钮
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddBwlActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化ViewPager
     */
        private void initPager(){
            viewPager = findViewById(R.id.view_pager);
            tabLayout = findViewById(R.id.tabs);
            MyFragment fragment = new MyFragment(getSupportFragmentManager());
            viewPager.setAdapter(fragment);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabMode(TabLayout.MODE_AUTO);
            //tabLayout监听
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                    if(tab.getPosition()==1){
                        Intent intent =new Intent();
                        intent.setClass(MainActivity.this,InfoActivity.class);
                        startActivity(intent);

                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }


            });
    }
}
