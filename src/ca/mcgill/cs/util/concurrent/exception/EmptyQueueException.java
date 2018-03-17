package ca.mcgill.cs.util.concurrent.exception;

public class EmptyQueueException extends RuntimeException {
    public EmptyQueueException() {
        super("Invalid operation. Queue has no items in it to pop");
    }
}
