package com.github.lyztxyd1995.dynamicsql.parameter.exceptions;

public class NoMarkValueFoundException extends RuntimeException {

    String key;

    public NoMarkValueFoundException(String key) {
        this.key = key;
    }

    @Override
    public String getMessage(){
        return "No value found for key " + key;
    }
}
