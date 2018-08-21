package executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * FixedThreadPool类型测试
 */
public class FixedThreadPool {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 6; i++) {
            FutureTask futureTask = new FutureTask(new MyFixedThread());
            // 提交任务
            executorService.submit(futureTask);
        }
        // 关闭线程池
        executorService.shutdown();
    }
}

class MyFixedThread implements Callable {

    @Override
    public Object call() throws Exception {
        System.out.println("Thread: " + Thread.currentThread().getName() + " begin...");
        Thread.sleep(5 * 1000);
        System.out.println("Thread: " + Thread.currentThread().getName() + " end...");
        return null;
    }
}

