###【关键词】
`修改皮肤` `自定义View`
***
###【 问题】
* 让改变皮肤变得简单；

###【效果图】 
![ratiocolor.gif](http://img.blog.csdn.net/20160512181253339)
###【分析】
**「动态加载皮肤分析」**

* 改变背景后将颜色值保存到`SharedPreferences`中；
* 当切换或回退到另一个界面，在显示之前，即对应声明周期`onStart`中对背景进行变化（也可以在设置背景的时候通过广播的方式及时修改另一个界面的背景）

**「自定义控件分析」**

* 每一次都只选择一个颜色，根据这个特性，我选择了继承RadioGroup，每一个元素都是一个RadioButton（这样，有多少个背景，就添加多少个对应的颜色元素）；
* 通过Selector来覆盖RadioButton的默认样式，选中的元素，圆圈要大一些（颜色通过java代码动态控制）；
* 改变颜色时能及时获取到值（对外提供一个改变时的接口回调，将改变的值回传给客户端）；
* 可以直接让某个颜色处于选中状态；
* 设置点击时的效果，增加趣味性；

###【难点】
* 动态修改颜色和选中状态；
* 当元素比较多时的滑动处理；

###【解决方案】
* 参考代码；

###【代码】
*「Activity代码」*
- [RatioColorActivity.java](https://github.com/lyloou/lou/blob/demo/loudemo/src/main/java/com/lou/as/lou/view/ratio_color/RatioColorActivity.java)


*「通用的自定义View源码」*
- [RatioColor.java](https://github.com/lyloou/lou/blob/demo/loulib/src/main/java/com/lyloou/lou/view/RatioColor.java)

*「Selector代码」*
view_ratio_color_selector.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_checked="true">
        <layer-list>
            <item android:bottom="4dp" android:left="4dp" android:right="4dp" android:top="4dp">
                <shape android:shape="oval">
                    <solid android:color="@android:color/transparent" />
                </shape>
            </item>
        </layer-list>
    </item>
 
    <item android:state_checked="false">
        <layer-list>
            <item android:bottom="12dp" android:left="12dp" android:right="12dp" android:top="12dp">
                <shape android:shape="oval">
                    <solid android:color="@android:color/transparent" />
                </shape>
            </item>
        </layer-list>
    </item>
</selector>
```
###【扩展】
* 目前对控件只做了横向处理，可以尝试下对控件进行纵向布局并处理滑动和回弹效果处理；

### 案例
- [https://github.com/lyloou/lou/blob/demo/loudemo/src/main/java/com/lou/as/lou/view/ratio_color](https://github.com/lyloou/lou/blob/demo/loudemo/src/main/java/com/lou/as/lou/view/ratio_color)

***
###【参考资料】

* [listview所带来的滑动冲突](http://blog.csdn.net/singwhatiwanna/article/details/8863232)
* [Android-onInterceptTouchEvent()和onTouchEvent()总结](http://blog.csdn.net/lvxiangan/article/details/9309927)
> 以此类推，我们可以得到各种具体的情况，整个layout的view类层次中都有机会截获，而且能看出来外围的容器view具有优先截获权。

* 《Android开发艺术探索》第三章——View的滑动冲突

