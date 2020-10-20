package ru.mai;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathParsing {

    private final String[] FUNCTIONS = {"abs", "sin", "cos", "tan", "exp", "log", "sqrt"};
    private final String[] OPERATORS = {"+", "-", "*", "/", "^"};

    private final ArrayList<String> fun;

    public MathParsing(String function)
            throws MathParsingException {

        fun = new ArrayList<>();

        Stack<String> stack = new Stack<>();

        for (String token : parsing(function)) {
            if (isNumber(token) || isVariable(token)) {
                fun.add(token);
            } else if (isFunction(token) || isOpenBracket(token)) {
                stack.push(token);
            } else if (isOperator(token)) {
                while (!stack.empty() && getPrecedence(stack.peek()) >= getPrecedence(token)) {
                    fun.add(stack.pop());
                }

                stack.push(token);
            } else if (isCloseBracket(token)) {
                while (!isOpenBracket(stack.peek())) {
                    try {
                        fun.add(stack.pop());
                    } catch (EmptyStackException e) {
                        throw new MathParsingException("Invalid string for math analysis");
                    }
                }

                stack.pop();

                if (!stack.empty() && isFunction(stack.peek())) {
                    fun.add(stack.pop());
                }
            }
        }

        while (!stack.empty()) {
            fun.add(stack.pop());
        }
    }

    public double calculate(double variable)
            throws MathParsingException {

        Stack<Double> stack = new Stack<>();

        for (String token : fun) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isVariable(token)) {
                stack.push(variable);
            } else if (isFunction(token)) {
                stack.push(getValueFun(token, stack.pop()));
            } else {
                try {
                    double value2 = stack.pop();
                    double value1 = stack.pop();
                    stack.push(getValueOperator(token, value1, value2));
                } catch (EmptyStackException | ArithmeticException e) {
                    throw new MathParsingException("Cannot calculate expression value");
                }
            }
        }

        return stack.pop();
    }

    public double calculate()
            throws MathParsingException {

        Stack<Double> stack = new Stack<>();

        for (String token : fun) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isFunction(token)) {
                stack.push(getValueFun(token, stack.pop()));
            } else {
                try {
                    double value2 = stack.pop();
                    double value1 = stack.pop();
                    stack.push(getValueOperator(token, value1, value2));
                } catch (EmptyStackException | ArithmeticException e) {
                    throw new MathParsingException("Cannot calculate expression value");
                }
            }
        }

        return stack.pop();
    }

    private ArrayList<String> parsing(String str) {

        ArrayList<String> answer = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\(|\\)|\\+|/|\\*|\\^|-|sin|cos|tan|exp|log|sqrt|x|(\\d+((\\.)\\d+)?)");
        Matcher matcher = pattern.matcher(str);

        String token;
        for (String tokenUntil = null; matcher.find(); tokenUntil = token) {
            token = matcher.group();
            if (token.equals("-") && (tokenUntil == null || tokenUntil.matches("\\D+") || tokenUntil.matches("x"))) {
                answer.add("0");
            }
            answer.add(token);
        }

        return answer;
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isVariable(String token) {
        return token.equals("x");
    }

    private boolean isFunction(String token) {
        for (String fun : FUNCTIONS) {
            if (fun.equals(token)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOperator(String token) {
        for (String item : OPERATORS) {
            if (item.equals(token)) {
                return true;
            }
        }

        return false;
    }

    private boolean isOpenBracket(String token) {
        return token.equals("(");
    }

    private boolean isCloseBracket(String token) {
        return token.equals(")");
    }

    private byte getPrecedence(String token)
            throws MathParsingException {
        switch (token) {
            case "^":
                return 3;
            case "*":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                throw new MathParsingException("The received token is not an operator");
        }
    }

    private double getValueFun(String fun, double value)
            throws MathParsingException {

        switch (fun) {
            case "abs":
                return Math.abs(value);
            case "sin":
                return Math.sin(value);
            case "cos":
                return Math.cos(value);
            case "tan":
                return Math.tan(value);
            case "exp":
                return Math.exp(value);
            case "log":
                return Math.log(value);
            case "sqrt":
                return Math.sqrt(value);
            default:
                throw new MathParsingException("The received token is not a function");
        }
    }

    private double getValueOperator(String operator, double value1, double value2)
            throws MathParsingException {

        switch (operator) {
            case "+":
                return value1 + value2;
            case "-":
                return value1 - value2;
            case "*":
                return value1 * value2;
            case "/":
                return value1 / value2;
            case "^":
                return Math.pow(value1, value2);
            default:
                throw new MathParsingException("The received token is not an operator");
        }
    }
}