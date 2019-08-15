package com.yize.dynamicsql.sqlNodes;


/**
 * Basic interface for each tag in xml
 */
public interface SqlNode {

    String parseText(String text);

    String getFullText();

    boolean display();
}
