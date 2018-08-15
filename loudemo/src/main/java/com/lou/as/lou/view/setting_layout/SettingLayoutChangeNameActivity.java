package com.lou.as.lou.view.setting_layout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lou.as.lou.R;
import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.util.Utoast;

public class SettingLayoutChangeNameActivity extends LouActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_setting_layout_change_name);
    }

    public void ok(View view) {
        EditText etName = (EditText) findViewById(R.id.et_name);
        String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Utoast.show(this, "请输入新用户名");
            return;
        }

        Intent data = new Intent();
        data.putExtra("name", name);
        setResult(10, data);
        finish();
    }
}
