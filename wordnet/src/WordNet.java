import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;

public class WordNet {

    private final HashMap<String, Bag<Integer>> nounMap;
    private final HashMap<Integer, String> synSets;
    private final Digraph wordnet;
    private final SAP sap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        nounMap = new HashMap<>();
        synSets = new HashMap<>();
        int size = readSynsets(synsets);
        wordnet = new Digraph(size);
        readHypernyms(hypernyms);
        DirectedCycle dCycle = new DirectedCycle(wordnet);
        if (dCycle.hasCycle()) throw new IllegalArgumentException();
        sap = new SAP(wordnet);
    }

    private int readSynsets(String synsets) {
        In inputStream = new In(synsets);
        int count = 0;
        while (inputStream.hasNextLine()) {
            String[] line = inputStream.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            synSets.put(id, line[1]);
            for (String noun : line[1].split(" ")) {
                if (nounMap.containsKey(noun)) {
                    Bag<Integer> bag = nounMap.get(noun);
                    bag.add(id);
                    nounMap.put(noun, bag);
                } else {
                    Bag<Integer> bag = new Bag<>();
                    bag.add(id);
                    nounMap.put(noun, bag);
                }
            }
            count++;
        }
        return count;
    }

    private void readHypernyms(String hypernyms) {
        In inputStream = new In(hypernyms);
        while (inputStream.hasNextLine()) {
            String[] line = inputStream.readLine().split(",");
            int i = 1;
            while (i < line.length) {
                int id = Integer.parseInt(line[0]);
                wordnet.addEdge(id,
                        Integer.parseInt(line[i++]));
            }
        }

    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nounMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        Queue<Integer> qA = new Queue<>();
        Queue<Integer> qB = new Queue<>();

        for (int x : nounMap.get(nounA)) qA.enqueue(x);
        for (int x : nounMap.get(nounB)) qB.enqueue(x);

        return sap.length(qA, qB);


    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Bag<Integer> bagA = nounMap.get(nounA);
        Bag<Integer> bagB = nounMap.get(nounB);

        Queue<Integer> qA = new Queue<>();
        Queue<Integer> qB = new Queue<>();

        for (int x : bagA) qA.enqueue(x);
        for (int x : bagB) qB.enqueue(x);

        int ancestor = sap.ancestor(qA, qB);

        return synSets.get(ancestor);

    }


    // do unit testing of this class
    public static void main(String[] args) {
//        WordNet w = new WordNet("synsets.txt", "hypernyms.txt");
//        SAP sap = new SAP(w.wordnet);
//        int v = 73799;
//        int W = 73800;
//        StdOut.println(sap.length(v, W));

    }
}
