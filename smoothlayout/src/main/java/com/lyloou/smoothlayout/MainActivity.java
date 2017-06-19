/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.smoothlayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.UUID;

import github.chenupt.springindicator.SpringIndicator;

import static com.lyloou.smoothlayout.Util.fillDatas;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showAppbarlayout();
//        showToolbar();
//        showViewpager();
//        showSpringindor();
//        showCoordinate();
    }


    // http://blog.csdn.net/qq_16628781/article/details/51569220
    private void showAppbarlayout() {

        setContentView(R.layout.activity_appbarlayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Util.setToolbarMarginTop(this, toolbar);
        toolbar.setTitle("开大门");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(Color.YELLOW);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    }

    private void showToolbar() {
        setContentView(R.layout.activity_toobar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        TextView tvTitle = (TextView) toolbar.findViewById(R.id.tv_top_title);
        tvTitle.setText("开大门");
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(28);

        toolbar.setNavigationIcon(R.mipmap.back_white);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void showCoordinate() {
        setContentView(R.layout.activity_coordinate);

        final TextView textView = (TextView) findViewById(R.id.tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(UUID.randomUUID().toString());
            }
        });
    }

    private void showSpringindor() {
        setContentView(R.layout.activity_sprintindicator);

        View v1 = LayoutInflater.from(this).inflate(R.layout.list, null);
        View v2 = LayoutInflater.from(this).inflate(R.layout.list, null);
        View v3 = LayoutInflater.from(this).inflate(R.layout.list, null);
        View v4 = LayoutInflater.from(this).inflate(R.layout.list, null);
        View v5 = LayoutInflater.from(this).inflate(R.layout.list, null);
        View v6 = LayoutInflater.from(this).inflate(R.layout.list, null);

        fillDatas((RecyclerView) v1.findViewById(R.id.rv_list));
        fillDatas((RecyclerView) v2.findViewById(R.id.rv_list));
        fillDatas((RecyclerView) v3.findViewById(R.id.rv_list));
        fillDatas((RecyclerView) v4.findViewById(R.id.rv_list));
        fillDatas((RecyclerView) v5.findViewById(R.id.rv_list));
        fillDatas((RecyclerView) v6.findViewById(R.id.rv_list));

        TitleViewPagerAdapter pagerAdapter = new TitleViewPagerAdapter();
        pagerAdapter.addView("1", v1);
        pagerAdapter.addView("2", v2);
        pagerAdapter.addView("3", v3);
        pagerAdapter.addView("4", v4);
        pagerAdapter.addView("5", v5);
        pagerAdapter.addView("6", v6);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_room);
        viewPager.setAdapter(pagerAdapter);

        SpringIndicator indicator = (SpringIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

    }

    private void showViewpager() {
        setContentView(R.layout.activity_viewpager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.back_white);
        toolbar.setTitle("开大门");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        View v1 = LayoutInflater.from(this).inflate(R.layout.list, null);
        View v2 = LayoutInflater.from(this).inflate(R.layout.list, null);

        fillDatas((RecyclerView) v1.findViewById(R.id.rv_list));
        fillDatas((RecyclerView) v2.findViewById(R.id.rv_list));

        TitleViewPagerAdapter pagerAdapter = new TitleViewPagerAdapter();
        pagerAdapter.addView("话题", v1);
        pagerAdapter.addView("系列课", v2);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_room);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablyt_room);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }


}
