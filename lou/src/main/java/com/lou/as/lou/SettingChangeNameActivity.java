package com.lou.as.lou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.view.SettingLayout;

public class SettingChangeNameActivity extends LouActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }

    public static void startActivity(Activity context, SettingLayout layout, SettingLayout.Item item) {
        sLayout = layout;
        sItem = item;
        context.startActivity(new Intent(context, SettingChangeNameActivity.class));
    }

    static SettingLayout sLayout;
    static SettingLayout.Item sItem;
    public void ok(View view){
        EditText etName = (EditText) findViewById(R.id.et_name);
        sItem.contentStr = etName.getText().toString();
        sLayout.refreshItem(sItem);
        finish();
    }
}
