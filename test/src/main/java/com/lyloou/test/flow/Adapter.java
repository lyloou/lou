package com.lyloou.test.flow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyloou.test.R;

import java.util.Set;

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    final Set<FlowDay> mSet;
    final Context context;
    Listener mListener;

    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    public Set<FlowDay> getList() {
        return mSet;
    }

    Adapter(Context context, Set<FlowDay> mSet) {
        this.context = context;
        this.mSet = mSet;
    }

    public void remove(FlowDay flowDay) {
        if (mSet == null) {
            return;
        }
        mSet.remove(flowDay);
    }

    public void add(FlowDay flowDay) {
        if (mSet == null) {
            return;
        }
        mSet.add(flowDay);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flow_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FlowDay flowDay = mSet.toArray(new FlowDay[0])[position];
        holder.tvTitle.setText(flowDay.getDay());
        if (flowDay.isSynced()) {
            holder.ivSyncStatus.setImageResource(0);
        } else {
            holder.ivSyncStatus.setImageResource(R.drawable.ic_sync_problem_black_24dp);
        }
        holder.view.setOnClickListener(v -> {
            if (mListener == null) {
                return;
            }
            mListener.onItemClicked(flowDay);
        });
        holder.view.setOnLongClickListener(view -> {
            if (mListener == null) {
                return false;
            }
            mListener.onItemLongClicked(flowDay);
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return mSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivSyncStatus;
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivSyncStatus = itemView.findViewById(R.id.iv_sync_status);
            tvTitle = itemView.findViewById(R.id.tv_one);
            tvTitle.setGravity(Gravity.CENTER);
        }
    }
}