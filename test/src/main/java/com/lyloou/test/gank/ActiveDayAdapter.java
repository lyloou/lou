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

package com.lyloou.test.gank;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.NetWork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ActiveDayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private List<ActiveDay> mList;
    private boolean mMaxed;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private String mTitle;
    private int mMode; // 0:表示正常模式；1：表示多选模式；

    ActiveDayAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        mMode = mode;
    }

    public List<ActiveDay> getList() {
        return mList;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void clearSelected() {
        for (ActiveDay activeDay : mList) {
            if (activeDay.isSelected())
                activeDay.setSelected(false);
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.item_gank_welfare, parent, false);
            return new ActiveDayHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_gank_header, parent, false);
            return new HeaderHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.item_gank_footer, parent, false);
            return new FooterHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " make sure you using types correctly.");
    }

    public boolean isMaxed() {
        return mMaxed;
    }

    public void setMaxed(boolean maxed) {
        mMaxed = maxed;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ActiveDayHolder) {
            ActiveDayHolder holder = (ActiveDayHolder) viewHolder;

            ActiveDay activeDay = mList.get(position - 1); // 注意需要减去header的数量
            if (mMode == 0) {
                holder.cbItem.setVisibility(View.GONE);
            } else if (mMode == 1) {
                holder.cbItem.setVisibility(View.VISIBLE);
                holder.cbItem.setChecked(activeDay.isSelected());
            }
            holder.tvItem.setText(activeDay.getDay());

            ImageView ivItem = holder.ivItem;
            ivItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onClick(holder.getAdapterPosition(), activeDay);
                }
            });
            ivItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mItemClickListener.onLongClick(ivItem);
                    return true;
                }
            });
            loadWelfareToImageView(activeDay.getDay(), ivItem);

        } else if (viewHolder instanceof HeaderHolder) {
            HeaderHolder holder = (HeaderHolder) viewHolder;
            if (!TextUtils.isEmpty(mTitle)) {
                holder.tvHeader.setText(mTitle);
            }
        } else if (viewHolder instanceof FooterHolder) {
            FooterHolder holder = (FooterHolder) viewHolder;
            if (mMaxed) {
                holder.tvFooter.setText("----- 我是有底线的 -----");
            } else {
                holder.tvFooter.setText("加载中...");
            }
        }

    }

    private void loadWelfareToImageView(String activeDay, final ImageView ivPic) {


        String[] split = activeDay.split("-");
        if (split.length == 3) {
            String year = split[0];
            String month = split[1];
            String day = split[2];

            Call<ResponseBody> gankData = NetWork.getGankApi().getGankData(year, month, day);
            gankData.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseBody body = response.body();
                            if (body == null) {
                                return;
                            }
                            String string = body.string();
                            JSONObject jsonObject = new JSONObject(string);
                            JSONObject results = jsonObject.getJSONObject("results");
                            JSONArray welfares = results.getJSONArray("福利");
                            JSONObject welfare = welfares.getJSONObject(0);
                            String welfareUrl = welfare.getString("url");

                            Context applicationContext = mContext.getApplicationContext();
                            Glide.with(applicationContext)
                                    .load(welfareUrl)
                                    .asBitmap()
                                    .placeholder(R.mipmap.bg)
                                    .centerCrop()
                                    .into(ivPic);
                            ivPic.setTag(ivPic.getId(), welfareUrl);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });


        } else {
            Toast.makeText(mContext, "格式不对：" + activeDay, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        if (position > getListSize()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return getListSize() + 2;
    }

    public void clearAll() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ActiveDay> activeDays) {
        mList.addAll(activeDays);
        notifyItemRangeInserted(getListSize(), activeDays.size());
    }

    public int getListSize() {
        return mList.size();
    }

    public int getItemTypeCount() {
        return 3;
    }

    interface OnItemClickListener {
        void onClick(int realPosition, ActiveDay activeDay);

        void onLongClick(ImageView view);
    }

    private static class ActiveDayHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvItem;
        ImageView ivItem;
        CheckBox cbItem;

        ActiveDayHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvItem = (TextView) view.findViewById(R.id.tv_item);
            ivItem = view.findViewById(R.id.iv_item);
            cbItem = view.findViewById(R.id.cb_item);
            cbItem.setClickable(false);
        }
    }

    private static class HeaderHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;

        HeaderHolder(View view) {
            super(view);
            tvHeader = view.findViewById(R.id.tv_header);
        }
    }

    private static class FooterHolder extends RecyclerView.ViewHolder {
        TextView tvFooter;

        FooterHolder(View view) {
            super(view);
            tvFooter = (TextView) view.findViewById(R.id.tv_footer);
        }
    }
}