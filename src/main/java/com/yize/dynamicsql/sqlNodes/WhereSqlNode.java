package com.yize.dynamicsql.sqlNodes;

import com.yize.dynamicsql.parameter.ParameterMap;
import org.dom4j.Element;

import java.util.Iterator;

public class WhereSqlNode extends AbstractSqlNode {


    public WhereSqlNode(Element element, ParameterMap parameterMap) {
        super(element, parameterMap);
    }

    @Override
    public String getFullText() {
        StringBuilder stringBuilder = new StringBuilder();
        if (element.isTextOnly()) {
            return stringBuilder.toString();
        }
        Iterator iterator = element.elementIterator();
        boolean firstMatch = true;
        while(iterator.hasNext()) {
            SqlNode sqlNode = SimpleSqlNodeFactory.getSqlNode((Element)iterator.next(), parameterMap);
            if (!(sqlNode instanceof IfSqlNode)) {
                throw new RuntimeException("Invalid xml tag inside where tag, must be 'if'");
            }
            if (sqlNode.display()) {
                String childText = sqlNode.getFullText();
                if (childText == null || childText.isEmpty()) {
                    continue;
                }
                childText = childText.trim();
                if (firstMatch) {
                    stringBuilder.append("WHERE ");
                    if (childText.toLowerCase().startsWith("and")) {
                        childText = childText.substring(3);
                    }
                    firstMatch = false;
                } else {
                    if (!childText.toLowerCase().startsWith("and")) {
                        childText = "AND" + childText;
                    }
                }
                stringBuilder.append(childText).append(System.lineSeparator());
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean display() {
        return true;
    }
}
