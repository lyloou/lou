package com.lyloou.lou.activity;

import android.app.Fragment;

import com.lyloou.lou.fragment.WebRootFragment;


public class WebRootActivity extends SingleFragmentActivity {

    public static final String KEY_URL = "key_url";

    @Override
    public Fragment createFragment() {

        String url = getIntent().getStringExtra(KEY_URL);
        if (url == null) {
            url = "http://lyloou.com";
        }
        WebRootFragment fragment = WebRootFragment.newInstance(url);
        return fragment;
    }
}
