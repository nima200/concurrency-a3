package ca.mcgill.cs.q2;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    public volatile int color = 0;
    public int id;
    private List<Node> aNeighbors;

    public Node(int pId) {
        aNeighbors = new ArrayList<>();
        id = pId;
    }

    public List<Node> getNeighbors() {
        return aNeighbors;
    }

    public void addNeighbor(Node pNeighbor) {
        aNeighbors.add(pNeighbor);
        pNeighbor.aNeighbors.add(this);
    }

    @Override
    public int compareTo(Node o) {
        if (o == null) {
            return -1;
        }
        return Integer.compare(this.id, o.id);
    }
}
