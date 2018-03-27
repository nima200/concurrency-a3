package ca.mcgill.cs.comp409.a3.q1.concurrent.lock_based.thread;

import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_based.queue.LBUnboundedQueue;
import ca.mcgill.cs.comp409.a3.q1.concurrent.exception.EmptyQueueException;
import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_based.queue.Node;

import java.util.ArrayList;
import java.util.List;

public class Dequeuer<T> extends Thread {

    private LBUnboundedQueue<T> aQueue;
    private int aDequeueCapacity;
    private int aDequeueCount;
    private List<Node<T>> aDequeuedNodes;

    public Dequeuer(LBUnboundedQueue<T> pQueue, int pDequeueCapacity) {
        aQueue = pQueue;
        aDequeueCapacity = pDequeueCapacity;
        aDequeuedNodes = new ArrayList<>();
    }

    @Override
    public void run() {
        while (aDequeueCount < aDequeueCapacity) {
            try {
                Node<T> result = aQueue.dequeue();
                if (result != null) {
                    aDequeueCount++;
                    aDequeuedNodes.add(result);
                }
                Thread.sleep(10);
            } catch (EmptyQueueException | InterruptedException ignored) {}
        }
    }

    public List<Node<T>> getDequeuedNodes() {
        return aDequeuedNodes;
    }
}
