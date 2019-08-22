# Dynamic Sql XML Parser

A light-weight, simple and fast library to generate dynamic sql query from XML based on Java. The goal of this library is 
to help Java developers easily write sql queries without painfully concatenating SQL Strings. By writing all sql quries as well as adding tags in XML files, all sql queries could be decoupled from the Java code and become dynamic and easily maintainable. This library only performs as a sql query generator, not an executor or ORM. Therefore, it could be easily integrated with any othe ORM frameworks, JPA (like Hibernate) and JDBC.

# 1. Get Started

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
<!--    specify the directory location, which will include all xml files for sql queries under the directory-->
    <directory-location path="/src/main/sqlconf/"/>
</mapping-locations>
```
By specifying the <file-location-path/>, the file in the path (*orm.xml* in the example) will be considered as one xml file used for sql queries (will be introduced below).

Also, by specifying the <directory-location-path/>, all the files under the path (*/src/main/sqlconf/* in the example) will be considered as files for queries.

### (2): Customize your own xml for sql quries.

The next step is to write the xml file which includes sql queries. Make sure the xml file follows the schema as the following example:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<entity-mappings namespace="com.test.testDao">
    <named-native-query name="deletePost">
        DELETE
        FROM POST
        WHERE date = ?
    </named-native-query>
  
    <named-native-query name="selectPostByDate">
        SELECT * FROM POST
        WHERE date = ?
    </named-native-query>
</entity-mappings>
```
The root element's **namepace** (entity-mappings) will indicate which Class this file will be used to generate the query for. There is no restriction for it, however, the full Class name is recommended to be used here. 

Each sql query should be wrapped inside one <named-native-query> tag with a unique query name. Also, there is no restriction for this name, however, the method name, for which this query is used, is recommended to be used.
  
Last but not the least, after finishing the customized sql xml, make sure the file path (or the file's directory path) is included in the **sqlMapping.xml** as shown above.


# 2. Code Example

Below is the code example to show how to use the library in a 

**sql query xml**:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<entity-mappings namespace="com.dao.testDaoImpl">
 
    <named-native-query name="selectSitesByDate">
        SELECT siteUUID 
        FROM  SITES
        WHERE url = ?
        <if text="date != null">
            AND date = #{date}
        </if>
    </named-native-query>
  
    <named-native-query name="selectSitesByWhere">
        SELECT * FROM SITES
        <where>
            <if text=" date != null ">
                AND date = #{date}
            </if>
            <if text="siteUUIDs != null">
                AND siteUUID IN(:siteUUIDs)
            </if>
        </where>
    </named-native-query>

    <named-native-query name="selectSitesBySwitch">
        SELECT *
        FROM SITES
        <switch>
            <case text=" date != null ">
                WHERE date = #{date}
            </case>
            <case text=" url != null ">
                WHERE url = #{url}
            </case>
            <otherwise>
                WHERE impression > 0
            </otherwise>
        </switch>
    </named-native-query>
</entity-mappings>
```
**Java Class which tests all queries above**
```java
public class testDaoImpl {

    public void selectSitesByDate() throws DocumentException{
        String queryString1 = QueryGenerator.createNamedQuery("com.dao.testDaoImpl", "selectSitesByDate")
                    .setParameter(1, "github.com")
                    .getQueryString();
        /**
         * print result: 'SELECT siteUUID FROM  SITES WHERE url = 'github.com''
         */
        System.out.println(queryString1);

        String queryString2 = QueryGenerator.createNamedQuery("com.dao.testDaoImpl", "selectSitesByDate")
                .setParameter(1, "github.com")
                .setParameter("date", "2019-08-20")
                .getQueryString();
        /**
         * print result: 'SELECT siteUUID FROM  SITES WHERE url = 'github.com' AND date = '2019-08-20''
         */
        System.out.println(queryString2);
    }

    public void selectSitesByWhere() throws DocumentException {
        String queryString1 = QueryGenerator.createNamedQuery("com.dao.testDaoImpl", "selectSitesByWhere")
                .setParameter("date", "2019-08-20")
                .getQueryString();
        /**
         * print result: 'SELECT * FROM SITES WHERE date = '2019-08-20''
         */
        System.out.println(queryString1);

        String queryString2 = QueryGenerator.createNamedQuery("com.dao.testDaoImpl", "selectSitesByWhere")
                .setParameter("siteUUIDs", Arrays.asList("id1", "id2"))
                .getQueryString();
        /**
         * print result: 'SELECT * FROM SITES WHERE siteUUIDs IN ('id1','id2')'
         */
        System.out.println(queryString1);
    }

    public void selectSitesBySwitch() throws DocumentException {
        String queryString1 = QueryGenerator.createNamedQuery("com.dao.testDaoImpl", "selectSitesBySwitch")
                .setParameter("date", "2019-08-20")
                .getQueryString();
        /**
         * print result: 'SELECT * FROM SITES WHERE date = '2019-08-20''
         */
        System.out.println(queryString1);

        String queryString2 = QueryGenerator.createNamedQuery("com.dao.testDaoImpl", "selectSitesBySwitch")
                .setParameter("url", "github.com")
                .getQueryString();
        /**
         * print result: 'SELECT * FROM SITES WHERE url = 'github.com''
         */
        System.out.println(queryString2);

        String queryString3 = QueryGenerator.createNamedQuery("com.dao.testDaoImpl", "selectSitesBySwitch")
                .getQueryString();
        /**
         * print result: 'SELECT * FROM SITES WHERE impression > 0'
         */
        System.out.println(queryString2);
    }
}
```

# 3. XML Tags Introduction
To write sql dynamically, several tags could be used in the xml for different clauses, such as 'IF', 'WHERE', 'SWITCH'. 

## if
```xml
 <if text = 'date != null'>
    WHERE date = #{date}
 </if>
 
  <if text = 'date == null'>
    WHERE date IS NULL
 </if>
 
  <if text = "date != null and url != null">
    WHERE date = #{date} AND url = #{url}
  </if>
```
The most common thing to do in dynamic SQL is conditionally include a part of a where clause. This could be easily achieved by using the 'IF' tag. The query string will only be appended when the expression for text attribute is true.  

The operators used in the expression text could be: 
EQUAL("=="), NOT_EQUAL("!="), LARGE_EQUAL(">="), LESS_EQUAL("<="), LESS("<"), LARGE(">")


## switch, case, otherwise

Sometimes we don’t want all of the conditionals to apply, instead we want to choose only one case among many options. Similar as a switch statement in Java, we also provide a switch element.

```xml
        <switch>
            <case text=" date != null ">
                WHERE date = #{date}
            </case>
            <case text=" url != null ">
                WHERE url = #{url}
            </case>
            <otherwise>
                WHERE impression > 0
            </otherwise>
        </switch>
```
The switch element will fetch only one child element if the expresion is true. Like the example above, 
if date is provided, the query will search by date. Otherwise, if url is provided, the query will search by url. If neither is provided, by default, the query will search by 'impression > 0' 

## where, if

The where tag will make the query more consistent and reduce the potential mistake we might make if we directly use 'if' clause.

The example is as below:

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG
  <where>
    <if text="state != null">
        AND state = #{state}
    </if>
    <if text="title != null">
        AND title like #{title}
    </if>
    <if text="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
  </where>
</select>
```
By using where, as long as the clause (eg: text="state != null") in each 'if' element is true, the text inside the element  (eg: AND state = #{state}) will be appended to the whole query string. If none of the clauses is true, there will be no WHERE statement in the sql query and the query will be 'SELECT * FROM BLOG'.
