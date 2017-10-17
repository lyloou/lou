- [Android：dagger2让你爱不释手-基础依赖注入框架篇 - 简书](http://www.jianshu.com/p/cd2c1c9f68d4)
- [Android：dagger2让你爱不释手-重点概念讲解、融合篇 - 简书](http://www.jianshu.com/p/1d42d2e6f4a5)
- [Android：dagger2让你爱不释手-终结篇 - 简书](http://www.jianshu.com/p/65737ac39c44)

- [Dagger2的使用介绍与原理分析 | jasonli822的博客](http://jasonli822.github.io/2016/03/24/dagger2-working-analysis/)
- [使用Dagger2前你必须了解的一些设计原则 - 简书]()
    依赖注入，用简单的话来说，不在类中实例化其他依赖的类，而是先把依赖的类实例化了，然后以参数的方式传入构造函数中，让上层模块和依赖进一步解耦。
    - set的方式：setUser(user);
    - 构造方法：Control(User user);
    - Injector注入器；


## dagger2对目标类进行依赖注入的过程
```
步骤1：查找Module中是否存在创建该类的方法。
步骤2：若存在创建类方法，查看该方法是否存在参数
    步骤2.1：若存在参数，则按从**步骤1**开始依次初始化每个参数
    步骤2.2：若不存在参数，则直接初始化该类实例，一次依赖注入到此结束
步骤3：若不存在创建类方法（module没有提供），则查找Inject注解的构造函数，看构造函数是否存在参数
    步骤3.1：若存在参数，则从**步骤1**开始依次初始化每个参数
    步骤3.2：若不存在参数，则直接初始化该类实例，一次依赖注入到此结束

- [Android：dagger2让你爱不释手-终结篇 - 简书](http://www.jianshu.com/p/65737ac39c44)
```