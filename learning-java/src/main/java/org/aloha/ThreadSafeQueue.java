package org.aloha;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * thread safe queue ,wrapped a list
 * 
 * @author aloha
 * @Date:2017年6月27日 上午12:49:07
 * @param <T>
 */
public class ThreadSafeQueue<T> {
    private List<T> innerList;
    private int size;

    private Lock lock = new ReentrantLock();
    private Condition addCondition = lock.newCondition();
    private Condition removeCondition = lock.newCondition();

    public ThreadSafeQueue(List<T> list, int size) {
        innerList = list;
        this.size = size;
    }

    public void add(T e) throws InterruptedException {
        lock.lock();
        try {
            while (innerList.size() == size) {
                addCondition.await();
            }
            innerList.add(e);
            list();
            removeCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public T pop() throws InterruptedException {
        lock.lock();
        try {
            while (innerList.size() == 0) {
                removeCondition.await();
            }
            T result = innerList.remove(innerList.size() - 1);
            list();
            addCondition.signal();
            return result;
        } finally {
            lock.unlock();
        }
    }

    private void list() {
        innerList.forEach(e -> {
            System.out.print(e + "\t");
        });
    }

    public static void test() {
        ThreadSafeQueue<Integer> queue = new ThreadSafeQueue<>(new ArrayList<Integer>(), 10);

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    queue.add(i);
                    System.out.println("add:" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        for (int i = 0; i < 100; i++) {
            try {
                int result = queue.pop();
                System.out.println("pop:" + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        test();
    }
}
