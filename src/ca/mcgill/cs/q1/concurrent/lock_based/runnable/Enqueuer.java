package ca.mcgill.cs.q1.concurrent.lock_based.runnable;

import ca.mcgill.cs.q1.concurrent.lock_based.queue.LBUnboundedQueue;

import java.util.Random;

public class Enqueuer implements Runnable {

    private LBUnboundedQueue<Integer> aQueue;

    public Enqueuer(LBUnboundedQueue<Integer> pQueue) {
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
