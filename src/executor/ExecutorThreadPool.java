package executor;

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

