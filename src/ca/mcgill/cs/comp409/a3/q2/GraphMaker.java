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
            int vertex2 = (vertex1 + rand.nextInt(n-1) + 1) % nodes.size();
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

        /*

        List<int[]> allEdges = new ArrayList<>();
        // Create all possible edges
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Skip self connecting edges
                if (i == j) continue;
                allEdges.add(new int[]{i, j});
            }
        }
        // Take out 'e' many random edges from list of all possible edges and make them the actual edges.
        for (int i = 0; i < e; i++) {
            int[] edge = allEdges.get(rand.nextInt(allEdges.size()));
            allEdges.remove(edge);
            if (nodes.get(edge[0]).getNeighbors().contains(nodes.get(edge[1]))) {
                i--;
                continue;
            }
            // Add edge to adjacency map
            adj_map.get(edge[0]).add(edge[1]);
            // Add node to node's neighbors
            nodes.get(edge[0]).addNeighbor(nodes.get(edge[1]));
        }*/
        return nodes;
    }
}
