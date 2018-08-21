package shutdown;

import java.util.concurrent.*;

/**
 * CountDownLatch方式等待线程执行结束
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {


        ExecutorService executorService = Executors.newFixedThreadPool(3);
        final CountDownLatch countDownLatch = new CountDownLatch(6);

        // 创建多个线程
        for (int i = 0; i < 6; i++) {
            FutureTask futureTask = new FutureTask(new Callable() {
                @Override
                public Object call() throws Exception {
                    System.out.println("Thread:" + Thread.currentThread().getName() + "begin...");
                    Thread.sleep(10 * 1000);
                    System.out.println("Thread:" + Thread.currentThread().getName() + "end...");
                    countDownLatch.countDown();
                    return null;
                }
            });
            executorService.submit(futureTask);
        }
        // 关闭线程池
        executorService.shutdown();
        // 等待
        countDownLatch.await();
        System.out.println("execute end...");

    }
}
