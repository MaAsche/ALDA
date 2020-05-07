package aufgabe1;

import java.util.Iterator;

public class BinaryTreeDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    static private class Node<K, V> {
        private int height;
        private Node<K, V> parent;
        private K key;
        private V value;
        private Node<K, V> left;
        private Node<K, V> right;

        private Node(K k, V v) {
            height = 0;
            key = k;
            value = v;
            left = null;
            right = null;
            parent = null;
        }
    }

    static private class MinEntry<K, V> {
        private K key;
        private V value;
    }

    private Node<K, V> root = null;
    private V oldValue;
    private int size = 0;

    //************************
    //insert
    //************************
    @Override
    public V insert(K key, V value) {
        root = insertR(key, value, root);
        if (root != null) {
            root.parent = null;
        }
        return oldValue;
    }

    private Node<K, V> insertR(K key, V value, Node<K, V> p) {      //Aus Vorlesungsunterlagen
        if (p == null) {
            p = new Node<K, V>(key, value);
            size++;
            oldValue = null;
            if (root != null) {
                root.parent = null;
            }
        } else if (key.compareTo(p.key) < 0) {
            p.left = insertR(key, value, p.left);       //Elternzeiger wird neu gesetzt
            if (p.left != null) {
                p.left.parent = p;
            }
        } else if (key.compareTo(p.key) > 0) {
            p.right = insertR(key, value, p.right);     //Elternzeiger wird neu gesetzt
            if (p.right != null) {
                p.right.parent = p;
            }
        } else {
            oldValue = p.value;
            p.value = value;
        }
        p = balance(p);
        return p;
    }

    //************************
    //search
    //************************
    @Override
    public V search(K key) {
        return searchR(key, root);
    }

    private V searchR(K key, Node<K, V> p) {
        if (p == null)
            return null;
        else if (key.compareTo(p.key) < 0)
            return searchR(key, p.left);
        else if (key.compareTo(p.key) > 0)
            return searchR(key, p.right);
        else
            return p.value;
    }


    //************************
    //remove
    //************************
    @Override
    public V remove(K key) {
        root = removeR(key, root);
        return oldValue;
    }

    private Node<K, V> removeR(K key, Node<K, V> p) {       //Aus Vorlesungsunterlagen
        if (p == null) {
            oldValue = null;
        } else if (key.compareTo(p.key) < 0) {       //Elternzeiger wird neu gesetzt
            p.left = removeR(key, p.left);
            if (p.left != null) {
                p.left.parent = p;
            }
        } else if (key.compareTo(p.key) > 0) {      //Elternzeiger wird neu gesetzt
            p.right = removeR(key, p.right);
            if (p.right != null) {
                p.right.parent = p;
            }
        } else if (p.left == null || p.right == null) {
            oldValue = p.value;
            if (p.left != null) {
                p = p.left;
            } else {
                p = p.right;
            }
        } else {
            MinEntry<K, V> min = new MinEntry<K, V>();
            p.right = getRemMinR(p.right, min);
            oldValue = p.value;
            p.key = min.key;
            p.value = min.value;
            size--;
        }
        p = balance(p);
        return p;
    }

    private Node<K, V> getRemMinR(Node<K, V> p, MinEntry<K, V> min) {
        assert p != null;
        if (p.left == null) {
            min.key = p.key;
            min.value = p.value;
            p = p.right;
        } else {
            p.left = getRemMinR(p.left, min);
        }
        p = balance(p);
        return p;
    }

    @Override
    public int size() {
        return size;
    }

    private int getHeight(Node<K, V> p) {           //Aus Vorlesungsunterlagen
        if (p != null)
            return p.height;
        else
            return -1;
    }

    private int getBalance(Node<K, V> p) {          //Aus Vorlesungsunterlagen
        if (p == null)
            return 0;
        else
            return getHeight(p.right) - getHeight(p.left);
    }

    //************************
    //balance
    //************************
    private Node<K, V> balance(Node<K, V> p) {      //Aus Vorlesungsunterlagen
        if (p == null)
            return null;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        if (getBalance(p) == -2) {
            if (getBalance(p.left) <= 0)
                p = rotateRight(p);
            else
                p = rotateLeftRight(p);
        } else if (getBalance(p) == +2) {
            if (getBalance(p.right) >= 0)
                p = rotateLeft(p);
            else
                p = rotateRightLeft(p);
        }
        return p;
    }

    private Node<K, V> rotateRight(Node<K, V> p) {
        assert p.left != null;
        Node<K, V> q = p.left;
        p.left = q.right;
        if (p.left != null) {
            p.left.parent = p;
        }
        q.right = p;
        if (q.right != null) {
            q.right.parent = q;
        }
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        q.height = Math.max(getHeight(q.left), getHeight(q.right)) + 1;
        return q;
    }

    private Node<K, V> rotateLeft(Node<K, V> p) {
        assert p.right != null;
        Node<K, V> q = p.right;
        p.right = q.left;
        if (p.right != null) {
            p.right.parent = p;
        }
        q.left = p;
        if (q.left != null) {
            q.left.parent = q;
        }
        p.height = Math.max(getHeight(p.right), getHeight(p.left)) + 1;
        q.height = Math.max(getHeight(q.right), getHeight(q.left)) + 1;
        return q;
    }

    private Node<K, V> rotateLeftRight(Node<K, V> p) {
        assert p.left != null;
        p.left = rotateLeft(p.left);
        if (p.left != null) {
            p.left.parent = p;
        }
        return rotateRight(p);
    }

    private Node<K, V> rotateRightLeft(Node<K, V> p) {
        assert p.right != null;
        p.right = rotateRight(p.right);
        if (p.right != null) {
            p.right.parent = p;
        }
        return rotateLeft(p);
    }

    private Node<K, V> leftMostDescendant(Node<K, V> p) {
        assert p != null;
        while (p.left != null)
            p = p.left;
        return p;
    }

    private Node<K, V> parentOfLeftMostAncestor(Node<K, V> p) {
        assert p != null;
        while (p.parent != null && p.parent.right == p)
            p = p.parent;
        return p.parent;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new BinaryTreeIterator();
    }

    //************************
    //Iterator
    //************************
    private class BinaryTreeIterator implements Iterator<Entry<K, V>> {
        private Node<K, V> current = leftMostDescendant(root);

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Entry<K, V> next() {
            Entry<K, V> curr = new Entry<K, V>(current.key, current.value);

            if (current != null) {
                if (current.right != null) {
                    current = leftMostDescendant(current.right);
                } else {
                    current = parentOfLeftMostAncestor(current);
                }
            }
            return curr;
        }
    }

    //prettyPrint
    public void prettyPrint() {
        long start = 0, end = 0;
        start = System.currentTimeMillis();
        System.out.println(root.key);
        if (root.left != null)
            prettyPrint(root.left, 0);
        if (root.right != null)
            prettyPrint(root.right, 0);
        end = System.currentTimeMillis();
        long diff = end - start;
        System.out.println("Duration: " + diff + "ms");
    }

    public void prettyPrint(Node<K, V> p, int depth) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("   ");
        }
        K parentKey = null;
        if (p != null && p.parent != null) {
            parentKey = p.parent.key;
        }
        if (parentKey != null) {
            System.out.println(sb.toString() + "|__" + p.key + " Parent: " + parentKey);
        } else {
            System.out.println(sb.toString() + "|__" + p.key);
        }
        if (p.left != null) {
            prettyPrint(p.left, depth + 1);
        } else if (p.right != null) {
            System.out.println(sb.toString() + "   |__#");
        }

        if (p.right != null) {
            prettyPrint(p.right, depth + 1);
        } else if (p.left != null) {
            System.out.println(sb.toString() + "   |__#");
        }

    }

}