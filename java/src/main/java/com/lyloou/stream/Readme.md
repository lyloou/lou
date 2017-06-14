| map: one -> one
  flatmap: one -> none, one, multiple

| 在复杂的方法链中，先过滤（filter），然后再转换（map）或排序（sorted），这样会使执行效率更高。
> Keep that in mind when composing complex method chains.

| anyMatch: return `true` as soon as the predicate applies to the given
  input method.

| Intermediate operations will only be executed when a terminal
  operations is present.
  只有在 terminal 操作符执行的时候，intermediate 操作才会得到执行。

| Most of those operations must be both _non-interfering_ and _stateless_.
  这意味着，stream的操作过程中，不影响数据源的变化、引用的外部域是不可变的。

- [Java 8 Stream Tutorial](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/)
- [Package java.util.stream](http://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)