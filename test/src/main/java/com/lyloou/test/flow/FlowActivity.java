package com.lyloou.test.flow;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.EmptyRecyclerView;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.util.Uanimation;
import com.lyloou.test.util.Uscreen;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FlowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flow);
        initView();
    }

    private void initView() {
        initTopPart();
        EmptyRecyclerView recyclerView = initRecycleView();
        initFabBottom(recyclerView);
    }

    @NonNull
    private EmptyRecyclerView initRecycleView() {
        EmptyRecyclerView recyclerView = findViewById(R.id.erv_flow);
        FlowAdapter adapter = new FlowAdapter(this);
        adapter.addAll(getFlowItems());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new TimeLineItemDecoration());
        return recyclerView;
    }

    @NonNull
    private ArrayList<FlowItem> getFlowItems() {
        ArrayList<FlowItem> items = new ArrayList<>();
        items.add(new FlowItem());
        for (int i = 0; i < 10; i++) {
            items.add(new FlowItem() {{
                setTime("10:20-12:00");
                setSpend("1:40");
                setContent("多么美好的一天 大太阳晒伤我的脸 我担心干旱持续好久水库会缺水 多么美好的一天 又接近末日一点点 我独自坚强稀释寂寞无聊的时间");
            }});
        }
        return items;
    }

    @SuppressLint("CheckResult")
    private void initTopPart() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.back_white);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Uscreen.setToolbarMarginTop(this, toolbar);

        ImageView ivHeader = (ImageView) findViewById(R.id.iv_header);
        TextView tvHeader = findViewById(R.id.tv_header);
        NetWork.getKingsoftwareApi()
                .getDaily("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(daily -> {
                            Glide
                                    .with(ivHeader.getContext().getApplicationContext())
                                    .load(daily.getPicture2())
                                    .centerCrop()
                                    .into(ivHeader);
                            tvHeader.setText(daily.getContent());
                            tvHeader.setTag(daily.getNote());
                            tvHeader.setVisibility(View.VISIBLE);
                        }
                        , Throwable::printStackTrace);

        View fab = findViewById(R.id.fab);
        fab.startAnimation(Uanimation.getRotateAnimation(3600));
        fab.setOnClickListener(view -> {
            Object tag = tvHeader.getTag();
            if (tag instanceof String) {
                String newStr = (String) tag;
                String oldStr = tvHeader.getText().toString();
                tvHeader.setText(newStr);
                tvHeader.setTag(oldStr);
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

    }

    private void initFabBottom(EmptyRecyclerView recyclerView) {
        View fabBottom = findViewById(R.id.fab_bottom);
        fabBottom.setOnClickListener(view -> recyclerView.smoothScrollToPosition(0));
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            int totalScrollRange = appBarLayout1.getTotalScrollRange();
            float offset = verticalOffset * 1.0f / totalScrollRange;
            fabBottom.setScaleX(offset);
            fabBottom.setScaleY(offset);
        });
    }
}
