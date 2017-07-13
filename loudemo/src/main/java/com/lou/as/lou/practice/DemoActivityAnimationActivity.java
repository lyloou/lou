package com.lou.as.lou.practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lou.as.lou.R;
import com.lyloou.lou.activity.LouActivity;

/**
 * 方案1和方案2区别请搜索；
 */
public class DemoActivityAnimationActivity extends LouActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("Activity切换动画");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DemoActivityAnimationActivity.this.startActivity(
                        new Intent(DemoActivityAnimationActivity.this, DemoActivityAnimationActivity.class)
                );

                // 动画方案1
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }
        });
        setContentView(textView);

        // 动画方案2
        //        getWindow().setWindowAnimations(R.style.lou_anim_right);
    }

    @Override
    public void finish() {
        super.finish();

        // 动画方案1
        //关闭窗体动画显示
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}