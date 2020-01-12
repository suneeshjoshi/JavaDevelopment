package com.suneesh.trading;

import com.suneesh.trading.models.requests.ContractsForSymbolRequest;
import com.suneesh.trading.models.responses.ContractsForSymbolResponse;
import com.suneesh.trading.models.responses.ResponseBase;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/1/2017
 */
public class ContractForSymbolTest {
    private ApiWrapper api;
    @Before
    public void setup() throws Exception{
        this.api = ApiWrapper.build("10");
    }

    @Test
    public void getContractForR_50() throws Exception {
        ContractsForSymbolRequest request = new ContractsForSymbolRequest("R_50");
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(request)
                .subscribe(testObserver);

        testObserver.await(10, TimeUnit.SECONDS);

        ContractsForSymbolResponse response = (ContractsForSymbolResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "contracts_for");
        assertNotEquals(response.getContractsFor().getAvailable(), null);

    }
}
