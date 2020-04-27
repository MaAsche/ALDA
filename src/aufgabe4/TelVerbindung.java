package aufgabe4;

public class TelVerbindung {

    int cost;
    TelKnoten start;
    TelKnoten end;

    public TelVerbindung(int cost, TelKnoten start, TelKnoten end) {
        this.cost = cost;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return start.toString() + " - " + cost + " - " + end.toString();
    }
}
