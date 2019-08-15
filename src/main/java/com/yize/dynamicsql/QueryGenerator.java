package com.yize.dynamicsql;

import com.yize.dynamicsql.sqlQuery.BasicSqlQuery;
import com.yize.dynamicsql.sqlQuery.EmptySqlQuery;
import com.yize.dynamicsql.sqlQuery.SqlQuery;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QueryGenerator {

    private static volatile Map<String, Element> classNameRootElementMap;

    private static final String ROOT_PATH = System.getProperty("user.dir");

    private QueryGenerator() {
    }

    public static SqlQuery createNamedQuery(String className, String queryName) throws DocumentException {
        Element rootElement = getElementFromMapping(className);
        if (rootElement == null || rootElement.isTextOnly()) {
            return new EmptySqlQuery();
        }
        Element element = findElementWithQueryName(queryName, rootElement);
        return element == null ? new EmptySqlQuery() : new BasicSqlQuery(element);
    }

    private static void initializeConfig() throws DocumentException {
        synchronized (QueryGenerator.class) {
            if (classNameRootElementMap == null) {
                classNameRootElementMap = new HashMap<>();
                File confFile = new File(ROOT_PATH + "/conf/sqlMapping.xml");
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(confFile);
                Element rootElement = document.getRootElement();
                Iterator iterator = rootElement.elementIterator();
                while (iterator.hasNext()) {
                    Element element = (Element) iterator.next();
                    String tagName = element.getName();
                    String path = ROOT_PATH + element.attributeValue("path");
                    if (tagName.equals("file-location") || tagName.equals("directory-location")) {
                        loadMappingFile(new File(path));
                    } else {
                        throw new RuntimeException("Invalid tag name, could only be 'file-location' or 'directory-location'");
                    }
                }
            }
        }
    }

    private static void loadMappingFile(File file) throws DocumentException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File mappingFile : files) {
                    loadMappingFile(mappingFile);
                }
            }
            return;
        }
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element rootElement = document.getRootElement();
        String nameSpace = rootElement.attributeValue("namespace");
        if (nameSpace == null || nameSpace.isEmpty()) {
            throw new RuntimeException("namespace for config file " + file.getName() + " cannot be empty");
        }
        classNameRootElementMap.put(nameSpace, rootElement);
    }

    private static Element getElementFromMapping(String className) throws DocumentException {
        if (classNameRootElementMap == null) {
            initializeConfig();
        }
        return classNameRootElementMap.get(className);
    }

    private static Element findElementWithQueryName(String queryName, Element rootElement) {
        Iterator iterator = rootElement.elementIterator();
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            if (element.getName().equals("named-native-query")) {
                String name = element.attributeValue("name");
                if (name != null && name.trim().equals(queryName.trim())) {
                    return element;
                }
            }
        }
        return null;
    }
}
