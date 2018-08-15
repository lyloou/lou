package com.lou.as.lou.view.setting_layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lou.as.lou.R;
import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.util.Uscreen;
import com.lyloou.lou.util.Utoast;
import com.lyloou.lou.view.SettingLayout;
import com.lyloou.lou.view.SettingLayout.SEP;

public class SettingLayoutActivity extends LouActivity {
    public static final int REQUEST_CODE_NAME_0 = 100;
    private Activity mContext;
    private SettingLayout mSettingLayout;

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
                        Intent intent = new Intent(mContext, SettingLayoutChangeNameActivity.class);
                        intent.putExtra("name", item.contentStr);
                        startActivityForResult(intent, REQUEST_CODE_NAME_0);
                        break;
                    case R.string.app_name_1:
                        item.contentStr = "双卡";
                        layout.refreshItem(item);
                        break;
                    case R.string.app_name_2:
                        item.contentStr = "关闭";
                        layout.refreshItem(item);
                        break;
                    case R.string.app_name_3:
                        item.contentStr = "已开启";
                        layout.refreshItem(item);
                        break;
                    case R.string.app_name_8:
                        item.contentStr = "静音";
                        layout.refreshItem(item);
                        break;
                    default:
                        Utoast.show(mContext, "点击了：" + mContext.getString(item.titleStrId));
                }
            }
        };

        SettingLayout.Item[] items = new SettingLayout.Item[]{
                new SettingLayout.Item(R.string.app_name_0, R.mipmap.ic_launcher, "楼", null, true, SEP.AFTERICON, listener),
                new SettingLayout.Item(R.string.app_name_0_1, R.mipmap.ic_launcher, "177", "CM", true, SEP.AFTERICON, listener),
                new SettingLayout.Item(R.string.app_name_0_2, R.mipmap.ic_launcher, "60", "KG", true, SEP.NO, listener),

                new SettingLayout.Item(R.string.app_name_1, R.mipmap.ic_launcher, null, null, true, SEP.AFTERICON, listener),
                new SettingLayout.Item(R.string.app_name_2, R.mipmap.ic_launcher, null, null, true, SEP.AFTERICON, listener),
                new SettingLayout.Item(R.string.app_name_3, R.mipmap.ic_launcher, null, null, true, SEP.AFTERICON, listener),
                new SettingLayout.Item(R.string.app_name_4, R.mipmap.ic_launcher, null, null, true, SEP.NO, listener),

                new SettingLayout.Item(R.string.app_name_5, R.mipmap.ic_launcher, null, null, true, SEP.AFTERICON, listener),
                new SettingLayout.Item(R.string.app_name_6, R.mipmap.ic_launcher, null, null, true, SEP.AFTERICON, listener),
                new SettingLayout.Item(R.string.app_name_7, R.mipmap.ic_launcher, null, null, true, SEP.NO, listener),

                new SettingLayout.Item(R.string.app_name_8, 0, null, null, true, SEP.FILL, listener),
                new SettingLayout.Item(R.string.app_name_9, 0, null, null, true, SEP.FILL, listener),
                new SettingLayout.Item(R.string.app_name_10, 0, null, null, true, SEP.NO, listener),

                new SettingLayout.Item(R.string.app_name_11, R.mipmap.ic_launcher, null, null, false, SEP.NO, listener),
                new SettingLayout.Item(R.string.app_name_12, R.mipmap.ic_launcher, null, null, false, SEP.FILL, listener),
                new SettingLayout.Item(R.string.app_name_13, R.mipmap.ic_launcher, null, null, false, SEP.NO, listener),
                new SettingLayout.Item(R.string.app_name_14, R.mipmap.ic_launcher, null, null, false, SEP.FILL, listener),
                new SettingLayout.Item(R.string.app_name_15, R.mipmap.ic_launcher, null, null, false, SEP.NO, listener),
        };

        mSettingLayout = (SettingLayout) findViewById(R.id.svg_set);
        for (SettingLayout.Item item : items) {
            if (item.titleStrId == R.string.app_name_0) {
                mSettingLayout.addSpace(Uscreen.dp2Px(this, 12));
                mSettingLayout.addHeadTips("个人信息");
            }

            if (item.titleStrId == R.string.app_name_1) {
                mSettingLayout.addSpace(Uscreen.dp2Px(mContext, 12));
                mSettingLayout.addHeadTips("无线和网络");
            }

            if (item.titleStrId == R.string.app_name_5) {
                mSettingLayout.addSpace(Uscreen.dp2Px(mContext, 24));
                mSettingLayout.addHeadTips("提示和通知");
            }

            if (item.titleStrId == R.string.app_name_8) {
                mSettingLayout.addSpace(Uscreen.dp2Px(mContext, 12));
                mSettingLayout.addHeadTips("反馈");
            }

            if (item.titleStrId == R.string.app_name_11) {
                mSettingLayout.addSpace(Uscreen.dp2Px(mContext, 24));
                mSettingLayout.addHeadTips("微信");
            }

            if (item.titleStrId == R.string.app_name_12) {
                mSettingLayout.addSpace(Uscreen.dp2Px(mContext, 12));
            }
            if (item.titleStrId == R.string.app_name_14) {
                mSettingLayout.addSpace(Uscreen.dp2Px(mContext, 12));
            }
            mSettingLayout.addItem(item);
        }

        mSettingLayout.addSpace(Uscreen.dp2Px(mContext, 48));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_NAME_0:
                if (resultCode != 10) {
                    return;
                }
                SettingLayout.Item item = mSettingLayout.getItem(R.string.app_name_0);
                item.contentStr = data.getStringExtra("name");
                mSettingLayout.refreshItem(item);
                break;
        }

    }
}