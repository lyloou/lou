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

package com.lyloou.test.flow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyloou.test.R;

import java.util.ArrayList;
import java.util.List;

class FlowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FlowItem> mList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    FlowAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_flow, parent, false);
        return new FlowHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FlowHolder holder = (FlowHolder) viewHolder;
        FlowItem item = mList.get(position);
        holder.tvTime.setText(item.getTime());
        holder.tvSpend.setText(item.getSpend());
        holder.tvContent.setText(item.getContent());
        holder.view.setOnClickListener(view -> {
            if (mItemClickListener != null) {
                mItemClickListener.onClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListSize();
    }

    public void clearAll() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<FlowItem> subjects) {
        mList.addAll(subjects);
        notifyDataSetChanged();
    }

    public int getListSize() {
        return mList.size();
    }

    interface OnItemClickListener {
        void onClick(FlowItem subject);
    }

    private static class FlowHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvTime;
        TextView tvSpend;
        TextView tvContent;

        FlowHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvTime = view.findViewById(R.id.tv_time);
            tvSpend = view.findViewById(R.id.tv_spend);
            tvContent = view.findViewById(R.id.tv_content);
        }
    }
}