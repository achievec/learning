package org.aloha;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

import org.junit.Test;

import junit.framework.Assert;

public class ForkJoinTaskTest {

    @Test
    public void test() throws InterruptedException, ExecutionException {
        ForkJoinPool pool = new ForkJoinPool();
        int start = 0;
        int end = 10;
        CountTask countTask = new CountTask(start, end);
        int result = pool.submit(countTask).get();
        if (countTask.isCompletedAbnormally()) {
            System.out.println(countTask.getException());
        }
        Assert.assertEquals(result, IntStream.rangeClosed(start, end).sum());
    }
}

/**
 * RecursiveAction (none result return) implements ForkJonTask, RecursizeTask
 * (return result) implements ForkJoinTask
 * 
 * @author aloha
 * @Date:2017年6月28日 上午12:26:29
 */

class CountTask extends RecursiveTask<Integer> {

    private static final long serialVersionUID = 1L;

    private static final int THREADHOLD = 2;

    private int start;
    private int end;

    public CountTask(int start, int end) {
        super();
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if ((end - start) <= THREADHOLD) {
            return IntStream.rangeClosed(start, end).sum();
        }
        int middle = (start + end) / 2;
        CountTask leftTask = new CountTask(start, middle);
        CountTask rightTask = new CountTask(middle + 1, end);
        leftTask.fork();
        rightTask.fork();
        return leftTask.join() + rightTask.join();
    }

}
