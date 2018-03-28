package ca.mcgill.cs.comp409.a3.q2;

import ca.mcgill.cs.comp409.a3.q2.util.CollectionUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        BigInteger maxNodeCount = new BigInteger(Integer.toString(n));
        BigInteger nMin1 = new BigInteger(Integer.toString(n - 1));
        maxNodeCount = maxNodeCount.multiply(nMin1);
        maxNodeCount = maxNodeCount.divide(new BigInteger(Integer.toString(2)));
        BigInteger eBigInt = new BigInteger(Integer.toString(e));

        if (eBigInt.compareTo(maxNodeCount) > 0) {
            System.out.println("Invalid edge count. Max edge count possible is (n*n-1) / 2");
            System.exit(1);
        }
        int meanTime = 0;
        for (int j = 0; j < 5; j++) {
            List<Node> nodes = GraphMaker.ConstructGraph(n, e);
            List<Node> conflicts = nodes;
            long start = System.currentTimeMillis();
            while (!conflicts.isEmpty()) {
                Assign(nodes, t);
                conflicts = DetectConflicts(conflicts, t);
            }
            long stop = System.currentTimeMillis();
            System.out.println("Run " + j + " time taken to color: " + (stop - start));
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
            meanTime += stop - start;
            System.out.println("Run " + j + " Max Degree: " + maxDim);
            System.out.println("Run " + j + " Max Color Used: " + maxCol);
            System.out.println("Coloring is proper: " + VerifyColoring(nodes));
        }
        meanTime /= 5;
        System.out.println("Average Coloring Time: " + meanTime);
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
                    }
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
                            while (!mutex.compareAndSet(1, 0)) {}
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

    private static boolean VerifyColoring(List<Node> vertices) {
        for (Node vertex :
                vertices) {
            for (Node neighbor :
                    vertex.getNeighbors()) {
                if (vertex.color == neighbor.color) {
                    return false;
                }
            }
        }
        return true;
    }
}
