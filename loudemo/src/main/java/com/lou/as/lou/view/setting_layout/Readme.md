# 【Android】通用系列 —— 快速搭建设置界面

## 【关键词】
`通用系列` `设置界面` `自定义View`

## 【问题】
· 减少重复性代码，快速搭建设置界面（通过简单的配置，就可以达到想要的布局）；

## 【效果图】
![效果图](https://img-blog.csdn.net/20160507202936768)

## 【分析】
- 设置界面大同小异，无非由标题，内容，图标等元素组成；
- 既然每一个设置项都有Title，那么就用Title的strId来作为它的唯一标识（便于点击等处理）；
- 复杂的地方在分割线的处理方式上（是整行显示，还是不要显示，又或者是在图像后面显示）；

## 【解决方案】
- 使用布局文件作为每一个Item的视图（便于灵活修改）；
- 继承自LinearLayout，要可以添加分割线，可以添加tips提示，还可以添加项与项之间的空白；
- 可以对视图的文字信息进行动态修改；
- 要具备添加其他视图的灵活性；
- 参考下方「用法展示」和「源代码 」；

## 【代码】
**「用法展示」** 
第一步： 添加字符串资源：
```xml
<string name="user_name">用户名</string>
```

第二步： 设置Activity的布局文件：
```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
    android:background="#f5f5f5">

    <com.lyloou.lou.view.SettingLayout
        android:id="@+id/svg_set"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"/>
</ScrollView>
```

第三步： 配置Item并添加Item到SettingLayout：
```java
// 找到SettingLayout
SettingLayout sl = (SettingLayout) findViewById(R.id.svg_set);
SettingLayout.Item item = new SettingLayout.Item(int titleStrId, int iconResId, String contentStr, String unitStr, boolean hasToRight, SEP sep,IClickListener listener);
sl.addItem(item);
// 之后的如何修改界面呢，可以对item的属性进行重新赋值，然后后通过`sl.refreshItem(item)`来使设置生效;
```

Item参数对应关系 
```java
/**
 * @param titleStrId 字符串ID，用来标识Item（不允许重复，且必须有效）
 * @param iconResId  最左边的Icon图标（当等于0时，不显示图标）
 * @param contentStr 内容字符串
 * @param unitStr    单位字符串（之所以没有和内容字符串合并，目的是更灵活地获取内容）
 * @param hasToRight 是否显示向右的图标
 * @param sep        分割线样式
 * @param listener   点击Item发生的事件
 */
```

## 【效果图代码】
-[https://github.com/lyloou/lou/tree/demo/loudemo/src/main/java/com/lou/as/lou/view/setting_layout](https://github.com/lyloou/lou/tree/demo/loudemo/src/main/java/com/lou/as/lou/view/setting_layout)