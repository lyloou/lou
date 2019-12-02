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

import com.google.gson.annotations.SerializedName;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.26 16:23
 * <p>
 * Description:
 */
public class TuPian {


    /**
     * title : 画面太美，我不忍直视！
     * thumburl : http://ww3.sinaimg.cn/large/e4e2bea6jw1ehshykdq20j20ci0bs74n.jpg
     * sourceurl : http://down.laifudao.com/images/tupian/2014626204758.jpg
     * height : 424
     * width : 450
     * class : 1
     * url : http://www.laifudao.com/tupian/33772.htm
     */

    private String title;
    private String thumburl;
    private String sourceurl;
    private String height;
    private String width;
    @SerializedName("class")
    private String classX;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumburl() {
        return thumburl;
    }

    public void setThumburl(String thumburl) {
        this.thumburl = thumburl;
    }

    public String getSourceurl() {
        return sourceurl;
    }

    public void setSourceurl(String sourceurl) {
        this.sourceurl = sourceurl;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getClassX() {
        return classX;
    }

    public void setClassX(String classX) {
        this.classX = classX;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
