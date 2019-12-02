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

package com.lyloou.test.github;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
class RepositoryAdapter extends RecyclerView.Adapter {
    private final List<Repository> mList;

    public RepositoryAdapter() {
        mList = new ArrayList<>();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<Repository> repositories) {
        int oldLength = mList.size();
        mList.addAll(repositories);
        notifyItemRangeInserted(oldLength, mList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_github_repository, null);
        return new JokeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof JokeViewHolder) {
            JokeViewHolder viewHolder = (JokeViewHolder) holder;
            Repository repository = mList.get(position);
            Glide.with(viewHolder.ivThumb.getContext()).load(repository.getAvatar()).into(viewHolder.ivThumb);
            viewHolder.tvOne.setText(repository.getName());
            viewHolder.tvSecond.setText(handleContent(repository));

        }
    }

    private String handleContent(Repository joke) {
        String content = joke.getAuthor();
        return content;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    private class JokeViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvOne;
        private final TextView tvSecond;
        private final ImageView ivThumb;

        public JokeViewHolder(View view) {
            super(view);
            tvOne = view.findViewById(R.id.tv_one);
            tvSecond = view.findViewById(R.id.tv_second);
            ivThumb = view.findViewById(R.id.iv_thumb);
        }
    }
}
