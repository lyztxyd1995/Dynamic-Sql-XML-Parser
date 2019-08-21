package com.github.lyztxyd1995.dynamicsql.sqlQuery;
import com.github.lyztxyd1995.dynamicsql.parameter.ParameterMap;
import com.github.lyztxyd1995.dynamicsql.sqlNodes.SimpleSqlNodeFactory;
import com.github.lyztxyd1995.dynamicsql.sqlNodes.SqlNode;
import org.dom4j.Element;

import java.util.List;
import java.util.StringJoiner;

public class BasicSqlQuery implements SqlQuery {

    private Element element;

    private ParameterMap parameterMap;

    public BasicSqlQuery(Element element) {
        this.element = element;
        this.parameterMap = new ParameterMap();
    }

    @Override
    public SqlQuery setParameter(int pos, String value){
        this.parameterMap.put(pos, prevProcessValue(value));
        return this;
    }

    @Override
    public SqlQuery setParameter(int pos, int value) {
        this.parameterMap.put(pos, String.valueOf(value));
        return this;
    }

    @Override
    public SqlQuery setParameter(int pos, boolean value) {
        if (value) {
            return setParameter(pos, 1);
        }
        return setParameter(pos, 0);
    }

    @Override
    public SqlQuery setParameter(int pos, List<String> list) {
        if (list == null) {
            String nullStr = null;
            return this.setParameter(pos, nullStr);
        }
        StringJoiner stringJoiner = new StringJoiner(",", "(", ")");
        for (String value : list) {
            stringJoiner.add(prevProcessValue(value));
        }
        parameterMap.put(pos, stringJoiner.toString());
        return this;
    }

    @Override
    public SqlQuery setParameter(String key, String value) {
        this.parameterMap.put(key, prevProcessValue(value));
        return this;
    }

    @Override
    public SqlQuery setParameter(String key, boolean value) {
        if (value) {
            return setParameter(key, 1);
        }
        return setParameter(key, 0);
    }

    @Override
    public SqlQuery setParameter(String key, int value) {
        this.parameterMap.put(key, String.valueOf(value));
        return this;
    }

    @Override
    public SqlQuery setParameter(String key, List<String> list) {
        if (list == null) {
            String nullstr = null;
            return this.setParameter(key, nullstr);
        }
        StringJoiner stringJoiner = new StringJoiner(",", "(", ")");
        for (String value : list) {
            stringJoiner.add(prevProcessValue(value));
        }
        parameterMap.put(key, stringJoiner.toString());
        return this;
    }

    @Override
    public String getQueryString() {
        SqlNode rootSqlNode = SimpleSqlNodeFactory.getSqlNode(element, parameterMap);
        return rootSqlNode.getFullText().trim()
                .replaceAll(System.lineSeparator(), " ")
                .replaceAll(" +", " ");
    }

    private String prevProcessValue(String value) {
        if (value == null) {
            return null;
        }
        if (value.isEmpty()) {
            return "";
        }
        if (value.startsWith("'") && value.endsWith("'")) {
            return value;
        }
        return "'" + value + "'";
    }

    @Override
    public String toString() {
        return getQueryString();
    }
}
