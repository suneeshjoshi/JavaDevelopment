package com.suneesh.trading;

import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.requests.SetSelfExclusionSettingsRequest;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.ResponseBase;
import com.suneesh.trading.models.responses.SetSelfExclusionSettingsResponse;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 9/12/2017
 */
public class SetSelfExlcusionSettingsRequestTest extends TestBase {

    @Test
    public void setSelfExclusionSettingsTest() throws Exception {
        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("CR_ADMIN"));
        SetSelfExclusionSettingsRequest request = new SetSelfExclusionSettingsRequest();
        request.setMaxBalance(new BigDecimal("1000000"));
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(10, TimeUnit.SECONDS);

        SetSelfExclusionSettingsResponse response = (SetSelfExclusionSettingsResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "set_self_exclusion");
        assertEquals(response.getError(), null);
        assertEquals(response.getResult(), new Integer(1));
    }
}
