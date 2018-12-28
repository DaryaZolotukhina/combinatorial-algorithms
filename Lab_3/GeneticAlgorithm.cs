using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab_3
{
    class GeneticAlgorithm
    {
        //Список точек
        private ListOfPoints list_of_points;
        //Массив цветов
        private Color[] colors;
        //Граф 
        private int[,] graph;
        //Массив задействованных цветов в раскраске графа
        private int[] color_array;
        //Минимальное хроматическое число
        private int min_chromatic;
        //Размер списка вершин ( количество вершин )
        private static int count;

        //вероятность мутации
        private double mutation_rate;
        //Размер популяции
        private int population_size;
        //Количество поколений
        private int count_of_generations;

        //Свойство доступа на чтение к полю хроматическое число
        public int GetMinChromatic()
        {
            return min_chromatic;
        }

        //Текущая популяция
        private ChildOfPopulation[] population;

        //Конструктор генетического алгоритма
        public GeneticAlgorithm(ListOfPoints _list_of_points, Color[] _colors, int[,] _graph, int[] _color_array,
             int pop_size, int count_of_gen, double mutations)
        {
            list_of_points = _list_of_points;
            colors = _colors;
            graph = _graph;
            color_array = _color_array;

            count = list_of_points.Count();


            population_size = pop_size;
            count_of_generations = count_of_gen;
            mutation_rate = mutations;

            population = new ChildOfPopulation[population_size];
        }

        public void RunGeneticSolution()
        {
            int iterations = 1;
            //Инициализация первой популяции
            Initialization();
            //Оценка приспособленности каждого из набора в популяции
            FindSuitability();
            iterations++;
            do
            {
                //Селекция ( метод рулетки )
                Selection();
                //Кроссинговер ( скрещивание двух элементов популяции)
                Crossover();
                //Мутация ( намеренное изменение цветов некоторых вершин )
                Mutation();
                //Оценка приспособленности каждого из набора в популяции
                FindSuitability();
                //Генерация новой популяции
                GenerateNewPopulation();
                iterations++;
            } while (iterations < count_of_generations);
            FindBestResult();
        }

        //Формирование исходной популяции ( Набор из нескольких вариантов раскраски )
        private void Initialization()
        {
            Random random = new Random();
            for (int i = 0; i < population_size; i++)
            {
                int[] temp = new int[count];
                for (int j = 0; j < count; j++)
                {
                    temp[j] = random.Next(1, 12);
                }
                population[i] = new ChildOfPopulation(temp, graph);
            }
        }

        // Оценка приспособленности ( пригодности )
        private void FindSuitability()
        {
            for (int i = 0; i < population.Length; i++)
            {
                population[i].FindSuitability();
            }
        }

        //Селекция ( метод рулетки - вероятность выбора особи тем вероятнее, чем лучше её значение функции приспособленности )
        private void Selection()
        {
            int summ_of_valid = 0;
            List<int> roulette = new List<int>();
            //Расчет суммы значения валидности для всей популяции
            for (int i = 0; i < population.Length; i++)
            {
                summ_of_valid += population[i].Valid;
            }

            int can_to_select = 0;

            //Расчет шанса участвовать в генерации следующего поколения
            for (int i = 0; i < population.Length; i++)
            {
                population[i].Chance = ((double)population[i].Valid / (double)summ_of_valid) * 100;

                int chance = (int)Math.Round(population[i].Chance);
                if (chance != 0)
                {
                    can_to_select++;
                    for (int j = 1; j <= chance; j++)
                    {
                        roulette.Add(i);
                    }
                }
            }

            Random random = new Random();
            List<int> temp_pop = new List<int>();
            for (int i = 0; i < can_to_select; i++)
            {
                //Получаем элемент рулетки
                int choosen = random.Next(0, roulette.Count - 1);
                //Извлекаем индекс элемента популяции по полученному элементу рулетки
                int index = roulette.ElementAt(choosen);
                //Если мы такой элемент уже обрабатывали, то не можем его добавить еще раз в следующее поколение,
                //значит должны пройтись заново по циклу, предварительно откатив счетчик i
                if (temp_pop.Contains(index))
                {
                    i--;
                    continue;
                }
                else
                {
                    //Иначе добавляем в новую популяцию успешный элемент
                    population[index].CanCross = true;
                    temp_pop.Add(index);
                }
            }
        }

        //Кроссинговер ( скрещивание двух элементов популяции)
        private void Crossover()
        {
            Random random = new Random();

            int count_can_to_cross = 0;
            foreach (ChildOfPopulation elem in population)
            {
                if (elem.CanCross)
                {
                    count_can_to_cross++;
                }
            }

            //Количество операций кроссинговера
            int count_of_crossovers = random.Next(0, count_can_to_cross / 2);

            for (int i = 0; i < count_of_crossovers; i++)
            {
                int first_index = random.Next(0, population.Length);
                int second_index = random.Next(0, population.Length);
                ChildOfPopulation first_element = population[first_index];
                ChildOfPopulation second_element = population[second_index];

                // Если мы случайно выбрали один и тот же элемент для кроссинговера или один из них не пригоден
                if ((first_index == second_index) || (!first_element.CanCross) || (!second_element.CanCross))
                {
                    i--;
                    continue;
                }

                int[] temp_array = new int[color_array.Length];

                for (int j = 0; j < color_array.Length; j++)
                {
                    int which_el = random.Next(0, 1);
                    if (which_el == 0)
                    {
                        temp_array[j] = first_element.Color_Array[j];
                    }
                    else
                    {
                        temp_array[j] = second_element.Color_Array[j];
                    }
                }
                //Полученный элемент по итогу кроссинговера
                ChildOfPopulation temp_element = new ChildOfPopulation(temp_array, graph);
                //Обновленное поколение по итогу кроссинговера
                ChildOfPopulation[] temp_population = new ChildOfPopulation[population.Length + 1];
                for (int k = 0; k < population.Length; k++)
                {
                    temp_population[k] = population[k];
                }
                temp_population[population.Length] = temp_element;

                population = temp_population;
            }
        }

        //Мутация ( намеренное изменение цветов некоторых вершин )
        private void Mutation()
        {
            Random random = new Random();
            // Исходная вероятность в диапазоне ( 0 - 100 )
            int source_rate = (int)(mutation_rate * 100);

            // Для каждой раскраски проверяем ее на вероятность мутации
            for (int i = 0; i < population.Length; i++)
            {
                // Случайное число
                int rate = random.Next(0, 100);

                // Если случайное число меньше нашей вероятности, то
                if (source_rate >= rate)
                {
                    // Получаем элемент и его текущую раскраску
                    ChildOfPopulation temp = population[i];
                    int[] temp_colors = temp.Color_Array;

                    // Считаем, сколько вершин необходимо изменить
                    int count_tops = random.Next(0, temp_colors.Length);

                    // Инициализируем список вершин, подлежащих замене
                    List<int> list_of_tops = new List<int>();
                    for (int k = 0; k < count_tops; k++)
                    {
                        int top = random.Next(0, temp_colors.Length - 1);
                        //если вершина уже есть, то откатываемся
                        if (list_of_tops.Contains(top))
                        {
                            k--;
                            continue;
                        }
                        // Добавляем индекс вершины в список
                        list_of_tops.Add(top);
                    }

                    Random colors = new Random();
                    // Для каждой вершины из списка
                    for (int k = 0; k < list_of_tops.Count; k++)
                    {
                        int new_color = colors.Next(1, 12);
                        temp_colors[list_of_tops[k]] = new_color;
                    }
                    // Обновляем текущий элемент на мутированный
                    population[i] = temp;
                }
                else
                {
                    continue;
                }
            }
        }

        //Генерация новой популяции
        private void GenerateNewPopulation()
        {
            List<ChildOfPopulation> list_of_elem = new List<ChildOfPopulation>();
            int good_elems = 0;
            foreach (ChildOfPopulation elem in population)
            {
                list_of_elem.Add(elem);
                if (elem.Valid > 0)
                {
                    good_elems++;
                }
            }
            int max_new_size = (int)population.Length - population.Length / count_of_generations;

            int new_size;
            if (max_new_size < good_elems)
            {
                new_size = max_new_size;
            }
            else
            {
                new_size = good_elems;
            }

            //выбор лучших
            list_of_elem.Sort(delegate (ChildOfPopulation child1, ChildOfPopulation child2)
            { return child2.Valid.CompareTo(child1.Valid); });
            ChildOfPopulation[] temp_population = new ChildOfPopulation[new_size];

            for (int i = 0; i < new_size; i++)
            {
                temp_population[i] = list_of_elem[i];
            }

            population = temp_population;
        }

        private void FindBestResult()
        {
            List<ChildOfPopulation> childOfPopulations = new List<ChildOfPopulation>();
            foreach (ChildOfPopulation elem in population)
            {
                childOfPopulations.Add(elem);
            }

            //сортируем по валидности
            childOfPopulations.Sort(delegate (ChildOfPopulation child1, ChildOfPopulation child2)
            { return child2.Valid.CompareTo(child1.Valid); });

            //берем самую валидную раскраску
            color_array = childOfPopulations[0].Color_Array;
            List<int> result_set = new List<int>();

            for (int i = 0; i <= count - 1; i++)
            {
                //количество цветов
                if (!result_set.Contains((color_array[i])))
                {
                    min_chromatic++;
                }
                result_set.Add(color_array[i]);
                MyPoint myPoint = list_of_points.GetPoint(i);
                myPoint.Draw(1, colors[color_array[i]]);
            }
        }
    }
}
