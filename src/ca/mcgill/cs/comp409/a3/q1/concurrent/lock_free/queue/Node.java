package ca.mcgill.cs.comp409.a3.q1.concurrent.lock_free.queue;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicReference;

public class Node<T> {
    public T aValue;
    public int aId;
    public AtomicReference<Node<T>> next;
    private Timestamp aAdded;
    private Timestamp aRemoved;

    public Node(T pValue) {
        aValue = pValue;
        next = new AtomicReference<>(null);
    }

    public void setAdded(long pAddedTime) {
        aAdded = new Timestamp(pAddedTime);
    }

    public void setRemoved(long pRemovedTime) {
        aRemoved = new Timestamp(pRemovedTime);
    }

    public long getAddedTime() {
        assert aAdded != null;
        return aAdded.getTime();
    }

    public long getRemovedTime() {
        assert aRemoved != null;
        return aRemoved.getTime();
    }

    public T getValue() {
        return aValue;
    }
}
