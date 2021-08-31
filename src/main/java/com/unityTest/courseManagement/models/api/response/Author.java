package com.unityTest.courseManagement.models.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.keycloak.representations.AccessToken;

/**
 * Models an author's information
 */
@AllArgsConstructor
@Data
@ApiModel(value = "Author")
public class Author {
	public static Author of(AccessToken token) {
		return new Author(token.getGivenName(), token.getFamilyName());
	}

	@ApiModelProperty(value = "First name", example = "John")
	private String firstname;

	@ApiModelProperty(value = "Last name", example = "Deer")
	private String lastname;
}