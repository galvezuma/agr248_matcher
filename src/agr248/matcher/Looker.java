package agr248.matcher;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author galvezs
 */
public class Looker {
    private static boolean firstLine = true;
    public static StringBuilder str= new StringBuilder(750_000_000);
    public static HashMap<String, ResultPair> hm = new HashMap<>();

    public static void main2(String[] args) throws InterruptedException {
        char letters[] = {'A', 'B', 'D'};
        System.out.println(Runtime.getRuntime().maxMemory());    
//        for(int number = 1; number <= 7; number++){
//            for(char letter : letters){
//                    loadChromosome(""+number+letter);
//                    System.out.println("Chromosome "+number+letter+": "+str.length());
//            }
//        }
//        System.exit(0);
        // loadChromosome("4A");
        
        // Create threads pool 
        ExecutorService tpes = Executors.newFixedThreadPool(8);
        Collection<Callable<Void>> all = new ArrayList<>();
        List<Sequence> queries = loadFasta("G:\\Data_JIC\\NetBeansProjects\\Utilities\\src\\perfectmatch\\DartComplete.fasta");
        for(int number = 1; number <= 7; number++){
            for(char letter : letters){
                System.out.println("Loading "+number+letter);
//                if (anyQueryBelongsToChromosome(queries, ""+number+letter))
                    loadChromosome(""+number+letter);
                for(Sequence query: queries) {
//                    if (query.belongsTo(""+number+letter)) {
                        if (number == 1 && letter == 'A') hm.put(query.name, new ResultPair());
                        all.add(new Callable<Void>() {
                            @Override
                            public Void call() {
                                query.locateAnyManyTimes(str, hm);
                                return null;
                            }
                        });
//                    }
                }
                tpes.invokeAll(all);
                // tpes.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
                all.clear();
            }
        }
        tpes.shutdown();
        showResults();
    }

    public static List<Sequence> loadFasta(String filename) {
        List<Sequence> ret = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))))) {
            String line;
            Sequence seq = null;
            while((line = in.readLine()) != null){
                if (line.startsWith(">")) {
                    if (seq != null)
                        ret.add(seq);
                    seq = new Sequence(line);
                } else {
                    seq.data += line;
                }
            }
            if (seq != null)
                ret.add(seq);
        } catch (Exception x) {
            x.printStackTrace();
        }
        return ret;
    }

    public static void loadChromosome(String name) {
    	loadChromosome(name, str);
    }

    public static void loadChromosome(Chromosome chr) {
    	loadChromosome(chr.getName(), chr.getStr());
    }

    public static void loadChromosome(String name, StringBuilder str) {
        str.delete(0, Integer.MAX_VALUE);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/home/galvez/triticum/WGA1.0/chromosomes/chr"+name+".fa"))))) {
            in.lines().forEach(l -> {
                if (firstLine) {
                    firstLine = false;
                } else {
                    str.append(l);
                }
            });
        } catch (Exception x) {
            System.out.println(Runtime.getRuntime().maxMemory());
            x.printStackTrace();
        }
    }

	private static boolean anyQueryBelongsToChromosome(List<Sequence> queries, String chromosomeName) {
        for(Sequence query: queries) {
                    if (query.belongsTo(chromosomeName))
                        return true;
        }
        return false;
    }
    
    protected static class ResultPair {
        int pos;
        int qty;
    }
  
    private static void showResults() {
        for(String isbp: hm.keySet()){
            Looker.ResultPair rp = hm.get(isbp);
            System.out.print(isbp+"\t"+rp.pos+"\t"+rp.qty);
        }
    }
    
    private static void showResults2() {
        for(String isbp: MY_ORDER){
            Looker.ResultPair rp = hm.get(">"+isbp+" FORWARD");
            System.out.print(isbp+"\t"+rp.pos+"\t"+rp.qty);
            rp = hm.get(">"+isbp+" REVERSEC");
            System.out.println("\t"+rp.pos+"\t"+rp.qty);
        }
    }
    
    private static final String[] MY_ORDER = { "25A",
"59A",
"91A",
"46B",
"221B",
"231B",
"308B",
"5C",
"3D",
"4D",
"5D",
"71A",
"3B",
"25B",
"70B",
"152B",
"180B",
"6D",
"1A",
"6A",
"44A",
"66A",
"113A",
"118A",
"129A",
"87B",
"270B",
"2D",
"23E",
"10A",
"17A",
"32A",
"48A",
"51A",
"87A",
"106A",
"138A",
"116B",
"203B",
"1C",
"13C",
"5E",
"53E",
"67E",
"87E" };
}