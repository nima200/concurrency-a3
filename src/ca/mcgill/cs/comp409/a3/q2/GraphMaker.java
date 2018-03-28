package ca.mcgill.cs.comp409.a3.q2;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GraphMaker {
    public static List<Node> ConstructGraph(int n, int e) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodes.add(new Node(i));
        }
        for (int i = 0; i < e; i++) {
            int vertex1 = ThreadLocalRandom.current().nextInt(n);
            int vertex2 = vertex1 + ThreadLocalRandom.current().nextInt(1, n - 1) % nodes.size();

            if (nodes.get(vertex1).getNeighbors().contains(nodes.get(vertex2))) {
                i--;
                continue;
            }

            nodes.get(vertex1).addNeighbor(nodes.get(vertex2));
        }
        return nodes;
    }
}
