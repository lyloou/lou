package com.lyloou.test.show;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lyloou.test.R;
import com.lyloou.test.util.Usystem;
import com.lyloou.test.util.Uview;

public class ShowIntentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_intent);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        String subject = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        String content = intent.getStringExtra(Intent.EXTRA_TEXT);

        this.<TextView>findViewById(R.id.subject).setText(subject);
        this.<TextView>findViewById(R.id.content).setText(content);
        this.<TextView>findViewById(R.id.copy).setOnClickListener(v -> {
                    Usystem.copyString(this, subject + "\n" + content);
                    Snackbar.make(Uview.getRootView(this), "已复制到剪切板", Snackbar.LENGTH_SHORT).show();
                }
        );

    }
}
