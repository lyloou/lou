package com.lyloou.test.common.popview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lyloou.test.R;
import com.lyloou.test.util.Uscreen;

import java.util.List;

/**
 * [Android PopupWindow仿微信、QQ、支付宝右上角弹出效果（超详细） - 简书](https://www.jianshu.com/p/2adaa6a5f85f)
 */
public class MenuPopView {

    public static void show(Context context, View view, List<Item> items) {
        PopupWindow popView = new PopupWindow(context);
        // 设置布局文件
        popView.setContentView(getPopView(context, popView, items));
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        popView.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popView.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop透明效果
        popView.setBackgroundDrawable(new ColorDrawable(0x0000));
        // 设置pop出入动画
        popView.setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        // popView.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        popView.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        popView.setOutsideTouchable(true);
        // 相对于 + 号正下面，同时可以设置偏移量
        int dp20 = Uscreen.dp2Px(context, 140);
        popView.showAsDropDown(view, -dp20, 0);

    }

    private static LinearLayout getPopView(Context context, PopupWindow popView, List<Item> items) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setBackgroundResource(R.drawable.icon_other_9);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (Item item : items) {
            linearLayout.addView(getPopItemView(context, popView, item));
        }
        return linearLayout;
    }

    @NonNull
    private static TextView getPopItemView(Context context, PopupWindow popView, Item item) {
        TextView tv = new TextView(context);
        tv.setText(item.getText());
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        // https://stackoverflow.com/questions/37987732/programmatically-set-selectableitembackground-on-android-view
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        tv.setBackgroundResource(outValue.resourceId);
        tv.setTextSize(16);
        tv.setOnClickListener(v -> {
            item.getListener().onClick(v);
            popView.dismiss();
        });
        int dp10 = Uscreen.dp2Px(context, 10);
        int dp20 = Uscreen.dp2Px(context, 20);
        tv.setPadding(dp20, dp10, dp20, dp10);
        return tv;
    }
}
