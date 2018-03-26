package ca.mcgill.cs.comp409.a3.q1.concurrent.lock_based.runnable;

import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_based.queue.LBUnboundedQueue;
import ca.mcgill.cs.comp409.a3.q1.concurrent.exception.EmptyQueueException;

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
                Thread.sleep(10);
            } catch (EmptyQueueException | InterruptedException ignored) {}
        }
    }
}
