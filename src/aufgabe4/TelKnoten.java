package aufgabe4;

import java.util.Objects;

public class TelKnoten {
     int x;
     int y;

    TelKnoten(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelKnoten telKnoten = (TelKnoten) o;
        return x == telKnoten.x &&
                y == telKnoten.y;

    }


    public int hashCode() {
        return Objects.hash(x, y);
    }

    public String toString() {
        return "TelKnoten: {" +
                "x: " + x + "," +
                "y: " + y + "}";

    }
}
