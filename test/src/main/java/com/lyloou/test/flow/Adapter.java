package com.lyloou.test.flow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyloou.test.R;

import java.util.Set;

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    final Set<FlowDay> list;
    final Context context;
    Listener mListener;

    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    public Set<FlowDay> getList() {
        return list;
    }

    Adapter(Context context, Set<FlowDay> list) {
        this.context = context;
        this.list = list;
    }

    public void remove(FlowDay flowDay) {
        if (list == null) {
            return;
        }
        list.remove(flowDay);
    }

    public void add(FlowDay flowDay) {
        if (list == null) {
            return;
        }
        list.add(flowDay);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flow_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FlowDay flowDay = list.toArray(new FlowDay[0])[position];
        holder.tvTitle.setText(flowDay.getDay());
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
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvTitle = itemView.findViewById(R.id.tv_one);
            tvTitle.setGravity(Gravity.CENTER);
        }
    }
}