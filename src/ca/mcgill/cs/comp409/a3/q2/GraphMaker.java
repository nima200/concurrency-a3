package ca.mcgill.cs.comp409.a3.q2;

import java.util.*;

public class GraphMaker {
    public static List<Node> ConstructGraph(int n, int e) {
        List<Node> nodes = new ArrayList<>();
        HashMap<Integer, HashSet<Integer>> adj_map = new HashMap<>();
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            nodes.add(new Node(i));
        }
        for (int i = 0; i < e; i++) {
            int vertex1 = rand.nextInt(n);
            int vertex2 = (vertex1 + rand.nextInt(n - 1) + 1) % nodes.size();
            // Lazy load elements into adjacency map
            if (!adj_map.containsKey(vertex1)) adj_map.put(vertex1, new HashSet<>());
            if (!adj_map.containsKey(vertex2)) adj_map.put(vertex2, new HashSet<>());

            if (adj_map.get(vertex1).contains(vertex2)) {
                i--;
                continue;
            }

            adj_map.get(vertex1).add(vertex2);
            adj_map.get(vertex2).add(vertex1);

            nodes.get(vertex1).addNeighbor(nodes.get(vertex2));
        }
        return nodes;
    }
}
