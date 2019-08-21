package com.github.lyztxyd1995.dynamicsql.sqlNodes;

import com.github.lyztxyd1995.dynamicsql.parameter.ParameterMap;
import org.dom4j.Element;

public class SimpleSqlNodeFactory {

    public static SqlNode getSqlNode(Element element, ParameterMap parameterMap) {
        if (element == null) {
            return null;
        }
        SqlNode result;
        switch (element.getName().toLowerCase()) {
            case "if":
                result = new IfSqlNode(element, parameterMap);
                break;
            case "switch":
                result = new SwitchSqlNode(element, parameterMap);
                break;
            case "case":
                result = new CaseSqlNode(element, parameterMap);
                break;
            case "where":
                result = new WhereSqlNode(element, parameterMap);
                break;
            case "otherwise":
                result = new OtherwiseSqlNode(element, parameterMap);
                break;
            case "named-native-query":
                result = new BasicSqlNode(element, parameterMap);
                break;
            default:
                throw new InvalidTagNameException("Invalid tag name: " + element.getName());
        }
        return result;
    }
}
