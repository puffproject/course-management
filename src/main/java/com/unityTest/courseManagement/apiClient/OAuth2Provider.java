package com.unityTest.courseManagement.apiClient;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2Provider {

	// Using anonymous user principal as its S2S authentication
	public static final Authentication ANONYMOUS_USER_AUTHENTICATION =
		new AnonymousAuthenticationToken("key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

	private final OAuth2AuthorizedClientManager authorizedClientManager;

	public String getAuthenticationToken(final String authServerName) {
		final OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
			.withClientRegistrationId(authServerName)
			.principal(ANONYMOUS_USER_AUTHENTICATION)
			.build();
		return "Bearer " + authorizedClientManager.authorize(request).getAccessToken().getTokenValue();
	}
}
