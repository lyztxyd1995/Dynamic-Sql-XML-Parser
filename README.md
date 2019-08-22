# Dynamic Sql XML Parser

A light-weight, simple and fast library to generate dynamic sql query from XML based on Java. The goal of this library is 
to help Java developers easily write sql queries without painfully concatenating SQL Strings. By writing all sql quries as well as adding tags in XML files, all sql queries could be decoupled from the Java code and become dynamic and easily maintainable. This library only performs as a sql query generator, not an executor or ORM. Therefore, it could be easily integrated with any othe ORM frameworks, JPA (like Hibernate) and JDBC.

# Get Started

## 1. Installation

### (1). By using maven

```xml
<dependency>
  <groupId>com.github.lyztxyd1995</groupId>
  <artifactId>dynamicSQL</artifactId>
  <version>1.0.1-RELEASE</version>
</dependency>
```

### (2). Directly download jar file from the link below and import to the project: 
https://oss.sonatype.org/service/local/repositories/comgithublyztxyd1995-1000/content/com/github/lyztxyd1995/dynamicSQL/1.0.1-RELEASE/dynamicSQL-1.0.1-RELEASE.jar

## 2. Custom configurations

### (1): Add the *sqlMapping.xml* configuration file under the conf folder
To use the package, we need first add a **sqlMapping.xml** file under **conf** folder, which is directly under the project's root directory. If there is no conf folder in the project, you need to manually create one and make sure it's directly under the root directory.

The format of the configuration file should be as below example:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mapping-locations>
<!--    specify the file location for one xml file which includes sql queries-->
    <file-location path="/src/main/sqlconf/orm.xml"/>
<!--    specify the directory location, which will include xml files for sql queries-->
    <directory-location path="/src/main/sqlconf/"/>
</mapping-locations>
```
By specifying the <file-location-path/>, the file in the path (*orm.xml* in the example) will be considered as one xml file used for sql queries (will be introduced below).

Also, by specifying the <directory-location-path/>, all the files under the path (*/src/main/sqlconf/* in the example) will be considered as files for queries.

### (2): Customize your own xml for sql quries.

The next step is to write the xml file which includes sql queries. Make sure the xml file follows the schema as the following exaple: 







