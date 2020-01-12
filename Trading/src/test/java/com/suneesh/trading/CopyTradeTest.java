package com.suneesh.trading;

import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.requests.StartCopyTradeRequest;
import com.suneesh.trading.models.requests.StopCopyTradeRequest;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.ResponseBase;
import com.suneesh.trading.models.responses.StartCopyTradeResponse;
import com.suneesh.trading.models.responses.StopCopyTradeResponse;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/9/2017
 */
public class CopyTradeTest extends TestBase {

    @Test
    public void startCopyTradeTest() throws Exception {
        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("VRTC_TRADE"));
        StartCopyTradeRequest request = new StartCopyTradeRequest(properties.getProperty("CR_TRADE"));
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(5, TimeUnit.SECONDS);

        StartCopyTradeResponse response = (StartCopyTradeResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "copy_start");
        assertNotEquals(response.getError(), null);
        assertEquals(response.getError().getCode(), "CopyTradingNotAllowed");
    }

    @Test
    public void stopCopyTradeTest() throws Exception {
        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("VRTC_TRADE"));
        StopCopyTradeRequest request = new StopCopyTradeRequest(properties.getProperty("CR_TRADE"));
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(5, TimeUnit.SECONDS);

        StopCopyTradeResponse response = (StopCopyTradeResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "copy_stop");
        assertEquals(response.getError(), null);
        assertEquals(response.getResult(), new Integer(1));
    }
}
