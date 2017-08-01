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


import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.07.13 19:25
 * <p>
 * Description:
 */
public class JsonTest {
    @Test
    public void test() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("a", "aaa");
        jsonObject.addProperty("b", "bbb");
        System.out.println(String.valueOf(jsonObject));
    }

    @Test
    public void testMap() {
        // 拼接网址
        String url = "http://lyloou.com?a=3";
        HashMap<String, String> maps = new HashMap<>();
        maps.put("b", "1");
        maps.put("c", "2");
        System.out.println(maps.toString());
        System.out.println(maps.entrySet());

        for (String s : maps.keySet()) {
            String s1 = maps.get(s);
            url += "&" + s + "=" + s1;
        }
        System.out.println(url);
    }

    @Test
    public void testBus() throws JSONException {


        String busStr = "**YGKJ{\"jsonr\":{\"data\":{\"targetOrder\":35,\"feed\":0,\"depDesc\":\"\",\"ad\":{\"unfoldMonitorLink\":\"\",\"clickMonitorLink\":\"\",\"combpic\":\"http:\\/\\/pic1.chelaile.net.cn\\/adv\\/android35f6970a-8786-4071-b147-43a7b473475d.png\",\"link\":\"https:\\/\\/ad.chelaile.net.cn\\/?link=http%3A%2F%2Fcuxiao.m.suning.com%2Fchannel%2F818kmh.html%3Futm_source%3Dyd-cll%26utm_medium%3Dpf&advId=11575&adtype=05&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f\",\"targetType\":0,\"pic\":\"\",\"type\":1,\"adMode\":30,\"praise\":false,\"openType\":0,\"monitorType\":0,\"provider_id\":\"1\",\"showType\":5,\"idPraiseCount\":0,\"id\":11575},\"buses\":[{\"acBus\":0,\"beforeBaseIndex\":-1,\"beforeLat\":-1.0,\"beforeLng\":-1.0,\"busBaseIndex\":-1,\"busId\":\"BS33822D\",\"delay\":0,\"distanceToSc\":913,\"lat\":22.6777,\"licence\":\"BS33822D\",\"link\":\"\",\"lng\":113.92167,\"mTicket\":0,\"order\":6,\"rType\":0,\"shareId\":\"\",\"state\":0,\"syncTime\":8,\"travels\":[{\"arrivalTime\":1501557692607,\"order\":35,\"pRate\":-1.0,\"travelTime\":3406}]},{\"acBus\":0,\"beforeBaseIndex\":-1,\"beforeLat\":-1.0,\"beforeLng\":-1.0,\"busBaseIndex\":-1,\"busId\":\"BS39889D\",\"delay\":0,\"distanceToSc\":0,\"lat\":22.685432,\"licence\":\"BS39889D\",\"link\":\"\",\"lng\":113.92602,\"mTicket\":0,\"order\":6,\"rType\":0,\"shareId\":\"\",\"state\":1,\"syncTime\":4,\"travels\":[{\"arrivalTime\":1501557579607,\"order\":35,\"pRate\":-1.0,\"travelTime\":3293}]},{\"acBus\":0,\"beforeBaseIndex\":-1,\"beforeLat\":-1.0,\"beforeLng\":-1.0,\"busBaseIndex\":-1,\"busId\":\"BS03621D\",\"delay\":0,\"distanceToSc\":0,\"lat\":22.68475,\"licence\":\"BS03621D\",\"link\":\"\",\"lng\":113.92762,\"mTicket\":0,\"order\":11,\"rType\":0,\"shareId\":\"\",\"state\":1,\"syncTime\":4,\"travels\":[{\"arrivalTime\":1501557187607,\"order\":35,\"pRate\":-1.0,\"travelTime\":2901}]},{\"acBus\":0,\"beforeBaseIndex\":-1,\"beforeLat\":-1.0,\"beforeLng\":-1.0,\"busBaseIndex\":-1,\"busId\":\"BS01781D\",\"delay\":0,\"distanceToSc\":372,\"lat\":22.668633,\"licence\":\"BS01781D\",\"link\":\"\",\"lng\":113.86567,\"mTicket\":0,\"order\":17,\"rType\":0,\"shareId\":\"\",\"state\":0,\"syncTime\":10,\"travels\":[{\"arrivalTime\":1501556518607,\"order\":35,\"pRate\":-1.0,\"travelTime\":2232}]},{\"acBus\":0,\"beforeBaseIndex\":-1,\"beforeLat\":-1.0,\"beforeLng\":-1.0,\"busBaseIndex\":-1,\"busId\":\"BS01793D\",\"delay\":0,\"distanceToSc\":0,\"lat\":22.6552,\"licence\":\"BS01793D\",\"link\":\"\",\"lng\":113.85858,\"mTicket\":0,\"order\":22,\"rType\":0,\"shareId\":\"\",\"state\":1,\"syncTime\":7,\"travels\":[{\"arrivalTime\":1501555856607,\"order\":35,\"pRate\":-1.0,\"travelTime\":1570}]},{\"acBus\":0,\"beforeBaseIndex\":-1,\"beforeLat\":-1.0,\"beforeLng\":-1.0,\"busBaseIndex\":-1,\"busId\":\"BS01440D\",\"delay\":0,\"distanceToSc\":450,\"lat\":22.655518,\"licence\":\"BS01440D\",\"link\":\"\",\"lng\":113.85851,\"mTicket\":0,\"order\":26,\"rType\":0,\"shareId\":\"\",\"state\":0,\"syncTime\":3,\"travels\":[{\"arrivalTime\":1501555462607,\"order\":35,\"pRate\":-1.0,\"travelTime\":1176}]},{\"acBus\":0,\"beforeBaseIndex\":-1,\"beforeLat\":-1.0,\"beforeLng\":-1.0,\"busBaseIndex\":-1,\"busId\":\"粤BBD057\",\"delay\":0,\"distanceToSc\":498,\"lat\":22.616346,\"licence\":\"粤BBD057\",\"link\":\"\",\"lng\":113.85638,\"mTicket\":0,\"order\":27,\"rType\":0,\"shareId\":\"\",\"state\":0,\"syncTime\":13,\"travels\":[{\"arrivalTime\":1501555298607,\"order\":35,\"pRate\":-1.0,\"travelTime\":1012}]},{\"acBus\":0,\"beforeBaseIndex\":-1,\"beforeLat\":-1.0,\"beforeLng\":-1.0,\"busBaseIndex\":-1,\"busId\":\"BS00145D\",\"delay\":0,\"distanceToSc\":570,\"lat\":22.617416,\"licence\":\"BS00145D\",\"link\":\"\",\"lng\":113.85595,\"mTicket\":0,\"order\":29,\"rType\":0,\"shareId\":\"\",\"state\":0,\"syncTime\":2,\"travels\":[{\"arrivalTime\":1501555058607,\"order\":35,\"pRate\":-1.0,\"travelTime\":772}]},{\"acBus\":0,\"beforeBaseIndex\":-1,\"beforeLat\":-1.0,\"beforeLng\":-1.0,\"busBaseIndex\":-1,\"busId\":\"BS06862D\",\"delay\":0,\"distanceToSc\":245,\"lat\":22.604517,\"licence\":\"BS06862D\",\"link\":\"\",\"lng\":113.866486,\"mTicket\":0,\"order\":38,\"rType\":0,\"shareId\":\"\",\"state\":0,\"syncTime\":6,\"travels\":[]},{\"acBus\":0,\"beforeBaseIndex\":-1,\"beforeLat\":-1.0,\"beforeLng\":-1.0,\"busBaseIndex\":-1,\"busId\":\"粤BBD120\",\"delay\":0,\"distanceToSc\":681,\"lat\":22.581884,\"licence\":\"粤BBD120\",\"link\":\"\",\"lng\":113.87699,\"mTicket\":0,\"order\":39,\"rType\":0,\"shareId\":\"\",\"state\":0,\"syncTime\":6,\"travels\":[]},{\"acBus\":0,\"beforeBaseIndex\":-1,\"beforeLat\":-1.0,\"beforeLng\":-1.0,\"busBaseIndex\":-1,\"busId\":\"粤BBD071\",\"delay\":0,\"distanceToSc\":154,\"lat\":22.54144,\"licence\":\"粤BBD071\",\"link\":\"\",\"lng\":113.94063,\"mTicket\":0,\"order\":46,\"rType\":0,\"shareId\":\"\",\"state\":0,\"syncTime\":6,\"travels\":[]},{\"acBus\":0,\"beforeBaseIndex\":-1,\"beforeLat\":-1.0,\"beforeLng\":-1.0,\"busBaseIndex\":-1,\"busId\":\"粤BBD148\",\"delay\":0,\"distanceToSc\":966,\"lat\":22.534174,\"licence\":\"粤BBD148\",\"link\":\"\",\"lng\":113.96036,\"mTicket\":0,\"order\":48,\"rType\":0,\"shareId\":\"\",\"state\":0,\"syncTime\":6,\"travels\":[]}],\"line\":{\"desc\":\"\",\"lineId\":\"0755-M3553-0\",\"name\":null,\"shortDesc\":\"\",\"state\":0,\"type\":0},\"roads\":[[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":0.57,\"TVL\":2},{\"TPC\":0.43000000000000005,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":0.8,\"TVL\":1},{\"TPC\":0.19999999999999996,\"TVL\":2}],[{\"TPC\":0.54,\"TVL\":2},{\"TPC\":0.18,\"TVL\":1},{\"TPC\":0.27999999999999997,\"TVL\":2}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":0.88,\"TVL\":2},{\"TPC\":0.12,\"TVL\":1}],[{\"TPC\":0.57,\"TVL\":1},{\"TPC\":0.43000000000000005,\"TVL\":2}],[{\"TPC\":0.62,\"TVL\":2},{\"TPC\":0.38,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":0.62,\"TVL\":1},{\"TPC\":0.38,\"TVL\":2}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":0.14,\"TVL\":2},{\"TPC\":0.86,\"TVL\":3}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":0.85,\"TVL\":2},{\"TPC\":0.15000000000000002,\"TVL\":1}],[{\"TPC\":0.6,\"TVL\":2},{\"TPC\":0.13,\"TVL\":1},{\"TPC\":0.27,\"TVL\":2}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":0.32,\"TVL\":2},{\"TPC\":0.6799999999999999,\"TVL\":3}],[{\"TPC\":0.42,\"TVL\":3},{\"TPC\":0.5800000000000001,\"TVL\":2}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":0.86,\"TVL\":1},{\"TPC\":0.14,\"TVL\":2}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":1,\"TVL\":1}],[{\"TPC\":1,\"TVL\":2}],[{\"TPC\":1,\"TVL\":1}]],\"depTable\":0,\"fav\":0,\"notify\":0},\"errmsg\":null,\"status\":\"00\",\"sversion\":null}}YGKJ##";

        String busJsonStr = busStr.substring(6, busStr.length() - 6);
        System.out.println(busJsonStr);
    }

}
