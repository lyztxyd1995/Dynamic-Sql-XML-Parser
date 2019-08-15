package com.yize.dynamicsql.sqlNodes.expression;

public class InvalidExpressionException extends RuntimeException {

    public InvalidExpressionException(String errorMsg) {
        super(errorMsg);
    }
}
