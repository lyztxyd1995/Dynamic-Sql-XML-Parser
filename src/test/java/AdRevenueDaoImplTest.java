import com.yize.dynamicsql.QueryGenerator;
import com.yize.dynamicsql.parameter.exceptions.IllegalParameterIndexException;
import com.yize.dynamicsql.parameter.exceptions.NoMarkValueFoundException;
import com.yize.dynamicsql.parameter.exceptions.NoParameterFoundForMarkException;
import com.yize.dynamicsql.sqlQuery.BasicSqlQuery;
import com.yize.dynamicsql.sqlQuery.EmptySqlQuery;
import com.yize.dynamicsql.sqlQuery.SqlQuery;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class AdRevenueDaoImplTest {

    @Test
    public void testReadFile() throws DocumentException {
        SqlQuery sqlQuery = QueryGenerator.createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "deleteAdRevenues");
        assertEquals(sqlQuery instanceof BasicSqlQuery, true);
        sqlQuery = QueryGenerator.createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "empty");
        assertEquals(sqlQuery instanceof EmptySqlQuery, true);
        assertEquals(sqlQuery.getQueryString(), null);
        sqlQuery = QueryGenerator.createNamedQuery("empty", "empty");
        assertEquals(sqlQuery instanceof EmptySqlQuery, true);
        assertEquals(sqlQuery.getQueryString(), null);
    }

    @Test
    public void testQueryDeleteAdRevenues() throws DocumentException {

        /**
         * Test valid queries
         */
        String queryString = QueryGenerator
                .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl",  "deleteAdRevenues")
                .setParameter(1, "2019-01-01")
                .setParameter(2, "Openx")
                .setParameter(3,true)
                .getQueryString();
        assertEquals(queryString, "DELETE FROM ADREVENUE WHERE date = '2019-01-01' AND ssp = 'Openx' AND isEBDA = 1");


        queryString = QueryGenerator
                .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl",  "deleteAdRevenues")
                .setParameter(1, "2019-09-10")
                .setParameter(2, "AppNexus")
                .setParameter(3,false)
                .getQueryString();
        assertEquals(queryString, "DELETE FROM ADREVENUE WHERE date = '2019-09-10' AND ssp = 'AppNexus' AND isEBDA = 0");

        /**
         * Test invalid queries
         */
        assertThrows(NoParameterFoundForMarkException.class, () -> {
            QueryGenerator.createNamedQuery("v1.adrevenue.AdRevenueDaoImpl",  "deleteAdRevenues")
                    .setParameter(1, "2019-01-01")
                    .setParameter(2, "openx").getQueryString();
                });

        assertThrows(NoParameterFoundForMarkException.class, () -> {
            QueryGenerator.createNamedQuery("v1.adrevenue.AdRevenueDaoImpl",  "deleteAdRevenues")
                    .setParameter(2, "2019-01-01")
                    .setParameter(3, "openx").getQueryString();
        });

        assertThrows(IllegalParameterIndexException.class, () -> {
            QueryGenerator.createNamedQuery("v1.adrevenue.AdRevenueDaoImpl",  "deleteAdRevenues")
                    .setParameter(0, "2019-01-01")
                    .setParameter(3, "openx").getQueryString();
        });
    }

    @Test
    public void testGetAllSSPsWithRevenueByDate() throws DocumentException {
        String queryString = QueryGenerator
                .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "getAllSSPsWithRevenueByDate")
                .getQueryString();
        assertEquals( "SELECT DISTINCT(ssp) FROM ADREVENUE", queryString);

        queryString = QueryGenerator
                .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "getAllSSPsWithRevenueByDate")
                .setParameter("date", "2019-07-29")
                .getQueryString();
        assertEquals("SELECT DISTINCT(ssp) FROM ADREVENUE WHERE date = '2019-07-29'", queryString);

        queryString = QueryGenerator
                .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "getAllSSPsWithRevenueByDate")
                .setParameter("date", "2019-07-29")
                .setParameter("siteUUIDs", Arrays.asList("12", "34"))
                .getQueryString();
        assertEquals("SELECT DISTINCT(ssp) FROM ADREVENUE WHERE date = '2019-07-29' AND siteUUID IN('12','34')", queryString);

        queryString = QueryGenerator
                .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "getAllSSPsWithRevenueByDate")
                .setParameter("siteUUIDs", Arrays.asList("12", "34"))
                .getQueryString();
        assertEquals("SELECT DISTINCT(ssp) FROM ADREVENUE WHERE siteUUID IN('12','34')", queryString);
    }

    @Test
    public void testGetSiteSSPsMapByDate() throws DocumentException {
        String queryString = QueryGenerator
                .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "getSiteSSPsMapByDate")
                .getQueryString();
        assertEquals("SELECT siteUUID, group_concat(DISTINCT ssp separator ',') AS SSPs" +
                " FROM SITEDOMAIN JOIN ADREVENUE USING(domain) WHERE impression > 0", queryString);

        queryString = QueryGenerator
                .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "getSiteSSPsMapByDate")
                .setParameter("date", "2019-05-01")
                .getQueryString();
        assertEquals("SELECT siteUUID, group_concat(DISTINCT ssp separator ',') AS SSPs" +
                " FROM SITEDOMAIN JOIN ADREVENUE USING(domain) WHERE date = '2019-05-01'", queryString);

        queryString = QueryGenerator
                .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "getSiteSSPsMapByDate")
                .setParameter("isEBDA", 0)
                .getQueryString();
        assertEquals("SELECT siteUUID, group_concat(DISTINCT ssp separator ',') AS SSPs" +
                " FROM SITEDOMAIN JOIN ADREVENUE USING(domain) WHERE isEBDA = 0", queryString);

        queryString = QueryGenerator
                .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "getSiteSSPsMapByDate")
                .setParameter("date", "2019-05-01")
                .setParameter("isEBDA", 0)
                .getQueryString();
        assertEquals("SELECT siteUUID, group_concat(DISTINCT ssp separator ',') AS SSPs" +
                " FROM SITEDOMAIN JOIN ADREVENUE USING(domain) WHERE date = '2019-05-01'", queryString);
    }


    @Test
    public void testGetAllRevenueWithUnusualFormat() throws DocumentException{
        /**
         * Test valid result
         */
        String queryString = QueryGenerator
                .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "getAllRevenueWithUnusualFormat")
                .setParameter("name", "yize")
                .getQueryString();
        assertEquals( "SELECT name, revenue FROM ADREVENUE WHERE name = 'yize' GROUP BY name", queryString);

        queryString = QueryGenerator
                .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "getAllRevenueWithUnusualFormat")
                .setParameter("name", "yize")
                .setParameter("date", "2019-08-01")
                .getQueryString();
        assertEquals( "SELECT name, revenue FROM ADREVENUE WHERE name = 'yize' AND date = '2019-08-01' GROUP BY name", queryString);

        queryString = QueryGenerator
                .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "getAllRevenueWithRecursiveStructure")
                .setParameter("name", "yize")
                .setParameter("date", "2019-08-01")
                .setParameter("ssp", "openx")
                .setParameter("siteUUID", "12")
                .getQueryString();
        assertEquals("SELECT name, revenue FROM ADREVENUE WHERE name = 'yize' AND date = '2019-08-01' AND ssp = 'openx' AND siteUUID = '12' GROUP BY name", queryString);

        /**
         * Error test
         */
        assertThrows(NoMarkValueFoundException.class, ()->{
             QueryGenerator
                    .createNamedQuery("v1.adrevenue.AdRevenueDaoImpl", "getAllRevenueWithUnusualFormat")
                     .getQueryString();
        });
    }

}
