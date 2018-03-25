package ca.mcgill.cs.comp409.a3.q1.concurrent.lock_free.runnable;

import ca.mcgill.cs.comp409.a3.q1.concurrent.exception.EmptyQueueException;
import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_free.queue.LFUnboundedQueue;

public class Dequeuer implements Runnable {

    private LFUnboundedQueue aQueue;
    private int aDequeueCapacity;
    private int aDequeueCount;

    public Dequeuer(LFUnboundedQueue pQueue, int pDequeueCapacity) {
        aQueue = pQueue;
        aDequeueCapacity = pDequeueCapacity;
    }

    @Override
    public void run() {
        while (aDequeueCount < aDequeueCapacity) {
            try {
                aQueue.dequeue();
                aDequeueCount++;
            } catch (EmptyQueueException ignored) {}
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }
}
