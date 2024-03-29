## 介绍

基于CompletableFuture对多线程串行化执行的扩展和精简，通过组合的关系避开CompletableFuture中复杂的逻辑结构，并精炼出其核心常用的功能进行扩展。简化CompletableFuture的串行化执行方式，对CompletableFuture并行化执行结果的后去方式进行扩充，并将两者进行结合，实现串行化和并行化的互相转化。



快速使用
---

引入依赖：

~~~xml
<dependency>
    <groupId>io.github.pizihao</groupId>
    <artifactId>crow</artifactId>
    <version>0.0.8</version>
</dependency>
~~~

### 串行化

通过本项目进行串行化的任务执行有两种方式：

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
// 结果为：
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

SerialMulti是对Multi的简单封装，仅仅包含有关串行化的核心方法，如thenApply，thenAccept，thenRun和exceptionally

#### 3，补充(0.0.8)

​	在0.0.8版本中为串行化过程添加了任务队列和结果集，这使得整个串行化过程的每个节点的执行过程一目了然，因为不再丢弃和覆盖之前的节点，所以串行化执行过程更加可控，即可以随时回退到中间的某个过程，也可以通过任务集直接查看整个任务中某个节点的执行结果

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
public static void main(String[] args) {
    ExecutorService executorService = ThreadPool.executorService();
    SerialMulti<Integer> serialMulti1 = SerialMulti.of(executorService, () -> 10)
        .add(integer -> integer + 10);
    SerialMulti<Integer> serialMulti2 = SerialMulti.of(executorService, () -> 20)
        .add(integer -> integer + 20);
    SerialMulti<Integer> serialMulti3 = SerialMulti.of(executorService, () -> 30)
        .add(integer -> integer + 30);

    List<?> objects = ParallelMulti.of(executorService)
        .add(serialMulti1)
        .add(serialMulti2)
        .add(serialMulti3)
        .resultList();

    System.out.println(objects);

}
// 结果：
// [20, 40, 60]
~~~

注意：ParallelMulti再执行时并不会保留中间过程的结果

#### 2，有交集并行化

~~~mermaid
graph LR
A --> D --> E --> H
A --> C --> F --> H
A --> B --> G --> H
~~~

以上仅是一个简略的模型，A同样可以看作是一个无意义的节点，三组并行执行的任务最终都汇聚向了H，H之后可能也会存在任务，此时A->H这个模型就可以看成是一个串行化结构的一个节点。这也是一种复杂的串行化模型

在ParallelMulti存在交集处理的方法：

~~~java
public static void main(String[] args) {
    ExecutorService executorService = ThreadPool.executorService();
    SerialMulti<Integer> serialMulti1 = SerialMulti.of(executorService, () -> 10)
        .add(integer -> integer + 10);
    SerialMulti<Integer> serialMulti2 = SerialMulti.of(executorService, () -> 20)
        .add(integer -> integer + 20);
    SerialMulti<Integer> serialMulti3 = SerialMulti.of(executorService, () -> 30)
        .add(integer -> integer + 30);

    ParallelMulti parallelMulti = ParallelMulti.of(executorService)
        .add(serialMulti1)
        .add(serialMulti2)
        .add(serialMulti3);

    parallelMulti.thenRun(() -> System.out.println("处理完成"));
    parallelMulti.thenExecList((Consumer<List<?>>) System.out::println);
    String thenExecList = parallelMulti.thenExecList(objects -> {
        System.out.println(objects);
        return "执行完成";
    });
    System.out.println(thenExecList);

}
// 结果
// 处理完成
// [20, 40, 60]
// [20, 40, 60]
// 执行完成
~~~

#### 3，并行化补充(0.0.8)

​	在0.0.7版本，并行化的执行有很大的缺陷，即任务的装入顺序为结果的获取顺序，尽管通过类型匹配和填充可以解决一大部分结果顺序造成的代码繁琐问题但是并行化任务的执行并不应该是一个加入任务和等待结果的过程，为了使并行化的任务节点更加可控，为每个节点添加索引，添加新的任务时可以指定索引，该索引也可以由并行化过程直接生成，通过索引使得节点的结果顺序完全可见。  

