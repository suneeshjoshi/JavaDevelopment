package com.suneesh.trading;

import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.requests.OAuthApplicationsRequest;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.OAuthApplicationResponse;
import com.suneesh.trading.models.responses.ResponseBase;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/7/2017
 */
public class OAuthApplicationsTest extends TestBase {

    @Test
    public void getOauthApplicationsTest() throws Exception {
        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("CR_READ"));
        OAuthApplicationsRequest request = new OAuthApplicationsRequest();
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(5, TimeUnit.SECONDS);

        OAuthApplicationResponse response = (OAuthApplicationResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "oauth_apps");
        assertEquals(response.getError(), null);
        assertNotEquals(response.getOauthApplications(), null);
    }
}
