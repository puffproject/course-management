package com.unityTest.courseManagement.configuration;

import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.web.client.RestTemplate;

@Profile("cloud")
@Configuration
public class CustomConfigServiceBootstrapConfiguration {

	@Value("${keycloak.realm}")
	private String realm;

	// @Value("${keycloak.auth-server-url}")
	private String authServerUrl = "http://localhost:8180/auth";

	// @Value("${service-account.client-id}")
	private String clientId = "puff-service-acc";

	// @Value("${service-account.client-secret}")
	private String clientSecret = "6d29c988-5da1-4528-9058-c288f0434e89";

	@Autowired
	private ConfigurableEnvironment environment;

	public ConfigClientProperties customConfigClientProperties() {
		ConfigClientProperties client = new ConfigClientProperties(this.environment);
		client.setEnabled(false);
		return client;
	}

	public RestTemplate createRestTemplate() {
		return new OAuth2RestTemplate(getClientCredentialsResourceDetails(), new DefaultOAuth2ClientContext());
	}

	private ClientCredentialsResourceDetails getClientCredentialsResourceDetails() {
		String accessTokenUri = String.format("%s/realms/%s/protocol/openid-connect/token", authServerUrl, realm);

		ClientCredentialsResourceDetails clientCredentialsResourceDetails = new ClientCredentialsResourceDetails();

		clientCredentialsResourceDetails.setAccessTokenUri(accessTokenUri);
		clientCredentialsResourceDetails.setAuthenticationScheme(AuthenticationScheme.header);
		clientCredentialsResourceDetails.setClientId(clientId);
		clientCredentialsResourceDetails.setClientSecret(clientSecret);

		return clientCredentialsResourceDetails;
	}

	@Bean
	public ConfigServicePropertySourceLocator configServicePropertySourceLocator() {
		ConfigClientProperties clientProperties = customConfigClientProperties();
		ConfigServicePropertySourceLocator configServicePropertySourceLocator =
			new ConfigServicePropertySourceLocator(clientProperties);
		configServicePropertySourceLocator.setRestTemplate(createRestTemplate());
		return configServicePropertySourceLocator;
	}
}
