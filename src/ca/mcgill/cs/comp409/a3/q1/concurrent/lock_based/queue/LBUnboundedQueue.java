package ca.mcgill.cs.comp409.a3.q1.concurrent.lock_based.queue;

import ca.mcgill.cs.comp409.a3.q1.concurrent.exception.EmptyQueueException;
import ca.mcgill.cs.comp409.a3.q1.concurrent.util.QOp;
import ca.mcgill.cs.comp409.a3.q1.concurrent.util.QOpRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class LBUnboundedQueue<T> {
    private ReentrantLock aEnqueueLock;
    private ReentrantLock aDequeueLock;
    private HashSet<Integer> aUsedIDs;
    private Node<T> aHead, aTail;

    public LBUnboundedQueue() {
        aHead = new Node<>(0, null);
        aTail = aHead;
        aEnqueueLock = new ReentrantLock();
        aDequeueLock = new ReentrantLock();
        aUsedIDs = new HashSet<>();
        aUsedIDs.add(0);
    }

    public void enqueue(T x) {
        // Reserve node value
        int newId = getNextID();
        // Prevent anyone else from enqueueing at the same time
        aEnqueueLock.lock();
        try {
            // Create new node out of value and ID
            Node<T> n = new Node<>(newId, x);
            // Update tail->next and new tail
            long timeStamp = System.nanoTime();
            n.setAdded(timeStamp);
            aTail.next = n;
            aTail = n;
        } finally {
            aEnqueueLock.unlock();
        }
    }

    public Node<T> dequeue() throws EmptyQueueException {
        aDequeueLock.lock();
        try {
            if (aHead.next == null) {
                throw new EmptyQueueException();
            }
            Node<T> oldNode = aHead.next;
            long timeStamp = System.nanoTime();
            // Set removal timestamp
            oldNode.setRemoved(timeStamp);
            aHead = aHead.next;
            return oldNode;
        } finally {
            aDequeueLock.unlock();
        }
    }

    private synchronized int getNextID() {
        int newID = Collections.max(aUsedIDs) + 1;
        aUsedIDs.add(newID);
        return newID;
    }
}
