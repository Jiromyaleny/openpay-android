/*
 * Copyright 2013 Opencard Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.openpay.android.test;

import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Address;
import mx.openpay.android.model.Card;
import mx.openpay.android.services.CardService;
import mx.openpay.android.services.ServicesFactory;

import org.junit.After;
import org.junit.Test;

import android.test.InstrumentationTestCase;

/**
 * @author Luis Delucio
 * 
 */
public class CardServicesTest extends InstrumentationTestCase {

	private static final String URL_SANDBOX = "https://sandbox-api.openpay.mx";
	private CardService cardServices;

	@After
	protected void setUp() throws Exception {
		super.setUp();
		this.cardServices = ServicesFactory.getInstance(URL_SANDBOX, "mh9ovicn5oqq2wgpyo1v",
				"pk_4f007c56b11e46189100740e593ee747").getService(CardService.class);
	}

	@Test
	public void createMerchantCard() throws OpenpayServiceException, ServiceUnavailableException {

		Card newCard = this.cardServices.create(this.getCard(), "");

		assertNotNull(newCard);
	}

	@Test
	public void createMerchantCardWithAddress() throws OpenpayServiceException, ServiceUnavailableException {

		Card card = this.getCard();
		card.address(this.getAddres());
		Card newCard = this.cardServices.create(card, "");

		assertNotNull(newCard);
		assertNotNull(newCard.getAddress());
	}

	@Test
	public void createMerchantCard_ErrorDataCard() throws ServiceUnavailableException {
		Card card = this.getCard();
		card.expirationYear(12);
		try {
			this.cardServices.create(card, "");
		} catch (OpenpayServiceException e) {
			assertNotNull(e.getDescription());
			assertNotNull(e.getRequestId());
			assertNotNull(e.getCategory());
			assertNotNull(e.getHttpCode());
			assertNotNull(e.getErrorCode());
		}
	}

	private Address getAddres() {
		return new Address().city("Mexico").countryCode("MX").line1("Calle de la felicidad").line2("Numero 20")
				.line3("Col ensueño").postalCode("76900").state("Mexico");
	}

	private Card getCard() {
		Card card = new Card().cardNumber("4111111111111111").holderName("Pedro Paramo").expirationMonth(2)
				.expirationYear(15).cvv2("123");
		return card;
	}

}
