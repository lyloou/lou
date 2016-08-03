package com.lyloou.lou.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2016/7/23 14:26.
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2016/7/23        Lou                 1.0             1.0
 * Why & What is modified:
 */
public class LouFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName();
    protected Activity mContext;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        super.onCreate(savedInstanceState);
    }
}
