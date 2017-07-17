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

package com.lyloou.demo.persistence.sqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lyloou.demo.R;

import java.util.Arrays;
import java.util.List;

public class LouSqliteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lousqlite);
        initData();
    }

    private void initData() {
        // 初始化
        LouSQLite.init(this.getApplicationContext(), new MyCallBack());

        // 清空之前的数据
        LouSQLite.deleteFrom(MyCallBack.TABLE_NAME_PHRASE);
        query();

        // 插入一个数据到数据库
        Phrase phrase = new Phrase("青青子衿，悠悠我心");
        LouSQLite.insert(MyCallBack.TABLE_NAME_PHRASE, phrase);
        query();

        // 插入一组数据
        Phrase phrase1 = new Phrase("窈窕淑女，君子好逑");
        Phrase phrase2 = new Phrase("海上生明月，天涯共此时");
        Phrase phrase3 = new Phrase("青青子衿，悠悠我心");
        Phrase phrase4 = new Phrase("人生若只如初见");
        List<Phrase> lists = Arrays.asList(
                phrase1
                , phrase2
                , phrase3
                , phrase4
        );
        LouSQLite.insert(MyCallBack.TABLE_NAME_PHRASE, lists);
        query();

        // 更新一个数据
        phrase.setContent(phrase.getContent() + " 嘿嘿嘿");
        LouSQLite.update(MyCallBack.TABLE_NAME_PHRASE
                , phrase
                , PhraseEntry.COLEUM_NAME_ID + "=?"
                , new String[]{phrase.getId()});
        query();

        // 更新一组数据
        LouSQLite.execSQL("update " + MyCallBack.TABLE_NAME_PHRASE
                + " set " + PhraseEntry.COLEUM_NAME_FAVORITE + "=1 "
                + "where " + PhraseEntry.COLEUM_NAME_FAVORITE + "=0");
        query();

        // 删除一个数据
        LouSQLite.delete(MyCallBack.TABLE_NAME_PHRASE
                , PhraseEntry.COLEUM_NAME_ID + "=?"
                , new String[]{phrase.getId()});
        query();

    }

    private void query() {
        List<Phrase> lists = LouSQLite.query(MyCallBack.TABLE_NAME_PHRASE
                , "select * from " + MyCallBack.TABLE_NAME_PHRASE
                , null);
        System.out.println(Arrays.toString(lists.toArray()));
    }
}
