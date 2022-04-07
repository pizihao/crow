### CompletionStage

CompletableFuture的实现依赖java工具类 CompletionStage ，通过实现CompletionStage来完成数据在不同线程之间的转换。

它可以表示某个异步任务的中间阶段或者完成阶段，某个异步计算的中间阶段或者完成阶段。

#### 串行化

例如：计算 A + B + C = ABC

~~~mermaid
graph LR
A(A+B) --> B(AB) 
C(AB + C) --> D(ABC) 

~~~

> 为了演示此处可以把等式进行如上图的转换，先计算A+B，再把结果和C进行计算，这样就把计算分成了两个独立的部分，现在要让这两个部分在两个线程中进行。按照常规的做法：
>
> 1. 启动一个线程：a
> 2. 让a计算A + B
> 3. 获得a中的值 AB
> 4. 启动一个线程：b
> 5. 让b计算AB + C
> 6. 获得b中的值 ABC，即为最终的结果
>
>  后来引入了线程池，所以有了更加优雅的做法：
>
> 1. 创建一个线程池：a
> 2. 让a计算 A + B
> 3. 获取结果 AB
> 4. 让a计算 AB + C
> 5. 取得结果 ABC
>
> 从此线程的复用性得到的保障，再后来CompletableFuture带来了多线程并发中的流式编程。
>
> 1. 创建CompletableFuture：c
> 2. 使用c计算A + B
> 3. 通过CompletableFuture中提供的方法，直接把上一步的结果给到 C 
> 4. 获取结果ABC

在CompletableFuture中提供了大量的承接运算，例如符合上述规则的thenApply和thenApplyAsync。

上述表达了一种异步计算的运算关系，串行化。其特点为前面的任务是后面任务的前置任务，只有完成了前面的任务才能开始后面的任务

#### 并行化

和串行化相对的就是并行化，并行化注重的是两个完全独立的任务，例如：计算 (A + B) = AB, (C + D) = CD

~~~mermaid
graph LR
A(A + B) --> B(AB)
C(C + D) --> D(CD)
~~~

>如上，两个算式属于完全的独立关系，中间不存在任务交汇，常规情况下，使用线程池来计算:
>
>1. 创建线程池：a
>2. 让a计算 A + B 和 C + D
>3. 获取结果AB 和 CD
>
>对于CompletableFuture来说，并没有完全额外提供更多的API，无非就是：
>
>1. 创建CompletableFuture：a1，a2
>2. a1计算A+B，a2计算C+D
>3. 获取其中的值

因为在串行化中两个任务是完全独立的，不存在交汇点，所以完全可以使用两个不同的线程分别执行，其获取值的顺序并不影响程序的运算结果。

#### 交汇关系

真实的业务模型往往是比较复杂的，其中一定是包含了串行化和并行化的组合方式，其中在不同的并行化中可能会存在一个交汇点，既然存在交汇那么就存在逻辑运算，基本的运算关系包含and和or，就是 & 和 |。

> &：
>
> 与关系，只有多个判断逻辑都为true才会执行的逻辑。
>
> CompletableFuture中表示：对于两个前置任务，只有两个全部都执行完成才执行下一个。

> |:
>
> 或关系，只有又一个判断逻辑为true就代表验证通过。
>
> CompletableFuture中表示：对于两个前置任务，只要有一个执行完成就可以执行下一个任务。

#### 异常处理

CompletableFuture的任务执行方式对于异常并不敏感，如果某一个任务中出现了异常，通常会打断其后置任务的执行，但是对于整个CompletableFuture对象执行完成之后的操作并没有任何影响，更多时候我们希望在出现异常的地方会直接中断该位置之后全部的执行。

在CompletableFuture中提供了exceptionally()API，专门应对异常的出现，如果在一个CompletableFuture出现了异常则直接执行异常逻辑。

另外还有handle()也可用于处理异常，不同的是exceptionally仅在出现异常后执行，而handle()无论如何都会执行，如果出现了异常，那么handle接收到的参数值为null，反之异常值为null。

另外whenComplete也可以用来处理异常，不过其参数是BiConsumer，whenComplete直接把其上一步的返回值作为参数传递给下一个方法，而handler会将自己处理后的值作为参数传递给下一个值

> handle和exceptionally都属于中间操作，两个是可以连用的，handle只会获取链上传递过来的异常信息，如果在这之前已经被其他操作将异常处理了，那么handle就无法拿到异常信息了，反之，如果存在操作在exceptionally之前完成了异常处理，那么exceptionally就不会接收到异常信息，除非在两次异常处理方法之前又出现了异常。

### 栈和CompletableFuture

> 通过使用，不难发现在CompletableFuture中，应该存在一个链式的关系，这个链存放了CompletableFuture整个运行期间各个任务调用之间的承接关系，因为这是一个单项的链，简单来说只需要一个next的指针即可完整表示，“下一个” 这种逻辑，但是在CompletableFuture中存在着正对单个CompletableFuture的多层调用，这种就需要依靠栈的思路

示例1：

~~~java
CompletableFuture a;
CompletableFuture  b = a.thenApply();
CompletableFuture  C = b.thenApply();
~~~

上述代码的执行逻辑是这样的：

~~~mermaid
graph LR
a --> b --> c
~~~



示例2:

~~~java
CompletableFuture a;
CompletableFuture  b = a.thenApply();
CompletableFuture  c = a.thenApply();
CompletableFuture  d = a.thenApply();
CompletableFuture  e = b.thenApply();
CompletableFuture  f = c.thenApply();
CompletableFuture  g = d.thenApply();
public static void main(String[] args) throws Exception {
    CompletableFuture<Void> a = CompletableFuture.runAsync(() -> {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("A");
    });
    CompletableFuture<String> b = a.thenApply(unused -> {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("B");
        return "B";
    });
    CompletableFuture<String> c = a.thenApply(unused -> {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("C");
        return "C";
    });
    CompletableFuture<String> d = a.thenApply(unused -> {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("D");
        return "D";
    });
    CompletableFuture<String> e = b.thenApply(unused -> {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("E");
        return "E";
    });
    CompletableFuture<String> f = c.thenApply(unused -> {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("F");
        return "F";
    });
    CompletableFuture<String> g = d.thenApply(unused -> {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("G");
        return "G";
    });
    TimeUnit.SECONDS.sleep(50);
}

~~~

<details>
  <summary>结果</summary>
ADGCFBE
</details>

以上代码的执行逻辑是这样的：

~~~mermaid
graph LR
a --> d --> g
a --> c --> f
a --> b --> e
d --> c
c --> b
~~~

其顺序是从上到下的，b优先被放入链中，但是是最后被回调的。这个过程是在上一个还没有完成时添加的，如果执行速度足够快，执行顺序基本就是代码的顺序。

> 当上一个CompletableFuture没有完成时，将新的CompletableFuture封装Completion的添加到上一个CompletableFuture的stack中。再将新的CompletableFuture封装后的Completion放到原本上一个CompletableFuture的stack的next的位置。这样在上一个执行完成后，其回调的就是新的CompletableFuture。

上述过程简述：



在这里使用的是thenApply进行连接，thenApply是典型的串行化结构，其对应的内部类为UniApply。

UniApply是UniCompletion的子类，UniCompletion是Completion的子类。

所以在UniApply中还存在几个属性值：

- next：
- dev：
- src：





