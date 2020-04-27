package aufgabe1;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TUI {
    public static final Scanner INPUT = new Scanner(System.in);
    private static Dictionary<String, String> dict = new SortedArrayDictionary<>();

    public static void main(String[] args) {
        String in;
        while (INPUT.hasNext()) {
            in = INPUT.next();
            switch (in) {
                case "create":
                    switch (INPUT.next()) {
                        case "b":
                            dict = create(2);
                            break;
                        case "h":
                            dict = create(1);
                            break;
                        default:
                            dict = create(0);
                    }
                    break;
                case "read":
                    if (INPUT.hasNextInt()) {
                        dict = read(INPUT.nextInt(), INPUT.next());
                        break;
                    } else {
                        read(INPUT.next());
                        break;
                    }
                case "p":
                    print();
                    break;
                case "s":
                    search(INPUT.next());
                    break;

                case "i":
                    insert(INPUT.next(), INPUT.next());
                    break;

                case "r":
                    remove(INPUT.next());
                    break;
                case "exit":
                    exit();
                    break;

            }
        }
    }

    private static Dictionary<String, String> read(int nextInt, String next) {
        System.out.println("Reading Dictionary");

        if (dict == null) {
            create(0);
        }

        try {
            LineNumberReader in = new LineNumberReader(new FileReader(next));
            String line;

            for (int i = 0; (line = in.readLine()) != null; i++) {
                String[] sf = line.split(" ");
                if (nextInt == -1 || nextInt > i) {
                    dict.insert(sf[0], sf[1]);
                } else if (nextInt == i) {
                    break;
                }
            }

            in.close();


        } catch (IOException ex) {
            System.out.println("Error");
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dict;
    }

    private static void read(String next) {
        read(-1, next);
    }

    private static Dictionary<String, String> create(int i) {
        System.out.println("Creating dictionary");
        if (i == 2) {
            System.out.println("BinaryTreeDictionary");
            return new BinaryTreeDictionary<>();
        } else if (i == 1) {
            System.out.println("HashDictionary");
            return new HashDictionary<>(3);
        } else {
            System.out.println("SortedArrayDictionary");
            return new SortedArrayDictionary<>();
        }
    }

    private static void search(String key) {
        System.out.println("Searching for " + key);
        String s;
        if ((s = dict.search(key)) != null) {

            System.out.println("Search result: " + s);
        } else {
            System.out.println("No entry found");
        }

    }

    private static void insert(String key, String value) {
        dict.insert(key, value);
    }

    private static void remove(String key) {
        dict.remove(key);
    }

    private static void print() {
        long start = 0, end = 0;
        System.out.println("Printing dictionary");
        if (dict instanceof BinaryTreeDictionary) {
            pp();
        } else {
            start = System.currentTimeMillis();
            for (Dictionary.Entry<String, String> e : dict) {
                System.out.println(e.getKey() + ": " + e.getValue());
            }
            end = System.currentTimeMillis();
            long diff = end - start;
            System.out.printf("Duration: %dms", diff);
        }

    }

    private static void pp() {
        ((BinaryTreeDictionary<String, String>) dict).prettyPrint();
    }

    private static void exit() {
        System.out.println("Ende");
        System.exit(0);
    }
}
