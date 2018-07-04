package agr248.matcher;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author galvez
 */
public class SimpleMarker {
    private static final byte MAX_MISMATCHES = 1;
    private static Recorder recorder;
	
    String name;
    String forward;
    
    public SimpleMarker(){}
    
    public SimpleMarker(String line) {
        String[] parts = line.split("\t");
        name = parts[0];
        forward = parts[1];
    }
    
    public SimpleMarker(String name, String forward) {
        this.name = name;
        this.forward = forward;
    }
    
    public static String reverseComplementary(String data){
        StringBuilder str = new StringBuilder(10_000);
        for (int i=0; i < data.length(); i++){
            str.append(Sequence.complementary(data.charAt(i)));
        }
        return str.reverse().toString();
    }
    
    public static String reverse(String data){
        return new StringBuilder(data).reverse().toString();
    }    
    
    public static String complementary(String data){
        StringBuilder str = new StringBuilder(10_000);
        for (int i=0; i < data.length(); i++){
            str.append(Sequence.complementary(data.charAt(i)));
        }
        return str.toString();
    }
    
    int locate(Chromosome chr, String src, int from, byte mmm, String type) { // mmm =is= max mis matches
    	StringBuilder str = chr.getStr();
        for (int i=from; i<str.length()-src.length()+1; i++){
            byte mam = mmm; // mam =is= max allowed mismatches
            for(int j=0;j<src.length() && mam != -1;j++){
                if (str.charAt(i+j) != src.charAt(j)) mam--;
            }
            if (mam != -1) {
            	PrintWriter out = new PrintWriter(System.out);
            	StringWriter sw = null;
            	if (recorder != null) out = new PrintWriter(sw = new StringWriter());
                out.println("\n----------------");
                out.println("Found "+name+" "+type+" at "+i+" with "+(mmm-mam)+" mismatches");
                out.println("\t"+str.substring(i, i+src.length()));
                out.println("\t"+src);
                out.print("\t");
                for(int j=0;j<src.length();j++){
                    out.print((str.charAt(i+j) != src.charAt(j))?"*":" ");
                }
                if (recorder != null) recorder.register(chr.getName(), this.name, sw.toString());
                System.out.println("");
                return i;
            }
            //if (i%1_000_000 == 0) System.out.println("Looking for"+name+" at "+i);
        }
        return -1;
    }
    
    void locateAnyManyTimes(Chromosome chr){
        locateManyTimes(chr, forward, "Forward");
        locateManyTimes(chr, complementary(forward), "Complementary");
        locateManyTimes(chr, reverse(forward), "Reverse");
        locateManyTimes(chr, reverseComplementary(forward), "Reverse complementary");
    }
    
    private void locateManyTimes(Chromosome chr, String src, String type) {
        int pos = -1;
        pos = locate(chr, src, pos+1, MAX_MISMATCHES, type);
        while (pos != -1) {
            pos = locate(chr, src, pos+1, MAX_MISMATCHES, type);
        }
    }
    
    @Override
    public String toString(){
        return ">"+name+" FORWARD\n"+forward;
    }

    public static void setRecorder(Recorder r) {
            recorder = r;
    }

}