import java.util.concurrent.atomic.AtomicInteger;

public class Lucky {

    // атомарный счётчик текущего номера билета
    static AtomicInteger x = new AtomicInteger(0);

    // атомарный счётчик количества счастливых билетов
    static AtomicInteger count = new AtomicInteger(0);

    static class LuckyThread extends Thread {
        @Override
        public void run() {
            while (true) {
                // каждый поток атомарно получает СВОЙ следующий номер
                int current = x.incrementAndGet();

                if (current > 999_999) {
                    break;
                }

                int d1 = current % 10;
                int d2 = (current / 10) % 10;
                int d3 = (current / 100) % 10;
                int d4 = (current / 1000) % 10;
                int d5 = (current / 10_000) % 10;
                int d6 = (current / 100_000) % 10;

                if (d1 + d2 + d3 == d4 + d5 + d6) {
                    System.out.println(current);
                    count.incrementAndGet(); // атомарно увеличиваем
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        Thread t1 = new LuckyThread();
        Thread t2 = new LuckyThread();
        Thread t3 = new LuckyThread();

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        long end = System.currentTimeMillis();
        System.out.println("Total: " + count.get());
        System.out.println("Time: " + (end - start) + " ms");
    }
}