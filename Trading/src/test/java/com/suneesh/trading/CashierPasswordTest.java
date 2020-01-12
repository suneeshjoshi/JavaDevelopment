package com.suneesh.trading;

import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.requests.CashierPasswordRequest;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.CashierPasswordResponse;
import com.suneesh.trading.models.responses.ResponseBase;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 9/14/2017
 */
public class CashierPasswordTest extends TestBase {

    @Test
    public void cashierPasswordTest() throws Exception {

        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("CR_PAYMENTS"));
        CashierPasswordRequest request = new CashierPasswordRequest();
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(10, TimeUnit.SECONDS);

        CashierPasswordResponse response = (CashierPasswordResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "cashier_password");
        assertEquals(response.getError(), null);
        assertFalse(response.isLock());
    }
}
