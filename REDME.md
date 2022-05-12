## 介绍

基于CompletableFuture对多线程串行化执行的扩展和精简，通过组合的关系避开CompletableFuture中复杂的逻辑结构，并精炼出其核心常用的功能进行扩展。简化CompletableFuture的串行化执行方式，对CompletableFuture并行化执行结果的后去方式进行扩充，并将两者进行结合，实现串行化和并行化的互相转化。

使用
---

引入依赖：

~~~xml
<dependency>
    <groupId>com.deep.crow</groupId>
    <artifactId>crow</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
~~~

### 串行化

通过本项目进行串行话的任务执行有两中方式：

串行化的执行过程如下图所示：

~~~mermaid
graph LR
A --> B --> D --> E
~~~

#### 1，Multi

> 直接使用CompletableFuture的扩展类：Multi，使用方式如下：

~~~java
// 借助工具类MultiHelper创建Multi
public static void main(String[] args) {
    ExecutorService executorService = ThreadPool.executorService();
    Integer join = MultiHelper.create(executorService)
        .thenRun(() -> System.out.println(5))
        .thenApply(unused -> 41)
        .thenApply(integer -> integer - 10)
        .join();
    System.out.println(join);
}
// 输入：
// 5
// 31
~~~

Multi提供的方法是对CompletableFuture精简后的方法，分别是：

~~~java
// 单个Multi的串行化执行
<U> Multi<U> thenApply(Function<? super T, ? extends U> fn);
Multi<Void> thenAccept(Consumer<? super T> action);
Multi<Void> thenRun(Runnable action);
// 多个Multi的聚合操作
<U, V> Multi<V> thenCombine(Multi<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn);
<U> Multi<Void> thenBiAccept(Multi<? extends U> other, BiConsumer<? super T, ? super U> action);
Multi<Void> runRunBoth(Multi<?> other, Runnable action);
<U> Multi<U> applyFun(Multi<? extends T> other, Function<? super T, U> fn);
Multi<Void> acceptFun(Multi<? extends T> other, Consumer<? super T> action);
Multi<Void> runFun(Multi<?> other, Runnable action);、
<U> Multi<U> thenCompose(Function<? super T, ? extends Multi<U>> fn);
// 针对异常的操作
Multi<T> exceptionally(Function<Throwable, ? extends T> fn);
Multi<T> whenComplete(BiConsumer<? super T, ? super Throwable> action);
<U> Multi<U> handle(BiFunction<? super T, Throwable, ? extends U> fn);
~~~

#### 2，SerialMulti

> 通过对Multi进一步封装，就形成了SerialMulti，通过SerialMulti可以更加简便的使用Multi，如下:

~~~java
public static void main(String[] args) {
    ExecutorService executorService = ThreadPool.executorService();
    String join = SerialMulti.of(executorService)
        .add(() -> 20)
        .add(integer -> integer + 5)
        .add(integer -> {
            System.out.println(integer);
        })
        .add(() -> System.out.println(10))
        .add(() -> "测试")
        .join();
    System.out.println(join);
}
// 结果为：
// 25
// 10
// 测试
~~~

SerialMulti是对Multi的简单疯转，仅仅包含有关串行化的核心方法，如thenApply，thenAccept，thenRun和exceptionally

### 并行化

并行化的执行过程如下图所示：

~~~mermaid
graph LR
A --> D
A --> C
A --> B

~~~

上图中的A代表任务的起点，可能是一个独立的任务也可能没有任何意义，总而言之，B、C、D三个任务是完全独立的。

此模式的任务可以通过ParallelMulti进行构建，如下：

~~~java
public static void main(String[] args) {
    ExecutorService executorService = ThreadPool.executorService();
    List<?> list = ParallelMulti.of(executorService)
        .add(() -> 12)
        .add(() -> 50)
        .add(() -> "测试")
        .resultList();
    System.out.println(list);
}
// 结果：
// [12, 50, 测试]
~~~

并行化执行的结果是按照任务装配的顺序输出的，通过这个特性可以精准的获取到对应的结果。

### 复杂并行化

#### 1，无交集并行化

以上是对简单并行化任务的执行实例，在实际的业务流程中，一个并行化任务的子节点可能就是一个串行化的任务，如下图：

~~~mermaid
graph LR
A --> D --> E
A --> C --> F
A --> B --> G

~~~

同样，A可以是一个无意义的节点，上图中，B和G，C和F，D和E三个组之间毫无关联，但是各个组内是串行化的关系，这是一种复杂的并行化。

同样可以使用ParallelMulti来实现上述模型的机制：

~~~java
~~~



