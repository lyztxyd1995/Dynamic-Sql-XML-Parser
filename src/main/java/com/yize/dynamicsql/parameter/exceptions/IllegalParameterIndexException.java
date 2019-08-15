package com.yize.dynamicsql.parameter.exceptions;

public class IllegalParameterIndexException extends RuntimeException {

    @Override
    public String getMessage() {
        return "The Index for the parameter should range from 1 to " + Integer.MAX_VALUE;
    }
}
