package com.suneesh.trading;

import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.requests.CreateRealSubAccountRequest;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.CreateRealSubAccountResponse;
import com.suneesh.trading.models.responses.ResponseBase;
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
public class CreateRealSubAccountTest extends TestBase {

    @Test
    public void createSubAccountTest() throws Exception {

        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("CR_ADMIN"));
        CreateRealSubAccountRequest request = new CreateRealSubAccountRequest();
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(10, TimeUnit.SECONDS);

        CreateRealSubAccountResponse response = (CreateRealSubAccountResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "new_sub_account");
        assertNotEquals(response.getError(), null);
    }
}
