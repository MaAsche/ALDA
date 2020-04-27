package aufgabe1;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedArrayDictionary<K extends Comparable<? super K>,V> implements Dictionary<K, V> {

    private static final int DEF_CAPACITY = 16;
    private int size;
    private Entry<K,V>[] data;
    private int modCount = 0;

    @SuppressWarnings("unchecked")
    public SortedArrayDictionary() {
        size = 0;
        data = new Entry[DEF_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    public void ensureCapacity(int newCapacity) {
        if (newCapacity < size) {
            return;
        }
        Entry<K,V>[] old = data;
        data = new Entry[newCapacity];
        System.arraycopy(old, 0, data, 0, size);
    }

    @Override
    public V search(K key) {
        int i = searchKey(key);
        if (i >= 0) {
            return data[i].getValue();
        } else {
            return null;
        }
    }

    private int searchKey(K key) {
        for (int i = 0; i < size; i++) {
            if (data[i].getKey().equals(key)) {
                return i; //gefunden
            }
        }
        return -1; //nicht gefunden
    }

    @Override
    public V remove(K key) {
        int i = searchKey(key);
        if (i == -1) {
            return null;
        }

        //Datensatz loeschen und Luecke schliessen
        V r = data[i].getValue();
        for (int j = i; j < size-1; j++) {
            data[j] = data[j+1];
        }
        data[--size] = null;

        modCount++;

        return r;
    }

    @Override
    public V insert(K key, V value) {
        int i = searchKey(key);

        //Vorhandener Eintrag wird ueberschrieben
        if (i != -1) {
            V r = data[i].getValue();
            data[i].setValue(value);
            return r;
        }

        //Neuer Eintrag
        if (data.length == size) {
            ensureCapacity(size * 2);
        }

        int j = size-1;
        while(j >= 0 && key.compareTo(data[j].getKey()) < 0) {
            data[j+1] = data[j];
            j--;
        }
        data[j+1] = new Entry<K,V> (key, value);
        size++;

        modCount++;

        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<aufgabe1.Dictionary.Entry<K, V>> iterator() {
        return new ArrayListIterator();
    }

    private class ArrayListIterator implements Iterator<aufgabe1.Dictionary.Entry<K, V>> {
        private int current = 0;
        private int expectedMod = modCount;

        @Override
        public boolean hasNext() {
            return (current < size());
        }

        @Override
        public aufgabe1.Dictionary.Entry<K, V> next() {
            if (expectedMod != modCount) {
                throw new ConcurrentModificationException();
            }
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            return data[current++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}