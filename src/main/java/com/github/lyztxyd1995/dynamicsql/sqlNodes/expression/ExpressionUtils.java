package com.github.lyztxyd1995.dynamicsql.sqlNodes.expression;

import com.github.lyztxyd1995.dynamicsql.parameter.ParameterMap;
import com.github.lyztxyd1995.dynamicsql.parameter.exceptions.NoMarkValueFoundException;

import java.util.regex.Pattern;

public class ExpressionUtils {

    private static final String AND_KEYWORDS = " +AND +| +and +";

    private static final String OR_KEYWORDS = " +OR +| +or +";

    private ExpressionUtils() {}

    public static boolean isTrueExpression(String expression, ParameterMap parameterMap) {
        checkIfExpressionIsValid(expression);
        if (isAndExpression(expression)) {
            return isTrueAndExpression(expression, parameterMap);
        }
        if (isOrExpression(expression)) {
            return isTrueOrExpression(expression, parameterMap);
        }

        return isTrueSingleExpression(expression, parameterMap);
    }

    private static boolean isTrueAndExpression(String expression, ParameterMap parameterMap) {
        String[]statements = expression.split(AND_KEYWORDS);
        for (String singleStatement : statements) {
            if (!isTrueSingleExpression(singleStatement, parameterMap)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isTrueOrExpression(String expression, ParameterMap parameterMap) {
        String[]statements = expression.split(AND_KEYWORDS);
        for (String singleStatement : statements) {
            if (isTrueSingleExpression(singleStatement, parameterMap)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Support statement author != null, author == null, author == 'yize', author != 'yize'
     *
     * @param expression
     * @param parameterMap
     * @return
     */
    private static boolean isTrueSingleExpression(String expression, ParameterMap parameterMap) {
        expression = expression.trim();
        for (Operator operator : Operator.values()) {
            if (expression.contains(operator.toString())) {
                String[]words = expression.split(operator.toString());
                if (words.length != 2) {
                    throw new InvalidExpressionException("Invalid expression " + expression);
                }
                String leftVal = words[0].trim();
                String rightVal = words[1].trim();
                String value = parameterMap.get(leftVal);
                switch (operator) {
                    case EQUAL: {
                        if (rightVal.toLowerCase().equals("null")) {
                            return value == null;
                        } else {
                            if (!parameterMap.containsKey(leftVal)) {
                                throw new NoMarkValueFoundException(leftVal);
                            }
                            return rightVal.equals(value);
                        }
                    }
                    case NOT_EQUAL: {
                        if (rightVal.toLowerCase().equals("null")) {
                            return value != null;
                        } else {
                            if (!parameterMap.containsKey(leftVal)) {
                                throw new NoMarkValueFoundException(leftVal);
                            }
                            return !rightVal.equals(value);
                        }
                    }
                    case LARGE: {
                        if (!parameterMap.containsKey(leftVal)) {
                            throw new NoMarkValueFoundException(leftVal);
                        }
                        return Integer.parseInt(value) > Integer.parseInt(rightVal);
                    }
                    case LARGE_EQUAL: {
                        if (!parameterMap.containsKey(leftVal)) {
                            throw new NoMarkValueFoundException(leftVal);
                        }
                        return Integer.parseInt(value) >= Integer.parseInt(rightVal);
                    }
                    case LESS: {
                        if (!parameterMap.containsKey(leftVal)) {
                            throw new NoMarkValueFoundException(leftVal);
                        }
                        return Integer.parseInt(value) < Integer.parseInt(rightVal);
                    }
                    case LESS_EQUAL: {
                        if (!parameterMap.containsKey(leftVal)) {
                            throw new NoMarkValueFoundException(leftVal);
                        }
                        return Integer.parseInt(value) <= Integer.parseInt(rightVal);
                    }
                    default:
                        return false;
                }
            }
        }
        return false;
    }

    private static void checkIfExpressionIsValid(String expression) {
        if (expression == null || expression.isEmpty()) {
            throw new InvalidExpressionException("Expression is empty");
        }
        if (isAndExpression(expression) && isOrExpression(expression)) {
            throw new InvalidExpressionException("Expression doesn't support the combination of both 'AND' and 'OR' condition");
        }
    }

    private static boolean isAndExpression(String expression) {
        return Pattern.compile(AND_KEYWORDS).matcher(expression).find();
    }

    private static boolean isOrExpression(String expression) {
        return Pattern.compile(OR_KEYWORDS).matcher(expression).find();
    }
}
