import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.RejectedExecutionException;

public class PoolExample {

    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3, 3, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3));

        // сколько задач выполнилось
        AtomicInteger count = new AtomicInteger(0);

        // сколько задач выполняется прямо сейчас
        AtomicInteger inProgress = new AtomicInteger(0);

        for (int i = 0; i < 30; i++) {
            final int number = i;
            Thread.sleep(10);

            Runnable task = () -> {
                int working = inProgress.incrementAndGet();
                System.out.println("start #" + number + ", in progress: " + working);
                try {
                    Thread.sleep(Math.round(1000 + Math.random() * 2000));
                } catch (InterruptedException e) {
                }
                working = inProgress.decrementAndGet();
                int done = count.incrementAndGet();
                System.out.println("end   #" + number + ", in progress: " + working
                        + ", done tasks: " + done);
            };

            System.out.println("creating #" + number);

            while (true) {
                try {
                    executor.submit(task);
                    break;
                } catch (RejectedExecutionException e) {
                    Thread.sleep(50);
                }
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        System.out.println("All tasks done: " + count.get());
    }
}
