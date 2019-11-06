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
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import com.lyloou.test.bus.notification.AlarmReceiver;
import com.lyloou.test.bus.weather.Weather;
import com.lyloou.test.bus.weather.WeatherApi;
import com.lyloou.test.common.Constant;
import com.lyloou.test.common.Consumer;
import com.lyloou.test.common.EmptyRecyclerView;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.util.Uapp;
import com.lyloou.test.util.Udialog;
import com.lyloou.test.util.Ugson;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Usp;
import com.lyloou.test.util.Utime;
import com.lyloou.test.util.Utoast;
import com.lyloou.test.util.Uview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

import static com.lyloou.test.bus.notification.NotificationConstant.KEY_ALARM_CLOCK_BUS_HOUR_OF_DAY;
import static com.lyloou.test.bus.notification.NotificationConstant.KEY_ALARM_CLOCK_BUS_MINUTE;

public class BusActivity extends AppCompatActivity {

    private static final String TAG = BusActivity.class.getSimpleName();


    private List<BusParam> mList;
    private Activity mContext;
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private BusAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BusDatabase mBusDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setMyTitle("车来了");
        setContentView(R.layout.activity_bus);

        initData();
        initView();
        reloadAllData();
    }

    private void initData() {
        mBusDatabase = new BusDatabase(mContext);
        mList = getList();
    }


    @NonNull
    private List<BusParam> getList() {
        List<BusParam> list = mBusDatabase.readData();
        // 如果取不到，就取几个默认的
        if (list == null || list.size() == 0) {
            list = new ArrayList<>();
            list.add(new BusParam("上班-m395", "https://api.chelaile.net.cn/bus/line!busesDetail.action?stats_act=auto_refresh&sign=iR6cZFa3lR1nNTpOSxgzIg%3D%3D&language=1&cityId=014&lineNo=06510&phoneBrand=HONOR&lchsrc=shortcut&system_push_open=1&lat=22.574164&deviceType=BKL-AL20&geo_type=gcj&o1=89211709d6cf31e368b0d64d39c7b4058607f1f8&targetStationLng=113.9007037650382&cryptoSign=4b66b21b6393a2b0191325143996b801&lng=113.927254&first_src=app_huawei_store&vc=160&isNewLineDetail=1&gpstype=gcj&geo_lt=5&accountId=34617165&last_src=app_huawei_store&sstate=3&wifi_open=0&screenDensity=3.0&flpolicy=0&astate=1&screenWidth=1080&cshow=linedetail&nw=MOBILE_LTE&stats_referer=searchResult&lineName=M395&secret=37fde6b9dfc64fdf9a56063198a66659&AndroidID=e55ed573e14d8ad3&mac=58%3A02%3A03%3A04%3A05%3A06&stationName=%E8%A3%95%E5%AE%89%E8%B7%AF%E5%8F%A3%E2%91%A0&udid=a4068f5c-39ee-42e8-ab8c-52511762004d&targetStationLat=22.573404716848184&direction=1&targetOrder=22&push_open=1&sv=9&geo_lac=29.0&screenHeight=2088&lineId=0755-06510-1&userAgent=Mozilla%2F5.0+(Linux%3B+Android+9%3B+BKL-AL20+Build%2FHUAWEIBKL-AL20%3B+wv)+AppleWebKit%2F537.36+(KHTML%2C+like+Gecko)+Version%2F4.0+Chrome%2F72.0.3626.121+Mobile+Safari%2F537.36&userId=unknown&filter=1&paramsMakeUp=is&s=android&geo_lng=113.927254&geo_lat=22.574164&v=3.85.4&imei=862848046848866&stats_order=1-1"));
            list.add(new BusParam("上班-m378", "https://api.chelaile.net.cn/bus/line!busesDetail.action?stats_act=auto_refresh&sign=wl50fDo3Fr8tTOKwXULO0w%3D%3D&language=1&cityId=014&lineNo=M3783&phoneBrand=HONOR&lchsrc=shortcut&system_push_open=1&lat=22.574181&deviceType=BKL-AL20&geo_type=gcj&o1=89211709d6cf31e368b0d64d39c7b4058607f1f8&targetStationLng=113.90117882310646&cryptoSign=cb1e6ab2b64c10fdbcb16359133f1f89&lng=113.927291&first_src=app_huawei_store&vc=160&isNewLineDetail=1&gpstype=gcj&geo_lt=5&accountId=34617165&last_src=app_huawei_store&sstate=3&wifi_open=0&screenDensity=3.0&flpolicy=0&astate=1&screenWidth=1080&cshow=linedetail&nw=MOBILE_LTE&stats_referer=searchResult&lineName=M378&secret=37fde6b9dfc64fdf9a56063198a66659&AndroidID=e55ed573e14d8ad3&mac=58%3A02%3A03%3A04%3A05%3A06&stationName=%E8%A3%95%E5%AE%89%E8%B7%AF%E5%8F%A3&udid=a4068f5c-39ee-42e8-ab8c-52511762004d&targetStationLat=22.57290849087809&direction=0&targetOrder=21&push_open=1&sv=9&geo_lac=29.0&screenHeight=2088&lineId=0755-M3783-0&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+9%3B+BKL-AL20+Build%2FHUAWEIBKL-AL20%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F72.0.3626.121+Mobile+Safari%2F537.36&userId=unknown&filter=1&paramsMakeUp=is&s=android&geo_lng=113.927291&geo_lat=22.574181&v=3.85.4&imei=862848046848866&stats_order=1-1"));
            list.add(new BusParam("回家-m395", "https://api.chelaile.net.cn/bus/line!busesDetail.action?stats_act=auto_refresh&sign=Fm%2BW70el84%2B3hLN5ar7lQQ%3D%3D&language=1&cityId=014&lineNo=06510&phoneBrand=HONOR&lchsrc=shortcut&system_push_open=1&lat=22.57416&deviceType=BKL-AL20&geo_type=gcj&o1=89211709d6cf31e368b0d64d39c7b4058607f1f8&targetStationLng=113.92468540887381&cryptoSign=246bbba6d14b73b0091e7c1a50f685a7&lng=113.927274&first_src=app_huawei_store&vc=160&isNewLineDetail=1&gpstype=gcj&geo_lt=5&accountId=34617165&last_src=app_huawei_store&sstate=3&wifi_open=0&screenDensity=3.0&flpolicy=0&astate=1&screenWidth=1080&cshow=linedetail&nw=MOBILE_LTE&stats_referer=searchHistory&lineName=M395&secret=37fde6b9dfc64fdf9a56063198a66659&AndroidID=e55ed573e14d8ad3&mac=58%3A02%3A03%3A04%3A05%3A06&stationName=%E6%96%B0%E7%A6%8F%E5%B8%82%E5%9C%BA%E2%91%A0&udid=a4068f5c-39ee-42e8-ab8c-52511762004d&targetStationLat=22.587014643396046&direction=0&targetOrder=38&push_open=1&sv=9&geo_lac=29.0&screenHeight=2088&lineId=0755-06510-0&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+9%3B+BKL-AL20+Build%2FHUAWEIBKL-AL20%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F72.0.3626.121+Mobile+Safari%2F537.36&userId=unknown&filter=1&paramsMakeUp=is&s=android&geo_lng=113.927274&geo_lat=22.57416&v=3.85.4&imei=862848046848866&stats_order=1-2"));
            list.add(new BusParam("回家-m378", "https://api.chelaile.net.cn/bus/line!busesDetail.action?stats_act=auto_refresh&sign=rwS%2BCBeAfPaj5rdJajzMzg%3D%3D&language=1&cityId=014&lineNo=M3783&phoneBrand=HONOR&lchsrc=shortcut&system_push_open=1&lat=22.574181&deviceType=BKL-AL20&geo_type=gcj&o1=89211709d6cf31e368b0d64d39c7b4058607f1f8&targetStationLng=113.9246803751752&cryptoSign=d6a4f3fb35c6dba89865a808b7113ba4&lng=113.927291&first_src=app_huawei_store&vc=160&isNewLineDetail=1&gpstype=gcj&geo_lt=5&accountId=34617165&last_src=app_huawei_store&sstate=3&wifi_open=0&screenDensity=3.0&flpolicy=0&astate=1&screenWidth=1080&cshow=linedetail&nw=MOBILE_LTE&stats_referer=searchResult&lineName=M378&secret=37fde6b9dfc64fdf9a56063198a66659&AndroidID=e55ed573e14d8ad3&mac=58%3A02%3A03%3A04%3A05%3A06&stationName=%E6%96%B0%E7%A6%8F%E5%B8%82%E5%9C%BA%E2%91%A0&udid=a4068f5c-39ee-42e8-ab8c-52511762004d&targetStationLat=22.58697971490557&direction=1&targetOrder=39&push_open=1&sv=9&geo_lac=29.0&screenHeight=2088&lineId=0755-M3783-1&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+9%3B+BKL-AL20+Build%2FHUAWEIBKL-AL20%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F72.0.3626.121+Mobile+Safari%2F537.36&userId=unknown&filter=1&paramsMakeUp=is&s=android&geo_lng=113.927291&geo_lat=22.574181&v=3.85.4&imei=862848046848866&stats_order=1-1"));
        }

        return list;
    }

    private void setMyTitle(String title) {
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
            supportActionBar.setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bus_add:
                addBus();
                break;
            case R.id.menu_bus_default:
                recoverData();
                break;
            case R.id.menu_bus_reload:
                reloadAllDataAndUI();
                break;
            case R.id.menu_bus_shortcut:
                addShortcut();
                break;
            case R.id.menu_bus_set_alarm_clock:
                setAlarmClock();
                break;
            case R.id.menu_bus_cancel_alarm_clock:
                cancelAlarmClock();
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancelAlarmClock() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent());
        Utoast.show(mContext, "已经取消闹钟");
    }

    private void setAlarmClock() {
        TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    getCalendar(hourOfDay, minute).getTimeInMillis(),
                    1000 * 60 * 60 * 24,
                    getPendingIntent());

            saveAlarmClockConfig(hourOfDay, minute);
            Utoast.show(mContext, String.format("闹钟已设置为 %s:%s", hourOfDay, minute));
        };
        Udialog.showTimePicker(mContext, listener, Utime.getValidTime(null));
    }

    // 保存下来，以便开机启动闹钟
    private void saveAlarmClockConfig(int hourOfDay, int minute) {
        Usp.init(mContext)
                .putInt(KEY_ALARM_CLOCK_BUS_HOUR_OF_DAY, hourOfDay)
                .putInt(KEY_ALARM_CLOCK_BUS_MINUTE, minute)
                .commit();
    }

    private PendingIntent getPendingIntent() {
        Intent notifyIntent = new Intent(mContext, AlarmReceiver.class);
        return PendingIntent.getBroadcast(mContext,
                0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @NonNull
    private Calendar getCalendar(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    private void addShortcut() {
        Uapp.addShortCutCompat(this, BusActivity.class.getCanonicalName(), "bus", R.mipmap.airplane, this.getResources().getString(R.string.bus_line));
    }

    private void reloadAllDataAndUI() {
        mList.clear();
        mList.addAll(getList());
        mAdapter.notifyDataSetChanged();
        reloadAllData();
    }

    private void recoverData() {
        mBusDatabase.delete();
        reloadAllDataAndUI();
    }

    @SuppressLint("InflateParams")
    public void addBus() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_bus_line_add, null);

        new AlertDialog.Builder(this)
                .setTitle("添加更多公交")
                .setView(view)
                .setPositiveButton("是的", (dialog, whichButton) -> {
                    EditText etName = view.findViewById(R.id.et_name);
                    EditText etAddress = view.findViewById(R.id.et_address);

                    String name = etName.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        Utoast.show(mContext, "无效的名称");
                        return;
                    }
                    String address = etAddress.getText().toString();
                    try {
                        new URL(address);
                        mList.add(0, new BusParam(name, address));
                        mAdapter.notifyDataSetChanged();
                        mBusDatabase.writeData(mList);
                    } catch (MalformedURLException e) {
                        Utoast.show(mContext, "无效的URL");
                    }

                })
                .show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initView() {
        Uview.initStatusBar(this, R.color.colorAccent);

        TextView tvWeather = findViewById(R.id.tv_weather);
        tvWeather.setOnClickListener(v -> loadWeather(tvWeather));
        loadWeather(tvWeather);

        EmptyRecyclerView erv = findViewById(R.id.erv_bus);
        erv.setLayoutManager(new LinearLayoutManager(this));
        erv.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(this, 16)));
        mAdapter = new BusAdapter(this, mList);
        erv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BusParam param) {
                reloadData(param);
            }

            @Override
            public void onItemLongClick(BusParam param) {
                new AlertDialog.Builder(mContext)
                        .setMessage("删除：" + param.getName() + "?")
                        .setPositiveButton("是的", (dialog, whichButton) -> {
                            mList.remove(param);
                            mBusDatabase.writeData(mList);
                            mAdapter.notifyDataSetChanged();
                        })
                        .show();
            }
        });

        mSwipeRefreshLayout = findViewById(R.id.srl_bus);
        mSwipeRefreshLayout.setOnRefreshListener(this::reloadAllData);
    }

    private void loadWeather(TextView tvWeather) {
        mCompositeDisposable.add(NetWork.get(Constant.Url.Weather.getUrl(), WeatherApi.class)
                .getWeather("101280601")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> {
                    if (weather.getStatus() != 200) {
                        return;
                    }
                    List<Weather.DataBean.ForecastBean> forecast = weather.getData().getForecast();
                    if (forecast != null && forecast.size() > 0) {
                        Weather.DataBean.ForecastBean fb = forecast.get(0);
                        StringBuilder sb = new StringBuilder();
                        sb.append(fb.getWeek())
                                .append("\t")
                                .append(fb.getType())
                                .append("\n")
                                .append(fb.getLow())
                                .append(" ~ ")
                                .append(fb.getHigh())
                                .append("\t")
                                .append("\t(")
                                .append(fb.getFx())
                                .append(" ")
                                .append(fb.getFl())
                                .append(")\n")
                                .append(fb.getNotice());
                        tvWeather.setText(sb.toString());
                    }
                }, throwable ->
                        Toast.makeText(mContext, "连接天气服务异常", Toast.LENGTH_SHORT).show())
        );
    }

    private void reloadData(BusParam bp) {
        Disposable subscribe = sendRequest(bp, s -> {
            bp.setResult(s);
            mAdapter.notifyDataSetChanged();
        });

        mCompositeDisposable.add(subscribe);
    }

    private Disposable sendRequest(BusParam bp, Consumer<String> result) {
        Request request = new Request
                .Builder()
                .url(bp.getAddress())
                .build();
        Call call = mOkHttpClient.newCall(request);
        return Flowable
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
                        sb.append(bus).append("\n");
                    }
                    result.accept(sb.toString());

                }, throwable -> Toast.makeText(mContext, "异常了" + throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void reloadAllData() {
        mSwipeRefreshLayout.setRefreshing(true);
        List<Disposable> subscribes = new ArrayList<>();
        for (BusParam bp : mList) {
            Disposable subscribe = sendRequest(bp, s -> {
                bp.setResult(s);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            });
            subscribes.add(subscribe);
        }
        mCompositeDisposable.addAll(subscribes.toArray(new Disposable[0]));

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

            holder.itemView.setOnLongClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemLongClick(busParam);
                }
                return true;
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

        void onItemLongClick(BusParam param);
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
