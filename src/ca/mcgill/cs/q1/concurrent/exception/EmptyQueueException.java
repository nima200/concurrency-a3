package ca.mcgill.cs.q1.concurrent.exception;

public class EmptyQueueException extends RuntimeException {
    public EmptyQueueException() {
        super("Invalid operation. Queue has no items in it to pop");
    }
}
