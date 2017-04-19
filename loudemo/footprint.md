| SwipeRefreshLayout
- [hanks-zyh/SwipeRefreshLayout: 谷歌的下拉刷新，新的界面效果](https://github.com/hanks-zyh/SwipeRefreshLayout)
- [SwipeRefreshLayout | Android Developers](https://developer.android.com/reference/android/support/v4/widget/SwipeRefreshLayout.html?hl=zh-cn)
注意：
通过`ViewCompat.canScrollVertically(mScrollUpChild, -1)`，可以来判断被包含的子child是否可以向上滑动；
（参考 `com.example.android.architecture.blueprints.todoapp.tasks.ScrollChildSwipeRefreshLayout`）

| [Google-Play-Style-Tabs-using-TabLayout](https://github.com/codepath/android_guides/wiki/Google-Play-Style-Tabs-using-TabLayout)

| [TimesSquare for Android](https://github.com/square/android-times-square)
```java
private void initView() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.YEAR, 2);

    Date today = new Date();
    mCpvTimesquare.init(today, calendar.getTime(), Locale.CHINA).withSelectedDate(today);
}
```