package main.java.wave;

import java.awt.*;
import java.util.ArrayDeque;

public class Algorithm {
    static int max_size=7;

    static int[] dCol ={-2,-2,-1,-1,1,1,2,2};
    static int[] dRow={-1,1,-2,2,-2,2,-1,1};

    //нахождение пути
    public static boolean findWay( int rowend,int colend,int rowbeg, int colbeg, int[][] a)
    {
        int p,m;

        boolean res=false;
        Coordinates coordBeg=new Coordinates(colbeg,rowbeg);
        ArrayDeque<Coordinates> qu=new ArrayDeque<Coordinates>();
        qu.push(coordBeg);
        a[colbeg][rowbeg]=1;
        while (!qu.isEmpty() && !res)
        {
            Coordinates coord1;
            coord1=qu.pop();
            p=coord1.x;
            m=coord1.y;
            //проверяем в какие клетки конь может попасть из текущей(главной) клетки
            for(int t=0;t<8;t++)
            {
                int x1=(p+dCol[t]);
                int y1=(m+dRow[t]);
                if ((x1<0) | (y1<0) | (x1>max_size) | (y1>max_size)) //если конь за границами доски
                    continue;
                if((a[p+dCol[t]][m+dRow[t]])==0)//если ячейка не помечена
                {
                    a[p+dCol[t]][m+dRow[t]]=a[p][m]+1;
                    //если дошли до финиша
                    if((p+dCol[t]==colend)&&(m+dRow[t]==rowend))
                        res=true;
                    Coordinates coord2=new Coordinates(p+dCol[t],m+dRow[t]);
                    qu.push(coord2);
                }
            }
        }
        return res;
    }

    //раскраска ячейки
    public static void light(int aCol, int aRow, int step) throws InterruptedException {
        MainForm.squares[aCol][aRow].setBackground(Color.yellow);
        MainForm.squares[aCol][aRow].setText(String.valueOf(step));
    }


    //построение пути
    public static void buildWay(int rowend,int colend, int rowbeg, int colbeg,int[][] a) throws InterruptedException {
        int p=colend;
        int m=rowend;
        int step=1;
        light(p,m,step);
        while (p!=colbeg || m!=rowbeg){
            int t=-1;
            boolean found=false;
            while (!found && t<=6){
                t++;
                int x1=(p+dCol[t]);
                int y1=(m+dRow[t]);
                if ((x1<0) | (y1<0) | (x1>max_size) | (y1>max_size)) //если конь за границами доски
                    continue;
                found=(a[p][m]-a[p+dCol[t]][m+dRow[t]]==1);
            }
            if (found)
            {
                step++;
                light(p+dCol[t],m+dRow[t],step);
                p=p+dCol[t];
                m=m+dRow[t];
            }
        }
        light(p,m,step);
    }
}
