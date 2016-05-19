package com.lou.as.lou;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lyloou.lou.util.Utoast;
import com.lyloou.lou.view.CountView;

public class CountViewActivity extends AppCompatActivity {
    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_count_view);
        initView();
    }

    private void initView() {
        CountView countView = (CountView) findViewById(R.id.cv_main);
        if (countView != null) {
            // countView.setIndex(6);// 设置默认值
            countView.setOnChangeListener(new CountView.OnChangeListener() {
                @Override
                public void doIndex(int index) {
                    Utoast.show(mContext, "选中了：" + index + "次");
                }
            });
        }
    }
}
