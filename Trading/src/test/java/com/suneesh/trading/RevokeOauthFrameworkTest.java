package com.suneesh.trading;

import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.requests.RevokeOauthApplicationRequest;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.ResponseBase;
import com.suneesh.trading.models.responses.RevokeOauthApplicationResponse;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 9/4/2017
 */
public class RevokeOauthFrameworkTest extends TestBase {

    @Test
    public void revokeInvalidApplicationTest() throws Exception {
        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("VRTC_ADMIN"));
        RevokeOauthApplicationRequest request = new RevokeOauthApplicationRequest(1234L);
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(10, TimeUnit.SECONDS);

        RevokeOauthApplicationResponse response = (RevokeOauthApplicationResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "revoke_oauth_app");
        assertEquals(response.getError(), null);
        assertEquals(response.getResult(), new Integer(1));
    }
}
