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

package com.lyloou.test.onearticle;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.Constant;
import com.lyloou.test.common.LouAdapter;
import com.lyloou.test.common.LouDialog;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.common.db.LouSQLite;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utime;
import com.lyloou.test.util.dialog.Content;
import com.lyloou.test.util.dialog.Udialog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OneArticleActivity extends AppCompatActivity {

    private Activity mContext;
    private Article mCurrentDay;
    private List<Article> mFavorites;
    private CompositeDisposable mDisposable;
    private MenuItem mItemFavorite;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onearticle);
        mContext = this;
        mDisposable = new CompositeDisposable();

        initData();
        initView();

        layoutIt(NetWork.get(Constant.Url.Meiriyiwen.getUrl(), OneArticleApi.class).getOneArticle(1));
    }

    private void initData() {
        LouSQLite.init(this.getApplicationContext(), DbCallback.getInstance());
    }

    private void layoutIt(Observable<OneArticle> observable) {
        mDisposable.add(observable
                .doOnNext(oneArticle -> queryFromDb())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showArticle
                        , throwable -> {
                            throwable.printStackTrace();
                            Toast.makeText(mContext, "加载失败：" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }));
    }

    private void queryFromDb() {
        if (mFavorites == null) {
            mFavorites = LouSQLite.query(
                    DbCallback.TABLE_NAME_ONE_ARTICLE
                    , "select * from " + DbCallback.TABLE_NAME_ONE_ARTICLE
                    , null
            );
        }
    }


    private boolean contains(Article art) {
        for (Article article : mFavorites) {
            if (article.getDate().equals(art.getDate())) {
                return true;
            }
        }
        return false;
    }


    private void showArticle(@NonNull OneArticle oneArticle) {
        mCurrentDay = new Article(oneArticle.getData().getDate().getCurr(), oneArticle.getData().getAuthor(), oneArticle.getData().getTitle());
        refreshItemFavoriteStatus(contains(mCurrentDay));
        setTopBg(mCurrentDay.getDate());

        String title = oneArticle.getData().getTitle();
        String authDate = oneArticle.getData().getAuthor() + "（" + mCurrentDay.getDate() + "）";
        String htmlContent = oneArticle.getData().getContent();

        this.<CollapsingToolbarLayout>findViewById(R.id.coolapsing_toolbar_layout_onearticle).setTitle(title);

        TextView tvTitle = findViewById(R.id.tv_one);
        TextView tvAuthorDate = findViewById(R.id.tv_author_date);
        tvTitle.setText(title);
        tvAuthorDate.setText(authDate);

        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/render.css\" type=\"text/css\">";

        String result = "<!DOCTYPE html>\n"
                + "<html lang=\"ZH-CN\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                + "<head>\n<meta charset=\"utf-8\" />\n"
                + css
                + "\n</head>\n<body>\n"
                + "<div class=\"container bs-docs-container\">\n"
                + "<div class=\"post-container\">\n"
                + htmlContent
                + "</div>\n</div>\n</body>\n</html>";

        WebView webView = findViewById(R.id.wv_content);
        webView.loadDataWithBaseURL("x-data://base", result, "text/html", "utf-8", null);
        scrollToTop(findViewById(R.id.nsv_view));
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar_onearticle);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        Uscreen.setToolbarMarginTop(mContext, mToolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.coolapsing_toolbar_layout_onearticle);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);


        String today = Utime.getDayWithFormatTwo();
        setTopBg(today);

        View fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> scrollToTop(findViewById(R.id.nsv_view)));
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            int totalScrollRange = appBarLayout1.getTotalScrollRange();
            float offset = verticalOffset * 1.0f / totalScrollRange;
            fab.setScaleX(offset);
            fab.setScaleY(offset);
        });
    }

    private void setTopBg(String day) {
        int image = Integer.parseInt(day) % 98;
        String url = OneArticleUtil.getImage(image);
        Glide.with(mContext).load(url).asBitmap().centerCrop().into(this.<ImageView>findViewById(R.id.iv_header));
    }

    private void scrollToTop(NestedScrollView nestedScrollView) {
        ObjectAnimator anim = ObjectAnimator.ofInt(nestedScrollView, "scrollY", nestedScrollView.getScrollY(), 0);
        anim.setDuration(500);
        anim.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_one_artical, menu);
        mItemFavorite = menu.findItem(R.id.menu_one_article_collect);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Observable<OneArticle> observable;
        switch (item.getItemId()) {
            case R.id.menu_one_article_today:
                observable = NetWork.get(Constant.Url.Meiriyiwen.getUrl(), OneArticleApi.class).getOneArticle(1);
                layoutIt(observable);
                break;
            case R.id.menu_one_article_random:
                observable = NetWork.get(Constant.Url.Meiriyiwen.getUrl(), OneArticleApi.class).getRandomArticle(1);
                layoutIt(observable);
                break;
            case R.id.menu_one_article_select:
                showSpecialDayArticle();
                break;
            case R.id.menu_one_article_here:
                showSpecialDayArticleHere();
                break;
            case R.id.menu_one_article_collect:
                toggleFavoriteStatus();
                break;
            case R.id.menu_one_article_my_favorites:
                showMyFavorites();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void showMyFavorites() {
        ListView listView = new ListView(mContext);

        final LouAdapter<Article> adapter = new LouAdapter<Article>(listView, R.layout.item_onearticle_favorite) {
            @Override
            protected void assign(ViewHolder holder, Article s) {
                holder.putText(R.id.tv_one, s.getTitle());
                holder.putText(R.id.tv_author_date, s.getAuthor() + " - " + s.getDate());
            }
        };

        if (mFavorites == null) {
            Toast.makeText(mContext, "重新加载中", Toast.LENGTH_SHORT).show();
            queryFromDb();
        }

        adapter.initList(mFavorites);
        LouDialog louDialog = LouDialog.newInstance(mContext, listView, R.style.Theme_AppCompat_Dialog);
        louDialog.setWH(-1, Uscreen.getScreenHeight(mContext) * 2 / 3);
        louDialog.setCancelable(true);
        Dialog dialog = louDialog.getDialog();
        dialog.setTitle("我的收藏夹");
        louDialog.show();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Article article = mFavorites.get(i);
            Observable<OneArticle> observable = NetWork.get(Constant.Url.Meiriyiwen.getUrl(), OneArticleApi.class).getSpecialArticle(1, article.getDate());
            layoutIt(observable);
            louDialog.dismiss();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private void toggleFavoriteStatus() {
        mDisposable.add(Observable.just(mCurrentDay)
                .map(article -> {
                    boolean contains = contains(article);
                    addToOrRemoveFromFavorites(article, contains);
                    return !contains;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::refreshItemFavoriteStatus));
    }

    private void refreshItemFavoriteStatus(@NonNull Boolean exist) {
        mItemFavorite.setIcon(exist ? R.mipmap.ic_favorite : R.mipmap.ic_favorite_border);
    }

    // 注意：不要在主线程中操作
    private void addToOrRemoveFromFavorites(@NonNull Article art, boolean remove) {
        // 包含的话则移除，不包含的话则添加
        if (remove) {
            LouSQLite.delete(DbCallback.TABLE_NAME_ONE_ARTICLE
                    , ArticleEntry.COLEUM_NAME_DATE + "=?"
                    , new String[]{art.getDate()});
            Article toBeDeleteArticle = null;
            for (Article article : mFavorites) {
                if (art.getDate().equals(article.getDate())) {
                    toBeDeleteArticle = article;
                    break;
                }
            }
            mFavorites.remove(toBeDeleteArticle);
        } else {
            LouSQLite.insert(DbCallback.TABLE_NAME_ONE_ARTICLE, art);
            mFavorites.add(art);
        }
    }

    private void showSpecialDayArticle() {
        LouDialog louDialog = LouDialog
                .newInstance(mContext, R.layout.dialog_onearticle, R.style.Theme_AppCompat_Dialog)
                .setCancelable(true);
        DatePicker datePicker = louDialog.getView(R.id.dp_one_article);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                (datePicker1, year, month, dayOfMonth) -> {
                    Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
                    System.out.printf("当前时间：%1$TY-%1$Tm-%1$Td %1$TH:%1$TM:%1$TS", new Date());
                    String y = String.format(Locale.getDefault(), "%04d", year);
                    String m = String.format(Locale.getDefault(), "%02d", month + 1);
                    String d = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                    String date = y + m + d;
                    layoutIt(NetWork.get(Constant.Url.Meiriyiwen.getUrl(), OneArticleApi.class)
                            .getSpecialArticle(1, date));
                    louDialog.dismiss();
                });

        louDialog.show();
    }

    private void showSpecialDayArticleHere() {
        Content content = Content.Builder.builder()
                .title("输入日期")
                .hint("20171111")
                .type(Content.Type.NUMBER)
                .focus(true)
                .build();
        Udialog.showInputDialog(mContext, content, date -> {
            if (!TextUtils.isEmpty(date.trim())) {
                layoutIt(NetWork.get(Constant.Url.Meiriyiwen.getUrl(), OneArticleApi.class)
                        .getSpecialArticle(1, date)
                );
            } else {
                Toast.makeText(mContext, "请输入内容", Toast.LENGTH_SHORT).show();
                showSpecialDayArticleHere();
            }
        });
    }
}
