package com.lou.as.lou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class SkinActivity extends Activity {

    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_skin);
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.rlyt_bg).setBackgroundColor(SharedPreferencesUtil.getInstance(mContext).getSkin());
    }

    public void changeBg(View view) {
        startActivity(new Intent(mContext, SkinChangeActivity.class));
    }


}
