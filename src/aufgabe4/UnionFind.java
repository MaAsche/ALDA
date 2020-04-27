package aufgabe4;

import java.util.Arrays;
import java.util.Iterator;

public class UnionFind {
    private int size;
    private Integer[] tree;

    public UnionFind(int n) {
        tree = new Integer[n];
        Arrays.fill(tree, -1);
        size = n;
    }

    public static void main(String[] args) {


        UnionFind unionFind = new UnionFind(14);
        //Beispiel aus der Vorlesung
        unionFind.union(0,2);
        unionFind.union(0,1);
        unionFind.union(5,8);
        unionFind.union(6,10);
        unionFind.union(11,13);
        unionFind.union(6,11);
        unionFind.union(4,9);
        unionFind.union(7,12);
        unionFind.union(4,7);
        unionFind.union(6,4);
        unionFind.union(3,5);
        unionFind.union(0,5);
        unionFind.union(0,6);



        System.out.println(unionFind);
        for (int i=0; i< unionFind.size(); i++){
            System.out.println(unionFind.find(i));
        }

    }


    public int find(int e) {
        while (tree[e] >= 0) {
            e = tree[e];
        }
        return e;
    }

    public int size() {
        return size;
    }

    void union(int s1, int s2) throws IllegalArgumentException {
        if (s1 == s2) return;
        if (tree[s1] >= 0 || tree[s2] >= 0) return;

        if (-tree[s1] < -tree[s2])
            tree[s1] = s2;
        else {
            if (-tree[s1] == -tree[s2])
                tree[s1]--;
            tree[s2] = s1;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> it = StringIterator();
        while (it.hasNext())
            stringBuilder.append(it.next());
        return stringBuilder.toString();
    }

    private Iterator<String> StringIterator() {
        return new Iterator<>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public String next() {
                String color = tree[i] <= 0 ? "\u001B[3" + -tree[i] + "m" : "";
                return color + "[" + i + "] = " + tree[i++] + "\u001B[0m" + " \t";
            }
        };
    }
}
