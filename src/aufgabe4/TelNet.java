package aufgabe4;

import java.awt.*;
import java.util.List;
import java.util.*;

public class TelNet {
    private final int lbg;
    private Map<TelKnoten, Integer> telMap;
    private PriorityQueue<TelVerbindung> telQueue;
    private List<TelVerbindung> optTelNet;
    private int size;
    private static int xMax = 1000;
    private static int yMax = 1000;
    private static int n = 1000;


    private TelNet(int lbg) {
        this.lbg = lbg;
        telMap = new HashMap<>();
        optTelNet = new LinkedList<>();
        telQueue = new PriorityQueue<>(Comparator.comparing(x -> x.cost));
        this.size = 0;
    }

    public static void main(String[] args) {
        //test();
        testk();

    }
    private static void testk() {
        TelNet telNet = new TelNet(7);

        telNet.addTelKnoten(1, 1);
        telNet.addTelKnoten(3, 1);
        telNet.addTelKnoten(4, 2);
        telNet.addTelKnoten(3, 4);
        telNet.addTelKnoten(2, 6);
        telNet.addTelKnoten(4, 7);
        telNet.addTelKnoten(7, 5);
        telNet.computeOptTelNet();

        System.out.println("optTelNet = " + telNet.getOptTelNet());
        System.out.println("Size = " + telNet.size);
        System.out.println("optCost = " + telNet.getOptTelNetKosten());
        telNet.drawOptTelNet(7, 7);
    }

    private static void test() {
        TelNet telNet = new TelNet(100);
        telNet.generateRandomTelNet(n, xMax, yMax);
        telNet.computeOptTelNet();
        //System.out.println("optTelNet = " + telNet.getOptTelNet());
        System.out.println("Size = " + telNet.size);
        System.out.println("optCost = " + telNet.getOptTelNetKosten());
        telNet.drawOptTelNet(xMax, yMax);
    }

    private void generateRandomTelNet(int n, int xMax, int yMax) {
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int rx1 = random.nextInt(xMax);
            int ry1 = random.nextInt(yMax);
            addTelKnoten(rx1, ry1);
        }
    }

    private int cost(TelKnoten a, TelKnoten b) {
        if (dist(a, b) <= lbg)
            return dist(a, b);
        return Integer.MAX_VALUE;
    }

    private int dist(TelKnoten a, TelKnoten b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private boolean addTelKnoten(int x, int y) {
        TelKnoten knoten = new TelKnoten(x, y);
        if (telMap.containsKey(knoten))
            return false;
        telMap.put(knoten, size++);
        for (TelKnoten tk : telMap.keySet()) {
            if (knoten.x != tk.x && knoten.y != tk.y)
                addTelVerbindung(tk.x, tk.y, knoten.x, knoten.y);
        }
        return true;

    }

    private void addTelVerbindung(int x, int y, int x1, int y1) {
        TelKnoten t1 = new TelKnoten(x, y);
        TelKnoten t2 = new TelKnoten(x1, y1);
        if (cost(t1, t1) <= lbg && cost(t2, t2) < Integer.MAX_VALUE) {
            TelVerbindung tel1 = new TelVerbindung(cost(t1, t2), t1, t2);
            TelVerbindung tel2 = new TelVerbindung(cost(t2, t1), t2, t1);
            if (!telQueue.contains(tel1)) {
                telQueue.add(tel1);
                telQueue.add(tel2);
            }
        }
    }

    private boolean computeOptTelNet() {
        UnionFind bestNet = new UnionFind(size());

        while (bestNet.size() != 1 && !telQueue.isEmpty()) {
            TelVerbindung min = telQueue.poll();
            int t1 = bestNet.find(telMap.get(min.start));
            int t2 = bestNet.find(telMap.get(min.end));
            if (t1 != t2) {
                bestNet.union(t1, t2);
                optTelNet.add(min);
            }
        }

        return !telQueue.isEmpty() || bestNet.size() == 1;

    }


    private void drawOptTelNet(int xMax, int yMax) {
        if (optTelNet.isEmpty()) throw new IllegalStateException();
        StdDraw.setCanvasSize(xMax, yMax);

        for (TelVerbindung v : optTelNet) {
            double x1 = factorize(v.start.x, xMax);
            double y1 = factorize(v.start.y, yMax);
            double x2 = factorize(v.end.x, xMax);
            double y2 = factorize(v.end.y, yMax);
            StdDraw.setPenRadius(0.001);
            StdDraw.setPenColor(Color.RED);
            StdDraw.filledSquare(x1, y1, 0.001);
            StdDraw.filledSquare(x2, y2, 0.001);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.line(x1, y1, x2, y1);
            StdDraw.line(x2, y1, x2, y2);
        }
        StdDraw.show();
    }

    private double factorize(int xy, int xyMax) {
        return (1.0 / xyMax) * xy;
    }

    private List<TelVerbindung> getOptTelNet() {
        return optTelNet;

    }

    private int getOptTelNetKosten() {
        return optTelNet.stream().mapToInt(telVerbindung -> telVerbindung.cost).sum();

    }

    public int size() {
        return size;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        telMap.forEach((x, y) -> s.append(y));
        return s.toString();
    }
}
