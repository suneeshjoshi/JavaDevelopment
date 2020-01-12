package com.suneesh.trading;

import com.suneesh.trading.models.enums.Scopes;
import com.suneesh.trading.models.requests.ApplicationUpdateRequest;
import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.responses.ApplicationUpdateResponse;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.ResponseBase;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/27/2017
 */
public class ApplicationUpdateTest extends TestBase {

    @Test
    public void updateInvalidApplicationTest() throws Exception {
        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("VRTC_ADMIN"));
        ApplicationUpdateRequest request = new ApplicationUpdateRequest(
                1234L,
                "testAppRegistration",
                Arrays.asList(new Scopes[]{Scopes.READ, Scopes.TRADE}),
                "https://www.test.com"
        );

        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(10, TimeUnit.SECONDS);

        ApplicationUpdateResponse response = (ApplicationUpdateResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "app_update");
        assertNotEquals(response.getError(), null);
    }
}
