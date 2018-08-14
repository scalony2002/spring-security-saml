/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.springframework.security.saml.spi.deprecated;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.saml.saml2.authentication.Assertion;
import org.springframework.security.saml.saml2.authentication.AuthenticationRequest;
import org.springframework.security.saml.saml2.metadata.IdentityProviderMetadata;
import org.springframework.security.saml.saml2.metadata.NameId;
import org.springframework.security.saml.saml2.metadata.ServiceProviderMetadata;
import org.springframework.security.saml.spi.DefaultSessionAssertionStore;

public abstract class IdpAssertionHandler<T extends IdpAssertionHandler> extends DefaultSamlMessageHandler<T> {

	protected Assertion getAssertion(IdentityProviderMetadata local,
									 AuthenticationRequest authn,
									 ServiceProviderMetadata sp,
									 Authentication authentication,
									 HttpServletRequest request,
									 DefaultSessionAssertionStore assertionStore) {

		Assertion assertion = createAssertion(local, authn, sp, authentication);
		assertionStore.addMessage(request, assertion.getId(), assertion);
		return assertion;
	}

	private Assertion createAssertion(IdentityProviderMetadata local,
									  AuthenticationRequest authn,
									  ServiceProviderMetadata sp,
									  Authentication authentication) {
		String principal = authentication.getName();
		return getSamlDefaults().assertion(sp, local, authn, principal, NameId.PERSISTENT);
	}


}