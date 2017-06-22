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

package com.lyloou.test.laifudao;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyloou.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.20 17:31
 * <p>
 * Description:
 */
class XiaoHuaAdapter extends RecyclerView.Adapter {
    private final List<XiaoHua> mList;
    private OnItemClickListener mOnItemClickListener;

    public XiaoHuaAdapter() {
        mList = new ArrayList<>();
    }

    public void addItem(XiaoHua xiaoHua) {
        mList.add(xiaoHua);
    }

    public void addItems(List<XiaoHua> xiaoHuas) {
        mList.addAll(xiaoHuas);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xiao_hua, null);
        return new XiaoHuaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof XiaoHuaViewHolder) {
            XiaoHuaViewHolder viewHolder = (XiaoHuaViewHolder) holder;
            XiaoHua xiaoHua = mList.get(position);

            viewHolder.tvTitle.setText(xiaoHua.getTitle());
            viewHolder.tvContent.setText(Html.fromHtml(xiaoHua.getContent()));
            viewHolder.view.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(xiaoHua.getUrl());
                }
            });
            viewHolder.view.setOnLongClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onLongClick(viewHolder.tvContent.getText().toString());
                }
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    interface OnItemClickListener {
        void onClick(String url);

        void onLongClick(String content);
    }

    private class XiaoHuaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvContent;
        private final View view;

        public XiaoHuaViewHolder(View view) {
            super(view);
            this.view = view;
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
        }
    }
}
