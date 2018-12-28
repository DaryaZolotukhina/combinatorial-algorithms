using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab_3
{
    class MyPoint
    {
        private int radius;
        private int X;
        private int Y;
        private bool choose;
        private Graphics graphics;

        public MyPoint(int _x, int _y, Graphics _graphics, int _radius)
        {
            X = _x;
            Y = _y;
            choose = false;
            graphics = _graphics;
            radius = _radius;
        }

        public int GetX()
        {
            return this.X;
        }

        public int GetY()
        {
            return this.Y;
        }

        // 0 - заполнить черным цветом (создание вершины)
        // 1 - заполнить пользовательским цветом при прогоне алгоритма
        // 2 - сделать цветную окантовку (выделение вершины для соединения)
        public void Draw(int status, Color _color)
        {
            if (status == 1)
            {
                Pen blackPen = new Pen(Color.Black, 3);
                graphics.DrawEllipse(blackPen, X - radius, Y - radius, 2 * radius, 2 * radius);
                SolidBrush brush = new SolidBrush(_color);
                graphics.FillEllipse(brush, X - radius, Y - radius, 2 * radius, 2 * radius);
            }
        }

        // 0 - заполнить черным цветом (создание вершины)
        // 1 - заполнить пользовательским цветом при прогоне алгоритма
        // 2 - сделать цветную окантовку (выделение вершины для соединения)
        public void Draw(int status)
        {
            if (status == 0)
            {
                Pen blackPen = new Pen(Color.Black, 3);
                graphics.DrawEllipse(blackPen, X - radius, Y - radius, 2 * radius, 2 * radius);
                graphics.FillEllipse(Brushes.Black, X - radius, Y - radius, 2 * radius, 2 * radius);
            }
            if (status == 2)
            {
                Pen redPen = new Pen(Color.Red, 3);
                graphics.DrawEllipse(redPen, X - radius, Y - radius, 2 * radius, 2 * radius);
            }
        }

        public bool HasPoint(int _x, int _y)
        {
            bool result = ((_x >= X - 2 * radius)) && (_x <= (X + 2 * radius)) && (_y >= (Y - 2 * radius)) && (_y <= (Y + 2 * radius));
            return result;
        }

        public bool GetChoose()
        {
            return choose;
        }

        public void SetChoose(bool _choose)
        {
            choose = _choose;
        }
    }
}
