package ca.mcgill.cs.comp409.a3.q1.concurrent.lock_free.queue;

import ca.mcgill.cs.comp409.a3.q1.concurrent.exception.EmptyQueueException;
import ca.mcgill.cs.comp409.a3.q1.concurrent.util.QOp;
import ca.mcgill.cs.comp409.a3.q1.concurrent.util.QOpRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LFUnboundedQueue<T> {
    private HashSet<Integer> aUsedIDs;
    private AtomicReference<Node<T>> aHead;
    private AtomicReference<Node<T>> aTail;
    private List<QOpRecord> aQOpRecords;

    public LFUnboundedQueue() {
        Node<T> sentinel = new Node<>(null);
        aHead = new AtomicReference<>(sentinel);
        aTail = new AtomicReference<>(sentinel);
        aQOpRecords = new ArrayList<>();
        aUsedIDs = new HashSet<>();
        aUsedIDs.add(0);
    }

    public void enqueue(T x) {
        // Create new node object from value
        Node<T> node = new Node<>(x);
        while (true) {
            Node<T> last = aTail.get();
            Node<T> next = last.next.get();
            if (last == aTail.get()) {
                if (next == null) {
                    if (last.next.compareAndSet(next, node)) {
                        // Reserve node value
                        int newId = getNextID();
                        long timeStamp = System.currentTimeMillis();
                        node.setAdded(timeStamp);
                        node.aId = newId;
                        aTail.compareAndSet(last, node);
//                         Create a new queue operation record for node addition
                        addRecord(new QOpRecord(QOp.ENQ, timeStamp, newId));
                        return;
                    }
                } else {
                    aTail.compareAndSet(last, next);
                }
            }
        }
    }

    public T dequeue() throws EmptyQueueException {
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
                    T value = next.aValue;
                    if (aHead.compareAndSet(first, next)) {
                        long timeStamp = System.currentTimeMillis();
                        next.setRemoved(timeStamp);
                        addRecord(new QOpRecord(QOp.DEQ, timeStamp, next.aId));
                        return value;
                    }
                }
            }
        }
    }

    private synchronized void addRecord(QOpRecord pRecord) {
        aQOpRecords.add(pRecord);
    }

    public List<QOpRecord> getQOpRecords() {
        return aQOpRecords;
    }

    private synchronized int getNextID() {
        int newID = Collections.max(aUsedIDs) + 1;
        aUsedIDs.add(newID);
        return newID;
    }
}
