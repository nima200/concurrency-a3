package ca.mcgill.cs.comp409.a3.q1.concurrent.exception;

public class EmptyQueueException extends RuntimeException {
    public EmptyQueueException() {
        super("Invalid operation. Queue has no items in it to pop");
    }
}
