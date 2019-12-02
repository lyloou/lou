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
class JokeAdapter extends RecyclerView.Adapter {
    private final List<JokeResult.Data.Joke> mList;

    public JokeAdapter() {
        mList = new ArrayList<>();
    }

    public void addItems(List<JokeResult.Data.Joke> jokes) {
        int oldLength = mList.size();
        mList.addAll(jokes);
        notifyItemRangeInserted(oldLength, mList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mxnzp_joke, null);
        return new JokeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof JokeViewHolder) {
            JokeViewHolder viewHolder = (JokeViewHolder) holder;
            JokeResult.Data.Joke joke = mList.get(position);

            viewHolder.tvContent.setText(handleContent(joke));
            viewHolder.tvTitle.setText(joke.getUpdateTime());

        }
    }

    private String handleContent(JokeResult.Data.Joke joke) {
        String content = joke.getContent();
        content = content.replaceAll("&nbsp;", " ");
        content = content.replaceAll("\n", "\n\n");
        content = content.trim();
        return content;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    private class JokeViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvContent;

        public JokeViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_one);
            tvContent = view.findViewById(R.id.tv_content);
        }
    }
}
