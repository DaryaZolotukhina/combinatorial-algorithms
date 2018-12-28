using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab_3
{
    class ExactAlgorithm
    {
        private ListOfPoints list_of_points;
        private Color[] colors;
        private int[,] graph;
        private int[] color_array;
        private int min_chromatic;

        private static int count;

        public int[] GetResultArray()
        {
            return color_array;
        }

        public int GetMinChromatic()
        {
            min_chromatic = 0;
            for (int i = 0; i < color_array.Length; i++)
            {
                if (min_chromatic < color_array[i])
                {
                    min_chromatic = color_array[i];
                }
            }
            return min_chromatic + 1;
        }

        //конструктор
        public ExactAlgorithm(ListOfPoints _list_of_points, Color[] _colors, int[,] _graph, int[] _color_array)
        {
            list_of_points = _list_of_points;
            colors = _colors;
            graph = _graph;
            color_array = _color_array;
            count = list_of_points.Count();
        }

        public void RunExactSolution()
        {
            HashSet<int> result_set = new HashSet<int>();
            for (int i = 0; i <= count - 1; i++)
            {
                color_array[i] = 0;
            }

            for (int i = 0; i <= count - 1; i++)
            {
                result_set.Add(i);
                color_array[i] = ExactSolution(i, result_set);
                MyPoint myPoint = list_of_points.GetPoint(i);
                myPoint.Draw(1, colors[color_array[i]]);
            }
        }

        public int ExactSolution(int index, HashSet<int> result_set)
        {
            HashSet<int> ColorSet = new HashSet<int>();
            bool check = false;
            int result = 0;
            
            //цвета, которые уже заняты
            for (int j = 0; j <= count - 1; j++)
            {
                if (result_set.Contains(j) && (graph[index, j] == 1))
                {
                    ColorSet.Add(color_array[j]);
                }
            }

            //пока не найдем первый доступный цвет
            do
            {
                if (ColorSet.Contains(result))
                {
                    result++;
                    check = false;
                }
                else
                {
                    check = true;
                }
            } while (!check);

            return result;
        }
    }
}
