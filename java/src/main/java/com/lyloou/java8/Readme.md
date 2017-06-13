| But the Java8 API is also full of new functional interfaces to make
your life easier.

| Default methods cannot be accessed from within lambda expressions.

| 给接口添加 `@FunctionalInterface`注解的目的：
The compiler is aware of this annotation and throw a compiler error as
soon as you try to add a second abstract method declaration to the
interface.
https://github.com/winterbe/java8-tutorial#functional-interfaces
另外可以参考 `@FunctionalInterface`的源码文档。


## 参考资料
- [java8-tutorial](https://github.com/winterbe/java8-tutorial)