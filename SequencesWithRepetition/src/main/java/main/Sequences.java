package main;


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


    public static int check(String wrd,String[] voc,ArrayList<String> seq) {//main method of counting sequences
        Permutation perm=new Permutation();
        int res = 0;
        int step;
        int n = wrd.length();
        Arrays.sort(voc, Comparator.comparing(String::length));
        step=skipShort(voc, n);
        if (step == (-1))
            return res;
        for (int i = step; i < voc.length; i = step) {//main loop
            int m = voc[i].length();
            perm.initSeq(m);
            int end=cntStep(i, voc);
            res+=perm.generate(n,m,step,end,wrd,voc,seq);
            step = end;
            }
        return res;
    }

}
