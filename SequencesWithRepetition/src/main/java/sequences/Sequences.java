package sequences;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Sequences {

    public static int skipShort(String[] voc, int n) { //skip all words of shorter length than the word entered
        int f=0;
        while (f < voc.length && voc[f].length() < n) {
            f++;
        }
        if (f == (voc.length)) //if there weren't words of longer or equal length
            return (-1);
        return f;
    }

    public static int cntStep(int i, String[] voc){ //counting words with the same length
            i++;
            while (i < voc.length && voc[i].length() == voc[i - 1].length())
                i++;
            return i;
    }


    public static ArrayList<String> check(String wrd,String[] voc) {//main method of counting sequences
        Permutation perm=new Permutation();
        ArrayList<String> seq=new ArrayList<>();
        int step;
        int n = wrd.length();
        Arrays.sort(voc, Comparator.comparing(String::length));
        step=skipShort(voc, n);
        if (step == (-1))
            return seq;
        for (int i = step; i < voc.length; i = step) {//main loop
            int m = voc[i].length();
            perm.initSeq(m);
            int end=cntStep(i, voc);
            while (perm.next(n, m)) { //all sequence generation
                if (perm.isCorrect(n))//if the sequence is correct
                    for (int j = step; j < end; j++) {
                        if (perm.toString(m, wrd).equals(voc[j])) {
                            seq.add(voc[j]);
                            break;
                        }
                    }
            }
            step = end;
            }
        return seq;
    }

}
