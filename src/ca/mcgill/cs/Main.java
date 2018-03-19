package ca.mcgill.cs;

import ca.mcgill.cs.util.concurrent.blocking.LBUnboundedQueue;
import ca.mcgill.cs.util.concurrent.runnable.Dequeuer;
import ca.mcgill.cs.util.concurrent.runnable.Enqueuer;
import ca.mcgill.cs.util.concurrent.util.QOpRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Invalid number of arguments. Expected 3, received " + args.length + ".");
            System.exit(1);
        }
        int p = 0, q = 0, n = 0;
        try {
            p = Integer.parseInt(args[0]);
            q = Integer.parseInt(args[1]);
            n = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Unable to convert arguments. Please enter digits for all 3 arguments.");
            System.exit(1);
        }

        LBUnboundedQueue<Integer> intQueue = new LBUnboundedQueue<>();

        List<Thread> enqueuers = new ArrayList<>();
        List<Thread> dequeuers = new ArrayList<>();
        for (int i = 0; i < Math.max(p, q); i++) {
            if (i < p) {
                Thread enqueueThread = new Thread(new Enqueuer(intQueue));
                enqueuers.add(enqueueThread);
                enqueueThread.start();
            }
            if (i < q) {
                Thread dequeueThread = new Thread(new Dequeuer(intQueue, n));
                dequeuers.add(dequeueThread);
                dequeueThread.start();
            }
        }

        waitOnThreads(dequeuers);

        for (Thread eqThread:
                enqueuers) {
            eqThread.interrupt();
        }

        waitOnThreads(enqueuers);
        List<QOpRecord> records = intQueue.getQOpRecords();
        Collections.sort(records);

        for (QOpRecord record :
                intQueue.getQOpRecords()) {
            System.out.println(record.operation + " " + record.id);
        }
    }

    private static void waitOnThreads(List<Thread> pDequeuers) {
        for (Thread dqThread :
                pDequeuers) {
            try {
                dqThread.join();
            } catch (InterruptedException ignored) {
                System.out.println("ERROR: Main thread unexpectedly interrupted while waiting on queue operation threads to complete. Exiting...");
                System.exit(1);
            }
        }
    }
}
