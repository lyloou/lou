package com.lyloou.lou.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.lyloou.lou.R;
import com.lyloou.lou.util.Uscreen;

/**
 * 类描述：
 * 创建人： Lou
 * 创建时间： 2016/6/23 9:41
 * 修改人：Lou
 * 修改时间：2016/6/23 9:41
 * 修改备注：
 */
public class LouDialogAtBottom {
    final LouDialog mLouDialog;

    public static LouDialogAtBottom newInstance(Context context, int layoutId) {
        return new LouDialogAtBottom(context, layoutId);
    }

    private LouDialogAtBottom(Context context, int layoutId) {
        mLouDialog = LouDialog.newInstance(context, layoutId,
                R.style.lou_dialog_theme_bottom);
        mLouDialog.setWindowAnimation(R.style.lou_anim_bottom)
                .setWH(Uscreen.getScreenWidth(context), -1)
                .setPositionAndAlpha(Gravity.BOTTOM, 0, 0, 1f)
                .setDimAmount(0.6f)
                .setCancelable(true);

    }

    public <T extends View> T getView(int viewId){
        return mLouDialog.getView(viewId);
    }

    public LouDialog getLouDialog(){
        return mLouDialog;
    }
    public void show() {
        mLouDialog.show();
    }
}
