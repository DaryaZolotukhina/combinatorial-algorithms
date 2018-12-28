using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Lab_3
{
    public partial class MainForm : Form
    {
        //Поле, в котором мы рисуем
        Graphics graphicsExact, graphicsGenetic;
        //Высота окна главной формы
        int captionHeight;
        //Границы главной формы
        Size borderSize;
        //Радиус узла графа
        int radius;

        //Список вершин графа
        ListOfPoints list_of_points_exact, list_of_points_genetic;

        //Матрица смежности графа
        int[,] graph;

        //Массив с полученными цветами для узлов
        int[] color_array_exact, color_array_genetic;

        int Count()
        {
            return list_of_points_exact.Count();
        }

        //Цвета, в которые будет краситься граф
        Color[] colors = new Color[] { Color.Green, Color.Red, Color.Purple, Color.Blue, Color.Yellow, Color.Aqua,
            Color.Maroon, Color.Olive, Color.Navy, Color.Teal, Color.Fuchsia, Color.Lime };

        public MainForm()
        {
            InitializeComponent();

            graphicsExact = pictureBoxGraph.CreateGraphics();
            graphicsGenetic = pictureBoxGenetic.CreateGraphics();

            captionHeight = SystemInformation.CaptionHeight;
            borderSize = SystemInformation.BorderSize;
            list_of_points_exact = new ListOfPoints();
            list_of_points_genetic = new ListOfPoints();
            graph = new int[0, 0];
            radius = 10;
            color_array_exact = new int[0];
            color_array_genetic = new int[0];
        }

        private void buttonAbout_Click(object sender, EventArgs e)
        {
            MessageBox.Show(this, "Раскраска графа.\nПолный перебор и генетический алгоритм.", "Условие");
        }

        private void buttonRun_Click(object sender, EventArgs e)
        {
            if (list_of_points_exact.Count() == 0)
            {
                MessageBox.Show(this, "Не указано ни одной вершины графа", "Ошибка", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            Thread threadExact = new Thread(RunExact);
            threadExact.Name = "Exact Solution";

            Thread threadGenetic = new Thread(RunGenetic);
            threadGenetic.Name = "Genetic Solution";

            threadExact.Start();
            threadGenetic.Start();
        }

        public void RunExact()
        {
            ExactAlgorithm algorithms = new ExactAlgorithm(list_of_points_exact, colors, graph, color_array_exact);
            DateTime start_time = DateTime.Now;
            algorithms.RunExactSolution();
            DateTime finish_time = DateTime.Now;
            textBoxMinChromaticExact.Invoke(new Action(() => { textBoxMinChromaticExact.Text = algorithms.GetMinChromatic().ToString(); }));
            string total_time = Convert.ToString(finish_time.Subtract(start_time).TotalSeconds);
            textBoxTimeExact.Invoke(new Action(() => { textBoxTimeExact.Text = total_time; }));
        }

        private void pictureBoxGraph_Click(object sender, EventArgs e)
        {

        }

        private void groupBoxGraph_Enter(object sender, EventArgs e)
        {

        }

        private void pictureBoxGenetic_Click(object sender, EventArgs e)
        {

        }

        private void numericUpDownMutations_ValueChanged(object sender, EventArgs e)
        {

        }

        public void RunGenetic()
        {
            // Количество особей в популяции
            int population_size = (int)numericUpDownCountChildren.Value;
            // Количество поколений
            int count_of_generations = (int)numericUpDownCountGenerations.Value;
            // Вероятность мутаций
            double mutations = (double)numericUpDownMutations.Value;

            GeneticAlgorithm algorithms = new GeneticAlgorithm(list_of_points_genetic, colors, graph, color_array_genetic,
                population_size, count_of_generations, mutations);

            textBoxMinChromaticGenetic.Invoke(new Action(() => { textBoxMinChromaticGenetic.Text = "Вычисление"; }));
            DateTime start_time = DateTime.Now;
            algorithms.RunGeneticSolution();
            DateTime finish_time = DateTime.Now;
            textBoxMinChromaticGenetic.Invoke(new Action(() => { textBoxMinChromaticGenetic.Text = algorithms.GetMinChromatic().ToString(); }));
            string total_time = Convert.ToString(finish_time.Subtract(start_time).TotalSeconds);
            textBoxTimeGenetic.Invoke(new Action(() => { textBoxTimeGenetic.Text = total_time; }));
            pictureBoxGenetic.Invoke(new Action(() => { pictureBoxGenetic.Visible = true; }));
        }

        private void pictureBoxGraph_MouseDown(object sender, MouseEventArgs e)
        {
            var location = e.Location;
            //Правая кнопка мыши - создание точки
            if (e.Button == MouseButtons.Right)
            {
                MyPoint myPointExact = new MyPoint(location.X, location.Y, graphicsExact, radius);
                MyPoint myPointGenetic = new MyPoint(location.X, location.Y, graphicsGenetic, radius);
                if (!list_of_points_exact.Cross(myPointExact.GetX(), myPointExact.GetY()))
                {
                    myPointExact.Draw(0);
                    myPointGenetic.Draw(0);
                    list_of_points_exact.Add(myPointExact);
                    list_of_points_genetic.Add(myPointGenetic);

                    if (graph.Length > 0)
                    {
                        int count = list_of_points_exact.Count();
                        int[,] result = new int[count, count];
                        for (int i = 0; i < count - 1; i++)
                        {
                            for (int j = 0; j < count - 1; j++)
                            {
                                result[i, j] = graph[i, j];
                            }
                            result[i, count - 1] = 0;
                        }
                        graph = result;

                        int[] temp_result_exact = new int[count];
                        for (int i = 0; i < count - 2; i++)
                        {
                            temp_result_exact[i] = color_array_exact[i];
                        }
                        color_array_exact = temp_result_exact;

                        int[] temp_result_genetic = new int[count];
                        for (int i = 0; i < count - 2; i++)
                        {
                            temp_result_genetic[i] = color_array_genetic[i];
                        }
                        color_array_genetic = temp_result_genetic;
                    }
                    else
                    {
                        color_array_exact = new int[1];
                        color_array_genetic = new int[1];
                        graph = new int[1, 1];
                    }
                }
                else
                {
                    int i = list_of_points_exact.IndexOfPoint(myPointExact.GetX(), myPointExact.GetY());
                    MyPoint temp = list_of_points_exact.GetPoint(i);
                    MyPoint temp_genetic = list_of_points_genetic.GetPoint(i);
                    temp.SetChoose(false);
                    temp_genetic.SetChoose(false);
                    temp.Draw(0);
                    temp_genetic.Draw(0);
                }
            }
            //Левая кнопка мыши - выделение узла для установки связи
            if (e.Button == MouseButtons.Left)
            {
                if (list_of_points_exact.Cross(location.X, location.Y))
                {
                    int i = list_of_points_exact.IndexOfPoint(location.X, location.Y);
                    MyPoint temp = list_of_points_exact.GetPoint(i);
                    MyPoint temp_genetic = list_of_points_genetic.GetPoint(i);
                    temp.SetChoose(true);
                    temp_genetic.SetChoose(true);
                    temp.Draw(0);
                    temp_genetic.Draw(0);
                    temp.Draw(2);
                    temp_genetic.Draw(2);
                    if (list_of_points_exact.Selected() == 2)
                    {
                        temp.Draw(0);
                        temp_genetic.Draw(0);
                        temp.SetChoose(false);
                        temp_genetic.SetChoose(false);
                        int j = list_of_points_exact.GetChoosen();
                        MyPoint temp_point_2 = list_of_points_exact.GetPoint(j);
                        MyPoint temp_point_2_genetic = list_of_points_genetic.GetPoint(j);
                        temp_point_2.Draw(0);
                        temp_point_2_genetic.Draw(0);
                        temp_point_2.SetChoose(false);
                        temp_point_2_genetic.SetChoose(false);
                        Pen pen_line = new Pen(Color.Black, 2);
                        graphicsExact.DrawLine(pen_line, temp.GetX(), temp.GetY(), temp_point_2.GetX(), temp_point_2.GetY());
                        graphicsGenetic.DrawLine(pen_line, temp_genetic.GetX(), temp_genetic.GetY(), temp_point_2_genetic.GetX(), temp_point_2_genetic.GetY());
                        graph[i, j] = 1;
                        graph[j, i] = 1;
                    }
                }
            }
        }

        private void buttonClear_Click(object sender, EventArgs e)
        {
            pictureBoxGraph.Image = null;
            list_of_points_exact = new ListOfPoints();
            list_of_points_genetic = new ListOfPoints();
            graph = new int[0, 0];
            color_array_exact = new int[0];
            color_array_genetic = new int[0];
            textBoxMinChromaticExact.Clear();
            textBoxMinChromaticGenetic.Clear();
            textBoxTimeExact.Clear();
            textBoxTimeGenetic.Clear();
            pictureBoxGenetic.Image = null;
        }
    }
}
