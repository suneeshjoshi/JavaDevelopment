package com.suneesh.trading;

import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.requests.TransactionsStreamRequest;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.ResponseBase;
import com.suneesh.trading.models.responses.TransactionsStreamResponse;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/8/2017
 */
public class TransactionStreamTest extends TestBase {

    @Test
    public void subscribeTransactionsTest() throws Exception {
        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("VRTC_READ"));
        TransactionsStreamRequest request = new TransactionsStreamRequest();
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(5, TimeUnit.SECONDS);

        TransactionsStreamResponse response = (TransactionsStreamResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "transaction");
        assertEquals(response.getError(), null);
        assertNotEquals(response.getTransaction().getId(), null);
    }
}
