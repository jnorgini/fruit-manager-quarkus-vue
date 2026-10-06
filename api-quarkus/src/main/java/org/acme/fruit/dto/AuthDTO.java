package org.acme.fruit.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthDTO {

	@NotBlank(message = "Username is required.")
	public String username;

	@NotBlank(message = "Password is required.")
	public String password;
	
}
