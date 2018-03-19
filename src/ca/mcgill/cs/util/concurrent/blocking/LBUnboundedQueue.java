package ca.mcgill.cs.util.concurrent.blocking;

import ca.mcgill.cs.util.concurrent.exception.EmptyQueueException;
import ca.mcgill.cs.util.concurrent.util.QOp;
import ca.mcgill.cs.util.concurrent.util.QOpRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class LBUnboundedQueue<T> {
    private ReentrantLock aEnqueueLock, aDequeueLock, aIDSetLock;
    private HashSet<Integer> aUsedIDs;
    private Node<T> aHead, aTail;

    private List<QOpRecord> aQOpRecords;

    public LBUnboundedQueue() {
        aHead = new Node<>(0, null);
        aTail = aHead;
        aEnqueueLock = new ReentrantLock();
        aDequeueLock = new ReentrantLock();
        aIDSetLock = new ReentrantLock();
        aQOpRecords = new ArrayList<>();
        aUsedIDs = new HashSet<>();
        aUsedIDs.add(0);
    }

    public void enqueue(T x) {
        // Reserve node value
        aIDSetLock.lock();
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
            long timeStamp = System.currentTimeMillis();
            n.setAdded(timeStamp);
            addRecord(new QOpRecord(QOp.ENQ, timeStamp, newId));
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
            long timeStamp = System.currentTimeMillis();
            oldNode.setRemoved(timeStamp);
            addRecord(new QOpRecord(QOp.DEQ, timeStamp, oldNode.getId()));
        } finally {
            aDequeueLock.unlock();
        }
        return result;
    }

    private synchronized void addRecord(QOpRecord pRecord) {
        aQOpRecords.add(pRecord);
    }

    public List<QOpRecord> getQOpRecords() {
        return aQOpRecords;
    }
}
