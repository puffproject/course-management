package com.unityTest.courseManagement.service;

import com.unityTest.courseManagement.models.api.response.Author;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for authenticating with the keycloak api through a service client
 */
@Slf4j
@Service
public class KeycloakService {

	@Value("${keycloak.auth-server-url}")
	private String authUrl;

	@Getter
	@Value("${keycloak.realm}")
	private String realmName;

	@Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
	private String clientSecret;

	private Keycloak instance;

	/**
	 * Initialize an api auth call to the keycloak server using the service client credentials
	 */
	private void initializeConnection() {
		this.instance = KeycloakBuilder
			.builder()
			.serverUrl(this.authUrl)
			.grantType(OAuth2Constants.CLIENT_CREDENTIALS)
			.realm(this.realmName)
			.clientId(this.clientId)
			.clientSecret(this.clientSecret)
			.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(5).build())
			.build();
	}

	/**
	 * Get an authenticated Keycloak object to make api calls with
	 *
	 * @return Authenticated Keycloak obj
	 */
	public Keycloak getConnection() {
		if (this.instance != null)
			return this.instance;
		this.initializeConnection();
		return this.instance;
	}

	public Author getAuthorDetails(String authorId) {
		Author author = null;
		try {
			UserRepresentation user = getConnection().realm(this.realmName).users().get(authorId).toRepresentation();
			// Create author object with the user representation
			author = new Author(user.getFirstName(), user.getLastName());
		} catch (javax.ws.rs.NotFoundException e) {
			String errMsg = "Failed to find keycloak user with id %s. Error %s";
			log.error(String.format(errMsg, authorId, e.getLocalizedMessage()));
		}
		return author;
	}
}
