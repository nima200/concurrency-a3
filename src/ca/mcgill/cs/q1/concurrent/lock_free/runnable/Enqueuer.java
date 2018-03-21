package ca.mcgill.cs.q1.concurrent.lock_free.runnable;

import ca.mcgill.cs.q1.concurrent.lock_free.queue.LFUnboundedQueue;

import java.util.Random;

public class Enqueuer implements Runnable {

    private LFUnboundedQueue<Integer> aQueue;

    public Enqueuer(LFUnboundedQueue<Integer> pQueue) {
        aQueue = pQueue;
    }

    @Override
    public void run() {
        Random rand = new Random();
        while (true) {
            aQueue.enqueue(rand.nextInt(100));
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }
}
