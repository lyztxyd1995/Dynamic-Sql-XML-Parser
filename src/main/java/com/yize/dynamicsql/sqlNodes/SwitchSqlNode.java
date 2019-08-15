package com.yize.dynamicsql.sqlNodes;

import com.yize.dynamicsql.parameter.ParameterMap;
import org.dom4j.Element;

import java.util.Iterator;

public class SwitchSqlNode extends AbstractSqlNode {

    public SwitchSqlNode(Element element, ParameterMap parameterMap) {
        super(element, parameterMap);
    }

    @Override
    public String getFullText() {
        StringBuilder stringBuilder = new StringBuilder();
        if (element.isTextOnly()) {
            return stringBuilder.toString();
        }
        Iterator iterator = element.elementIterator();
        while (iterator.hasNext()) {
            SqlNode sqlNode = SimpleSqlNodeFactory.getSqlNode((Element) iterator.next(), parameterMap);
            if (!(sqlNode instanceof CaseSqlNode) && !(sqlNode instanceof OtherwiseSqlNode)) {
                throw new RuntimeException("Invalid xml tag inside switch tag, must be 'case' or 'otherwise'");
            }
            if (sqlNode.display()) {
                stringBuilder.append(sqlNode.getFullText()).append(System.lineSeparator());
                break;
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean display() {
        return true;
    }
}
