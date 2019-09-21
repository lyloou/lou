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

package com.lyloou.test.bus;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lyloou.test.R;
import com.lyloou.test.util.Uscreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class BusTvActivity extends AppCompatActivity {

    @SuppressWarnings("unchecked")
    private static final Map<String, String> map = new LinkedHashMap() {{
        put("323  西乡盐田大门-裕安路口",
                "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&modelVersion=0.0.8&last_src=app_360_sj&s=android&stats_referer=nearby&push_open=1&stats_act=switch_stn&userId=unknown&geo_lt=4&lorder=1&geo_lat=22.575608&vc=88&sv=5.1&v=3.39.0&targetOrder=6&gpstype=gcj&imei=866808025006643&lineId=0755-03230-1&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=lL2IimUqQSn8Vj2GdouR4A%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-1&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.867149&geo_lac=35.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043610+Safari%2F537.36&lat=22.575608&beforAds=&geo_lng=113.867149"
        );
        put("M400  西乡盐田大门-金成名苑",
                "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&last_src=app_360_sj&s=android&stats_referer=searchResult&push_open=1&stats_act=switch_stn&userId=unknown&geo_lt=4&geo_lat=22.575624&vc=88&sv=5.1&v=3.39.0&targetOrder=37&gpstype=gcj&imei=866808025006643&lineId=0755-M4003-0&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=KJal%2Bc5LEI8kmS1Gz%2BekwA%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-1&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.867222&flpolicy=0&geo_lac=35.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043610+Safari%2F537.36&lat=22.575624&geo_lng=113.867222"
        );
        put("M433  海滨广场-同仁妇科医院",
                "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&last_src=app_360_sj&s=android&stats_referer=searchResult&push_open=1&stats_act=switch_stn&userId=unknown&geo_lt=4&geo_lat=22.575506&vc=88&sv=5.1&v=3.39.0&targetOrder=28&gpstype=gcj&imei=866808025006643&lineId=0755-M4333-0&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=13sMEf8ghnrxSb2OlI3WrQ%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-1&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.867054&flpolicy=0&geo_lac=38.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043610+Safari%2F537.36&lat=22.575506&geo_lng=113.867054"
        );
        put("M380  海滨广场-同仁妇科医院",
                "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&last_src=app_360_sj&s=android&stats_referer=searchResult&push_open=1&stats_act=switch_stn&userId=unknown&geo_lt=4&geo_lat=22.575506&vc=88&sv=5.1&v=3.39.0&targetOrder=10&gpstype=gcj&imei=866808025006643&lineId=0755-M3803-0&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=BDs8W%2FbGxrr3aXeIDU29ag%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-1&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.867054&flpolicy=0&geo_lac=38.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043610+Safari%2F537.36&lat=22.575506&geo_lng=113.867054"
        );
        put("高峰30  海滨广场-同仁妇科医院",
                "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&last_src=app_360_sj&s=android&stats_referer=searchResult&push_open=1&stats_act=switch_stn&userId=unknown&geo_lt=4&geo_lat=22.575545&vc=88&sv=5.1&v=3.39.0&targetOrder=15&gpstype=gcj&imei=866808025006643&lineId=0755-00305-0&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=MLGcNMhmwuYUpQNioGkuwg%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-1&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.867094&flpolicy=0&geo_lac=38.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043610+Safari%2F537.36&lat=22.575545&geo_lng=113.867094"
        );
        put("610  海滨广场-同仁妇科医院",
                "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&last_src=app_360_sj&s=android&stats_referer=transfer&push_open=1&stats_act=switch_stn&userId=unknown&geo_lt=4&geo_lat=22.575512&vc=88&sv=5.1&v=3.39.0&targetOrder=15&gpstype=gcj&imei=866808025006643&lineId=0755-06100-0&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=UN0ylhrjKfgS%2B8Ewz0ZFXQ%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.86706&flpolicy=0&geo_lac=38.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043610+Safari%2F537.36&lat=22.575512&geo_lng=113.86706"
        );
        put("M355  裕安路口-科技园",
                "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&modelVersion=0.0.8&last_src=app_meizhu_store&s=android&stats_referer=nearby&push_open=1&stats_act=auto_refresh&userId=unknown&geo_lt=4&lorder=1&geo_lat=22.544675&vc=83&sv=5.1&v=3.34.2&targetOrder=35&gpstype=gcj&imei=866808025006643&lineId=0755-M3553-0&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=Bv52Ecvs79wuuZVTTRbzNQ%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-1&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.947299&geo_lac=35.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043305+Safari%2F537.36&lat=22.544675&beforAds=&geo_lng=113.947299"
        );
        put("观光1  裕安路口-科技园",
                "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&last_src=app_meizhu_store&s=android&stats_referer=searchResult&push_open=1&stats_act=switch_stn&userId=unknown&geo_lt=4&geo_lat=22.544674&vc=83&sv=5.1&v=3.34.2&targetOrder=6&gpstype=gcj&imei=866808025006643&lineId=0755-0001B-0&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=mQalYbka%2BvjBXd9twBkaJA%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-3&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.947297&flpolicy=0&geo_lac=29.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043305+Safari%2F537.36&lat=22.544674&geo_lng=113.947297"
        );
    }};


    Activity mContext;
    OkHttpClient mOkHttpClient = new OkHttpClient();
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_bustv);
        initView();
    }

    private void initView() {
        LinearLayout ll = findViewById(R.id.llyt_bustv);

        int space = Uscreen.dp2Px(mContext, 24);
        int sep = Uscreen.dp2Px(mContext, 16);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams sepParams = new LinearLayout.LayoutParams(MATCH_PARENT, sep);
        for (String s : map.keySet()) {

            TextView tv = new TextView(mContext);
            tv.setLayoutParams(layoutParams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv.setElevation(4);
            }
            tv.setPadding(space, space, space, space);
            tv.setBackgroundResource(R.drawable.ripple_gray_radius_2);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setText(s);
            tv.setTextSize(16);
            tv.setTextColor(getResources().getColor(android.R.color.black));
            tv.setOnClickListener(v -> whereIsBus(s, tv));
            ll.addView(tv);

            View view = new View(mContext);
            view.setLayoutParams(sepParams);
            ll.addView(view);
        }
    }


    private void whereIsBus(String key, TextView tv) {

        Request request = new Request
                .Builder()
                .url(map.get(key))
                .build();
        Call call = mOkHttpClient.newCall(request);

        mCompositeDisposable.add(Flowable
                .fromCallable(() -> {
                    ResponseBody body = call.execute().body();
                    if (body != null) {
                        return body.string();
                    }
                    return "";
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(str -> {
                    if (str.length() <= 12) {
                        return "";
                    }
                    return str.substring(6, str.length() - 6);
                })
                .map(s -> {
                    List<Bus> busList = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.has("jsonr")) {
                            JSONObject jsonr = jsonObject.getJSONObject("jsonr");
                            if (jsonr.has("data")) {
                                JSONObject data = jsonr.getJSONObject("data");
                                if (data.has("buses")) {
                                    JSONArray buses = data.getJSONArray("buses");
                                    Gson gson = new GsonBuilder().setLenient().create();
                                    for (int i = 0; i < buses.length(); i++) {
                                        Object o = buses.get(i);
                                        String str = String.valueOf(o);
                                        Bus bus = gson.fromJson(str, Bus.class);
                                        if (bus.getTravels().size() > 0) {
                                            busList.add(bus);
                                        }
                                        System.out.println(str);
                                    }
                                }
                            }
                        }
                        if (busList.size() > 1) {
                            Collections.reverse(busList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return busList;
                })
                .subscribe(buses -> {
                    String busss = Arrays.toString(buses.toArray());
                    System.out.println(busss);
                    StringBuilder sb = new StringBuilder(key);
                    for (Bus bus : buses) {
                        sb.append("\n  ").append(bus);
                    }
                    tv.setText(sb);
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));

                }, throwable -> Toast.makeText(mContext, "异常了" + throwable.getMessage(), Toast.LENGTH_SHORT).show()));
    }

    private void dispose() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        dispose();

        super.onDestroy();
    }
}
