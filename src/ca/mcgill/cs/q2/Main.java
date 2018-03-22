package ca.mcgill.cs.q2;

import ca.mcgill.cs.q2.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Main {
    private static final AtomicInteger mutex = new AtomicInteger(1);
    public static void main(String[] args) throws InterruptedException {

        if (args.length != 3) {
            System.out.println("Invalid number of arguments. Expected: 3, received: " + args.length);
            System.exit(1);
        }
        int n = 0, e = 0, t = 0;
        try {
            n = Integer.parseInt(args[0]);
            e = Integer.parseInt(args[1]);
            t = Integer.parseInt(args[2]);
        } catch (NumberFormatException nfe) {
            System.out.println("Unable to convert arguments to integers. Please enter valid integers only.");
            System.exit(1);
        }

        List<Node> nodes = GraphMaker.ConstructGraph(n, e);
        List<Node> conflicts = nodes;
        long start = System.currentTimeMillis();
        while (!conflicts.isEmpty()) {
            Assign(nodes, 1);
            conflicts = DetectConflicts(conflicts, 1);
        }
        long stop = System.currentTimeMillis();
        System.out.println(stop - start);
        int maxDim = 0;
        int maxCol = 0;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getNeighbors().size() > maxDim) {
                maxDim = nodes.get(i).getNeighbors().size();
            }
            if (nodes.get(i).color > maxCol) {
                maxCol = nodes.get(i).color;
            }
        }
        System.out.println(maxDim);
        System.out.println(maxCol);
    }

    private static void Assign(List<Node> conflicting, int threadCount) throws InterruptedException {
        List<List<Node>> conflicting_subsets = CollectionUtils.MakePartitions(conflicting, threadCount);
        List<Thread> threads = new ArrayList<>();

        for(List<Node> subset : conflicting_subsets) {
            Thread thread = new Thread(() -> {
                for(Node vertex : subset) {
                    List<Integer> neighborColors = new ArrayList<>();
                    for (Node neighbor : vertex.getNeighbors()) {
                        neighborColors.add(neighbor.color);
                        int biggestColor = Collections.max(neighborColors) + 1;
                        int newColor = -1;
                        for (int i = 1; i <= biggestColor + 1; i++) {
                            if (!neighborColors.contains(i)) {
                                newColor = i;
                                break;
                            }
                        }
                        vertex.color = newColor;
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread:
                threads) {
            thread.join();
        }
    }

    private static List<Node> DetectConflicts(List<Node> conflicting, int threadCount) throws InterruptedException {
        List<Node> newConflicts = new ArrayList<>();
        List<List<Node>> conflicting_subsets = CollectionUtils.MakePartitions(conflicting, threadCount);
        List<Thread> threads = new ArrayList<>();
        for(List<Node> subset: conflicting_subsets) {
            Thread thread = new Thread(() -> {
                for(Node vertex : subset) {
                    for (Node neighbor : vertex.getNeighbors()) {
                        if (vertex.color == neighbor.color && neighbor.id < vertex.id) {
                            while (mutex.compareAndSet(1, 0)) {}
                            newConflicts.add(vertex);
                            mutex.set(1);
                        }
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }
        for (Thread thread :
                threads) {
            thread.join();
        }
        return newConflicts;
    }
}