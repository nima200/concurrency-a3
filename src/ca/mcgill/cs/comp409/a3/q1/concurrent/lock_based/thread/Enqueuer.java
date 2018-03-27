package ca.mcgill.cs.comp409.a3.q1.concurrent.lock_based.thread;

import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_based.queue.LBUnboundedQueue;

import java.util.Random;

public class Enqueuer extends Thread {

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
