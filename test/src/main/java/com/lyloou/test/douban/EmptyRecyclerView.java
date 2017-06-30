/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.test.douban;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.02 10:40
 * <p>
 * Description: 给RecyclerView 设置空试图
 * Link: [Empty View for Android's RecyclerView](http://akbaribrahim.com/empty-view-for-androids-recyclerview/)
 */
public class EmptyRecyclerView extends RecyclerView {
    private ICallBack mCallBack;
    private View mEmptyView;
    private int mItemTypeCount = 1;
    AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCallBack(ICallBack callBack) {
        mCallBack = callBack;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        checkIfEmpty();
    }

    public void setItemTypeCount(int itemTypeCount) {
        this.mItemTypeCount = itemTypeCount;
    }

    private void checkIfEmpty() {
        if (mEmptyView != null && getAdapter() != null) {
            boolean isEmpty = getAdapter().getItemCount() <= mItemTypeCount - 1;
            mEmptyView.setVisibility(isEmpty ? VISIBLE : GONE);
            setVisibility(isEmpty ? GONE : VISIBLE);
            if (isEmpty && mCallBack != null) {
                mCallBack.whenEmpty();
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(mObserver);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mObserver);
        }
        checkIfEmpty();
    }

    public interface ICallBack {
        void whenEmpty();
    }
}
