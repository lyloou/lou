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

package com.lyloou.collect;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ListSortSearch {
    static List<String> LIST = Arrays.asList("one Two three Four five Six seven Eight".split(" "));

    public static void main(String[] args) throws IOException {
        List<String> list2 = new ArrayList<>(LIST);
        Collections.shuffle(list2, new Random(47));
        System.out.println(list2);

        List<String> list3 = new ArrayList<>(LIST);
        Collections.shuffle(list3, new Random(46));
        System.out.println(list3);

        URL url = new URL("http://lyloou.com");
        URLConnection uc = url.openConnection();// 生成连接对象
        uc.connect();// 发出连接
        long webTimeMillis = uc.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String formatWebTime = simpleDateFormat.format(new Date(webTimeMillis));
        System.out.println(formatWebTime);
    }
}
