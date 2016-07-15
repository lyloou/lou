package com.lyloou.lou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyloou.lou.R;
import com.lyloou.lou.util.Uscreen;
import com.lyloou.lou.util.Uview;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lou on 2016/3/23.
 * Updated By Lou on 2016/05/07.
 */

/**
 * 特色功能：
 * ★ 通用（使用ListView和GridView的地方都可以用此类来简化）
 * ★ 简单，不需要重写BaseAdapter；
 * ★ 删除伴随着动画（不突兀）；
 * ★ 数据和ListView都托管给了LouAdapter；
 * ★ 强制使用ViewHolder缓存；
 * ★ 局部刷新；
 * ★ 多选模式/单选模式的控制；（需要给Item布局设置selector才可以在视觉上区分是否为选中模式）
 * ★ 绑定ListView到this（即在使用的时候不用给ListView 设置Adapter）；
 * ★ 以前在使用ListView的时候需要做这些处理：
 * {@code
 * // 查找ListView；
 * // 设置Adapter；
 * // 如果布局复杂的话还需要重新Adapter；
 * // 绑定数据；
 * }
 * ★ 现在只需要简单分配参数，即使是复杂的布局也可以很快实现出来：
 * {@code
 * ListView listView = new ListView(mContext);
 * int layoutId = android.R.layout.simple_list_item_1;
 * // <> 中的内容是你的Bean元素；
 * // 第一个参数(listView)：被托管的ListView；
 * // 第二个参数（layoutId）：ListView的Item布局；
 * // 方法assign：用来分配Bean和Item布局里视图的对应关系；
 * LouAdapter<String> adapter = new LouAdapter<String>(listView, layoutId) { @Override protected void assign(ViewHolder holder, String s) {holder.putText(android.R.id.text1, s);}};
 * }
 */
public abstract class LouAdapter<T> extends BaseAdapter {

    private ArrayList<T> mLists;
    private Context mContext;
    private AbsListView mListView;
    private int mLayoutId;

    public LouAdapter(AbsListView listView, int layoutId) {
        mContext = listView.getContext();
        mListView = listView;
        mLayoutId = layoutId;
        mLists = new ArrayList<T>();
        mListView.setAdapter(this);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public T getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return updateView(position, convertView);
    }

    public AbsListView getBindView() {
        return mListView;
    }

