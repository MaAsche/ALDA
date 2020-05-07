package aufgabe1;

import java.util.*;

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
    private void ensureCapacity (int newCapacity) { //Tabelle wir vergrößert & Daten kopiert
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
                if (tab[hash(curr.getKey())] == null) {
                    tab[hash(curr.getKey())] = new LinkedList<Entry<K,V>>();
                }
                tab[hash(curr.getKey())].add(0, new Entry<K,V>(curr.getKey(), curr.getValue()));
            }
        }
    }

    @Override
    public V insert(K key, V value) {
        if (tab[hash(key)] == null) {                   //Liste  nicht vorhanden
            tab[hash(key)] = new LinkedList<>();
        }
        for (Entry<K, V> item : tab[hash(key)]) {
            if (item.getKey().equals(key)) {            //Key vorhanden -> Value wird ersetzt
                V oldValue = item.getValue();
                item.setValue(value);
                return oldValue;
            }
        }

        if (size / tab.length > 2) {                    //load factor wird überschritten
            ensureCapacity(2 * size);
        }
        if (tab[hash(key)] == null) {
            tab[hash(key)] = new LinkedList<>();
        }
        tab[hash(key)].add(new Entry<K, V>(key, value));        //neuer Eintrag wird angelegt
        size++;
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

    private int hash(K key) {               //Aus Vorlesungsunterlagen
        int i = key.hashCode();
        if (i < 0) {
            i = -i;
        }
        return i % tab.length;
    }

    @Override
    public V search(K key) {
        if (tab[hash(key)] == null)                //Key nicht vorhanden
            return null;
        for (Entry<K, V> item : tab[hash(key)]) {
            if (item.getKey().equals(key)) {
                return item.getValue();             //Ausgabe der zugehörigen Value
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        if (tab[hash(key)] == null)             //Key nicht vorhanden
            return null;
        for (Entry<K, V> item : tab[hash(key)]) {
            if (item.getKey().equals(key)) {
                V old = item.getValue();
                tab[hash(key)].remove(item);        //Entferne Daten
                size--;
                return old;                     //Return der entfernten Daten
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