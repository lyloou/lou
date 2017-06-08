## 知识点
- RxJava1.0
- Retrofit2.0
- Glide

## 功能
- 下拉刷新
- 拉到底部加载更多

## [使用RxJava优雅的处理服务器返回异常 - 简书](http://www.jianshu.com/p/dcb06efb6e3f)
需要注意的是，在定义 Service 的时候，泛型是：
`HttpResult<User>` or `HttpResult<List<Subject>>` (即 json对象 or json数组)

## 注意：
| gson 的生成要和 json字符串对象名一一对应。
也就是说：
```java
int count;
int start;
int total;
String title;
T subjects;
```
其中的 `subjects` 不可以是`data`，否则解析不出来。


- [Android RecyclerView with header, footer and pagination](http://www.gadgetsaint.com/android/recyclerview-header-footer-pagination/#.WTkbk5B96Cp)