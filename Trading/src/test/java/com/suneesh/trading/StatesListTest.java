package com.suneesh.trading;

import com.suneesh.trading.models.requests.StatesListRequest;
import com.suneesh.trading.models.responses.ResponseBase;
import com.suneesh.trading.models.responses.StatesListResponse;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/2/2017
 */
public class StatesListTest {
    private ApiWrapper api;
    @Before
    public void setup() throws Exception{
        this.api = ApiWrapper.build("10");
    }

    @Test
    public void getStatesListTest() throws Exception {
        StatesListRequest request = new StatesListRequest("de");
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(request)
                .subscribe(testObserver);

        testObserver.await(10, TimeUnit.SECONDS);

        StatesListResponse response = (StatesListResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "states_list");
        assertEquals(response.getError(), null);
        assertTrue(response.getStatesList().size() > 0);
    }
}
