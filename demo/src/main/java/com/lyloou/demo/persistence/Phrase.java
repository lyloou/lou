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

package com.lyloou.demo.persistence;

import java.util.UUID;

public class Phrase {
    private String mId; // 短语的唯一标识
    private String mContent; // 短语的内容
    private int mFavorite; // 是否是收藏状态：0表示未收藏，1表示已收藏；

    public Phrase(String content) {
        this(UUID.randomUUID().toString(), content, 0);
    }

    public Phrase(String content, int favorite) {
        this(UUID.randomUUID().toString(), content, favorite);
    }

    public Phrase(String id, String content, int favorite) {
        mId = id;
        mContent = content;
        mFavorite = favorite;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getFavorite() {
        return mFavorite;
    }

    public void setFavorite(int favorite) {
        mFavorite = favorite;
    }

    @Override
    public String toString() {
        return "\n Phrase id: " + mId
                + " content: " + mContent
                + " favorite: " + mFavorite;
    }
}