package ca.mcgill.cs.comp409.a3.q1.concurrent.lock_free.queue;

import ca.mcgill.cs.comp409.a3.q1.concurrent.exception.EmptyQueueException;
import ca.mcgill.cs.comp409.a3.q1.concurrent.util.QOp;
import ca.mcgill.cs.comp409.a3.q1.concurrent.util.QOpRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LFUnboundedQueue<T> {
    private HashSet<Integer> aUsedIDs;
    private AtomicInteger nextId;
    private AtomicReference<Node<T>> aHead;
    private AtomicReference<Node<T>> aTail;

    public LFUnboundedQueue() {
        Node<T> sentinel = new Node<>(null);
        aHead = new AtomicReference<>(sentinel);
        aTail = new AtomicReference<>(sentinel);
        nextId = new AtomicInteger(1);
        aUsedIDs = new HashSet<>();
        aUsedIDs.add(0);
    }

    public void enqueue(T x) {
        // Create new node object from value
        Node<T> node = new Node<>(x);
        // Reserve node id
        node.aId = nextId.getAndIncrement();
        while (true) {
            Node<T> last = aTail.get();
            Node<T> next = last.next.get();
            if (last == aTail.get()) {
                if (next == null) {
                    // Must set timestamp before actually setting the node or else a dequeuer can dequeue it before the
                    // timestamp was set
                    long timeStamp = System.nanoTime();
                    if (last.next.compareAndSet(next, node)) {
                        node.setAdded(timeStamp);
                        aTail.compareAndSet(last, node);
                        return;
                    }
                } else {
                    aTail.compareAndSet(last, next);
                }
            }
        }
    }

    public Node<T> dequeue() throws EmptyQueueException {
        while (true) {
            Node<T> first = aHead.get();
            Node<T> last = aTail.get();
            Node<T> next = first.next.get();
            if (first == aHead.get()) {
                if (first == last) {
                    if (next == null) {
                        throw new EmptyQueueException();
                    }
                    aTail.compareAndSet(last, next);
                } else {
                    long timeStamp = System.nanoTime();
                    if (aHead.compareAndSet(first, next)) {
                        next.setRemoved(timeStamp);
                        return next;
                    }
                }
            }
        }
    }
}
