import com.yize.dynamicsql.QueryGenerator;
import com.yize.dynamicsql.parameter.exceptions.NoMarkValueFoundException;
import com.yize.dynamicsql.parameter.exceptions.NoParameterFoundForMarkException;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class AdRefreshDaoImplTest {

    @Test
    public void testDeleteAdRefreshes() throws DocumentException {
        String queryString = QueryGenerator.createNamedQuery("v1.admanager.AdRefreshDaoImpl", "deleteAdRefreshes")
                .setParameter(1, "2019-03-21")
                .getQueryString();
        assertEquals("DELETE FROM ADREFRESH WHERE date = '2019-03-21'",queryString);

        assertThrows(NoParameterFoundForMarkException.class, ()->{
            QueryGenerator.createNamedQuery("v1.admanager.AdRefreshDaoImpl", "deleteAdRefreshes")
                    .setParameter(2, "2019-03-02")
                    .getQueryString();
        });
    }

    @Test
    public void testGetRefreshImpressionsMap() throws DocumentException {
        String queryString = QueryGenerator.createNamedQuery("v1.admanager.AdRefreshDaoImpl", "getRefreshImpressionsMap")
                .setParameter("date", "2019-08-13")
                .getQueryString();
        assertEquals("SELECT deviceType, SUM(impression) AS impressions FROM ADREFRESH WHERE date = '2019-08-13' GROUP BY deviceType", queryString);

        queryString = QueryGenerator.createNamedQuery("v1.admanager.AdRefreshDaoImpl", "getRefreshImpressionsMap")
                .setParameter("date", "2019-08-13")
                .setParameter("impressionType", "display")
                .getQueryString();
        assertEquals("SELECT deviceType, SUM(impression) AS impressions FROM ADREFRESH WHERE date = '2019-08-13'" +
                " AND impressionType = 'display' GROUP BY deviceType", queryString);

        queryString = QueryGenerator.createNamedQuery("v1.admanager.AdRefreshDaoImpl", "getRefreshImpressionsMap")
                .setParameter("date", "2019-08-13")
                .setParameter("ssps", Arrays.asList("openx", "appnexux"))
                .setParameter("devices", Arrays.asList("mobile", "desktop"))
                .getQueryString();
        assertEquals("SELECT deviceType, SUM(impression) AS impressions FROM ADREFRESH WHERE date = '2019-08-13'" +
                " AND ssp IN ('openx','appnexux') AND devices IN ('mobile','desktop') GROUP BY deviceType", queryString);


        queryString = QueryGenerator.createNamedQuery("v1.admanager.AdRefreshDaoImpl", "getRefreshImpressionsMap")
                .setParameter("date", "2019-08-13")
                .setParameter("ssps", Arrays.asList("openx", "appnexux"))
                .getQueryString();
        assertEquals("SELECT deviceType, SUM(impression) AS impressions FROM ADREFRESH WHERE date = '2019-08-13'" +
                " GROUP BY deviceType", queryString);
    }

    @Test
    public void testGetRefreshImpressionsMapNew() throws DocumentException {
        String queryString = QueryGenerator.createNamedQuery("v1.admanager.AdRefreshDaoImpl", "getRefreshImpressionsMapNew")
                .setParameter("revenueType", "display")
                .setParameter(1, "2019-08-13")
                .setParameter(2, "video")
                .setParameter("ssps", Arrays.asList("openx", "appnexux"))
                .getQueryString();
        assertEquals("SELECT siteUUID, deviceType, SUM(impression) AS impressions FROM ADREVENUE " +
                "WHERE date = '2019-08-13' AND impressionType != 'video' AND ssp IN ('openx','appnexux') GROUP BY deviceType, siteUUID",
                queryString);

         queryString = QueryGenerator.createNamedQuery("v1.admanager.AdRefreshDaoImpl", "getRefreshImpressionsMapNew")
                .setParameter("revenueType", "video")
                .setParameter(1, "2019-08-13")
                .setParameter(2, "video")
                .setParameter("ssps", Arrays.asList("openx", "appnexux"))
                .getQueryString();
        assertEquals("SELECT siteUUID, deviceType, SUM(impression) AS impressions FROM VIDEOREVENUE " +
                        "WHERE date = '2019-08-13' AND impressionType != 'video' AND ssp IN ('openx','appnexux') GROUP BY deviceType, siteUUID",
                queryString);

        queryString = QueryGenerator.createNamedQuery("v1.admanager.AdRefreshDaoImpl", "getRefreshImpressionsMapNew")
                .setParameter("revenueType", "null")
                .setParameter(1, "2019-08-13")
                .setParameter(2, "video")
                .setParameter("ssps", Arrays.asList("openx", "appnexux"))
                .getQueryString();
        assertEquals("SELECT siteUUID, deviceType, SUM(impression) AS impressions FROM SITEREVENUE " +
                        "WHERE date = '2019-08-13' AND impressionType != 'video' AND ssp IN ('openx','appnexux') GROUP BY deviceType, siteUUID",
                queryString);

        String nullStr = null;
        queryString = QueryGenerator.createNamedQuery("v1.admanager.AdRefreshDaoImpl", "getRefreshImpressionsMapNew")
                .setParameter("revenueType", nullStr)
                .setParameter(1, "2019-08-13")
                .setParameter(2, "video")
                .setParameter("ssps", Arrays.asList("openx", "appnexux"))
                .getQueryString();
        assertEquals("SELECT siteUUID, deviceType, SUM(impression) AS impressions FROM SITEREVENUE " +
                        "WHERE date = '2019-08-13' AND impressionType != 'video' AND ssp IN ('openx','appnexux') GROUP BY deviceType, siteUUID",
                queryString);


        assertThrows(NoMarkValueFoundException.class,  () -> {
            QueryGenerator.createNamedQuery("v1.admanager.AdRefreshDaoImpl", "getRefreshImpressionsMapNew")
                .setParameter(1, "2019-08-13")
                .setParameter(2, "video")
                .setParameter("ssps", Arrays.asList("openx", "appnexux"))
                .getQueryString();
        });
    }

    @Test
    public void testGetAdRefreshRateMapForAllSites() throws DocumentException {
        String queryString = QueryGenerator.createNamedQuery("v1.admanager.AdRefreshDaoImpl", "getAdRefreshRateMapForAllSites")
                .setParameter(1, "2019-08-13")
                .setParameter(2, "2019-08-12")
                .getQueryString();
        String res = "SELECT totalTable.siteUUID, totalTable.deviceType, refreshTable.refreshMount / totalTable.totalMount AS " +
                "refreshRate FROM (SELECT siteUUID, deviceType, SUM(impression) AS refreshMount " +
                "FROM ADREFRESH WHERE date = '2019-08-13' AND impressionType != \"INITIAL_LOAD\" " +
                "AND ssp NOT IN ('Taboola', 'Insticator') GROUP BY deviceType, siteUUID) " +
                "AS refreshTable JOIN (SELECT siteUUID, deviceType, SUM(impression) totalMount " +
                "FROM ADREFRESH WHERE date = '2019-08-12' " +
                "AND ssp NOT IN ('Taboola', 'Insticator') " +
                "GROUP BY deviceType, siteUUID " +
                "HAVING totalMount > 0) " +
                "AS totalTable " +
                "ON totalTable.siteUUID = refreshTable.siteUUID " +
                "AND totalTable.deviceType = refreshTable.deviceType";
        assertEquals(res, queryString);
    }



}
