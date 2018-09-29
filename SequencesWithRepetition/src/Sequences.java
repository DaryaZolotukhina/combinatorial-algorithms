import java.util.*;


public class Sequences {
    public static boolean nextSet(int ar[], int n, int m) { //numerical sequence generation
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

    public static String formSlovo(int ar[], int m, String wrd) { //word formation based on numerical sequence
        char[] resWrd=new char[m];
        for (int i=0;i<ar.length;i++)
            resWrd[i]=wrd.charAt(ar[i]-1);
        return String.valueOf(resWrd);
    }

    public static boolean correctSeq(int[] ar, int n){ //check the sequence for the contents of each number at least once
        HashSet<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < ar.length; i++) { //add all numbers of the sequence to the set
            int elem = ar[i];
            if (!set.contains(elem))
                set.add(elem);
        }
        for (int j = 1; j < n + 1; j++)
            if (!set.contains(j)) //check the content
                return false;
        return true;
    }

    public static int Check(String wrd,String[] voc,ArrayList<String> seq) { //main method of counting sequences
        int res=0;
        int step;
        int f=0;
        int n=wrd.length();
        Comparator<String> comprator = (o1, o2) -> o1.length() - o2.length();
        Arrays.sort(voc,comprator);
        while (f<voc.length && voc[f].length()<n){
            f++;
        }
        step=f; //skip all words of shorter length than the word entered
        if (step==(voc.length)) //if there weren't words of longer or equal length
            return res;
        for (int i=step; i<voc.length;i=step) { //main loop
                boolean flag = true;
                HashSet<Integer> set = new HashSet<Integer>();
                ArrayList<int[]> listSeq=new ArrayList<int[]>();
                int m = voc[i].length();
                int[] ar = new int[m];
                for (int j = 0; j < m; j++) //the first sequence (all numbers are 1)
                    ar[j] = 1;
                set.add(1);
                for (int k = 1; k < n + 1; k++) //check the first sequence
                    if (!set.contains(k))
                        flag = false;
                if (flag == true) { //add to the array of all sequences
                    int[] helpAr;
                    helpAr=Arrays.copyOf(ar,ar.length);
                    listSeq.add(helpAr);
                        }
                while (nextSet(ar, n, m)){ //all sequence generation
                               if (correctSeq(ar,n)) { //if the sequence is correct
                                   int[] helpAr;
                                   helpAr = Arrays.copyOf(ar, ar.length);
                                   listSeq.add(helpAr);//add to the array of all sequences
                               }
                }
            do
    { //compare sequences with all words of equal length
        for (int[] arraySeq : listSeq) {
            if (formSlovo(arraySeq, m, wrd).equals(voc[i])) {
                res++;
                seq.add(voc[i]);
                break;
            }
        }
        i++;
    }
    while (i<voc.length && voc[i].length()==voc[i-1].length()); //while the length of words is equal
                step=i;
            }
        return res;
    }

}
