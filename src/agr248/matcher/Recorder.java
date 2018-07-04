package agr248.matcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/*
 * This stores a list of matches per marker per chromosome.
 */
public class Recorder {
    private final Hashtable<String, Hashtable<String, List<String>>> outerTable = new Hashtable<>();

    public void register(String chr, String marker, String data) {
        System.out.println("Recording");
        if (! outerTable.containsKey(chr))
            outerTable.put(chr, new Hashtable<>());
        Hashtable<String, List<String>> innerTable = outerTable.get(chr);
        if (! innerTable.containsKey(marker))
            innerTable.put(marker, new ArrayList<>());
        List<String> results = innerTable.get(marker);
        results.add(data);
    }
	
    public void printPerChromosome() {
        Set<String> chrs = outerTable.keySet();
        ArrayList<String> lista= new ArrayList<>(chrs);
        Collections.sort(lista);
        for(String chr: lista) {
            System.out.println("----\n----\nChromosome: "+chr);
            for (String marker: outerTable.get(chr).keySet()) {
                System.out.println("\n"+marker);
                for (String data: outerTable.get(chr).get(marker)) {
                        System.out.println(data);
                }
            }
        }
    }
	
}
