package com.yize.dynamicsql.sqlNodes;

import com.yize.dynamicsql.parameter.ParameterMap;
import com.yize.dynamicsql.sqlNodes.expression.ExpressionUtils;
import org.dom4j.Element;

public class CaseSqlNode extends AbstractSqlNode {


    public CaseSqlNode(Element element, ParameterMap parameterMap) {
        super(element, parameterMap);
    }

    @Override
    public boolean display() {
        return ExpressionUtils.isTrueExpression(this.element.attributeValue("text"), parameterMap);
    }
}
