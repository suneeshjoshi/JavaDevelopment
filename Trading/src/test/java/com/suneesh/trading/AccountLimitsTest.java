package com.suneesh.trading;

import com.suneesh.trading.models.requests.AccountLimitsRequest;
import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.responses.AccountLimitsResponse;
import com.suneesh.trading.models.responses.ResponseBase;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/4/2017
 */
public class AccountLimitsTest {
    private ApiWrapper api;
    private Properties properties;
    @Before
    public void setup() throws Exception{
        TestUtility testUtility = new TestUtility();
        this.properties = testUtility.getProperties();
        this.api = testUtility.getApi();
    }

    @Test
    public void getAccountLimitsTest() throws Exception {
        AccountLimitsRequest request = new AccountLimitsRequest();
        AuthorizeRequest authorizeRequest = new AuthorizeRequest(properties.getProperty("CR_READ"), false);
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authorizeRequest)
                .subscribe();
        testObserver.await(5, TimeUnit.SECONDS);

        this.api.sendRequest(request)
                .subscribe(testObserver);

        testObserver.await(10, TimeUnit.SECONDS);

        AccountLimitsResponse response = (AccountLimitsResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "get_limits");
        assertEquals(response.getError(), null);
        assertNotEquals(response.getLimits(), null);

    }
}
