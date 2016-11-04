package com.lyloou.demo;

import android.os.Bundle;
import android.widget.TextView;

import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.util.Usp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends LouActivity {

    @BindView(R.id.textView)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private static final String KEY_ARG1 = "ARG1";

    private void initView() {
        // 从本地获取已经保存的“次数”
        int arg1 = Usp.getInstance().getInt(KEY_ARG1, 0);
        String showText = "Call times:" + arg1;

        // 显示调用次数
        mTextView.setText(showText);

        // 本地“次数”加1
        Usp.getInstance().putInt(KEY_ARG1, arg1 + 1);
    }

}
