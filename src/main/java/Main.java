

import java.io.*;
import java.util.*;

public class Main {
    static String path = "/Users/pshah/Desktop/data";
    static class LookupObject {
        //TODO: replace with simple Arraylist and check last before adding in
        TreeSet<Integer> occurences;
        int count;
        public LookupObject() {
            this.occurences = new TreeSet<>();
            this.count = 0;
        }
        void add(Integer num ){
            count++;
            occurences.add(num);
        }
        public TreeSet<Integer> getOccurences() {
            return occurences;
        }
        public int getCount() {
            return count;
        }
    }
    static class Preprocessor {
        HashMap<String, HashMap<String, LookupObject>> everything; // filename to <word, LookupObject.
        String path;

        public Preprocessor(String path){
            this.everything = new HashMap<>();
            this.path = path;
        }

        // Note: lookup arraylist can have the same line num appear multiple times.
        // TODO: check in the reader code
        public static void processLine(String line, HashMap<String, LookupObject> lookup, int linenum){
            String[] tokens = line.split("\\W+");

            for(String token: tokens){
                LookupObject linesIn = new LookupObject();
                if(lookup.containsKey(token)) {
                    linesIn = lookup.get(token);
                }
                linesIn.add(linenum);
                lookup.put(token, linesIn);
            }
        }

        public static HashMap<String, LookupObject> preProcessFile(File file) {
            HashMap<String, LookupObject> lookup = new HashMap<>();
            try {
                FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String line;
                int linenum = 1;
                while((line = br.readLine()) !=null){
                    Preprocessor.processLine(line, lookup, linenum);
                    linenum++;
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return lookup;
        }
        public void preprocessDirectory(String dirpath) {
            File dir = new File(dirpath);
            File[] listofFiles = dir.listFiles();
            for(File file: listofFiles) {
                this.everything.put(file.getName(), preProcessFile(file));
            }
        }

        public void searchForWord(String word){
            System.out.println("Your word: " + word + " is in:");
            for (String file: everything.keySet()){
                HashMap<String, LookupObject> temp = everything.get(file);
                if(temp.get(word) == null)
                    continue;
                System.out.println(file + ", " + temp.get(word).getCount() + ", " + temp.get(word).getOccurences().toString());
            }
        }
    }


    public static void main(String[] args){
       // System.out.println("hello");
        Preprocessor pp = new Preprocessor(path);
        pp.preprocessDirectory(path);
        pp.searchForWord("Juliet");

    }
}
