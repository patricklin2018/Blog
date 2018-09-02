# 生产者与消费者模式的概念、运用、3种实现

> 本文为笔者的学习整理总结，因此，若有任何不严谨或错误之处，还望不吝指教！

**内容：**

```
一、 概念
二、 运用
三、 实现
 	1. Sychronized、wait、notify
 	2. ReentrantLock
 	3. BlockingQueue
```

## 一、概念

生产者与消费者模式的核心在于生产者与消费者的关系解藕，生产者与消费者不直接进行通讯和产品交接，而是通过一个阻塞队列，起到一个缓冲区的作用——生产者将生产完成的产品放入阻塞队列，消费者从阻塞队列取。以达到平衡生产者与消费者的处理能力，提高运行效率。

很多程序员都喜欢说自己搬砖，那就以搬砖为例理解，过程先是通过人力把砖搬到砖场或砖堆，然后等待客户整车拉走。那么人力相当于生产者，客户相当于消费者，而这个砖堆/砖场相当于阻塞队列，平衡了人力和汽车搬砖的速率。

当然了，实际运用中，不一定都是如此一个阻塞队列多个消费者多个生产者的构建模式，也有可能是不同的对应关系。因此，具体构建还需要根据现实进行设计，只需要遵循核心——生产者与消费者的关系解藕，通过阻塞队列平衡处理能力。

## 二、运用

生产者与消费者模式运用广泛，尤其在消息队列的机制，比如订单系统，客户产生订单后，将订单放入订单队列，等待处理；比如公司下各应用各网络节点将运营数据放入消息队列收集，再提供给公司的运营部门查阅；比如 Java 的线程池，程序员将任务丢给线程池，线程池根据当前池里的执行情况，决定进行执行还是进入阻塞队列。

## 三、实现

以下通过三种方式实现，分别为：

* Synchronized、 wait、 notify，[代码链接-java](ProduceConsumer1.java)
* RetreenLock，[代码链接-java](ProduceConsumer2.java)
* BlockingQueue，[代码链接-java](ProduceConsumer3.java)

### 1. Synchronized、wait、notify

[代码 - java](ProduceConsumer1.java)

```java
/**
 * @Author: patrick-mac
 * @Date: 2018/8/31 15:31
 * @Description:
 */
public class ProduceConsumer1 {

    public static void main(String[] args) {
        Resource resource = new Resource();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
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

        /**
         * 加入资源
         */
        public void add (int num) {
            synchronized (list) {
                System.out.print("【生产数量】：" + num + "，【队列数量】：" +
                        list.size());

                if (list.size() + num > MAX_SIZE) {
                    System.out.println("，任务不执行，队列容量不足");

                    try {
                        list.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 塞入产品
                for (int i = 0; i < num; ++i) {
                    list.add(new Object());
                }

                System.out.println("，任务执行，更新后【队列数量】：" + list.size());
                list.notifyAll();
            }
        }

        /**
         * 消费资源
         */
        public void remove(int num) {
            synchronized (list) {
                System.out.print("【消费数量】：" + num + "，【队列数量】：" +
                        list.size());

                if (list.size() - num < 0) {
                    System.out.println("，任务不执行，队列数量不足！");

                    try {
                        list.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 取出产品
                for (int i = 0; i < num; ++i) {
                    list.poll();
                }
                System.out.println("，任务执行，更新后【队列数量】：" + list.size());
                list.notifyAll();
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
```

### 2. RentrantLock

```java
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

```

### 3.  BlockingQueue

```java
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

```

