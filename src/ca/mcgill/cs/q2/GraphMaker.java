package ca.mcgill.cs.q2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GraphMaker {
    public static List<Node> ConstructGraph(int n, int e) {
        List<Node> nodes = new ArrayList<>();
        HashMap<Integer, List<Integer>> adj_map = new HashMap<>();
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            nodes.add(new Node(i));
            adj_map.put(i, new ArrayList<>());
        }
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
        }
        return nodes;
    }
}
