package com.suneesh.trading;

import com.suneesh.trading.models.requests.ApplicationDetailsRequest;
import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.responses.ApplicationDetailsResponse;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.ResponseBase;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/27/2017
 */
public class FrameworkDetailsTest extends TestBase {

    @Test
    public void getInvalidApplicationTest() throws Exception {
        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("VRTC_ADMIN"));
        ApplicationDetailsRequest request = new ApplicationDetailsRequest(11111L);
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(10, TimeUnit.SECONDS);

        ApplicationDetailsResponse response = (ApplicationDetailsResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "app_get");
        assertNotEquals(response.getError(), null);
    }
}
