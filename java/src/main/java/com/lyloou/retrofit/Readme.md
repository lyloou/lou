

| 在使用官网的例子时(GitHubService)，遇到错误：
`java.lang.IllegalArgumentException: Unable to create converter for java.util.List<com.lyloou.retrofit.Repo>
    for method GitHubService.listRepos`
解决办法是，在创建 Retrofit 时，通过添加 .addConverterFactory(GsonConverterFactory.create()) 来增加 Gson 格式转换
http://stackoverflow.com/questions/32367469/unable-to-create-converter-for-my-class-in-android-retrofit-library

## 参考资料
- http://square.github.io/retrofit/
- [你真的会用Retrofit2吗?Retrofit2完全教程](http://www.jianshu.com/p/308f3c54abdd)
- [用 Retrofit 2 简化 HTTP 请求](https://realm.io/cn/news/droidcon-jake-wharton-simple-http-retrofit-2/)
- [Retrofit2.0使用详解](http://blog.csdn.net/ljd2038/article/details/51046512)
- [Can Retrofit with OKHttp use cache data when offline](https://stackoverflow.com/questions/23429046/can-retrofit-with-okhttp-use-cache-data-when-offline?noredirect=1&lq=1)