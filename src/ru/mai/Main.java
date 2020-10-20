package ru.mai;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        info();

        try {
            boolean flag;

            do {
                flag = menu();
                System.out.println();
            } while (flag);
        } catch (NumMethodsException | MathParsingException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Unknown error");
        }
    }

    private static void info() {
        System.out.println("Курсовая работа по предмету");
        System.out.println("Компьютерное обеспечение математических вычеслений");
        System.out.println();
    }

    private static void menuText() {
        System.out.println("Меню:");
        System.out.println("0) Выход из программы");
        System.out.println("1) Посчитать математическое выражение");
        System.out.println("2) Найти корень уравнения на промежутке");
        System.out.println("3) Найти производную функции в точке");
        System.out.println("4) Найти интеграл функции на промежутке");
        System.out.println("5) Решить СЛАУ");
        System.out.println();
    }

    private static boolean menu() throws MathParsingException, NumMethodsException {
        Scanner in = new Scanner(System.in);

        boolean flag;
        byte item;

        menuText();
        System.out.print("Введите пункт меню: ");

        do {
            item = in.nextByte();
            if (item == 0) {
                return false;
            } else if (item < 1 || item > 5) {
                System.out.println("Нет такого пункта");
                System.out.print("Введите пункт меню: ");
                flag = true;
            } else {
                flag = false;
            }
        } while (flag);

        System.out.println();

        MathParsing mp;
        double left, right;
        switch (item) {
            case 1:
                System.out.println("Введите математическое выражение: ");
                in.nextLine();
                mp = new MathParsing(in.nextLine());
                System.out.println("Ответ: " + mp.calculate());
                return true;
            case 2:
                System.out.println("Введите уравнение: ");
                in.nextLine();
                mp = new MathParsing(in.nextLine());
                System.out.print("Введите левую границу нахождения корня: ");
                left = in.nextDouble();
                System.out.print("Введите правую границу нахождения корня: ");
                right = in.nextDouble();
                System.out.println("Решение уравнения: " + NumMethods.equationHalfDiv(mp, left, right));
                return true;
            case 3:
                System.out.println("Введите функцию: ");
                in.nextLine();
                mp = new MathParsing(in.nextLine());
                System.out.print("Введите точку в которой найти производную: ");
                double point = in.nextDouble();
                System.out.println("Производная в точке: " + NumMethods.derivative(mp, point));
                return true;
            case 4:
                System.out.println("Введите функцию: ");
                in.nextLine();
                mp = new MathParsing(in.nextLine());
                System.out.print("Введите нижнюю границу интегрирования: ");
                left = in.nextDouble();
                System.out.print("Введите верхнюю границу интегрирования: ");
                right = in.nextDouble();
                System.out.println("Ответ: " + NumMethods.integralSimpson(mp, left, right));
                return true;
            default:
                System.out.print("Введите количестов уравнений: ");
                int size = in.nextInt();
                System.out.println("Введите коэффициенты по строчно: ");
                double[][] matrix = new double[size][size];
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        matrix[i][j] = in.nextDouble();
                    }
                }
                System.out.println("Введите вектор столбец свободных коэффициентов");
                double[] vec = new double[size];
                for (int i = 0; i < size; i++) {
                    vec[i] = in.nextDouble();
                }
                double[] answer = NumMethods.slaeMethodLU(matrix, vec);
                for (int i = 0; i < answer.length; i++) {
                    System.out.println("x" + i + "=" + answer[i]);
                }
                return true;
        }
    }
}


