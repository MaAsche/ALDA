package aufgabe1;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class HashDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    private int size;
    private int m;
    private List<Entry<K, V>>[] tab;
    private int modCount = 0;

    @SuppressWarnings("unchecked")
    public HashDictionary(int m) {
        size = 0;
        this.m = m;
        tab = new LinkedList[m];
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity (int newCapacity) {
        newCapacity = makePrime(newCapacity);
        List<Entry<K,V>>[] old = tab;
        tab = new LinkedList[newCapacity];
        for(List<Entry<K,V>> list: old) {
            if (list == null) {
                continue;
            }
            for (Entry<K,V> curr: list) {
                if (curr == null) {
                    continue;
                }
                if (tab[h(curr.getKey())] == null) {
                    tab[h(curr.getKey())] = new LinkedList<Entry<K,V>>();
                }
                tab[h(curr.getKey())].add(0, new Entry<K,V>(curr.getKey(), curr.getValue()));
            }
        }
    }

    @Override
    public V insert(K key, V value) {
        if (tab[h(key)] == null) {
            tab[h(key)] = new LinkedList<>();
        }
        for (Entry<K, V> item : tab[h(key)]) {
            if (item.getKey().equals(key)) {
                V oldValue = item.getValue();
                item.setValue(value);
                return oldValue;
            }
        }

        if (size / tab.length > 2) {
            ensureCapacity(2 * size);
        }
        if (tab[h(key)] == null) {
            tab[h(key)] = new LinkedList<>();
        }
        tab[h(key)].add(new Entry<K, V>(key, value));
        size++;
        modCount++;
        return null;
    }

    public int makePrime(int m) {
        int p = m;
        while(!isPrime(p)){
            p++;
        }
        return p;
    }

    private boolean isPrime(int i) {
        int zahl = i;
        int zaehler;
        boolean primzahl = true;
        for (zaehler = 2; zaehler < Math.sqrt(zahl) + 1; zaehler++) {
            if (zahl % zaehler == 0) {
                primzahl = false;
                break;
            }
        }
        return primzahl;
    }

    private int h(K key) {
        int i = key.hashCode();
        if (i < 0) {
            i = -i;
        }
        return i % tab.length;
    }

    @Override
    public V search(K key) {
        if (tab[h(key)] == null)
            return null;
        for (Entry<K, V> item : tab[h(key)]) {
            if (item.getKey().equals(key)) {
                return item.getValue();
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        if (tab[h(key)] == null)
            return null;
        for (Entry<K, V> item : tab[h(key)]) {
            if (item.getKey().equals(key)) {
                V old = item.getValue();
                tab[h(key)].remove(item);
                size--;
                modCount++;
                return old;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<aufgabe1.Dictionary.Entry<K, V>> iterator() {
        return new HashListIterator();
    }

    private class HashListIterator implements Iterator<aufgabe1.Dictionary.Entry<K, V>> {
        private int current = 0;
        private int currentInList = 0;
        private int currentInArray = 0;
        private int expectedMod = modCount;

        @Override
        public boolean hasNext() {
            return current < size();
        }

        @Override
        public aufgabe1.Dictionary.Entry<K, V> next() {
            if (expectedMod != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            while (tab[currentInArray] == null || tab[currentInArray].size() <= currentInList) {
                currentInArray++;
                currentInList = 0;
            }
            current++;
            return tab[currentInArray].get(currentInList++);
        }

    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}