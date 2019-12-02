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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
class GirlAdapter extends RecyclerView.Adapter<GirlAdapter.GirlViewHolder> {
    private final List<GirlResult.Data.Girl> mList;
    private OnItemGirlClickListener mOnItemGirlClickListener;

    public GirlAdapter() {
        mList = new ArrayList<>();
    }

    public void addItems(List<GirlResult.Data.Girl> girls) {
        int oldLength = mList.size();
        mList.addAll(girls);
        notifyItemRangeInserted(oldLength, girls.size());
    }

    @Override
    public GirlViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mxnzp_girl, null);

        return new GirlViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GirlViewHolder viewHolder, int position) {
        GirlResult.Data.Girl girl = mList.get(position);

        ImageView ivPoster = viewHolder.iv;
        Glide.with(ivPoster.getContext())
                .load(girl.getImageUrl())
                .into(ivPoster);
        viewHolder.view.setOnClickListener(v -> {
            if (mOnItemGirlClickListener != null) {
                mOnItemGirlClickListener.onClick(girl);
            }
        });
        viewHolder.view.setOnLongClickListener(v -> {
            if (mOnItemGirlClickListener != null) {
                mOnItemGirlClickListener.onLongClick(girl);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setOnItemGirlClickListener(OnItemGirlClickListener onItemGirlClickListener) {
        mOnItemGirlClickListener = onItemGirlClickListener;
    }

    interface OnItemGirlClickListener {
        void onClick(GirlResult.Data.Girl girl);

        void onLongClick(GirlResult.Data.Girl girl);
    }

    class GirlViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv;
        private final View view;

        public GirlViewHolder(View view) {
            super(view);
            this.view = view;
            iv = view.findViewById(R.id.iv);
        }
    }
}
