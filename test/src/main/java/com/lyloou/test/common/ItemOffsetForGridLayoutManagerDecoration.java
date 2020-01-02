package com.lyloou.test.common;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * [Android - ItemOffsetDecoration for GridLayoutManager in RecycleView | android Tutorial](https://riptutorial.com/android/example/32125/itemoffsetdecoration-for-gridlayoutmanager-in-recycleview)
 */
public class ItemOffsetForGridLayoutManagerDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public ItemOffsetForGridLayoutManagerDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public ItemOffsetForGridLayoutManagerDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildLayoutPosition(view);

        GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();

        if (position < manager.getSpanCount())
            outRect.top = mItemOffset;

        if (position % 2 != 0) {
            outRect.right = mItemOffset;
        }

        outRect.left = mItemOffset;
        outRect.bottom = mItemOffset;
    }
}