import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: patrick-mac
 * @Date: 2018/8/31 16:55
 * @Description:
 */
public class ProduceConsumer2 {
    public static void main(String[] args) {

        Lock lock = new ReentrantLock();
        Condition produceCondition = lock.newCondition();
        Condition consumeCondition = lock.newCondition();

        Resource resource = new Resource(lock, produceCondition, consumeCondition);

        // 4 个生产者
        Thread p1 = new Thread(new Producer(resource, 50));
        Thread p2 = new Thread(new Producer(resource, 30));
        Thread p3 = new Thread(new Producer(resource, 40));
        Thread p4 = new Thread(new Producer(resource, 20));
        // 5 个消费者
        Thread c1 = new Thread(new Consumer(resource, 10));
        Thread c2 = new Thread(new Consumer(resource, 20));
        Thread c3 = new Thread(new Consumer(resource, 20));
        Thread c4 = new Thread(new Consumer(resource, 30));
        Thread c5 = new Thread(new Consumer(resource, 30));

        p1.start();p2.start();p3.start();p4.start();
        c1.start();c2.start();c3.start();c4.start();c5.start();

    }

    /**
     * 模拟阻塞队列
     */
    static class Resource {
        // 队列最大数量
        private int MAX_SIZE = 100;
        // 队列产品
        private LinkedList list = new LinkedList();

        private Lock lock;
        private Condition produceCondition;
        private Condition consumeCondition;

        public Resource(Lock lock, Condition condition1, Condition condition2) {
            this.lock = lock;
            this.produceCondition = condition1;
            this.consumeCondition = condition2;
        }

        /**
         * 加入资源
         */
        public void add (int num) {
            lock.lock();
            try {
                while (list.size() + num > MAX_SIZE) {
                    System.out.println("【生产数量】：" + num + "，【队列数量】：" +
                            list.size() + "，任务暂不执行，进入等待！");

                    try {
                        produceCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 塞入产品
                for (int i = 0; i < num; ++i) {
                    list.add(new Object());
                }

                System.out.println("【生产数量】：" + num + "，任务执行，更新后【队列数量】：" + list.size());
                consumeCondition.signalAll();
            }
            finally {
                lock.unlock();
            }
        }

        /**
         * 消费资源
         */
        public void remove(int num) {
            lock.lock();
            try {
                while (list.size() - num < 0) {
                    System.out.println("【消费数量】：" + num + "，【队列数量】：" +
                            list.size() + "，任务暂不执行，任务进入等待！");

                    try {
                        consumeCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 取出产品
                for (int i = 0; i < num; ++i) {
                    list.poll();
                }
                System.out.println("【消费数量】：" + num + "，任务执行，更新后【队列数量】：" + list.size());
                produceCondition.signalAll();
            }
            finally {
                lock.unlock();
            }
        }
    }

    /**
     * 模拟生产者
     */
    static class Producer implements Runnable {
        private Resource resource;
        private int num;

        public Producer(Resource resource, int num) {
            this.resource = resource;
            this.num = num;
        }

        @Override
        public void run() {
            resource.add(num);
        }
    }

    /**
     * 模拟消费者
     */
    static class Consumer implements Runnable {
        private Resource resource;
        private int num;

        public Consumer(Resource resource, int num) {
            this.resource = resource;
            this.num = num;
        }

        @Override
        public void run() {
            resource.remove(num);
        }
    }
}
