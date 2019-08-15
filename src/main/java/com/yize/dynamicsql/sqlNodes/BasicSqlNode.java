package com.yize.dynamicsql.sqlNodes;

import com.yize.dynamicsql.parameter.ParameterMap;
import org.dom4j.Element;

public class BasicSqlNode extends AbstractSqlNode {

    public BasicSqlNode(Element element, ParameterMap parameterMap) {
        super(element, parameterMap);
    }

    @Override
    public boolean display() {
        return true;
    }
}
