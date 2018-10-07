import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: patrick-mac
 * @Date: 2018/10/7 16:59
 * @Description:
 *
 * ReentrantLock 实现方式
 *
 */
public class Solution2 {

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
        ReentrantLock lock;
        Condition cPre;
        Condition cCur;

        public MyThread(Data pre, Data cur, ReentrantLock lock, Condition cPre, Condition cCur) {
            this.pre = pre;
            this.cur = cur;
            this.lock = lock;
            this.cPre = cPre;
            this.cCur = cCur;
        }

        @Override
        public void run() {
            try {
                lock.lock();
                while (cur.i < cur.data.length) {
                    System.out.println(cur.data[cur.i++]);
                    cPre.signal();
                    if (pre.i < pre.data.length) {
                        cCur.await();
                    }
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        int[] a1 = {1, 2, 3, 4, 12, 13, 14, 15};
        int[] a2 = {5, 6, 7, 8, 9, 10, 11};

        Data d1 = new Data(a1);
        Data d2 = new Data(a2);
        ReentrantLock lock = new ReentrantLock();
        Condition c1 = lock.newCondition();
        Condition c2 = lock.newCondition();

        MyThread t1 = new MyThread(d1, d2, lock, c1, c2);
        MyThread t2 = new MyThread(d2, d1, lock, c2, c1);

        new Thread(t1).start();
        new Thread(t2).start();
    }
}