​	在0.0.8版本中针对并行化的异常也引入了新的处理逻辑，即在获取结果时再将异常节点加入的执行链中，异常节点可以全局添加也可以通过索引赋值。

### 任务调用顺序

在由一个根节点触发的多元化串行化的执行方法中，如果存在一个较长的链状调用，并且每个任务的执行时间都较长的话，会形成一个“先来后执行”的调用结构，这不同于一般的串行化和并行化结构，其本质依然是并行化，但是和并行化有着完全不同的任务调用顺序，比如：存在这样的几个任务，A，B，C，D，E，F，G

> A为根节点，由A衍生出B,C,D。如下图：

~~~mermaid
graph LR
A --> B
A --> C
A --> D
~~~

正常情况上看这是一个简单的并行化模型，在crow的简化下，这个链的执行顺序为B –> C -> D，并行化的结构下A只是BCD为一个组的象征，没有任何意义，当A有了实际的功能，A就成为了BCD三个任务的交点，如果按照装入顺序为B -> C -> D来看的话最终的调用顺序就是 A -> D -> C -> B，这是在每个任务的执行时间都较长的情况下。而经过这样的转化就无法通过转入的索引值来获取返回结果。

> 如果加入E，F，G在转入顺序是这样的情况下：

~~~mermaid
graph LR
A --> B --> E
A --> C --> F
A --> D --> G
~~~

考虑到正常业务中的方法耗时，其调用顺序是这样的：

~~~mermaid
graph LR
A --> D --> G
A --> C --> F
A --> B --> E
D --> C
C --> B
~~~

A -> D -> G -> C -> F -> B -> E,这种情况在单纯的串行化和并行化调用中并不会存在，如果试图在一个Multi上建立分支，如上图中的A -> B -> E 和 A -> C -> F 的关系，当然 Multi是支持这种情况的，不过这样的组织结构会让代码变得更加凌乱

> 如果出现了上述的业务情况，可以使用ParallelMulti来代替Multi，即A和B，C，D，E，F，G 区分来看，将B，C，D，E，F，G合并为一个ParallelMult此处使用P来表示。其和无交集并行化的结构相似，这样就可以使用SerialMulti来构建模型了，其结构为：A -> Z

## 更多

### 1，泛型支持

