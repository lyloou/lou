package com.lou.as.lou.view.color_picker;

import android.graphics.Color;
import android.os.Bundle;

import com.lou.as.lou.R;
import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.view.ColorPickerLayout;

public class ColorPickerLayoutActivity extends LouActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_color_picker_layout);

        initView();
    }

    private void initView() {
        ColorPickerLayout cpv = (ColorPickerLayout) findViewById(R.id.cpv_simple_light);
        if (cpv != null) {
            cpv.setOnColorChangeListener(color -> {
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                String str = "当前颜色 color=" + color
                        + " 对应rgb值：r=" + r + " g=" + g + " b=" + b;
                System.out.println(str);
            });
        }
    }
}
