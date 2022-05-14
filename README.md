## 介绍

基于CompletableFuture对多线程串行化执行的扩展和精简，通过组合的关系避开CompletableFuture中复杂的逻辑结构，并精炼出其核心常用的功能进行扩展。简化CompletableFuture的串行化执行方式，对CompletableFuture并行化执行结果的后去方式进行扩充，并将两者进行结合，实现串行化和并行化的互相转化。



快速使用
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

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }

    public List<Integer> getIntegers() {
        return integers;
    }

    public void setIntegers(List<Integer> integers) {
        this.integers = integers;
    }

    public List<Double> getDoubles() {
        return doubles;
    }

    public void setDoubles(List<Double> doubles) {
        this.doubles = doubles;
    }

    public List<Float> getFloats() {
        return floats;
    }

    public void setFloats(List<Float> floats) {
        this.floats = floats;
    }

    public List<Short> getShorts() {
        return shorts;
    }

    public void setShorts(List<Short> shorts) {
        this.shorts = shorts;
    }

    public List<Byte> getBytes() {
        return bytes;
    }

    public void setBytes(List<Byte> bytes) {
        this.bytes = bytes;
    }

    public List<Long> getLongs() {
        return longs;
    }

    public void setLongs(List<Long> longs) {
        this.longs = longs;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public List<Boolean> getBooleans() {
        return booleans;
    }

    public void setBooleans(List<Boolean> booleans) {
        this.booleans = booleans;
    }

    @Override
    public String toString() {
        return "Basic{" +
            "strings=" + strings +
            ", integers=" + integers +
            ", doubles=" + doubles +
            ", floats=" + floats +
            ", shorts=" + shorts +
            ", bytes=" + bytes +
            ", longs=" + longs +
            ", characters=" + characters +
            ", booleans=" + booleans +
            '}';
    }
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

## 其他

FixedMultiTools和MultiTools
