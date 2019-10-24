package com.lyloou.test.man;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyloou.test.R;
import com.lyloou.test.util.Udialog;

import java.util.List;

class ManAdapter extends RecyclerView.Adapter<ManAdapter.ViewHolder> {
    private List<Data> mList;
    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    ManAdapter(List<Data> list) {
        this.mList = list;
    }

    @Override
    public ManAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line_three, null);
        return new ManAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ManAdapter.ViewHolder holder, int position) {
        Data data = this.mList.get(position);
        String title = data.getTitle();
        String url = data.getUrl();
        String lastUrl = data.getLastUrl();
        holder.tvTitle.setText(title);
        holder.tvUrl.setText(url);
        holder.tvLastUrl.setText(TextUtils.isEmpty(lastUrl) ? url : lastUrl);
        holder.view.setOnClickListener(v -> WebActivity.newInstance(holder.view.getContext(), data));
        holder.view.setOnLongClickListener(v -> {
            Udialog.alert(v.getContext(), "清除它的历史记录：" + title, ok -> {
                if (ok) {
                    data.setLastUrl(null);
                    data.setPosition(0);
                    if (mListener != null) {
                        mListener.onDataChanged(data);
                    }
                }
            });
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvUrl;
        TextView tvLastUrl;
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvTitle = itemView.findViewById(R.id.tv_one);
            tvUrl = itemView.findViewById(R.id.tv_two);
            tvLastUrl = itemView.findViewById(R.id.tv_three);
        }
    }
}