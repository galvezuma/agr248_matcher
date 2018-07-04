package agr248.matcher;

import java.util.HashMap;

/**
 *
 * @author galvez
 */
public class Sequence {
    public String name;
    public String data;

    Sequence(String line) {
        name = line;
        data = "";
    }
    public String reverseComplementary(){
        StringBuilder str = new StringBuilder(10_000);
        for (int i=0; i < data.length(); i++){
            str.append(complementary(data.charAt(i)));
        }
        return str.reverse().toString();
    }
    
    public String complementary(){
        StringBuilder str = new StringBuilder(10_000);
        for (int i=0; i < data.length(); i++){
            str.append(complementary(data.charAt(i)));
        }
        return str.toString();
    }
    
    public String reverse(){
        StringBuilder str = new StringBuilder(10_000);
        for (int i=0; i < data.length(); i++){
            str.append(data.charAt(i));
        }
        return str.reverse().toString();
    }

    public static char complementary(char c) {
        switch(c) {
            case 'A' : return 'T';
            case 'T' : return 'A';
            case 'G' : return 'C';
            case 'C' : return 'G';
            default : return c;
        }
    }

    boolean belongsTo(String nameChromosome) {
        return name.contains("TRAES"+nameChromosome) || name.contains("Traes_"+nameChromosome);
    }

    void locateIn(StringBuilder str) {
         System.out.println("Trying: " + name);
        int pos = -1;
        String dataComplementary = reverseComplementary();
        for(int i=0; i < data.length() - 60; i+=60){
            pos = str.indexOf(data.substring(i, i+59));
            if (pos == -1) pos = str.indexOf(dataComplementary.substring(i, i+59));
            if (pos != -1) {
                System.out.println(""+(pos+i)+" .... "+name);
                break;
            } else System.out.print(".");
        }
        if (pos == -1) {
            System.out.println("Gene not found. "+name);
        } else if (str.indexOf(data, pos+1) != -1 || str.indexOf(dataComplementary, pos+1) != -1) {
            System.out.println("But something is wrong.");
        }
    }
    
    private int locateInternal(StringBuilder str) {
        int pos = -1;
        for(int i=0; i < data.length() - 60; i++){
            if (str == null) System.exit(1);
            pos = str.indexOf(data.substring(i, i+59));
            if (pos != -1) {
                System.out.println(""+(pos+i)+" .... "+name);
                break;
            }
        }
        return pos;
    }
        
    void locateAnyManyTimes(StringBuilder str, HashMap<String, Looker.ResultPair> hm){
        int pos;
        // System.out.println("Trying: " + name);
        pos = -1;
        while((pos = str.indexOf(data, pos+1)) != -1) { 
            System.out.println("("+name+") Forward found "+pos); 
            registerPos(pos, hm);
        }
        pos = -1;
        while ((pos = str.indexOf(reverseComplementary(), pos+1)) != -1) {
            System.out.println("("+name+") Forward Reverse Complementary found "+pos);
            registerPos(pos, hm);
        }
        pos = -1;
        while ((pos = str.indexOf(reverse(), pos+1)) != -1)  {
            System.out.println("("+name+") Forward Reverse found "+pos);
            registerPos(pos, hm);
        }
        pos = -1;
        while ((pos = str.indexOf(complementary(), pos+1)) != -1) {
            System.out.println("("+name+") Forward Complementary found "+pos);
            registerPos(pos, hm);
        }
        Looker.ResultPair rp = hm.get(name);
        //System.out.println(name + "\t" + rp.pos+"\t"+rp.qty);
    }

    private void registerPos(int pos, HashMap<String, Looker.ResultPair> hm) {
        Looker.ResultPair rp = hm.get(name);
        rp.qty++;
        rp.pos = (rp.qty == 1)? pos : 0 ;
    }

    StringBuilder searchForSegmentManyTimes(String segment, int maxErrors) {
        StringBuilder ret = null;
        for(int i=0; i<this.data.length()-segment.length()+1; i++){
            int numErrors = 0;
            int offset = 0;
            do {
                if (segment.charAt(offset) != data.charAt(i+offset)) numErrors ++;
                offset++;
            } while(numErrors <= maxErrors && offset < segment.length());
            if (numErrors <= maxErrors) {
                if (ret == null)
                    ret = new StringBuilder().append(name+"\n");
                ret.append(data.substring(i, i+segment.length())).append("("+numErrors+")\t");
                i += segment.length() - 2;
            }
        }
        return ret;
    }
}