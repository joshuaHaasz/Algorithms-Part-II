import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class SAP {

    private final Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        graph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        checkVertex(v);
        checkVertex(w);

        Queue<Integer> qV = new Queue<>();
        Queue<Integer> qW = new Queue<>();

        qV.enqueue(v);
        qW.enqueue(w);

        return ancestorSearch(qV, qW, true);
    }

    private void checkVertex(int v) {
        if (v < 0 || v > graph.V()) throw new IllegalArgumentException("Not a valid vertex");
    }

    private void checkIterable(Iterable<Integer> iter) {
        try {
            for (int v : iter) checkVertex(v);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Input is null or contains null entries.");
        }
    }

    private int ancestorSearch(Queue<Integer> qV, Queue<Integer> qW, boolean length) {
        HashMap<Integer, Integer> distV = new HashMap<>();
        HashMap<Integer, Integer> distW = new HashMap<>();
        ArrayList<Integer> ancestors = new ArrayList<>();

        for (int v : qV) {
            distV.put(v, 0);
        }
        for (int w : qW) {
            distW.put(w, 0);
        }

        // check if we already have a common ancestor
        for (int v : qV) {
            if (distW.containsKey(v)) {
                if (length) return 0;
                else return v;
            }
        }

        while (!qV.isEmpty() || !qW.isEmpty()) {

            if (!qV.isEmpty()) {
                int v = qV.dequeue();
                int distanceV = distV.get(v);
                for (int id : graph.adj(v)) {
                    if (!distV.containsKey(id)) {
                        qV.enqueue(id);
                        distV.put(id, distanceV + 1);
                        if (distW.containsKey(id)) ancestors.add(id);
                    }
                }
            }

            if (!qW.isEmpty()) {
                int w = qW.dequeue();
                int distanceW = distW.get(w);
                for (int id : graph.adj(w)) {
                    if (!distW.containsKey(id)) {
                        qW.enqueue(id);
                        distW.put(id, distanceW + 1);
                        if (distV.containsKey(id)) ancestors.add(id);
                    }
                }
            }
        }
        if (!ancestors.isEmpty()) {
            int first = ancestors.get(0);
            int minId = first;
            int minDistance = distV.get(first) + distW.get(first);
            for (int id : ancestors) {
                int distance = distV.get(id) + distW.get(id);
                if (distance < minDistance) {
                    minDistance = distance;
                    minId = id;
                }
            }
            if (length) return minDistance;
            else return minId;
        }
        return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {

        checkVertex(v);
        checkVertex(w);

        Queue<Integer> qV = new Queue<>();
        Queue<Integer> qW = new Queue<>();

        qV.enqueue(v);
        qW.enqueue(w);

        return ancestorSearch(qV, qW, false);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        if (v == null || w == null) throw new IllegalArgumentException();

        checkIterable(v);
        checkIterable(w);

        Queue<Integer> qV = new Queue<>();
        Queue<Integer> qW = new Queue<>();

        for (int x : v) qV.enqueue(x);
        for (int x : w) qW.enqueue(x);

        return ancestorSearch(qV, qW, true);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {

        checkIterable(v);
        checkIterable(w);

        Queue<Integer> qV = new Queue<>();
        Queue<Integer> qW = new Queue<>();

        for (int x : v) qV.enqueue(x);
        for (int x : w) qW.enqueue(x);

        return ancestorSearch(qV, qW, false);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("digraph3.txt");
        SAP sap = new SAP(new Digraph(in));
        for (int v = 0; v < sap.graph.V(); v++) {
            for (int w = 0; w < sap.graph.V(); w++) {
                StdOut.printf("v: %d \n", v);
                StdOut.printf("w: %d \n", w);
                StdOut.printf("Common ancestor: %d \n", sap.ancestor(v, w));
                StdOut.printf("Length: %d \n", sap.length(v, w));
            }
        }
    }
}
