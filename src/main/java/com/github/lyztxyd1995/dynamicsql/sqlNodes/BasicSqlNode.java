package com.github.lyztxyd1995.dynamicsql.sqlNodes;

import com.github.lyztxyd1995.dynamicsql.parameter.ParameterMap;
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
