package com.suneesh.trading;

import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.requests.LogoutRequest;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.LogoutResponse;
import com.suneesh.trading.models.responses.ResponseBase;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/7/2017
 */
public class LogoutTest extends TestBase {

    @Test
    public void logoutTest() throws Exception {
        AuthorizeRequest authRequest = new AuthorizeRequest(this.properties.getProperty("CR_READ"));
        LogoutRequest request = new LogoutRequest();
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(5, TimeUnit.SECONDS);

        LogoutResponse response = (LogoutResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "logout");
        assertEquals(response.getError(), null);
        assertTrue(response.getLogout());
    }
}
