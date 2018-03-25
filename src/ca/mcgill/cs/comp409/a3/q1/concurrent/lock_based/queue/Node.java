package ca.mcgill.cs.comp409.a3.q1.concurrent.lock_based.queue;

import java.sql.Timestamp;

public class Node<T> {
    private T aValue;

    public int getId() {
        return aId;
    }

    private int aId;
    private Timestamp aAdded;
    private Timestamp aRemoved;
    public Node<T> next;

    public Node(int pId, T pValue) {
        aId = pId;
        aValue = pValue;
        next = null;
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
