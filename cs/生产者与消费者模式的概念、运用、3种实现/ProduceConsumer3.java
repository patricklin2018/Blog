import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: patrick-mac
 * @Date: 2018/9/2 11:21
 * @Description:
 */
public class ProduceConsumer3 {

    public static void main(String[] args) {
        Resource resource = new Resource();

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

    static class Resource {
        final int MAX_SIZE = 100;
        private BlockingQueue que = new ArrayBlockingQueue(MAX_SIZE);

        /**
         * 加入资源
         */
        public void add (int num) {
            try {
                for (int i = 0; i < num; ++i) {
                    que.put(new Object());
                }
                System.out.println("【生产数量】：" + num + "，任务执行，更新后【队列数量】：" + que.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * 消费资源
         */
        public void remove(int num) {
            try {
                for (int i = 0; i < num; ++i) {
                    que.take();
                }
                System.out.println("【消费数量】：" + num + "，任务执行，更新后【队列数量】：" + que.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
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
