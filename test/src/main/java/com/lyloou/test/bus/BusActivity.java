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
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lyloou.test.R;
import com.lyloou.test.common.LouAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class BusActivity extends AppCompatActivity {

    private static final String URL_GG1 = "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&last_src=app_meizhu_store&s=android&stats_referer=searchResult&push_open=1&stats_act=switch_stn&userId=unknown&geo_lt=4&geo_lat=22.544674&vc=83&sv=5.1&v=3.34.2&targetOrder=6&gpstype=gcj&imei=866808025006643&lineId=0755-0001B-0&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=mQalYbka%2BvjBXd9twBkaJA%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-3&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.947297&flpolicy=0&geo_lac=29.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043305+Safari%2F537.36&lat=22.544674&geo_lng=113.947297";
    private static final String URL_M355 = "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&modelVersion=0.0.8&last_src=app_meizhu_store&s=android&stats_referer=nearby&push_open=1&stats_act=auto_refresh&userId=unknown&geo_lt=4&lorder=1&geo_lat=22.544675&vc=83&sv=5.1&v=3.34.2&targetOrder=35&gpstype=gcj&imei=866808025006643&lineId=0755-M3553-0&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=Bv52Ecvs79wuuZVTTRbzNQ%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-1&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.947299&geo_lac=35.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043305+Safari%2F537.36&lat=22.544675&beforAds=&geo_lng=113.947299";
    Activity mContext;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    LouAdapter<Bus> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(initListView());

        whereIsBus(URL_M355);
        setTitle("M355 裕安路口-科技园");
    }

    private View initListView() {
        adapter = new LouAdapter<Bus>(new ListView(mContext), android.R.layout.simple_list_item_1) {
            @Override
            protected void assign(ViewHolder holder, Bus s) {
                // 用视图显示数据
                holder.putText(android.R.id.text1, String.valueOf(s));
            }
        };

        return adapter.getBindView();
    }

    private void whereIsBus(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request
                .Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        Disposable subscribe = Flowable
                .fromCallable(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        ResponseBody body = call.execute().body();
                        if (body != null) {
                            return body.string();
                        }
                        return "";
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String str) throws Exception {
                        if (str.length() <= 12) {
                            return "";
                        }
                        return str.substring(6, str.length() - 6);
                    }
                })
                .map(new Function<String, List<Bus>>() {
                    @Override
                    public List<Bus> apply(@NonNull String s) throws Exception {
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
                    }
                })
                .subscribe(new Consumer<List<Bus>>() {
                    @Override
                    public void accept(@NonNull List<Bus> buses) throws Exception {
                        String busss = Arrays.toString(buses.toArray());
                        System.out.println(busss);
                        if (adapter != null) {
                            adapter.initList(buses);
                        }
                    }
                });
        mCompositeDisposable.add(subscribe);
    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bus_m355:
                whereIsBus(URL_M355);
                setTitle("M355  裕安路口-科技园");
                break;
            case R.id.menu_bus_gg1:
                whereIsBus(URL_GG1);
                setTitle("观光1  裕安路口-科技园");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTitle(String m355) {
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar) {
            supportActionBar.setTitle(m355);
        }
    }
}
