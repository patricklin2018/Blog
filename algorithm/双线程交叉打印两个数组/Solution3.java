import java.util.concurrent.Semaphore;

/**
 * @Author: patrick-mac
 * @Date: 2018/10/7 20:00
 * @Description:
 *
 * Semaphore 信号量实现
 *
 */
public class Solution3 {

    public static class Data {
        int[] data;
        int i = 0;
        public Data(int[] data) {
            this.data = data;
        }
    }

    public static class MyThread implements Runnable {
        Data pre;
        Data cur;
        Semaphore sPre;
        Semaphore sCur;

        public MyThread(Data pre, Data cur, Semaphore sPre, Semaphore sCur) {
            this.pre = pre;
            this.cur = cur;
            this.sPre = sPre;
            this.sCur = sCur;
        }

        @Override
        public void run() {
            try {
                while (cur.i < cur.data.length) {
                    sCur.acquire();
                    System.out.println(cur.data[cur.i++]);
                    sPre.release();
                }
                sPre.release(10000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int[] a1 = {1, 2, 3, 4};
        int[] a2 = {5, 6, 7, 8, 9, 10, 11};

        Data d1 = new Data(a1);
        Data d2 = new Data(a2);
        Semaphore s1 = new Semaphore(1);
        Semaphore s2 = new Semaphore(0);

        MyThread t1 = new MyThread(d1, d2, s1, s2);
        MyThread t2 = new MyThread(d2, d1, s2, s1);

        new Thread(t1).start();
        new Thread(t2).start();
    }

}
