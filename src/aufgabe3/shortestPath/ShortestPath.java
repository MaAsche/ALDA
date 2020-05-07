// O. Bittel;
// 28.02.2019

package aufgabe3.shortestPath;

import aufgabe3.graph.DirectedGraph;
import aufgabe3.sim.SYSimulation;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 *
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 27.01.2015
 */
public class ShortestPath<V> {

    SYSimulation sim = null;

    Map<V, Double> dist; // Distanz für jeden Knoten
    Map<V, V> pred; // Vorgänger für jeden Knoten
    DirectedGraph<V> mygraph;
    Heuristic<V> heuristic;
    V start;
    V end;
    private LinkedList<V> path;

    /**
     * Konstruiert ein Objekt, das im Graph g k&uuml;rzeste Wege
     * nach dem A*-Verfahren berechnen kann.
     * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
     * Wird h = null gewählt, dann ist das Verfahren identisch
     * mit dem Dijkstra-Verfahren.
     *
     * @param g Gerichteter Graph
     * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
     *          dem Dijkstra-Verfahren gesucht.
     */
    public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
        dist = new TreeMap<>();
        pred = new TreeMap<>();
        path = new LinkedList<>();
        mygraph = g;
        heuristic = h;
    }

    /**
     * Diese Methode sollte nur verwendet werden,
     * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
     * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
     * <p>
     * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
     * <p><blockquote><pre>
     *    if (sim != null)
     *       sim.visitStation((Integer) v, Color.blue);
     * </pre></blockquote>
     *
     * @param sim SYSimulation-Objekt.
     */
    public void setSimulator(SYSimulation sim) {
        this.sim = sim;
    }

    /**
     * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
     * <p>
     * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
     * der als nächstes aus der Kandidatenliste besucht wird, animiert.
     *
     * @param s Startknoten
     * @param g Zielknoten
     */
    public void searchShortestPath(V s, V g) {
        Queue<V> kandidatenListe = new PriorityQueue<>(Comparator.comparing(o -> dist.get(o)));         //Sortierung der Einträge nach distanz
        start = s;
        end = g;

        for (V v : mygraph.getVertexSet()) {
            dist.put(v, (double) Integer.MAX_VALUE); //Distanz zu Start -> Start nicht relevant => infinity
            pred.put(v, null); //Vorgänger -> Vorgänger nicht bekannt => null
        }

        dist.put(s, 0.0);           //Distanz für Startknoten
        kandidatenListe.add(s);

        while (!kandidatenListe.isEmpty()) {
            V min;
            if (heuristic != null) {
                min = kandidatenListe.stream()
                        .min(Comparator.comparing(o -> dist.get(o) + heuristic.estimatedCost(o, g)))            //löscht Knoten mit distanz + schätzwert = minimal
                        .get();
                kandidatenListe.remove(min);
                System.out.println(min);
            } else {
                min = kandidatenListe.poll();                   //vorderstes element
            }

            System.out.printf("Besuche Knoten %s mit d = %.2f", min, dist.get(min));
            if (heuristic != null) {
                //System.out.printf(" -> %.2f", heuristic.estimatedCost(min, g));
            }
            System.out.print("\n");
            if (min.equals(g)) return;                                          //Ziel erreicht

            if (sim != null) {
                Color c = heuristic != null ? Color.GREEN : Color.BLUE;
                sim.visitStation((int) min, c);
            }

            for (V w : mygraph.getSuccessorVertexSet(min)) {                    //für jeden anliegenden Knoten
                if (dist.get(w).equals((double) Integer.MAX_VALUE))             //wenn der knoten noch nicht besucht und nicht in der Kandidatenliste ist
                    kandidatenListe.add(w);
                if (dist.get(min) + mygraph.getWeight(min, w) < dist.get(w)) {      //Distanzwert für w verbessert sich -> Weg geht nun über min
                    pred.put(w, min);                                               //Wird als Vorgänger hinzugefügt
                    dist.put(w, dist.get(min) + mygraph.getWeight(min, w));         //distanz wird zu endlichem wert verbessert
                }
            }
        }
    }

   /* private void simulateShortestPath() {
        sim.startSequence("Kürzester Weg von " + start + " nach " + end);
        Iterator<V> iterator = path.iterator();
        V nextStation = iterator.next();
        while (iterator.hasNext()) {
            V thisStation = nextStation;
            nextStation = iterator.next();
            visitStation(thisStation);
            driveToStation(thisStation, nextStation);
        }
        visitStation(nextStation);
        path.clear();
        sim.stopSequence();
    }

    private void visitStation(V thisStation) {
        if (heuristic != null) sim.visitStation((int) thisStation, Color.GREEN);
        else sim.visitStation((int) thisStation, Color.BLUE);
    }

    private void driveToStation(V thisStation, V nextStation) {
        sim.drive((int) thisStation, (int) nextStation);
    }*/

    /**
     * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
     * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
     *
     * @return kürzester Weg als Liste von Knoten.
     * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
     */
    List<V> getShortestPath() {
        List<V> shortestPath = new LinkedList<>();
        V next = end;
        do {
            shortestPath.add(next);
            next = pred.get(next);
        } while (next != start);
        shortestPath.add(next);
        Collections.reverse(shortestPath);
        return Collections.unmodifiableList(shortestPath);
    }

    /**
     * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
     * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
     *
     * @return Länge eines kürzesten Weges.
     * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
     */
    public double getDistance() {
        return dist.get(end);
    }

}
