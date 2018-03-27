package ca.mcgill.cs.comp409.a3.q1.concurrent.lock_free.thread;

import ca.mcgill.cs.comp409.a3.q1.concurrent.exception.EmptyQueueException;
import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_free.queue.LFUnboundedQueue;
import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_free.queue.Node;

import java.util.ArrayList;
import java.util.List;

public class Dequeuer<T> extends Thread {

    private LFUnboundedQueue<T> aQueue;
    private List<Node<T>> aDequeuedNodes;
    private int aDequeueCapacity;
    private int aDequeueCount;

    public Dequeuer(LFUnboundedQueue<T> pQueue, int pDequeueCapacity) {
        aQueue = pQueue;
        aDequeueCapacity = pDequeueCapacity;
        aDequeuedNodes = new ArrayList<>();
    }

    @Override
    public void run() {
        while (aDequeueCount < aDequeueCapacity) {
            try {
                Node<T> result = aQueue.dequeue();
                aDequeuedNodes.add(result);
                aDequeueCount++;
                Thread.sleep(10);
            } catch (EmptyQueueException | InterruptedException ignored) {}
        }
    }

    public List<Node<T>> getDequeuedNodes() {
        return aDequeuedNodes;
    }
}