    // -------- 添加 choice调用（2016.03.25）
    public void clearChoice() {
        mListView.clearChoices();
        updateChange();
        // selectAllChoice(false);
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 注意需要使用Runnable才能生效；
                // 参考资料[ListView selection remains persistent after exiting choice mode]
                // (http://stackoverflow.com/questions/9754170/listview-selection-remains-persistent-after-exiting-choice-mode)
                mListView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
            }
        }, 160);
    }

    public void setChoiceMode(int mode) {
        mListView.setChoiceMode(mode);
    }

    public int getChoiceMode() {
        return mListView.getChoiceMode();
    }

    public void deleteChoicedItem() {
        if (mListView.getChoiceMode() != AbsListView.CHOICE_MODE_NONE) {
            // 移除被选中的ITEM；
            mLists.removeAll(getCheckedItems());
            mListView.clearChoices();
            updateChange();
        }
    }

    public void selectAllChoice(boolean selectAll) {
        for (int i = 0; i < mListView.getCount(); i++) {
            mListView.setItemChecked(i, selectAll);
        }
        updateChange();
    }

    public List<T> getCheckedItems() {
        SparseBooleanArray sba = mListView.getCheckedItemPositions();
        ArrayList<T> checkedLists = new ArrayList<>();
        for (int i = 0; i < sba.size(); i++) {
            if (sba.valueAt(i)) {
                int position = sba.keyAt(i);
                if (position != -1)
                    checkedLists.add(
                            getItem(sba.keyAt(i)));
            }
        }
        return checkedLists;
    }
    // ~~~~~~~~~~~~

    /**
     * contain 来表示是否已经包含元素；（可能需要重写，如果允许重复的话，就不必要重写了）；
     *
     * @param o 要判断的元素
     * @return 返回的结果
     */
    protected boolean contain(T o) {
        return false;
    }

    /**
     * 用数据来更新视图；
     *
     * @param position
     * @param convertView
     * @return
     */
    private View updateView(int position, View convertView) {
        ViewHolder holder = ViewHolder.getInstance(mContext, mLayoutId, convertView);
        assign(holder, getItem(position));
        return holder.getConvertView();
    }

    /**
     * 当更改了某一个Item之后，可以通过updateView(position);的方式只更新这一个Item；
     *
     * @param position
     */
    public void updateView(int position) {
        View convertView = getIndexView(position);
        updateView(position, convertView);
    }

    /**
     * @param holder
     * @param t
     */
    protected abstract void assign(ViewHolder holder, T t);


    /**
     * 获取可见元素的View；
     *
     * @param position
     * @return
     */
    public View getIndexView(int position) {
        int currentIndex = position - mListView.getFirstVisiblePosition();
        if (currentIndex < 0 || currentIndex >= mLists.size()) {
            return null;
        }
        return mListView.getChildAt(currentIndex);
    }


    public void addItem(T o, boolean filter) {
        if (filter && contain(o)) {
            return;
        }
        mLists.add(o);
        updateChange();
    }

    /**
     * 默认过滤添加的元素；
     *
     * @param o
     */
    public void addItem(T o) {
        addItem(o, true);
    }

    public List<T> getList() {
        return mLists;
    }

    /**
     * 初始化元素
     *
     * @param list
     */
    public void initList(List<T> list) {
        mLists.clear();
        mLists.addAll(list);
        updateChange();
    }

    public void deleteItem(T o) {
        mLists.remove(o);
        updateChange();
    }

    public void deleteItem(int position) {
        mLists.remove(position);
        updateChange();
    }

    /**
     * 高度变为0后删除元素；
     *
     * @param position
     */
    public void deleteItemWithAnim(final int position) {
        final View view = getIndexView(position);
        // 如果删除的元素未显示，则调用deleteItem方法删除
        if (view == null) {
            deleteItem(position);
            return;
        }

        final int initHeight = view.getMeasuredHeight();
        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t) {
                if (mLists.size() > 0 && interpolatedTime == 1) {
                    // 高度为0时删除元素，并更新 adapter
                    if (view.getTag() instanceof ViewHolder) {
                        ((ViewHolder) view.getTag()).mark = ViewHolder.MARK.DELETE;
                        deleteItem(position);
                    }
                } else {
                    // 不断的改变高度，直到高度为0；
                    view.getLayoutParams().height = initHeight
                            - (int) (initHeight * interpolatedTime);
                    view.requestLayout();
                }
            }
        };
        anim.setDuration(300);
        view.startAnimation(anim);
    }


    /**
     * @param t
     * @return the item's index in the list;
     */
    public int indexOf(T t) {
        return mLists.indexOf(t);
    }

    public void updateChange() {
        notifyDataSetChanged();
    }

    public void clear() {
        mLists.clear();
        updateChange();
    }


    /**
     * Created by Lou on 2016/3/23.
     */
    public static class ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;

        MARK mark;

        enum MARK {
            NORMAL, DELETE
        }

        private ViewHolder(Context context, int layoutId) {
            mViews = new SparseArray<>();
            mConvertView = LayoutInflater.from(context).inflate(layoutId, null);
            mConvertView.setTag(this);
        }

        // 权限：default 包级私有
        static ViewHolder getInstance(Context context, int layoutId, View convertView) {
            boolean needInflate = convertView == null || ((ViewHolder) convertView.getTag()).mark == MARK.DELETE;
            if (needInflate) {
                return new ViewHolder(context, layoutId);
            }
            return (ViewHolder) convertView.getTag();
        }

        public View getConvertView() {
            return mConvertView;
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public ViewHolder putText(int viewId, String text) {
            View v = getView(viewId);
            if (v instanceof TextView) {
                ((TextView) v).setText(text);
            }
            return this;
        }

        public ViewHolder putText(int viewId, int resId) {
            View v = getView(viewId);
            if (v instanceof TextView) {
                ((TextView) v).setText(resId);
            }
            return this;
        }

        public ViewHolder putImg(int viewId, int resId) {
            View v = getView(viewId);
            if (v instanceof ImageView) {
                ((ImageView) v).setImageResource(resId);
            }
            return this;
        }

        /**
         * @param viewId     视图Id
         * @param resId      图片资源Id
         * @param roundShape 是否是圆角图片
         * @return
         */
        public ViewHolder putImg(int viewId, int resId, boolean roundShape) {
            if (roundShape) {
                View v = getView(viewId);
                if (v instanceof ImageView) {
                    Bitmap bitmap = Uview.getBitmapByXfermode(v.getContext(),
                            resId,
                            Color.parseColor("#993382"),
                            Uscreen.dp2Px(v.getContext(), 48),
                            Uscreen.dp2Px(v.getContext(), 48),
                            PorterDuff.Mode.SRC_IN);
                    ((ImageView) v).setImageBitmap(bitmap);
                }
            } else {
                return putImg(viewId, resId);
            }

            return this;
        }

        public ViewHolder putImg(int viewId, Bitmap bitmap) {
            View v = getView(viewId);
            if (v instanceof ImageView) {
                ((ImageView) v).setImageBitmap(bitmap);
            }
            return this;
        }

        public ViewHolder putImg(int viewId, Drawable drawable) {
            View v = getView(viewId);
            if (v instanceof ImageView) {
                ((ImageView) v).setImageDrawable(drawable);
            }
            return this;
        }

        public void putCbtn(int viewId, int status, boolean clickable) {
            View v = getView(viewId);
            if (v instanceof CompoundButton) {
                CompoundButton cb = (CheckBox) v;
                if (status != 0) {
                    cb.setChecked(true);
                } else {
                    cb.setChecked(false);
                }
                cb.setClickable(clickable);
            }
        }

        //----------------- 网络加载图片（2016.03.26）

        /**
         * 网络加载图片；（使用了开源库：Picasso）[Picasso](https://github.com/square/picasso)
         *
         * @param viewId     要设置的ImageView或者
         * @param url        要显示的图片地址
         * @param roundShape 是否设置圆角；
         * @return 返回自己，链式编程；
         */
        public ViewHolder putImg(final int viewId, final String url, boolean roundShape) {
            return putImg(viewId, url, R.mipmap.ic_launcher, roundShape);
        }

        public ViewHolder putImg(final int viewId, final String url, final int placeholderImgId, boolean roundShape) {
            // 如果 viewId不是继承自 ImageView 或者 url为null, 则不做任何处理；
            if (!(getView(viewId) instanceof ImageView) || url == null) {
                return this;
            }

            final ImageView imageView = getView(viewId);
            final Context context = imageView.getContext();

            if (!roundShape) {
                // 该库已经做了错位处理了（如果只是将加载的图片加载到ImageView的话，就不需要错位问题）；
                Picasso.with(context).load(url).placeholder(placeholderImgId).into(imageView);
                return this;
            } else {
                new AsyncTask<Void, Void, Bitmap>() {
                    Bitmap bitmap;

                    @Override
                    protected void onPreExecute() {
                        // 首先设置默认图片
                        bitmap = Uview.getBitmapByXfermode(context, placeholderImgId,
                                Color.parseColor("#993382"),
                                Uscreen.dp2Px(context, 48),
                                Uscreen.dp2Px(context, 48),
                                PorterDuff.Mode.SRC_IN);
                        imageView.setImageBitmap(bitmap);

                        // 由于只是从网络获取图片，没有处理错位问题，这里需要单独处理；
                        // 防止图片过多导致显示错乱（用url来作为验证）；
                        imageView.setTag(url);
                    }

                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        try {
                            bitmap = Picasso.with(context).load(url).placeholder(placeholderImgId).get();
                            // 设置圆角
                            bitmap = Uview.getBitmapByXfermode(context, bitmap,
                                    Color.parseColor("#993382"),
                                    Uscreen.dp2Px(context, 48),
                                    Uscreen.dp2Px(context, 48),
                                    PorterDuff.Mode.SRC_IN);
                        } catch (IOException e) {
                            // 网络不可用时，使用默认图片
                        }

                        return bitmap;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        if (bitmap == null) {
                            return;
                        }

                        // 防止图片错乱；
                        String url2 = (String) imageView.getTag();
                        if (url.equals(url2)) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                }.execute();
            }
            return this;
        }
        // ~~~~~~~~~~~~~~~~

    }
}
