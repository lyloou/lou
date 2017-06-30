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

package com.lyloou.test.douban;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;

import java.util.ArrayList;
import java.util.List;

class SubjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private List<Subject> mList;
    private boolean mMaxed;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private String mTitle;

    SubjectAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.item_douban, parent, false);
            return new SubjectHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_douban_header, parent, false);
            return new HeaderHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.item_douban_footer, parent, false);
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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof SubjectHolder) {
            SubjectHolder holder = (SubjectHolder) viewHolder;
            Subject subject = mList.get(position - 1); // 注意需要减去header的数量
            holder.tvTitle.setText(subject.getTitle());
            String content = subject.getYear();
            List<Subject.CastsBean> casts = subject.getCasts();
            if (casts.size() >= 1) {
                content += "/" + casts.get(0).getName();
            }
            holder.tvSecond.setText(content);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onClick(subject);
                }
            });

            Subject.ImagesBean images = subject.getImages();
            if (images != null) {
                String small = images.getSmall();
                if (!TextUtils.isEmpty(small)) {
                    Glide.with(mContext)
                            .load(small)
                            .placeholder(R.mipmap.ic_launcher)
                            .centerCrop()
                            .crossFade()
                            .into(holder.ivThumb);
                }
            }
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

    public void addAll(List<Subject> subjects) {
        mList.addAll(subjects);
        notifyDataSetChanged();
    }

    public int getListSize() {
        return mList.size();
    }

    public int getItemTypeCount() {
        return 3;
    }

    interface OnItemClickListener {
        void onClick(Subject subject);
    }

    private static class SubjectHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvTitle;
        TextView tvSecond;
        ImageView ivThumb;

        SubjectHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvSecond = (TextView) view.findViewById(R.id.tv_second);
            ivThumb = (ImageView) view.findViewById(R.id.iv_thumb);
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
            tvFooter = (TextView) view.findViewById(R.id.tv_footer);
        }
    }
}