package com.lou.as.lou;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.adapter.LouAdapter;
import com.lyloou.lou.util.Utoast;

import java.util.Arrays;

public class DemoAdapterActivity extends LouActivity {

    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(initListView());
    }

    private View initListView() {
        final LouAdapter<String> adapter = new LouAdapter<String>(new ListView(mContext), android.R.layout.simple_list_item_1) {
            @Override
            protected void assign(ViewHolder holder, String s) {
                // 用视图显示数据
                holder.putText(android.R.id.text1, s);
            }
        };

        // 初始化列表
        adapter.initList(Arrays.asList("添加元素", "长按更新元素"));

        // 添加点击事件
        adapter.getBindView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.getItem(position).equals("添加元素")) {
                    // 添加元素
                    adapter.addItem("我是新元素" + adapter.getCount());
                } else if (adapter.getItem(position).equals("删除我呀")) {
                    // 删除元素（有删除动画）
                    adapter.deleteItemWithAnim(position);
                } else if (adapter.getItem(position).contains("我是新元素")) {
                    adapter.getList().set(position, "删除我呀");
                    adapter.updateView(position);
                } else if (adapter.getItem(position).equals("好吧，我投降")) {
                    adapter.deleteItemWithAnim(position);
                }
            }
        });
        adapter.getBindView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (adapter.getItem(position).equals("长按更新元素")) {
                    adapter.getList().set(position, "已被更新的元素");
                    adapter.updateView(position); // 局部更新
                } else if (adapter.getItem(position).equals("已被更新的元素")) {
                    adapter.getList().set(position, "都更新过了，还按我做啥");
                    adapter.updateView(position);
                } else if (adapter.getItem(position).equals("都更新过了，还按我做啥")) {
                    adapter.getList().set(position, "再按我，我就消失");
                    adapter.updateView(position);
                } else if (adapter.getItem(position).equals("再按我，我就消失")) {
                    adapter.getList().set(position, "好吧，我投降");
                    adapter.updateView(position);
                } else if (adapter.getItem(position).equals("添加元素")) {
                    Utoast.show(mContext, "滚犊子，连我也想删？\n活得不耐烦了！");
                    return true;
                }
                return false;
            }
        });

        return adapter.getBindView();
    }
}
