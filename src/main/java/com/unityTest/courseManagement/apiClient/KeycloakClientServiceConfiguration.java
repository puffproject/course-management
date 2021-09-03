package com.unityTest.courseManagement.apiClient;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class KeycloakClientServiceConfiguration {

	private final OAuth2Provider oauth2Provider;
	private final String AUTH_SERVER_NAME = "keycloak";

	@Bean
	public RequestInterceptor keycloakAuthInterceptor() {
		return (requestTemplate) -> requestTemplate
			.header(HttpHeaders.AUTHORIZATION, oauth2Provider.getAuthenticationToken(AUTH_SERVER_NAME));
	}
}
