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

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.lyloou.smoothlayout.R.id.toolbar;

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

    public static void setToolbarMarginTop(Activity activity, Toolbar toolbar){
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
        params.topMargin = getStatusBarHeight(activity);
    }

    public static int getStatusBarHeight(Activity activity){
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        return statusBarHeight;
    }
}
