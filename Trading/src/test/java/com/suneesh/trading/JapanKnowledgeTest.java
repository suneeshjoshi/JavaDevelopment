package com.suneesh.trading;

import com.suneesh.trading.models.enums.TestResult;
import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.requests.JapanKnowledgeTestRequest;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.JapanKnowledgeTestResponse;
import com.suneesh.trading.models.responses.ResponseBase;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/30/2017
 */
public class JapanKnowledgeTest extends TestBase {

    @Test
    public void failedJapanKnowledgeTest() throws Exception {
        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("VRTC_ADMIN"));
        JapanKnowledgeTestRequest request = new JapanKnowledgeTestRequest(12, TestResult.PASS);
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(10, TimeUnit.SECONDS);

        JapanKnowledgeTestResponse response = (JapanKnowledgeTestResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "jp_knowledge_test");
        assertNotEquals(response.getError(), null);
    }
}
