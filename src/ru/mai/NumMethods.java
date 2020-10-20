package ru.mai;

/**
 * Класс реализующий некоторые численные методы
 */
public class NumMethods {

    /**
     * Решение уравнения методом половинного деления
     *
     * @param fun    - функция
     * @param left   - левая граница расположения корня
     * @param right- правая граница расположения корня
     * @return решение уравнения
     * @throws MathParsingException - ошибка при работе с функцией
     */
    public static double equationHalfDiv(final MathParsing fun, double left, double right)
            throws MathParsingException {

        double answer = (left + right) / 2;

        double value = fun.calculate(answer);

        double del;

        do {
            if (value > 0 == fun.calculate(left) > 0) {
                left = (left + right) / 2;
            } else {
                right = (left + right) / 2;
            }

            answer = (left + right) / 2;

            del = value;
            value = fun.calculate(answer);
            del -= value;
        } while (del != 0);

        return answer;
    }

    /**
     * Нахождение производной в точке по определению
     *
     * @param fun   - функция
     * @param point - точка в которой надо найти производную
     * @return производная функции в точке
     * @throws MathParsingException - ошибка при работе с функцией
     */
    public static double derivative(final MathParsing fun, double point)
            throws MathParsingException {

        double del = 0.25;
        double delta;

        double answer = (fun.calculate(point + del) - fun.calculate(point)) / del;

        do {
            del /= 2;
            delta = answer;
            answer = (fun.calculate(point + del) - fun.calculate(point)) / del;
            delta -= answer;
        } while (delta != 0);

        return answer;
    }

    /**
     * Нахождение интеграла Методом Симпсона
     *
     * @param fun   - функция
     * @param left  - нижняя граница интегрированя
     * @param right - верхняя граница интегрирования
     * @return интеграл
     * @throws MathParsingException - ошибка при работе с функцией
     * @throws NumMethodsException  - ошибка во входных данных
     */
    public static double integralSimpson(final MathParsing fun, double left, double right)
            throws MathParsingException, NumMethodsException {

        final double del = 0.1;

        if (right - left <= del) {
            throw new NumMethodsException("Error in the input data");
        }

        int amount = (int) Math.floor((right - left) / del);

        if (amount % 2 != 0) {
            amount++;
        }

        double step = (right - left) / amount;

        double answer = fun.calculate(left);

        for (int i = 1; i < amount; i++) {
            if (i % 2 == 0) {
                answer += 2 * fun.calculate(left + step * i);
            } else {
                answer += 4 * fun.calculate(left + step * i);
            }
        }

        answer += fun.calculate(right);

        return answer * (step / 3);
    }

    /**
     * Решить СЛАУ с помощью LU разложения
     * Ax=B
     *
     * @param A - матрица системы
     * @param B - вектор столбец свободных коэффициентов
     * @return x - решение системы
     */
    public static double[] slaeMethodLU(final double[][] A, final double[] B)
            throws NumMethodsException {

        final int size = A.length;

        if (size != A[0].length && size != B.length) {
            throw new NumMethodsException("Invalid input data");
        }

        double[][] L = new double[size][size];
        double[][] U = new double[size][size];

        for (int k = 0; k < size; k++) {
            for (int i = k; i < size; i++) {
                double sum = 0;
                for (int m = 0; m < k; m++) {
                    sum += L[i][m] * U[m][k];
                }
                L[i][k] = A[i][k] - sum;
            }

            for (int j = k; j < size; j++) {
                double sum = 0;
                for (int m = 0; m < k; m++) {
                    sum += L[k][m] * U[m][j];
                }
                if (L[k][k] == 0) {
                    throw new NumMethodsException("Division by zero");
                }
                U[k][j] = (A[k][j] - sum) / L[k][k];
            }
        }

        double[] Y = new double[size];

        for (int i = 0; i < size; i++) {
            Y[i] = B[i];
            for (int j = 0; j < i; j++) {
                Y[i] -= Y[j] * L[i][j];
            }
            if (L[i][i] == 0) {
                throw new NumMethodsException("Division by zero");
            }
            Y[i] /= L[i][i];
        }

        double[] X = new double[size];

        for (int i = size - 1; i >= 0; i--) {
            X[i] = Y[i];
            for (int j = size - 1; j > i; j--) {
                X[i] -= X[j] * U[i][j];
            }
        }

        return X;
    }
}
