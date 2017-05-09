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

package com.lyloou.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.05.09 14:18
 * <p>
 * Description:
 */
public class Hi {
    public static void main(String[] args) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        String strDate = "2017-05-09T14:28:32.974Z";
        try {
            Date inputDate = inputFormat.parse(strDate);
            String outputDate = outputFormat.format(inputDate);

            System.out.println(outputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
