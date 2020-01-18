package com.suneesh.trading;

import com.suneesh.trading.models.requests.ApplicationListRequest;
import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.responses.ApplicationListResponse;
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
public class FrameworkListTest extends TestBase {

    @Test
    public void getApplicationsListTest() throws Exception {
        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("VRTC_ADMIN"));
        ApplicationListRequest request = new ApplicationListRequest();
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(10, TimeUnit.SECONDS);

        ApplicationListResponse response = (ApplicationListResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "app_list");
        assertEquals(response.getError(), null);
        assertNotEquals(response.getApplications(), null);
    }
}
