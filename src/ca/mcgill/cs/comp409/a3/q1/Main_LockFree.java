package ca.mcgill.cs.comp409.a3.q1;

import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_free.queue.LFUnboundedQueue;
import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_free.queue.Node;
import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_free.thread.Dequeuer;
import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_free.thread.Enqueuer;
import ca.mcgill.cs.comp409.a3.q1.concurrent.util.QOp;
import ca.mcgill.cs.comp409.a3.q1.concurrent.util.QOpRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main_LockFree {

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
        LFUnboundedQueue<Integer> intQueue = new LFUnboundedQueue<>();
        // Start the enqueuers and dequeuers
        List<Enqueuer> enqueuers = new ArrayList<>();
        List<Dequeuer<Integer>> dequeuers = new ArrayList<>();
        for (int i = 0; i < Math.max(p, q); i++) {
            if (i < p) {
                Enqueuer enqueueThread = new Enqueuer(intQueue);
                enqueuers.add(enqueueThread);
                enqueueThread.start();
            }
            if (i < q) {
                Dequeuer<Integer> dequeueThread = new Dequeuer<>(intQueue, n);
                dequeuers.add(dequeueThread);
                dequeueThread.start();
            }
        }

        // First wait on all dequeuers to finish dequeueing n items
        waitOnDequeuers(dequeuers);

        // Once all are done, signal enqueuers to stop and wait for them
        for (Thread eqThread:
                enqueuers) {
            eqThread.interrupt();
        }
        waitOnEnqueuers(enqueuers);

        // Collect all dequeued nodes from the dequeuers
        List<Node<Integer>> dequeuedNodes = new ArrayList<>();
        List<QOpRecord> records = new ArrayList<>();
        for (Dequeuer<Integer> dequeuer :
                dequeuers) {
            dequeuedNodes.addAll(dequeuer.getDequeuedNodes());
        }

        // Create queue operation records from all dequeued nodes, we can safely ignore the extra
        // few nodes enqueued after interruption
        for (Node<Integer> node :
                dequeuedNodes) {
            records.add(new QOpRecord(QOp.enq, node.getAddedTime(), node.aId));
            records.add(new QOpRecord(QOp.deq, node.getRemovedTime(), node.aId));
        }

        // Sort and print output for verification
        Collections.sort(records);

        for (QOpRecord record :
                records) {
            System.out.println(record.operation + " " + record.id);
        }
    }

    private static void waitOnEnqueuers(List<Enqueuer> pDequeuers) {
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

    private static void waitOnDequeuers(List<Dequeuer<Integer>> pDequeuers) {
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
