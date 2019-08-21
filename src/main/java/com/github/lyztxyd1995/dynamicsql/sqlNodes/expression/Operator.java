package com.github.lyztxyd1995.dynamicsql.sqlNodes.expression;

public enum Operator {

    EQUAL("=="), NOT_EQUAL("!="), LARGE_EQUAL(">="), LESS_EQUAL("<="), LESS("<"), LARGE(">");

    private String operator;

    Operator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return this.operator;
    }
}
