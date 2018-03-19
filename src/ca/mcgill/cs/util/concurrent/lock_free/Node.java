package ca.mcgill.cs.util.concurrent.lock_free;

import java.util.concurrent.atomic.AtomicReference;

public class Node<T> {
    public T value;
    public AtomicReference<Node<T>> next;
    public Node(T value) {
        value = value;
        next = new AtomicReference<>(null);
    }
}
