package com.github.lyztxyd1995.dynamicsql.parameter.exceptions;

public class NoParameterFoundForMarkException extends RuntimeException {

    private int index;

    public NoParameterFoundForMarkException(int index) {
        this.index = index;
    }

    @Override
    public String getMessage() {
        return "No parameter set for the '?' mark on index " + index;
    }
}
