package com.suneesh.trading;

import com.suneesh.trading.models.requests.ApiTokenManagementRequest;
import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.responses.ApiTokenManagementResponse;
import com.suneesh.trading.models.responses.AuthorizeResponse;
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
 * @since 8/9/2017
 */
public class ApiTokenManagementTest extends TestBase {

    @Test
    public void getTokensTest() throws Exception {
        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("VRTC_ADMIN"));
        ApiTokenManagementRequest request = new ApiTokenManagementRequest();
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(5, TimeUnit.SECONDS);

        ApiTokenManagementResponse response = (ApiTokenManagementResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "api_token");
        assertEquals(response.getError(), null);
        assertTrue(response.getApiTokenResult().getTokens().size() > 0);
    }
}
