package com.playtomic.tests.wallet;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import com.playtomic.tests.wallet.adapter.incoming.rest.WalletController;
import com.playtomic.tests.wallet.adapter.incoming.rest.request.ChargeWalletRequest;
import com.playtomic.tests.wallet.core.domain.Wallet;
import com.playtomic.tests.wallet.core.exceptions.ErrorResponse;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@EnableWireMock({@ConfigureWireMock(name = "stripe-client", property = "stripe.simulator.charges-uri")})
public class WalletApplicationIT {

	@InjectWireMock("stripe-client")
	private WireMockServer stripeService;

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach
	void setup() {
		stripeService.resetAll();
	}

	@Test
	void test_walletController_whenGetWallet_returnSuccessfulResponse() {
		URI testUri = URI.create("/wallet/234");
		ResponseEntity<Wallet> response = restTemplate.getForEntity(testUri, Wallet.class);
		assertEquals(200, response.getStatusCode().value());
		assertEquals(234L, response.getBody().getId());
		assertEquals(30.00, response.getBody().getAmount().floatValue());
	}

	@Test
	void test_walletController_whenGetWallet_andNotExist_returnNotFound() {
		ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(URI.create("/wallet/404"), ErrorResponse.class);
		assertEquals(404, response.getStatusCode().value());
		assertEquals("Wallet not found", response.getBody().getCause());

	}

	@Test
	void test_walletController_whenChargeWallet_returnSuccessfulResponse() {
		var request = new ChargeWalletRequest(234L, "1234567890", new BigDecimal(11));
		ResponseEntity<Wallet> response = restTemplate.postForEntity(URI.create("/wallet/charge"), request, Wallet.class);
		assertEquals(200, response.getStatusCode().value());
		assertEquals(234L, response.getBody().getId());
		assertEquals(41L, response.getBody().getAmount().floatValue());

		stripeService.verify(1, postRequestedFor(urlMatching("/")).withRequestBody(equalToJson("""
				{
					"credit_card": "1234567890",
					"amount": 11
				}
				""")));
	}

	@Test
	void test_walletController_whenChargeWallet_andWalletNotExist_thenReturnNotFound() {
		var request = new ChargeWalletRequest(001L, "1234567890", new BigDecimal(11));
		ResponseEntity<Wallet> response = restTemplate.postForEntity(URI.create("/wallet/charge"), request, Wallet.class);
		assertEquals(404, response.getStatusCode().value());

		stripeService.verify(0, postRequestedFor(urlMatching("/")));
	}

	@Test
	void test_walletController_whenChargeWallet_andAmountIsNotEnough_thenReturnError() {
		var request = new ChargeWalletRequest(234L, "0123456789", new BigDecimal(5));
		ResponseEntity<Wallet> response = restTemplate.postForEntity(URI.create("/wallet/charge"), request, Wallet.class);
		assertEquals(406, response.getStatusCode().value());

		stripeService.verify(0, postRequestedFor(urlMatching("/")));
	}

}
