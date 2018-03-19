package ca.mcgill.cs.util.concurrent.runnable;

import ca.mcgill.cs.util.concurrent.lock_based.LBUnboundedQueue;
import ca.mcgill.cs.util.concurrent.exception.EmptyQueueException;

public class Dequeuer implements Runnable {

    private LBUnboundedQueue<Integer> aQueue;
    private int aDequeueCapacity;
    private int aDequeueCount;

    public Dequeuer(LBUnboundedQueue<Integer> pQueue, int pDequeueCapacity) {
        aQueue = pQueue;
        aDequeueCapacity = pDequeueCapacity;
    }

    @Override
    public void run() {
        while (aDequeueCount < aDequeueCapacity) {
            try {
                aQueue.dequeue();
            } catch (EmptyQueueException ignored) {}
            aDequeueCount++;
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }
}
