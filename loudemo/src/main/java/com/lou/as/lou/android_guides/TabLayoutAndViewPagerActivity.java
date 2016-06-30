package com.lou.as.lou.android_guides;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lou.as.lou.R;
import com.lyloou.lou.activity.LouActivity;

/**
 * study url: https://github.com/codepath/android_guides/wiki/Google-Play-Style-Tabs-using-TabLayout
 */
public class TabLayoutAndViewPagerActivity extends LouActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_and_view_pager);
        initViewPager();


    }

    private void initViewPager() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(
                new TabLayoutAndViewPagerFragment
                        .SampleFragmentPagerAdapter(getSupportFragmentManager(),
                TabLayoutAndViewPagerActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
