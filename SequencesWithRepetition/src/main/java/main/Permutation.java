package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Permutation {

    Integer[] ar;

    public boolean isCorrect(int n){ //check the sequence for the contents of each number at least once
        Set<Integer> set = new HashSet<Integer>(Arrays.asList(ar));
        for (int j = 1; j < n + 1; j++)
            if (!set.contains(j)) //check the content
                return false;
        return true;
    }

    public String toString(int m, String wrd) { //word formation based on numerical sequence
        char[] resWrd=new char[m];
        for (int i=0;i<ar.length;i++)
            resWrd[i]=wrd.charAt(ar[i]-1);
        return String.valueOf(resWrd);
    }

    public boolean next(int n, int m) { //numerical sequence generation
        int ind=m-1;
        while ((ind >= 0) && (ar[ind] == n))
            ind--;
        if (ind < 0)
            return false;
        ar[ind]++;
        if (ind == m-1)
            return true;
        for (int k=ind+1; k<m; k++)
            ar[k]=1;
        return true;
    }

    public void initSeq(int m){ //initialization of the first sequence
        ar=new Integer[m];
        for (int j = 0; j < m-1; j++)
            ar[j] = 1;
        ar[m-1]=0;
    }

    public int generate(int n, int m, int step, int end, String wrd,String[] voc,ArrayList<String> seq) { //sequences generation for all words of equal length
        int res=0;
        while (this.next(n, m)) { //all sequence generation
            if (this.isCorrect(n))//if the sequence is correct
                for (int i = step; i < end; i++) {
                    if (this.toString(m, wrd).equals(voc[i])) {
                        res++;
                        seq.add(voc[i]);
                        break;
                    }
                }
        }
        return res;
    }
}
