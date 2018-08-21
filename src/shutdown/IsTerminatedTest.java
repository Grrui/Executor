package shutdown;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * isTerminated()方法等待线程执行结束
 */
public class IsTerminatedTest {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // 创建多个线程
        for (int i = 0; i < 6; i++) {
            FutureTask futureTask = new FutureTask(new Callable() {
                @Override
                public Object call() throws Exception {
                    System.out.println("Thread:" + Thread.currentThread().getName() + "begin...");
                    Thread.sleep(10 * 1000);
                    System.out.println("Thread:" + Thread.currentThread().getName() + "end...");
                    return null;
                }
            });
            executorService.submit(futureTask);
        }
        // 关闭线程池
        executorService.shutdown();
        // 等待
        while (true) {
            if (executorService.isTerminated()) {
                System.out.println("execute end...");
                break;
            }
            Thread.sleep(5 * 1000);
        }

    }
}