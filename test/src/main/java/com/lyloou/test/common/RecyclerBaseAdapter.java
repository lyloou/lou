package com.lyloou.test.common;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 参考《android 开发进阶从小工到专家》 C12
 *
 * @param <D> 数据的类型
 * @param <V> viewHolder
 */
public abstract class RecyclerBaseAdapter<D, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    protected final List<D> mList = new ArrayList<>();
    private OnItemClickListener<D> mItemClickListener;

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected D getItem(int position) {
        return mList.get(position);
    }

    public void clear() {
        mList.clear();
        notifyItemRangeRemoved(0, mList.size());
    }

    public void addItems(List<D> list) {
        int oldLength = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(oldLength, list.size());
    }

    @Override
    public void onBindViewHolder(@NonNull V holder, int position) {
        D item = getItem(position);
        bindDataToItemView(holder, item);
        setupItemViewClickListener(holder, item);
    }

    private void setupItemViewClickListener(V holder, D item) {
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(item);
            }
        });
    }

    protected abstract void bindDataToItemView(V holder, D item);

    protected View inflateItemView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }
}
