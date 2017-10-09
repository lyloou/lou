
## 《Android进阶之光》
### 线程的创建方法
- 继承Thread类，重写run()方法；
- 实现Runnable接口，实现run()方法；
- 实现Callable接口，实现call()方法；
    Callable可以在任务结束后提供一个返回值，Runnable无法提供这个功能；
    Callable可以在call()方法中抛出异常，Runnable的run()方法不能；
(p168)

###


## 参考链接
- http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/
- `../java8/ThreadMain.java`
- [Why Are Thread.stop, Thread.suspend,
   Thread.resume and Runtime.runFinalizersOnExit Deprecated? ](http://docs.oracle.com/javase/1.5.0/docs/guide/misc/threadPrimitiveDeprecation.html)
