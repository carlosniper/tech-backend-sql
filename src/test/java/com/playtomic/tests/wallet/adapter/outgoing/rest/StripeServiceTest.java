package com.playtomic.tests.wallet.adapter.outgoing.rest;


import com.playtomic.tests.wallet.adapter.outgoing.rest.responses.Payment;
import com.playtomic.tests.wallet.core.exceptions.stripe.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.core.exceptions.stripe.StripeRestTemplateResponseErrorHandler;
import com.playtomic.tests.wallet.core.exceptions.stripe.StripeServiceException;
import com.playtomic.tests.wallet.adapter.outgoing.rest.StripeService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test is failing with the current implementation.
 *
 * How would you test this?
 */

@ExtendWith(MockitoExtension.class)
public class StripeServiceTest {

    @Mock
    RestTemplateBuilder restTemplateBuilderMock;

    @Mock
    RestTemplate restTemplateMock;

    private StripeService s;

    URI testUri;

    @BeforeEach
    void setup() {
        testUri = URI.create("http://how-would-you-test-me.localhost");
        doReturn(this.restTemplateBuilderMock).when(this.restTemplateBuilderMock).errorHandler(any());
        doReturn(this.restTemplateMock).when(this.restTemplateBuilderMock).build();
        s = new StripeService(testUri, testUri, restTemplateBuilderMock);
    }

    @Test
    public void test_exception() {
        when(restTemplateMock.postForObject(any(), any(), any())).thenThrow(new StripeAmountTooSmallException());
        Assertions.assertThrows(StripeAmountTooSmallException.class, () -> {
            s.charge("4242 4242 4242 4242", new BigDecimal(5));
        });
    }

    @Test
    public void test_ok() throws StripeServiceException {
        var paymentResponse = mock(Payment.class);
        when(restTemplateMock.postForObject(any(), any(), any())).thenReturn(paymentResponse);
        s.charge("4242 4242 4242 4242", new BigDecimal(15));
    }
}
