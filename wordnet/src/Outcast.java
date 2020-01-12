import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet w;

    public Outcast(WordNet wordnet) {
        this.w = wordnet;
    }

    public String outcast(String[] nouns) {
        int maxDistance = 0;
        String outcast = "";
        for (String noun : nouns) {
            int distance = 0;
            for (String v : nouns) distance += w.distance(noun, v);
//            StdOut.println(noun + ": " + distance);
            if (distance > maxDistance) {
                maxDistance = distance;
                outcast = noun;
            }
        }
        return outcast;
    }

    public static void main(String[] args) {
        String[] nouns = new In("outcast5.txt").readAllStrings();
        for (String noun : nouns) StdOut.print(noun + " ");
        StdOut.println("\n");
        WordNet w = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast o = new Outcast(w);
        StdOut.printf("Outcast: %s", o.outcast(nouns));
    }
}
