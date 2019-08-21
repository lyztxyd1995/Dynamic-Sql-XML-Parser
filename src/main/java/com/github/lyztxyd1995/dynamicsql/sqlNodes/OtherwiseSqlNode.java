package com.github.lyztxyd1995.dynamicsql.sqlNodes;

import com.github.lyztxyd1995.dynamicsql.parameter.ParameterMap;
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
