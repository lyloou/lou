package com.lyloou.lou.activity;

import com.lyloou.lou.app.LouApplication;
import com.lyloou.lou.util.Ulog;

import android.app.Activity;
import android.os.Bundle;

public class LouActivity extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Ulog.v("--onCreate----> "+this.getClass().getSimpleName());
        LouApplication.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Ulog.v("--onDestroy----> "+this.getClass().getSimpleName());
        LouApplication.removeActivity(this);
    }
}
                                                  