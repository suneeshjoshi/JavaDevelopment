package com.suneesh.trading;

import com.suneesh.trading.core.ApiWrapper;
import org.junit.After;
import org.junit.Before;

import java.util.Properties;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/7/2017
 */
public class TestBase {

    protected ApiWrapper api;
    protected Properties properties;

    @Before
    public void setUp() throws Exception {
        TestUtility testUtility = new TestUtility();
        this.api = testUtility.getApi();
        this.properties = testUtility.getProperties();
    }

    @After
    public void cleanUp() throws Exception {
        this.api.closeConnection();
    }
}
