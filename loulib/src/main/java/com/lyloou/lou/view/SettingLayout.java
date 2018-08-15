package com.lyloou.lou.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.lyloou.lou.R;
import com.lyloou.lou.util.Uscreen;


public class SettingLayout extends LinearLayout {
    private Context mContext;
    private SparseArray<Item> mItems;
    private SparseArray<View> mItemViews;

    public SettingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mItems = new SparseArray<>();
        mItemViews = new SparseArray<>();
    }

    public SettingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingLayout(Context context) {
        this(context, null);
    }


    public SettingLayout addItem(Item item) {
        // 对参数进行判断（）
        if (item == null) {
            throw new IllegalArgumentException("item为空");
        }
        if (mItemViews.get(item.titleStrId) != null) {
            throw new IllegalArgumentException("此Item项已存在，请不要重复添加");
        }
        // 从布局中加载得到视图；
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_view_setting_layout, null);
        addView(v, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Uscreen.dp2Px(mContext, 48)));
        // 添加到SparseArray中，便于后期进行其他操作；
        mItemViews.put(item.titleStrId, v);
        mItems.put(item.titleStrId, item);
        refreshItem(item);
        return this;
    }

    public SettingLayout refreshItem(final Item item) {
        if (item == null) {
            throw new NullPointerException("Item为空");
        }

        View v = getItemView(item);
        if (v == null) {
            throw new IllegalArgumentException("尚未添加此Item, 请先通过addItem方法添加");
        }

        // 根据数据初始化视图
        if (item.iconResId != 0) {
            ImageView ivIcon = (ImageView) v.findViewById(R.id.iv_set_icon);
            ivIcon.setVisibility(View.VISIBLE);
            ivIcon.setImageResource(item.iconResId);
        }

        TextView tvTitle = (TextView) v.findViewById(R.id.tv_set_title);
        tvTitle.setText(item.titleStrId);
        if (!TextUtils.isEmpty(item.contentStr)) {
            TextView tvContent = (TextView) v.findViewById(R.id.tv_set_content);
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(item.contentStr);
        }

        if (!TextUtils.isEmpty(item.unitStr)) {
            TextView tvUnit = (TextView) v.findViewById(R.id.tv_set_unit);
            tvUnit.setVisibility(View.VISIBLE);
            tvUnit.setText(item.unitStr);
        }

        if (item.hasToRight) {
            ImageView ivToRight = (ImageView) v.findViewById(R.id.iv_set_to_right);
            ivToRight.setVisibility(View.VISIBLE);
        }

        View vDivider = v.findViewById(R.id.v_set_divider);
        if (item.sep == SEP.FILL) {
            vDivider.setVisibility(View.VISIBLE);
        } else if (item.sep == SEP.AFTERICON) {
            vDivider.setVisibility(View.VISIBLE);
            ViewGroup.MarginLayoutParams mp = ((ViewGroup.MarginLayoutParams) vDivider.getLayoutParams());
            mp.leftMargin = Uscreen.dp2Px(mContext, 48);
        }

        if (item.listener != null) {
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.listener.click(SettingLayout.this, item);
                }
            });
        }
        return this;
    }

    public SettingLayout addCustomView(View view) {
        addView(view);
        return this;
    }

    public SettingLayout addSpace(int spaceHeight) {
        Space space = new Space(mContext);
        space.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, spaceHeight));
        addView(space);
        return this;
    }

    public SettingLayout addHeadTips(int strId) {
        return addHeadTips(mContext.getResources().getString(strId));
    }

    public SettingLayout addHeadTips(String tips) {
        TextView tvTips = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.leftMargin = Uscreen.dp2Px(mContext, 16);
        params.bottomMargin = Uscreen.dp2Px(mContext, 8);
        tvTips.setLayoutParams(params);
        tvTips.setText(tips);
        tvTips.setTextColor(Color.DKGRAY);
        tvTips.setTextSize(12);
        addView(tvTips);
        return this;
    }

    public Item getItem(int strId) {
        return mItems.get(strId);
    }

    public View getItemView(Item item) {
        return getItemView(item.titleStrId);
    }

    /**
     * 通过Title的字符串资源ID来获取对应的View
     *
     * @param titleStrId 字符串资源ID
     * @return 对应的Item视图
     */
    public View getItemView(int titleStrId) {
        return mItemViews.get(titleStrId);
    }

    public interface IClickListener {
        void click(SettingLayout layout, Item item);
    }

    /**
     * NO 表示不显示底部分割线
     * FILL 表示占满整个底部的分割线
     * AFTERICON 表示紧随图像的分割线
     */
    public static enum SEP {
        NO, FILL, AFTERICON;
    }

    public static class Item {
        public int titleStrId;
        public int iconResId;
        public String contentStr;
        public String unitStr;
        public boolean hasToRight;
        public SEP sep;
        public IClickListener listener;

        public Item(int titleStrId) {
            this.titleStrId = titleStrId;
        }

        /**
         * @param titleStrId 字符串ID，用来标识Item（不允许重复，且必须有效）
         * @param iconResId  最左边的Icon图标（当等于0时，不显示图标）
         * @param contentStr 内容字符串
         * @param unitStr    单位字符串（之所以没有和内容字符串合并，目的是更灵活地获取内容）
         * @param hasToRight 是否显示向右的图标
         * @param sep        分割线样式
         * @param listener   点击Item发生的事件
         */
        public Item(int titleStrId, int iconResId, String contentStr, String unitStr, boolean hasToRight, SEP sep,
                    IClickListener listener) {
            super();
            this.iconResId = iconResId;
            this.titleStrId = titleStrId;
            this.contentStr = contentStr;
            this.unitStr = unitStr;
            this.hasToRight = hasToRight;
            this.sep = sep;
            this.listener = listener;
        }
    }
}