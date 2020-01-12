package com.suneesh.trading;

import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.requests.TransferBetweenAccountsRequest;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.ResponseBase;
import com.suneesh.trading.models.responses.TransferBetweenAccountsResponse;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 9/15/2017
 */
public class TransferBetweenAccountsTest extends TestBase {

    @Test
    public void invalidTransfer() throws Exception {

        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("CR_PAYMENTS"));
        TransferBetweenAccountsRequest request = new TransferBetweenAccountsRequest();
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        request.setAccountFrom("CR494048");
        request.setAccountTo("CR494048");
        request.setAmount(new BigDecimal(1000));
        request.setCurrency("USD");

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });


        testObserver.await(10, TimeUnit.SECONDS);

        TransferBetweenAccountsResponse response = (TransferBetweenAccountsResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "transfer_between_accounts");
        assertEquals(response.getError().getCode(), "TransferBetweenAccountsError");
        assertEquals(response.getError().getMessage(), "Account transfers are not available within same account.");
    }
}
