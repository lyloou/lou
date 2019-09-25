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

package com.lyloou.test.flow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lyloou.test.R;

import java.util.ArrayList;
import java.util.List;

class FlowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FlowItem> mList;
    private Context mContext;
    private OnItemListener mItemListener;

    FlowAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public void setOnItemListener(OnItemListener itemClickListener) {
        mItemListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_flow, parent, false);
        return new FlowHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FlowHolder holder = (FlowHolder) viewHolder;
        FlowItem item = mList.get(position);
        holder.tvTimeStart.setText(getFormatTime(item.getTimeStart()));
        holder.tvTimeEnd.setText(getFormatTime(item.getTimeEnd()));
        holder.tvTimeSep.setText(item.getTimeSep());
        holder.tvSpend.setText(item.getSpend());
        final EditText etContent = holder.etContent;
        etContent.setText(item.getContent());
        addChangeListener(etContent, item);

        holder.tvTimeStart.setOnClickListener(view -> {
            if (mItemListener != null) {
                mItemListener.onClickTimeStart(item);
            }
        });
        holder.tvTimeEnd.setOnClickListener(view -> {
            if (mItemListener != null) {
                mItemListener.onClickTimeEnd(item);
            }
        });
    }

    private void addChangeListener(EditText editText, FlowItem item) {
        // [How to get the Edit text position from Recycler View adapter using Text Watcher in android - Stack Overflow](https://stackoverflow.com/a/37916021)
        TextWatcher watcher = new TextWatcher() {
            private CharSequence s;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("TTAG", "beforeTextChanged:" + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("TTAG", "onTextChanged:" + s);
                this.s = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.hasFocus()) {
                    Log.e("TTAG", "afterTextChanged:" + s);
                    mItemListener.onTextChanged(item, this.s);
                }
            }
        };
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editText.addTextChangedListener(watcher);
            } else {
                editText.removeTextChangedListener(watcher);
            }
        });
    }


    private String getFormatTime(String timeStr) {
        if (timeStr == null) {
            return "--:--";
        }
        return timeStr;
    }

    @Override
    public int getItemCount() {
        return getListSize();
    }

    public void setList(List<FlowItem> items) {
        mList = items;
    }

    public void clearAll() {
        mList.clear();
    }

    public void addAll(List<FlowItem> items) {
        mList.addAll(items);
    }

    public int getListSize() {
        return mList.size();
    }

    interface OnItemListener {
        void onClickTimeStart(FlowItem item);

        void onClickTimeEnd(FlowItem item);

        void onTextChanged(FlowItem item, CharSequence s);
    }

    private static class FlowHolder extends RecyclerView.ViewHolder {
        TextView tvTimeStart;
        TextView tvTimeEnd;
        TextView tvTimeSep;
        TextView tvSpend;
        EditText etContent;

        FlowHolder(View view) {
            super(view);

            tvTimeStart = view.findViewById(R.id.tv_time_start);
            tvTimeEnd = view.findViewById(R.id.tv_time_end);
            tvTimeSep = view.findViewById(R.id.tv_time_sep);
            tvSpend = view.findViewById(R.id.tv_spend);
            etContent = view.findViewById(R.id.et_content);
        }
    }
}