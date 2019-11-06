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

package com.lyloou.test.gank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.lyloou.test.R;
import com.lyloou.test.util.Utime;

import java.util.List;

class WelfareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private List<Welfare> mList;
    private boolean mMaxed;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private String mTitle;
    private int mMode; // 0:表示正常模式；1：表示多选模式；

    WelfareAdapter(Context context, List<Welfare> list) {
        mContext = context;
        mList = list;
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        mMode = mode;
    }

    public List<Welfare> getList() {
        return mList;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void clearSelected() {
        for (Welfare activeDay : mList) {
            if (activeDay.isSelected())
                activeDay.setSelected(false);
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.item_gank_welfare, parent, false);
            return new WelfareHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_gank_header, parent, false);
            return new HeaderHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.item_gank_footer, parent, false);
            return new FooterHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " make sure you using types correctly.");
    }

    public boolean isMaxed() {
        return mMaxed;
    }

    public void setMaxed(boolean maxed) {
        mMaxed = maxed;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof WelfareHolder) {
            WelfareHolder holder = (WelfareHolder) viewHolder;

            Welfare welfare = mList.get(position - 1); // 注意需要减去header的数量
            if (mMode == 0) {
                holder.cbItem.setVisibility(View.GONE);
            } else if (mMode == 1) {
                holder.cbItem.setVisibility(View.VISIBLE);
                holder.cbItem.setChecked(welfare.isSelected());
            }

            holder.tvItem.setText(getDay(welfare));
            holder.view.setOnClickListener(view -> mItemClickListener.onClick(holder.getAdapterPosition(), welfare));
            holder.view.setOnLongClickListener(view -> mItemClickListener.onLongClick(welfare));
            Glide.with(mContext)
                    .load(welfare.getUrl())
                    .signature(new StringSignature(welfare.getUrl()))
                    .fitCenter()
                    .into(holder.ivItem);

        } else if (viewHolder instanceof HeaderHolder) {
            HeaderHolder holder = (HeaderHolder) viewHolder;
            if (!TextUtils.isEmpty(mTitle)) {
                holder.tvHeader.setText(mTitle);
            }
        } else if (viewHolder instanceof FooterHolder) {
            FooterHolder holder = (FooterHolder) viewHolder;
            if (mMaxed) {
                holder.tvFooter.setText("----- 我是有底线的 -----");
            } else {
                holder.tvFooter.setText("加载中...");
            }
        }

    }

    private String getDay(Welfare welfare) {
        return Utime.transferThreeToOne(welfare.getPublishedAt());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        if (position > getListSize()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return getListSize() + 2;
    }

    public void clearAll() {
        mList.clear();
        notifyDataSetChanged();
    }

    public int getListSize() {
        return mList.size();
    }

    public int getItemTypeCount() {
        return 3;
    }

    interface OnItemClickListener {
        void onClick(int realPosition, Welfare welfare);

        boolean onLongClick(Welfare welfare);
    }

    private static class WelfareHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvItem;
        ImageView ivItem;
        CheckBox cbItem;

        WelfareHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvItem = view.findViewById(R.id.tv_item);
            ivItem = view.findViewById(R.id.iv_item);
            cbItem = view.findViewById(R.id.cb_item);
            cbItem.setClickable(false);
        }
    }

    private static class HeaderHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;

        HeaderHolder(View view) {
            super(view);
            tvHeader = view.findViewById(R.id.tv_header);
        }
    }

    private static class FooterHolder extends RecyclerView.ViewHolder {
        TextView tvFooter;

        FooterHolder(View view) {
            super(view);
            tvFooter = view.findViewById(R.id.tv_footer);
        }
    }
}