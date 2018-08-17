package com.lou.as.lou.feature;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.adapter.LouAdapter;
import com.lyloou.lou.util.Utoast;

import java.util.Arrays;

public class AdapterActivity extends LouActivity {

    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(initListView());
    }

    private View initListView() {
        LouAdapter<String> adapter = new LouAdapter<String>(
                new ListView(mContext), android.R.layout.simple_list_item_1) {
            @Override
            protected void assign(ViewHolder holder, String s) {
                // 用视图显示数据
                holder.putText(android.R.id.text1, s);
            }
        };

        // 初始化列表
        adapter.initList(Arrays.asList("添加元素", "长按更新元素"));

        // 添加点击事件
        adapter.getBindView().setOnItemClickListener((parent, view, position, id) -> {
            switch (adapter.getItem(position)) {
                case "添加元素":
                    // 添加元素
                    adapter.addItem("我是新元素");
                    break;
                case "删除我呀":
                    // 删除元素（有删除动画）
                    adapter.deleteItemWithAnim(position);
                    break;
                case "我是新元素":
                    adapter.getList().set(position, "删除我呀");
                    adapter.updateView(position);
                    break;
                case "好吧，我投降":
                    adapter.deleteItemWithAnim(position);
                    break;
            }
        });
        adapter.getBindView().setOnItemLongClickListener((parent, view, position, id) -> {
            switch (adapter.getItem(position)) {
                case "长按更新元素":
                    adapter.getList().set(position, "已被更新的元素");
                    adapter.updateView(position); // 局部更新
                    break;
                case "已被更新的元素":
                    adapter.getList().set(position, "都更新过了，还按我做啥");
                    adapter.updateView(position);
                    break;
                case "都更新过了，还按我做啥":
                    adapter.getList().set(position, "再按我，我就消失");
                    adapter.updateView(position);
                    break;
                case "再按我，我就消失":
                    adapter.getList().set(position, "好吧，我投降");
                    adapter.updateView(position);
                    break;
                case "添加元素":
                    Utoast.show(mContext, "滚犊子，连我也想删？\n活得不耐烦了！");
                    return true;
            }
            return false;
        });

        return adapter.getBindView();
    }
}