参考：[TypeBuilder](https://github.com/ikidou/TypeBuilder)，提供了快速创建泛型的方式，包含对泛型上限和下限的控制和泛型嵌套的控制。

1. 创建一个List<Integer\>

   ~~~java
   Type build = TypeBuilder.make(List.class)
       .add(Integer.class)
       .build();
   ~~~

2. 创建一个List<？super Integer\>

   ~~~java
   Type build = TypeBuilder.make(List.class)
       .addSuper(Integer.class)
       .build();
   ~~~

3. 创建一个Map<String, Integer\>，对于多个泛型的类来说，类型被确定的顺序就是泛型的顺序，如下：String会成为key，Integer会成为value

   ~~~java
   Type build = TypeBuilder.make(Map.class)
       .add(String.class)
       .add(Integer.class)
       .build();
   ~~~

4. 创建一个Map<String, List<Integer\>>，通过一个嵌套结构控制嵌套的泛型关系

   ~~~java
   Type build = TypeBuilder.make(Map.class)
       .add(String.class)
       .nested(List.class)
       .add(Integer.class)
       .parent()
       .build();
   ~~~

5. 创建一个String，如果不执行泛型则直接创建一个和Class兼容的类型

   ~~~java
   Type build = TypeBuilder.make(Integer.class)
       .build();
   ~~~

### 2，类型匹配

类型匹配是针对串行化的执行会出现多个结果的情况进行优化，让并行化的结果获取更加便捷

#### 1，Class匹配

主要针对结果不存在泛型的情况，其优点是速度快，缺点是存在局限性不支持泛型

示例：

~~~java
public static void main(String[] args) {
    ExecutorService executorService = ThreadPool.executorService();


    String str = ParallelMulti.of(executorService)
        .add(() -> 12)
        .add(() -> "Multi")
        .add(() -> new Thread())
        .get(String.class);

    System.out.println(str);
}
// 结果
// Multi
~~~

#### 2，Type匹配

在兼容Class匹配的基础上支持对泛型的匹配，TypeBuilder被用于类型的构建

示例：

~~~java
public static void main(String[] args) {
    ExecutorService executorService = ThreadPool.executorService();
    List<String> str = ParallelMulti.of(executorService)
        .add(() -> {
            List<Integer> list = new ArrayList<>();
            list.add(1);
            return list;
        })
        .add(() -> {
            List<String> list = new ArrayList<>();
            list.add("Multi");
            return list;
        })
        .add(() -> {
            List<Thread> list = new ArrayList<>();
            list.add(new Thread());
            return list;
        })
        .get(TypeBuilder.list(String.class));

    System.out.println(str);
}
// 结果
// [Multi]
~~~

#### 3，类型填充

区别于类型匹配功能，是将一个并行化任务的结果按照类型和顺序的匹配关系直接填充到一个类中。

示例：

~~~java
@Data
@ToString
public class Basic {

    List<String> strings;
    List<Integer> integers;
    List<Double> doubles;
    List<Float> floats;
    List<Short> shorts;
    List<Byte> bytes;
    List<Long> longs;
    List<Character> characters;
    List<Boolean> booleans;

}
// =================================================

public static void main(String[] args) {

    Basic basic = new Basic();
    FixedMultiTools multiTools = new FixedMultiTools();
    Basic instance = multiTools.parallelMulti()
        .add(() -> {
            List<Integer> integers = new ArrayList<>();
            integers.add(1);
            integers.add(777);
            return integers;
        }).add(() -> {
            List<String> strings = new ArrayList<>();
            strings.add("123");
            strings.add("456");
            return strings;
        }).add(() -> {
            List<Double> doubles = new ArrayList<>();
            doubles.add(1.20001);
            doubles.add(20.220002);
            return doubles;
        }).add(() -> {
            List<Float> floats = new ArrayList<>();
            floats.add(1.1f);
            floats.add(1.556f);
            return floats;
        }).add(() -> {
            List<Short> shorts = new ArrayList<>();
            shorts.add((short) 4);
            shorts.add((short) 1288);
            return shorts;
        }).add(() -> {
            List<Byte> bytes = new ArrayList<>();
            bytes.add((byte) 125);
            bytes.add((byte) 15);
            return bytes;
        }).add(() -> {
            List<Long> longs = new ArrayList<>();
            longs.add(45L);
            longs.add(45487L);
            return longs;
        }).add(() -> {
            List<Character> characters = new ArrayList<>();
            characters.add('a');
            characters.add('Z');
            return characters;
        }).add(() -> {
            List<Boolean> booleans = new ArrayList<>();
            booleans.add(true);
            booleans.add(false);
            return booleans;
        }).add(() -> {
            List<Integer> integers = new ArrayList<>();
            integers.add(2);
            integers.add(888);
            return integers;
        })
        .getForInstance(basic);
    System.out.println(instance);

}
// 结果:
// Basic{strings=[123, 456], integers=[1, 777], doubles=[1.20001, 20.220002], floats=[1.1, 1.556], shorts=[4, 1288], bytes=[125, 15], longs=[45, 45487], characters=[a, Z], booleans=[true, false]}
~~~

### 3，类型压缩器

类型压缩器用于将一个大型的对象压缩为一个完全兼容的小型的对象，从而可以更快速的进行序列化。

其思路为用部分代表整体，系统中默认提供了对Iterator类型和Map类型的压缩器，使用时可自行扩展。

详情可见[wiki](https://github.com/pizihao/crow/wiki)开发日志0.0.7部分。

## 其他

FixedMultiTools和MultiTools
