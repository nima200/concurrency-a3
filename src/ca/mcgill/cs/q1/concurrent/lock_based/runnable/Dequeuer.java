package ca.mcgill.cs.q1.concurrent.lock_based.runnable;

import ca.mcgill.cs.q1.concurrent.lock_based.queue.LBUnboundedQueue;
import ca.mcgill.cs.q1.concurrent.exception.EmptyQueueException;

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
                Integer result = aQueue.dequeue();
                if (result != null) {
                    aDequeueCount++;
                }
            } catch (EmptyQueueException ignored) {}
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }
}