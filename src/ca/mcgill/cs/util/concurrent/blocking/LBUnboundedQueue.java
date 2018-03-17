package ca.mcgill.cs.util.concurrent.blocking;

import ca.mcgill.cs.util.concurrent.exception.EmptyQueueException;

import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class LBUnboundedQueue<T> {
    private ReentrantLock aEnqueueLock, aDequeueLock, aIDSetLock;
    private HashSet<Integer> aUsedIDs;
    private AtomicInteger aSize;
    private Node<T> aHead, aTail;

    public LBUnboundedQueue() {
        aHead = new Node<>(0, null);
        aTail = aHead;
        aSize = new AtomicInteger(0);
        aEnqueueLock = new ReentrantLock();
        aDequeueLock = new ReentrantLock();
        aIDSetLock = new ReentrantLock();
        aUsedIDs = new HashSet<>();
        aUsedIDs.add(0);
    }

    public void enqueue(T x) {
        // Reserve node value
        aIDSetLock.lock();
        // Do not worry about overflow for now
        Integer newId;
        try {
            newId = Collections.max(aUsedIDs) + 1;
            aUsedIDs.add(newId);
        } finally {
            aIDSetLock.unlock();
        }
        assert newId != 0;
        // Prevent anyone else from enqueueing at the same time
        aEnqueueLock.lock();
        try {
            // Create new node out of value and ID
            Node<T> n = new Node<>(newId, x);
            // Update tail->next and new tail
            aTail.next = n;
            aTail = n;
            n.setAdded(System.currentTimeMillis());
        } finally {
            aEnqueueLock.unlock();
        }
    }

    public T dequeue() throws EmptyQueueException {
        T result;
        aDequeueLock.lock();
        try {
            if (aHead.next == null) {
                throw new EmptyQueueException();
            }
            Node<T> oldNode = aHead.next;
            // Update queue structure
            result = oldNode.getValue();
            aHead = aHead.next;
            oldNode.setRemoved(System.currentTimeMillis());
        } finally {
            aDequeueLock.unlock();
        }
        return result;
    }
}
