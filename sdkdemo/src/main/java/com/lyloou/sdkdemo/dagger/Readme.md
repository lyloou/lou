- [Dagger2的使用介绍与原理分析 | jasonli822的博客](http://jasonli822.github.io/2016/03/24/dagger2-working-analysis/)
- [使用Dagger2前你必须了解的一些设计原则 - 简书]()
    依赖注入，用简单的话来说，不在类中实例化其他依赖的类，而是先把依赖的类实例化了，然后以参数的方式传入构造函数中，让上层模块和依赖进一步解耦。
    - set的方式：setUser(user);
    - 构造方法：Control(User user);
    - Injector注入器；