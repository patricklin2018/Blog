/**
 * @Author: patrick-mac
 * @Date: 2018/10/7 13:30
 * @Description:
 *
 * Synchronized 实现
 *
 */
public class Solution1 {

    public static class Data {
        int[] data;
        int i = 0;
        public Data(int[] data) {
            this.data = data;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        int[] a1 = {1, 2, 3, 4};
        int[] a2 = {5, 6, 7, 8, 9, 10, 11};

        Data d1 = new Data(a1);
        Data d2 = new Data(a2);

        MyThread t1 = new MyThread(d1, d2);
        MyThread t2 = new MyThread(d2, d1);

        new Thread(t1).start();
        new Thread(t2).start();
    }

    public static class MyThread implements Runnable {
        Data pre;
        Data cur;

        public MyThread(Data pre, Data cur) {
            this.pre = pre;
            this.cur = cur;
        }

        @Override
        public void run() {
            while (cur.i < cur.data.length) {
                synchronized (pre) {
                    synchronized (cur) {
                        System.out.println(cur.data[cur.i++]);
                        cur.notify();
                    }
                    if (pre.i < pre.data.length) {
                        try {
                            pre.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
