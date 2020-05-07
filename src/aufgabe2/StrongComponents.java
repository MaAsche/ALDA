// O. Bittel;
// 05-09-2018

package aufgabe2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Collections.reverse;


/**
 * Klasse für Bestimmung aller strengen Komponenten.
 * Kosaraju-Sharir Algorithmus.
 *
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 22.02.2017
 */
public class StrongComponents<V> {
    // comp speichert fuer jede Komponente die zughörigen Knoten.
    // Die Komponenten sind numeriert: 0, 1, 2, ...
    // Fuer Beispielgraph in Aufgabenblatt 2, Abb3:
    // Component 0: 5, 6, 7,
    // Component 1: 8,
    // Component 2: 1, 2, 3,
    // Component 3: 4,

    private final Map<Integer, Set<V>> comp = new TreeMap<>();
    private DirectedGraph<V> mygraph;

    /**
     * Ermittelt alle strengen Komponenten mit
     * dem Kosaraju-Sharir Algorithmus.
     *
     * @param g gerichteter Graph.
     */
    public StrongComponents(DirectedGraph<V> g) {  //post order -> invert -> post order
        mygraph = g;
        visitPost(new LinkedList<>(reverseOrder()), mygraph.invert());
    }

    /**
     * @return Anzahl der strengen Komponeneten.
     */
    public int numberOfComp() {
        return comp.size();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        comp.forEach((key, value) -> str.append("Component ").append(key).append(": ").append(value.toString(), 1, value.toString().length() - 1).append("\n"));
        return str.toString();
    }

    private List<V> reverseOrder() {
        List<V> reverseOrder = new LinkedList<>(new DepthFirstOrder<>(mygraph).postOrder());
        reverse(reverseOrder);
        return Collections.unmodifiableList(reverseOrder);
    }

    private void visitPost(List<V> order, DirectedGraph<V> g) {
        Set<V> visited = new TreeSet<>();
        int counter = 0;

        for (V vertex : order) {
            if (!visited.contains(vertex)) {
                comp.put(counter++, visitPost(vertex, visited, g, new TreeSet<>()));
            }
        }
    }

    private Set<V> visitPost(V v, Set<V> visited, DirectedGraph<V> g, Set<V> components) {
        visited.add(v);
        for (V w : g.getSuccessorVertexSet(v)) {
            if (!visited.contains(w)) {
                visitPost(w, visited, g, components);
            }
        }
        components.add(v);
        return components;
    }


    /**
     * Liest einen gerichteten Graphen von einer Datei ein.
     *
     * @param fn Dateiname.
     * @return gerichteter Graph.
     * @throws FileNotFoundException
     */
    public static DirectedGraph<Integer> readDirectedGraph(File fn) throws FileNotFoundException {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        Scanner sc = new Scanner(fn);
        sc.nextInt();
        sc.nextInt();
        while (sc.hasNextInt()) {
            int v = sc.nextInt();
            int w = sc.nextInt();
            g.addEdge(v, w);
        }
        return g;
    }

    private static void test1() {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 1);
        g.addEdge(2, 3);
        g.addEdge(3, 1);

        g.addEdge(1, 4);
        g.addEdge(5, 4);

        g.addEdge(5, 7);
        g.addEdge(6, 5);
        g.addEdge(7, 6);

        g.addEdge(7, 8);
        g.addEdge(8, 2);

        StrongComponents<Integer> sc = new StrongComponents<>(g);

        System.out.println(sc.numberOfComp());  // 4

        System.out.println(sc);
        // Component 0: 5, 6, 7,
        // Component 1: 8,
        // Component 2: 1, 2, 3,
        // Component 3: 4,
    }

    private static void test2() throws FileNotFoundException {
        DirectedGraph<Integer> g = readDirectedGraph(new File("mediumDG.txt"));
        System.out.println(g.getNumberOfVertexes());
        System.out.println(g.getNumberOfEdges());
        System.out.println(g);

        System.out.println("");

        StrongComponents<Integer> sc = new StrongComponents<>(g);
        System.out.println(sc.numberOfComp());  // 10
        System.out.println(sc);

    }

    public static void main(String[] args) throws FileNotFoundException {
        test1();
        //test2();
    }
}
