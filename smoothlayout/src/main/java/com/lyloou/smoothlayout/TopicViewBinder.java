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

package com.lyloou.smoothlayout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.05.22 17:38
 * <p>
 * Description:
 */
public class TopicViewBinder extends ItemViewBinder<Topic, TopicViewBinder.ViewHolder> {
    private final TopicItemListener mTopicItemListener;

    public TopicViewBinder(TopicItemListener topicItemListener) {
        mTopicItemListener = topicItemListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Topic topic) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTopicItemListener.onTopicClick(topic);
            }
        });
    }

    interface TopicItemListener {
        void onTopicClick(Topic topic);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        ViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }
}
