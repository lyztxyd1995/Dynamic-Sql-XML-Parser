package com.yize.dynamicsql.sqlQuery;

import java.util.List;

public interface SqlQuery {

    SqlQuery setParameter(int pos, String value);

    SqlQuery setParameter(int pos, int value);

    SqlQuery setParameter(int pos, boolean value);

    SqlQuery setParameter(int pos, List<String> list);

    SqlQuery setParameter(String key, String value);

    SqlQuery setParameter(String key, boolean value);

    SqlQuery setParameter(String key, int value);

    SqlQuery setParameter(String key, List<String> list);

    String getQueryString();
}
