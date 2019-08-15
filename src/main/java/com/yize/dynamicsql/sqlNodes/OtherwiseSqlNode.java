package com.yize.dynamicsql.sqlNodes;

import com.yize.dynamicsql.parameter.ParameterMap;
import org.dom4j.Element;

public class OtherwiseSqlNode extends AbstractSqlNode {

    public OtherwiseSqlNode(Element element, ParameterMap parameterMap) {
        super(element, parameterMap);
    }

    @Override
    public boolean display() {
        return true;
    }
}
