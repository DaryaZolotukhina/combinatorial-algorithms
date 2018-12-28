using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab_3
{
    class ListOfPoints
    {
        private List<MyPoint> list_of_points;

        public ListOfPoints()
        {
            list_of_points = new List<MyPoint>();
        }

        public int Count()
        {
            return list_of_points.Count;
        }

        public MyPoint GetPoint(int index)
        {
            MyPoint result = list_of_points.ElementAt(index);
            return result;
        }

        public void SetPoint(int index, MyPoint point)
        {
            list_of_points[index] = point;
        }

        public void Add(MyPoint point)
        {
            list_of_points.Add(point);
        }

        public void Delete(int index)
        {
            list_of_points.RemoveAt(index);
        }

        public int IndexOfPoint(int x, int y)
        {
            int i = 0;
            int count = this.Count();
            bool ok = false;

            while ((!ok) && (i != count))
            {
                ok = list_of_points[i].HasPoint(x, y);
                i++;
            }

            if (ok)
            {
                return i - 1;
            }
            else
            {
                return -1;
            }
        }

        public bool Cross(int x, int y)
        {
            int i = 0;
            int count = this.Count();
            bool ok = false;

            while ((!ok) && (i != count))
            {
                ok = list_of_points[i].HasPoint(x, y);
                i++;
            }
            return ok;
        }

        public int Selected()
        {
            int result = 0;
            int i = 0;
            int count = this.Count();

            while (i != count && result != 2)
            {
                if (list_of_points[i].GetChoose())
                {
                    result++;
                }
                i++;
            }
            return result;
        }

        public int GetChoosen()
        {
            int i = 0;
            int result = 1;
            int count = this.Count();

            while (i != count && result != -1)
            {
                if (list_of_points[i].GetChoose())
                {
                    result = i;
                }
                i++;
            }
            return result;
        }
    }
}
