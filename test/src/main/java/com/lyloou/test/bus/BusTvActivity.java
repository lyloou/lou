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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.test.R;
import com.lyloou.test.common.EmptyRecyclerView;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.util.Ugson;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Usp;
import com.lyloou.test.util.http.Uthread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class BusTvActivity extends AppCompatActivity {

    public static final String PARAM_DATA = "PARAM_DATA";


    List<BusParam> mList;

    Activity mContext;
    OkHttpClient mOkHttpClient = new OkHttpClient();
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    BusAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setMyTitle("车来了");
        setContentView(R.layout.activity_bustv);

        initData();
        initView();
        reloadAllData();
    }

    private void initData() {
        mList = getList();
    }

    @NonNull
    private List<BusParam> getList() {
        List<BusParam> list = null;
        String paramData = Usp.init(this).getString(PARAM_DATA, null);
        if (!TextUtils.isEmpty(paramData)) {
            list = Ugson.getList(paramData);
        }

        // 如果取不到，就取几个默认的
        if (list == null || list.size() == 0) {
            list = new ArrayList<>();
            list.add(new BusParam("上班-m395", "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&modelVersion=0.0.8&last_src=app_360_sj&s=android&stats_referer=nearby&push_open=1&stats_act=switch_stn&userId=unknown&geo_lt=4&lorder=1&geo_lat=22.575608&vc=88&sv=5.1&v=3.39.0&targetOrder=6&gpstype=gcj&imei=866808025006643&lineId=0755-03230-1&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=lL2IimUqQSn8Vj2GdouR4A%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-1&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.867149&geo_lac=35.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043610+Safari%2F537.36&lat=22.575608&beforAds=&geo_lng=113.867149"));
            list.add(new BusParam("上班-m378", "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&last_src=app_360_sj&s=android&stats_referer=searchResult&push_open=1&stats_act=switch_stn&userId=unknown&geo_lt=4&geo_lat=22.575624&vc=88&sv=5.1&v=3.39.0&targetOrder=37&gpstype=gcj&imei=866808025006643&lineId=0755-M4003-0&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=KJal%2Bc5LEI8kmS1Gz%2BekwA%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-1&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.867222&flpolicy=0&geo_lac=35.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043610+Safari%2F537.36&lat=22.575624&geo_lng=113.867222"));
            list.add(new BusParam("回家-m395", "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&last_src=app_360_sj&s=android&stats_referer=searchResult&push_open=1&stats_act=switch_stn&userId=unknown&geo_lt=4&geo_lat=22.575624&vc=88&sv=5.1&v=3.39.0&targetOrder=37&gpstype=gcj&imei=866808025006643&lineId=0755-M4003-0&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=KJal%2Bc5LEI8kmS1Gz%2BekwA%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-1&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.867222&flpolicy=0&geo_lac=35.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043610+Safari%2F537.36&lat=22.575624&geo_lng=113.867222"));
            list.add(new BusParam("回家-m378", "https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&last_src=app_360_sj&s=android&stats_referer=searchResult&push_open=1&stats_act=switch_stn&userId=unknown&geo_lt=4&geo_lat=22.575624&vc=88&sv=5.1&v=3.39.0&targetOrder=37&gpstype=gcj&imei=866808025006643&lineId=0755-M4003-0&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=KJal%2Bc5LEI8kmS1Gz%2BekwA%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-1&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.867222&flpolicy=0&geo_lac=35.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043610+Safari%2F537.36&lat=22.575624&geo_lng=113.867222"));
        }

        return list;
    }

    private void setMyTitle(String title) {
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bus_tv, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bus_add:
                addBus();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("InflateParams")
    public void addBus() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_bus_tv_add, null);

        new AlertDialog.Builder(this)
                .setTitle("添加更多公交")
                .setView(view)
                .setPositiveButton("是的", (dialog, whichButton) -> {
                    EditText etName = view.findViewById(R.id.et_name);
                    EditText etAddress = view.findViewById(R.id.et_address);

                    String name = etName.getText().toString();
                    String address = etAddress.getText().toString();
                    mList.add(0, new BusParam(name, address));
                })
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initView() {
        EmptyRecyclerView erv = findViewById(R.id.erv_bus);
        erv.setLayoutManager(new LinearLayoutManager(this));
        erv.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(this, 16)));
        mAdapter = new BusAdapter(this, mList);
        erv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this::reloadData);

        mSwipeRefreshLayout = findViewById(R.id.srl_bus);
        mSwipeRefreshLayout.setOnRefreshListener(this::reloadAllData);
    }

    private void reloadData(BusParam bp) {
        Request request = new Request
                .Builder()
                .url(bp.getAddress())
                .build();
        Call call = mOkHttpClient.newCall(request);
        Disposable subscribe = Flowable
                .fromCallable(() -> {
                    ResponseBody body = call.execute().body();
                    if (body != null) {
                        return body.string();
                    }
                    return "";
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::getValidJsonString)
                .map(this::getBuses)
                .subscribe(buses -> {
                    StringBuilder sb = new StringBuilder();
                    for (Bus bus : buses) {
                        sb.append("\n  ").append(bus);
                    }
                    bp.setResult(sb.toString());
                    mAdapter.notifyDataSetChanged();
                }, throwable -> Toast.makeText(mContext, "异常了" + throwable.getMessage(), Toast.LENGTH_SHORT).show());

        mCompositeDisposable.add(subscribe);

    }

    private void reloadAllData() {
        CountDownLatch latch = new CountDownLatch(mList.size());
        List<Disposable> subscribes = new ArrayList<>();
        for (BusParam bp : mList) {
            Request request = new Request
                    .Builder()
                    .url(bp.getAddress())
                    .build();
            Call call = mOkHttpClient.newCall(request);
            Disposable subscribe = Flowable
                    .fromCallable(() -> {
                        ResponseBody body = call.execute().body();
                        if (body != null) {
                            return body.string();
                        }
                        return "";
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(this::getValidJsonString)
                    .map(this::getBuses)
                    .subscribe(buses -> {
                        StringBuilder sb = new StringBuilder();
                        for (Bus bus : buses) {
                            sb.append("\n  ").append(bus);
                        }
                        bp.setResult(sb.toString());
                        mAdapter.notifyDataSetChanged();
                        latch.countDown();
                    }, throwable -> Toast.makeText(mContext, "异常了" + throwable.getMessage(), Toast.LENGTH_SHORT).show());
            subscribes.add(subscribe);
        }

        mCompositeDisposable.addAll(subscribes.toArray(new Disposable[0]));
        new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Uthread.runInMainThread(() -> mSwipeRefreshLayout.setRefreshing(false));
        });

    }

    @NonNull
    private String getValidJsonString(String str) {
        if (str.length() <= 12) {
            return "";
        }
        return str.substring(6, str.length() - 6);
    }

    @NonNull
    private List<Bus> getBuses(String s) {
        List<Bus> busList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("jsonr")) {
                JSONObject jsonr = jsonObject.getJSONObject("jsonr");
                if (jsonr.has("data")) {
                    JSONObject data = jsonr.getJSONObject("data");
                    if (data.has("buses")) {
                        JSONArray buses = data.getJSONArray("buses");

                        for (int i = 0; i < buses.length(); i++) {
                            Object o = buses.get(i);
                            String str = String.valueOf(o);
                            Bus bus = Ugson.getGson().fromJson(str, Bus.class);
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

    private static class BusAdapter extends RecyclerView.Adapter<MyViewHolder> {
        List<BusParam> mList;
        Context mContext;
        OnItemClickListener onItemClickListener;

        public BusAdapter(Context context, List<BusParam> list) {
            mContext = context;
            mList = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bus, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
            BusParam busParam = mList.get(i);
            holder.tvName.setText(busParam.getName());
            holder.tvResult.setText(busParam.getResult());
            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(busParam);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    interface OnItemClickListener {
        void onItemClick(BusParam param);
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvName;
        TextView tvResult;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvName = itemView.findViewById(R.id.tv_name);
            tvResult = itemView.findViewById(R.id.tv_result);
        }
    }
}
