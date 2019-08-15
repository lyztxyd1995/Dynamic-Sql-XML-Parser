package com.yize.dynamicsql.sqlQuery;

import java.util.List;

public class EmptySqlQuery implements SqlQuery {

    @Override
    public SqlQuery setParameter(int pos, String value) {
        return this;
    }

    @Override
    public SqlQuery setParameter(int pos, int value) {
        return null;
    }

    @Override
    public SqlQuery setParameter(int pos, boolean value) {
        return null;
    }

    @Override
    public SqlQuery setParameter(int pos, List<String> list) {
        return null;
    }

    @Override
    public SqlQuery setParameter(String key, String value) {
        return this;
    }

    @Override
    public SqlQuery setParameter(String key, boolean value) {
        return null;
    }

    @Override
    public SqlQuery setParameter(String key, int value) {
        return null;
    }

    @Override
    public SqlQuery setParameter(String key, List<String> list) {
        return null;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public String toString(){
        return "Empty Sql Query";
    }
}
