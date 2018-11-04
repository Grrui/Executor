# executor
Java四种线程池介绍

### 1. 使用线程池原因
简单说就是因为线程的创建、销毁也是一个开销很大的活动。后来出现了线程池的概念，大家发现：呀，我们可以使用线程池，通过线程池的重用，就不必繁琐的创建、销毁线程了。然后大家就开始疯狂的使用线程池了。


### 2. 四种线程池对象介绍

本文主要介绍四种线程池对象：
1. FixedThreadPool
2. SingleThreadPool
3. CachedThreadPool
4. ScheduledThreadPool


#### 2.1. FixedThreadPool
`public static ExecutorService newFixedThreadPool(int nThreads) `

**解释**
1. 线程池最大线程数为nThreads
2. 当任务数超过nThreads时，会使用一个无界队列存储提交的任务。
3. 创建线程池时，若无任务，则线程池中不会创建任何线程。

**使用：**
```java
import java.util.concurrent.*;

public class FixedThreadPool {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 6; i++) {
            FutureTask futureTask = new FutureTask(new MyThread());
            // 提交任务
            executorService.submit(futureTask);
        }
        // 关闭线程池
        executorService.shutdown();
    }
}

class MyThread implements Callable {

    @Override
    public Object call() throws Exception {
        System.out.println("Thread: " + Thread.currentThread().getName() + " begin...");
        Thread.sleep(5 * 1000);
        System.out.println("Thread: " + Thread.currentThread().getName() + " end...");
        return null;
    }
}
```

**控制台输出**
```
Thread: pool-1-thread-1 begin...
Thread: pool-1-thread-3 begin...
Thread: pool-1-thread-2 begin...
Thread: pool-1-thread-1 end...
Thread: pool-1-thread-2 end...
Thread: pool-1-thread-3 end...
Thread: pool-1-thread-2 begin...
Thread: pool-1-thread-3 begin...
Thread: pool-1-thread-1 begin...
Thread: pool-1-thread-2 end...
Thread: pool-1-thread-3 end...
Thread: pool-1-thread-1 end...
```
逻辑比较简单，创建一个最大3个线程的线程池，执行6个任务。控制台输出可以看出，最多三个线程同时执行；复用这三个线程完成6个任务。

#### 2.2. SingleThreadPool
`public static ExecutorService newSingleThreadExecutor() `

**解释**
1. 线程池只有一个线程。
2. 当任务数超过一个时，会使用一个无界队列存储提交的任务。

**使用：**
```java
import java.util.concurrent.*;

public class SingleThreadPool {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 3; i++) {
            FutureTask futureTask = new FutureTask(new MySingleThread());
            // 提交任务
            executorService.submit(futureTask);
        }
        // 关闭线程池
        executorService.shutdown();
    }
}

class MySingleThread implements Callable {

    @Override
    public Object call() throws Exception {
        System.out.println("Thread: " + Thread.currentThread().getName() + " begin...");
        Thread.sleep(5 * 1000);
        System.out.println("Thread: " + Thread.currentThread().getName() + " end...");
        return null;
    }
}
```

**控制台输出**
```
Thread: pool-1-thread-1 begin...
Thread: pool-1-thread-1 end...
Thread: pool-1-thread-1 begin...
Thread: pool-1-thread-1 end...
Thread: pool-1-thread-1 begin...
Thread: pool-1-thread-1 end...
```
**解释**  
输出可以看出，只有一个线程`pool-1-thread-1`循环复用完成了所有任务。

#### 2.3. CachedThreadPool
`public static ExecutorService newCachedThreadPool() `

**解释**
1. 线程池最大线程数为Integer.MAX_VALUE。
2. 当某个线程空闲60s，则会自动销毁。

**使用：**
```java
package com.example.demo.executor;

import java.util.concurrent.*;

public class CachedThreadPool {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {
            FutureTask futureTask = new FutureTask(new MyCachedThread());
            // 提交任务
            executorService.submit(futureTask);
        }
        // 关闭线程池
        executorService.shutdown();
    }
}

class MyCachedThread implements Callable {

    @Override
    public Object call() throws Exception {
        System.out.println("Thread: " + Thread.currentThread().getName() + " begin...");
        Thread.sleep(5 * 1000);
        System.out.println("Thread: " + Thread.currentThread().getName() + " end...");
        return null;
    }
}
```

**控制台输出**
```
Thread: pool-1-thread-1 begin...
Thread: pool-1-thread-3 begin...
Thread: pool-1-thread-2 begin...
Thread: pool-1-thread-1 end...
Thread: pool-1-thread-2 end...
Thread: pool-1-thread-3 end...
```
**解释**  
每产生一个任务，立即分配线程执行。当某个线程空闲60s时，会自动销毁。

#### 2.4. ScheduledThreadPool
`public static ExecutorService newScheduledThreadPool() `

**解释**
1. 线程池最大线程数为Integer.MAX_VALUE。
2. 周期性的执行规定的定时任务。


### 3. 四种线程池对象弊端
使用阿里Java开发者规范中的介绍：
1. FixedThreadPool 和 SingleThreadPool:  
允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM。
2. CachedThreadPool 和 ScheduledThreadPool:  
允许的创建线程数量为 Integer.MAX_VALUE， 可能会创建大量的线程，从而导致 OOM。

### 4. 线程池的创建
阿里大大的Java开发规范中介绍说`线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。`所以文章最后介绍一下ThreadPoolExecutor创建线程池。  
**构造方法：**  
`public ThreadPoolExecutor(intcorePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable>workQueue, RejectedExecutionHandlerhandler)`
**解释:**
1. corePoolSize：运行任务的核心线程数，如果当前线程数等于核心线程数，则多余的任务会放到等待队列。
2. maximumPoolSize：最大线程数。测试发现，当等待队列中任务满，且当前任务数<最大线程数，则会新开一个线程。
3. keepAliveTime：线程池中线程所允许的空闲时间
4. unit：线程池维护线程所允许的空闲时间的单位（NANOSECONDS、MICROSECONDS、MILLISECONDS、SECONDS等）
5. workQueue：线程池所使用的缓冲队列
6. handler：线程池对拒绝任务的处理策略。当缓存队列满，且当前线程数达到了最大线程数(maxmumPoolSize)，则会采取拒绝策略。  

**代码:**  
```java
import java.util.concurrent.*;

/**
 * ThreadPoolExecutor创建线程池
 */
public class ExecutorThreadPool {

    public static void main(String[] args) {
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(2,10,1, TimeUnit.MINUTES, new LinkedBlockingDeque<>());
        for (int i = 0; i < 100; i++) {
            FutureTask futureTask = new FutureTask(new MyExecutorThread());
            // 提交任务
            executorService.submit(futureTask);
        }
        // 关闭线程池
        executorService.shutdown();
    }
}

class MyExecutorThread implements Callable {

    @Override
    public Object call() throws Exception {
        System.out.println("Thread: " + Thread.currentThread().getName() + " begin...");
        Thread.sleep(5 * 1000);
        System.out.println("Thread: " + Thread.currentThread().getName() + " end...");
        return null;
    }
}
```

文章源代码地址：https://github.com/Grrui/executor
