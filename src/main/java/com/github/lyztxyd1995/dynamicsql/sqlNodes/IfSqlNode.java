package com.github.lyztxyd1995.dynamicsql.sqlNodes;

import com.github.lyztxyd1995.dynamicsql.parameter.ParameterMap;
import com.github.lyztxyd1995.dynamicsql.sqlNodes.expression.ExpressionUtils;
import org.dom4j.Element;

public class IfSqlNode extends AbstractSqlNode {

    public IfSqlNode(Element element, ParameterMap parameterMap) {
        super(element, parameterMap);
    }

    @Override
    public boolean display() {
        String expression = element.attributeValue("text");
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }
        return ExpressionUtils.isTrueExpression(this.element.attributeValue("text"), parameterMap);
    }
}
