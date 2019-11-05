package com.lyloou.test.common.popview;

import android.support.annotation.NonNull;
import android.view.View;

public class Item {

    public Item(@NonNull String text, @NonNull View.OnClickListener listener) {
        this.text = text;
        this.listener = listener;
    }

    private String text;
    private View.OnClickListener listener;

    public String getText() {
        return text;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

}
