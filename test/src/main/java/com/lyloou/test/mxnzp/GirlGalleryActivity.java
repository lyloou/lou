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

package com.lyloou.test.mxnzp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.lyloou.test.R;
import com.lyloou.test.common.Constant;
import com.lyloou.test.common.Consumer;
import com.lyloou.test.common.HackyViewPager;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.common.glide.PaletteBitmap;
import com.lyloou.test.common.glide.PaletteBitmapTranscoder;
import com.lyloou.test.util.Ugson;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utoast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GirlGalleryActivity extends AppCompatActivity {
    private static final String EXTRA_ITEMS = "items";
    private static final String EXTRA_PAGE = "page";
    private static final String EXTRA_POSITION = "position";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private int mPage = 0;
    private static final int COLOR_BLUE = Color.parseColor("#009edc");
    private static final int COLOR_BLACK = Color.parseColor("#000000");
    private Activity mContext;
    private HackyViewPager mViewPager;

    public static void startActivity(Context context, String items, int page, int position) {
        Intent intent = new Intent(context, GirlGalleryActivity.class);
        intent.putExtra(EXTRA_ITEMS, items);
        intent.putExtra(EXTRA_PAGE, page);
        intent.putExtra(EXTRA_POSITION, position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mViewPager = new HackyViewPager(mContext);
        mViewPager.setTag(true);
        mViewPager.setBackgroundColor(COLOR_BLUE);
        setContentView(mViewPager);

        initView();
        initData();
    }

    private void initData() {
        if (getIntent() != null) {
            String items = getIntent().getStringExtra(EXTRA_ITEMS);
            List<GirlResult.Data.Girl> list = Ugson.getGson().fromJson(items, GirlHelper.getType());
            List<String> data = toUrlList(list);
            mAdapter.addItemsAndNotify(data);
            mPage = getIntent().getIntExtra(EXTRA_PAGE, 0);
            int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            mViewPager.setCurrentItem(position);
        }
        if (mAdapter.getCount() <= 0) {
            loadMore();
        }
    }


    private List<String> toUrlList(List<GirlResult.Data.Girl> list) {
        List<String> data = new ArrayList<>();
        for (GirlResult.Data.Girl girl : list) {
            data.add(girl.getImageUrl());
        }
        return data;
    }


    private void loadData(int page, Consumer<List<GirlResult.Data.Girl>> consumer) {
        Observable<GirlResult> observable = NetWork.get(Constant.Url.Mxnzp.getUrl(), MxnzpApi.class).getGirl(page);
        Disposable disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jokeResults -> {
                    GirlResult.Data data = jokeResults.getData();
                    consumer.accept(data.getList());
                }, throwable -> Utoast.show(mContext, throwable.getMessage()));
        mCompositeDisposable.add(disposable);
    }

    boolean mIsEnd = false;

    private void loadMore() {
        if (mIsEnd) {
            return;
        }
        loadData(mPage++, list -> {
            if (list.size() == 0) {
                mIsEnd = true;
                return;
            }
            List<String> data = toUrlList(list);
            mAdapter.addItemsAndNotify(data);
        });
    }

    @Override
    protected void onDestroy() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

    private GalleryPagerAdapter mAdapter;

    private void initView() {

        mAdapter = new GalleryPagerAdapter();
        mAdapter.setClickListener(view -> toggleScreenStatus());
        mAdapter.setLongClickListener(view -> {
            if (view instanceof ImageView) {
                Uscreen.setWallpaperByImageView((ImageView) view, COLOR_BLUE);
                return true;
            }
            return false;
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > mAdapter.getCount() - 2) {
                    loadMore();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        final List<String> list;
        private View.OnClickListener clickListener;
        private View.OnLongClickListener longClickListener;

        public GalleryPagerAdapter() {
            list = new ArrayList<>();
        }

        public void setClickListener(View.OnClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public void setLongClickListener(View.OnLongClickListener longClickListener) {
            this.longClickListener = longClickListener;
        }

        public void addItemsAndNotify(List<String> list) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        public String getItem(int position) {
            if (position >= list.size() || position < 0) {
                throw new IndexOutOfBoundsException("越界了啦");
            }
            return list.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = container.getContext();
            PhotoView photoView = new PhotoView(context);
            container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
            String url = list.get(position);

            setImage(context, photoView, url);

            photoView.setOnClickListener(clickListener);
            photoView.setOnLongClickListener(longClickListener);
            return photoView;
        }

        private void setImage(Context context, ImageView iv, String url) {
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .transcode(new PaletteBitmapTranscoder(context), PaletteBitmap.class)
                    .into(new ImageViewTarget<PaletteBitmap>(iv) {
                        @Override
                        protected void setResource(PaletteBitmap resource) {
                            // [Converting bitmap to drawable in Android - Stack Overflow](https://stackoverflow.com/questions/23107090/converting-bitmap-to-drawable-in-android)
                            super.view.setImageDrawable(new BitmapDrawable(getResources(), resource.bitmap));
                            Palette p = resource.palette;
                            int color = p.getMutedColor(ContextCompat.getColor(context, R.color.colorAccent));
                            mViewPager.setBackgroundColor(color);
                        }
                    });
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
