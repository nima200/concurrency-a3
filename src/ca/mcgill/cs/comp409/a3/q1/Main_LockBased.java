package ca.mcgill.cs.comp409.a3.q1;

import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_based.queue.LBUnboundedQueue;
import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_based.queue.Node;
import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_based.thread.Dequeuer;
import ca.mcgill.cs.comp409.a3.q1.concurrent.lock_based.thread.Enqueuer;
import ca.mcgill.cs.comp409.a3.q1.concurrent.util.QOp;
import ca.mcgill.cs.comp409.a3.q1.concurrent.util.QOpRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main_LockBased {

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

        int meanTime = 0;
        for (int j = 0; j < 10; j++) {
            long start = System.currentTimeMillis();
            LBUnboundedQueue<Integer> intQueue = new LBUnboundedQueue<>();

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

            waitOnDequeuers(dequeuers);

            for (Thread eqThread :
                    enqueuers) {
                eqThread.interrupt();
            }

            waitOnEnqueuers(enqueuers);
            long stop = System.currentTimeMillis();
            meanTime += stop - start;


            List<Node<Integer>> dequeuedNodes = new ArrayList<>();
            List<QOpRecord> records = new ArrayList<>();

            for (Dequeuer<Integer> dequeuer :
                    dequeuers) {
                dequeuedNodes.addAll(dequeuer.getDequeuedNodes());
            }

            for (Node<Integer> node :
                    dequeuedNodes) {
                records.add(new QOpRecord(QOp.enq, node.getAddedTime(), node.getId()));
                records.add(new QOpRecord(QOp.deq, node.getRemovedTime(), node.getId()));
            }

            List<String> results = new ArrayList<>();
            for (QOpRecord record :
                    records) {
                results.add(record.operation + " " + record.id + " " + record.time_stamp);
            }

            Collections.sort(records);
            EnqDeqValidator.validate(results.toArray(new String[0]));
        }
        meanTime /= 10;
        System.out.println(meanTime);

//        for (QOpRecord record :
//                records) {
//            System.out.println(record.operation + " " + record.id + " " + record.time_stamp);
//        }
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
