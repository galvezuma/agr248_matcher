package agr248.matcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import static agr248.matcher.Looker.loadChromosome;

/**
 *
 * @author galvez
 */
public class RastreatorSimpleMarker {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Runtime.getRuntime().maxMemory());
        Recorder recorder= new Recorder();
        SimpleMarker.setRecorder(recorder);
        //List<SimpleMarker> queries = loadSimpleMarkersInFasta("I:\\triticum\\DHNs\\MQTL\\MQTLsToSearchFor.txt");
        List<SimpleMarker> queries = loadSimpleMarkers("/home/galvez/eclipse-workspace/Tests/src/loadtest/seqMarkers.tsv");
        queries.stream().forEach(System.out::println);
        Thread[] hilos = new Thread[21];
        for (int number = 1, count=0; number <= 7; number++) {
            for (char letter : new char[]{'A', 'B', 'D'}) {
            	Chromosome chr = new Chromosome("" + number + letter);
                loadChromosome(chr);
                System.out.println("Chromosome " + chr.getName() + ": " + chr.getStr().length());
                hilos[count] = new Thread() {
                        @Override
                	public void run() {
                		queries.parallelStream().forEach(marker -> marker.locateAnyManyTimes(chr));
                	}
                };
                hilos[count++].start();
            }
        }
        for(Thread hilo: hilos) if (hilo!=null) hilo.join();
        recorder.printPerChromosome();
    }

    
    private static List<SimpleMarker> loadSimpleMarkers(String filename) {
        List<SimpleMarker> ret = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))))) {
            String line;
            while ((line = in.readLine()) != null) {
                ret.add(new SimpleMarker(line));
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return ret;
    }
    
}