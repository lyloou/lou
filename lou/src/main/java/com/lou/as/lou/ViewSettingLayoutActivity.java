package com.lou.as.lou;

import android.app.Activity;
import android.os.Bundle;

import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.util.Uscreen;
import com.lyloou.lou.util.Utoast;
import com.lyloou.lou.view.SettingLayout;

public class ViewSettingLayoutActivity extends LouActivity {
    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_view_setting_layout);
        initView();
    }

    private void initView() {
        SettingLayout.IClickListener listener = new SettingLayout.IClickListener() {
            @Override
            public void click(SettingLayout layout, SettingLayout.Item item) {
                switch (item.titleStrId) {
                    case R.string.app_name_0:
                        ViewSettingLayoutChangeNameActivity.startActivity(mContext, layout, item);
                        break;
                    case R.string.app_name_1:
                        item.contentStr = "双卡";
                        break;
                    case R.string.app_name_2:
                        item.contentStr = "关闭";
                        break;
                    case R.string.app_name_3:
                        item.contentStr = "已开启";
                        break;
                    case R.string.app_name_8:
                        item.contentStr = "静音";
                        break;
                }
                layout.refreshItem(item);
                Utoast.show(mContext, "点击了：" + mContext.getString(item.titleStrId));
            }
        };

        SettingLayout.Item[] items = new SettingLayout.Item[]{

                new SettingLayout.
                        Item(R.string.app_name_0, R.mipmap.ic_launcher, "楼", null, true, SettingLayout.SEP.AFTERICON, listener),
                new SettingLayout.
                        Item(R.string.app_name_0_1, R.mipmap.ic_launcher, "177", "CM", true, SettingLayout.SEP.AFTERICON, listener),
                new SettingLayout.
                        Item(R.string.app_name_0_2, R.mipmap.ic_launcher, "60", "KG", true, SettingLayout.SEP.NO, listener),

                new SettingLayout.
                        Item(R.string.app_name_1, R.mipmap.ic_launcher, null, null, true, SettingLayout.SEP.AFTERICON, listener),
                new SettingLayout.
                        Item(R.string.app_name_2, R.mipmap.ic_launcher, null, null, true, SettingLayout.SEP.AFTERICON, listener),
                new SettingLayout.
                        Item(R.string.app_name_3, R.mipmap.ic_launcher, null, null, true, SettingLayout.SEP.AFTERICON, listener),
                new SettingLayout.
                        Item(R.string.app_name_4, R.mipmap.ic_launcher, null, null, true, SettingLayout.SEP.NO, listener),
                new SettingLayout.
                        Item(R.string.app_name_5, R.mipmap.ic_launcher, null, null, true, SettingLayout.SEP.AFTERICON, listener),
                new SettingLayout.
                        Item(R.string.app_name_6, R.mipmap.ic_launcher, null, null, true, SettingLayout.SEP.AFTERICON, listener),
                new SettingLayout.
                        Item(R.string.app_name_7, R.mipmap.ic_launcher, null, null, true, SettingLayout.SEP.NO, listener),
                new SettingLayout.
                        Item(R.string.app_name_8, 0, null, null, true, SettingLayout.SEP.FILL, listener),
                new SettingLayout.
                        Item(R.string.app_name_9, 0, null, null, true, SettingLayout.SEP.FILL, listener),
                new SettingLayout.
                        Item(R.string.app_name_10, 0, null, null, true, SettingLayout.SEP.NO, listener),
                new SettingLayout.
                        Item(R.string.app_name_11, R.mipmap.ic_launcher, null, null, false, SettingLayout.SEP.NO, listener),
                new SettingLayout.
                        Item(R.string.app_name_12, R.mipmap.ic_launcher, null, null, false, SettingLayout.SEP.FILL, listener),
                new SettingLayout.
                        Item(R.string.app_name_13, R.mipmap.ic_launcher, null, null, false, SettingLayout.SEP.NO, listener),
                new SettingLayout.
                        Item(R.string.app_name_14, R.mipmap.ic_launcher, null, null, false, SettingLayout.SEP.FILL, listener),
                new SettingLayout.
                        Item(R.string.app_name_15, R.mipmap.ic_launcher, null, null, false, SettingLayout.SEP.NO, listener),


        };

        SettingLayout s = (SettingLayout) findViewById(R.id.svg_set);
        for (SettingLayout.Item item : items) {
            if (item.titleStrId == R.string.app_name_0) {
                s.addSpace(Uscreen.dp2Px(this, 12));
                s.addHeadTips("个人信息");
            }

            if (item.titleStrId == R.string.app_name_1) {
                s.addSpace(Uscreen.dp2Px(mContext, 12));
                s.addHeadTips("无线和网络");
            }

            if (item.titleStrId == R.string.app_name_5) {
                s.addSpace(Uscreen.dp2Px(mContext, 24));
                s.addHeadTips("提示和通知");
            }

            if (item.titleStrId == R.string.app_name_8) {
                s.addSpace(Uscreen.dp2Px(mContext, 12));
                s.addHeadTips("反馈");
            }

            if (item.titleStrId == R.string.app_name_11) {
                s.addSpace(Uscreen.dp2Px(mContext, 24));
                s.addHeadTips("微信");
            }

            if (item.titleStrId == R.string.app_name_12) {
                s.addSpace(Uscreen.dp2Px(mContext, 12));
            }
            if (item.titleStrId == R.string.app_name_14) {
                s.addSpace(Uscreen.dp2Px(mContext, 12));
            }
            s.addItem(item);
        }

        s.addSpace(Uscreen.dp2Px(mContext, 48));
    }
}