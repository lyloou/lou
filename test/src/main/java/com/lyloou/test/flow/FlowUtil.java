package com.lyloou.test.flow;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.lyloou.test.util.Uapp;
import com.lyloou.test.util.Usystem;
import com.lyloou.test.util.Uview;

public class FlowUtil {
    public static void doCopy(Activity context, FlowDay flowDay, boolean jumpNow) {
        String content = flowDay.getDay()
                .concat("\n")
                .concat(FlowItemHelper.toPrettyText(flowDay.getItems()));

        Usystem.copyString(context, content);
        if (jumpNow) {
            toWps(context);
            return;
        }
        Snackbar snackbar = Snackbar.make(Uview.getRootView(context), "复制成功", Snackbar.LENGTH_LONG);
        snackbar.setAction("跳转到便签", v -> toWps(context));
        snackbar.show();
    }

    private static void toWps(Activity context) {
        String packageName = "cn.wps.note";
        Uapp.handlePackageIntent(context, packageName, intent -> {
            if (intent == null) {
                Toast.makeText(context, "没有安装" + packageName, Toast.LENGTH_SHORT).show();
                return;
            }
            context.startActivity(intent);
        });
    }
}
