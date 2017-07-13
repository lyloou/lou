package com.lou.as.lou.practice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.lou.as.lou.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewRecyclerViewActivity extends AppCompatActivity {

    @BindView(R.id.practice_rv)
    RecyclerView rvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recycler_view);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
//        rvMain.setLayoutManager(new GridLayoutManager(rvMain.getContext(), 3));
        rvMain.setLayoutManager(new LinearLayoutManager(rvMain.getContext()));
//        rvMain.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvMain.setAdapter(new MyAdapter(
                new ArrayList<>(Arrays.asList(
                        "0",
                        "1",
                        "22",
                        "333",
                        "4444",
                        "55555",
                        "666666",
                        "7777777",
                        "88888888",
                        "999999999"
                ))

        ));
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private static final String TAG = "MyAdapter";
        final List<String> mList;

        public MyAdapter(List<String> list) {
            mList = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_practice_view_recycler_view, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.tv.setText(mList.get(position));

            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    final View view = holder.itemView;
                    final int initHeight = view.getMeasuredHeight();
                    Animation anim = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime,
                                                           Transformation t) {
                            if (interpolatedTime == 1) {
                                // 高度为0时删除元素，并更新 adapter
                                notifyDataSetChanged();
                            } else {
                                // 不断的改变高度，直到高度为0；
                                view.getLayoutParams().height = initHeight
                                        - (int) (initHeight * interpolatedTime);
                                view.requestLayout();
                            }
                        }
                    };
                    anim.setDuration(300);
                    view.startAnimation(anim);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.practice_view_recycler_view_item_tv)
            public TextView tv;
            public View itemView;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;

                ButterKnife.bind(this, itemView);
            }
        }
    }
}
