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

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.15 14:20
 * <p>
 * Description:
 */
class Util {
    static RecyclerView fillDatas(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);

        MultiTypeAdapter adapterTopic = new MultiTypeAdapter();
        adapterTopic.register(Topic.class, new TopicViewBinder(new TopicViewBinder.TopicItemListener() {
            @Override
            public void onTopicClick(Topic topic) {

            }

        }));
        adapterTopic.setItems(new Items(getTopics()));
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterTopic);
        return recyclerView;
    }


    static List<Topic> getTopics() {
        ArrayList<Topic> topics = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            topics.add(new Topic("我是话题：" + i));
        }
        return topics;
    }
}
