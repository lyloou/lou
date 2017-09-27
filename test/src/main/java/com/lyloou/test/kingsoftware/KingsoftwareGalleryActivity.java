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

package com.lyloou.test.kingsoftware;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.util.Uscreen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KingsoftwareGalleryActivity extends AppCompatActivity {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private static final int COLOR_BLUE = Color.parseColor("#009edc");
    private static final int COLOR_BLACK = Color.parseColor("#000000");
    private Activity mContext;
    private HackyViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mViewPager = new HackyViewPager(mContext);
        mViewPager.setTag(true);
        mViewPager.setBackgroundColor(COLOR_BLUE);
        setContentView(mViewPager);

        initView();
    }

    private void initView() {

        GalleryPagerAdapter adapter = new GalleryPagerAdapter();
        mViewPager.setAdapter(adapter);
        adapter.setClickListener(view -> toggleScreenStatus());
        adapter.setLongClickListener(view -> {
            if (view instanceof ImageView) {
                Uscreen.setWallpaper((ImageView) view);
                return true;
            }
            return false;
        });

        String formatedToday = SDF.format(new Date());
        String formatedYesterday = oneDayAgo(formatedToday);
        String formatedTwodayAgo = oneDayAgo(formatedYesterday);
        adapter.addItemAndNotify(formatedToday);
        adapter.addItemAndNotify(formatedYesterday);
        adapter.addItemAndNotify(formatedTwodayAgo);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > adapter.getCount() - 2) {
                    String day = oneDayAgo(adapter.getItem(position));
                    if (!TextUtils.isEmpty(day)) {
                        adapter.addItemAndNotify(day);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private String oneDayAgo(String currentDay) {
        try {
            Calendar calendar = Calendar.getInstance();
            Date parse = SDF.parse(currentDay);
            long oneDayAgoLong = parse.getTime() - 24 * 60 * 60 * 1000;
            calendar.setTimeInMillis(oneDayAgoLong);
            Date oneDayAgoDate = calendar.getTime();
            String oneDayAgoString = SDF.format(oneDayAgoDate);
            return oneDayAgoString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadDataAndRenderView(String day, ImageView view) {
        NetWork.getKingsoftwareApi()
                .getDaily(day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(daily -> Glide
                                .with(mContext)
                                .load(daily.getFenxiang_img()).into(view),
                        Throwable::printStackTrace);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mViewPager.setTag(true);
            toggleScreenStatus();
        }
    }

    // http://blog.csdn.net/guolin_blog/article/details/51763825
    private void toggleScreenStatus() {
        boolean hideStatus = (boolean) mViewPager.getTag();
        if (Build.VERSION.SDK_INT >= 19) {
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            int invisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(hideStatus ? visibility : invisibility);
        }
        mViewPager.setBackgroundColor(hideStatus ? COLOR_BLUE : COLOR_BLACK);
        mViewPager.setTag(!hideStatus);
    }

    // https://github.com/chrisbanes/PhotoView/blob/master/sample/src/main/java/com/github/chrisbanes/photoview/sample/ViewPagerActivity.java
    private class GalleryPagerAdapter extends PagerAdapter {
        final List<String> days;
        private View.OnClickListener clickListener;
        private View.OnLongClickListener longClickListener;

        public GalleryPagerAdapter() {
            days = new ArrayList<>();
        }

        public void setClickListener(View.OnClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public void setLongClickListener(View.OnLongClickListener longClickListener) {
            this.longClickListener = longClickListener;
        }

        public void addItemAndNotify(String day) {
            days.add(day);
            notifyDataSetChanged();
        }

        public String getItem(int position) {
            if (position >= days.size() || position < 0) {
                throw new IndexOutOfBoundsException("越界了啦");
            }
            return days.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = container.getContext();
            PhotoView photoView = new PhotoView(context);
            container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
            loadDataAndRenderView(days.get(position), photoView);
            photoView.setOnClickListener(clickListener);
            photoView.setOnLongClickListener(longClickListener);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return days.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
