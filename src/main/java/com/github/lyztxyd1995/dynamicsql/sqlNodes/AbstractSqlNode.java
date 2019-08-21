package com.github.lyztxyd1995.dynamicsql.sqlNodes;

import com.github.lyztxyd1995.dynamicsql.parameter.ParameterMap;
import com.github.lyztxyd1995.dynamicsql.parameter.exceptions.NoMarkValueFoundException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractSqlNode implements SqlNode {

    /**
     * Regular expression to find the marks which are used to set the parameters
     * There are three types of marks: #{value}, (:List), ?
     */
    public static final String REGEXPS = "#\\{[^}]*}|\\(:[^)]*\\)|\\?";

    protected Element element;

    protected ParameterMap parameterMap;

    public AbstractSqlNode(Element element, ParameterMap parameterMap) {
        this.element = element;
        this.parameterMap = parameterMap;
    }

    @Override
    public String parseText(String text) {
        text = preProcessText(text);
        Pattern pattern = Pattern.compile(REGEXPS);
        Matcher matcher = pattern.matcher(text);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String key = getKeyFromMatchedPattern(matcher.group());
            if (key == null) {
                matcher.appendReplacement(stringBuffer, "");
            } else {
                if (key.equals("?")) {
                    matcher.appendReplacement(stringBuffer, parameterMap.poll());
                } else {
                    if (!parameterMap.containsKey(key)) {
                        throw new NoMarkValueFoundException(key);
                    }
                    matcher.appendReplacement(stringBuffer, parameterMap.get(key));
                }
            }
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    private List<Node> filterEmptyNodeList(List<Node>nodes) {
        List<Node>nodeList = new LinkedList<>();
        if (nodes == null) {
            return nodeList;
        }
        for (Node node : nodes) {
            if (node.getName() != null) {
                nodeList.add(node);
            } else {
                String text = node.getText();
                if (text == null) {
                    continue;
                }
                text = text.replaceAll(System.lineSeparator(), "");
                if (!text.trim().isEmpty()) {
                    nodeList.add(node);
                }
            }
        }
        return nodeList;
    }

    @Override
    public String getFullText() {
        if (element.isTextOnly()) {
            return parseText(element.getText());
        }
        StringBuilder stringBuilder = new StringBuilder();
        XPath xPath = DocumentHelper.createXPath("./node()");
        List<Node> nodeList = filterEmptyNodeList(xPath.selectNodes(element));
        for (Node node : nodeList) {
            if (node.getName() != null) {
                SqlNode sqlNode = SimpleSqlNodeFactory.getSqlNode((Element)node, parameterMap);
                if (sqlNode.display()) {
                    stringBuilder.append(System.lineSeparator()).append(sqlNode.getFullText());
                }
            } else {
                stringBuilder.append(parseText(node.getText()));
            }
        }
        return stringBuilder.toString();
    }

    private String preProcessText(String text) {
        text.replaceAll(System.lineSeparator(), " ");
        text = text.trim();
        return text + " ";
    }


    private String getKeyFromMatchedPattern(String matchedPattern) {
        if (matchedPattern == null || matchedPattern.isEmpty()) {
            return null;
        }
        switch (matchedPattern.charAt(0)) {
            case '#':
            case '(': {
                return matchedPattern.substring(2, matchedPattern.length() - 1);
            }
            case '?': {
                return "?";
            }
        }
        return null;
    }
}
