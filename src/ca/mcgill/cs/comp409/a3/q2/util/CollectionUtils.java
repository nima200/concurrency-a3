package ca.mcgill.cs.comp409.a3.q2.util;

import ca.mcgill.cs.comp409.a3.q2.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionUtils {
    public static List<List<Node>> MakePartitions(List<Node> vertices, int threadCount) {
        int partitionSize = vertices.size() / threadCount;
        List<List<Node>> outputs = new ArrayList<>();
        for (int start = 0; start < vertices.size(); start+=partitionSize) {
            int end = Math.min(vertices.size(), start + partitionSize);
            List<Node> partition = vertices.subList(start, end);
            outputs.add(partition);
        }
        return outputs;
    }
}
