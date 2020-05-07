// O. Bittel;
// 22.02.2017

package aufgabe2;

import java.util.*;

/**
 * Klasse zur Erstellung einer topologischen Sortierung.
 *
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 22.02.2017
 */
public class TopologicalSort<V> {
    private List<V> ts = new LinkedList<>(); // topologisch sortierte Folge
    private Map<V, Integer> inDegree;
    private DirectedGraph<V> myGraph;
    Queue<V> q;

    /**
     * Führt eine topologische Sortierung für g durch.
     *
     * @param g gerichteter Graph.
     */

    public TopologicalSort(DirectedGraph<V> g) {
        myGraph = g;
    }

    public List<V> topologicalSortedList() {
        Queue<V> q = new LinkedList<>();
        ts = new LinkedList<>();
        inDegree = new TreeMap<>();


        for (V v : myGraph.getVertexSet()) {
            inDegree.put(v, myGraph.getInDegree(v));
            if (myGraph.getInDegree(v) == 0) q.add(v);
        }
        while (!q.isEmpty()) {
            V v = q.remove();
            ts.add(v);
            for (V w : myGraph.getSuccessorVertexSet(v)) {
                inDegree.put(w, inDegree.get(w) - 1);
                if (inDegree.get(w) == 0)
                    q.add(w);
            }
        }
        return Collections.unmodifiableList(ts);
        //return ts.size() != myGraph.getNumberOfVertexes() ? null : Collections.unmodifiableList(ts);
    }

    /**
     * Liefert eine nicht modifizierbare Liste (unmodifiable view) zurück,
     * die topologisch sortiert ist.
     *
     * @return topologisch sortierte Liste
     */


    public static void main(String[] args) {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        /*g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        g.addEdge(3, 5);
        g.addEdge(4, 6);
        g.addEdge(5, 6);
        g.addEdge(6, 7);*/


        g.addEdge(1,2);
        g.addEdge(2,10);
        g.addEdge(3,4);
        g.addEdge(4,5);
        g.addEdge(4,2);
        g.addEdge(5,9);
        g.addEdge(6,7);
        g.addEdge(7,8);
        g.addEdge(8,9);
        g.addEdge(9,10);
        g.addEdge(10,11);
        g.addEdge(12,11);
        //g.addEdge(10,4);  //Schal vor der Hose
        System.out.println(g);

        TopologicalSort<Integer> ts = new TopologicalSort<>(g);

        if (ts.topologicalSortedList() != null) {
            System.out.println(ts.topologicalSortedList()); // [1, 2, 3, 4, 5, 6, 7]
        }
    }
}
