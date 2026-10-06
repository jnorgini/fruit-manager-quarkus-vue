package org.acme.fruit.dto;

import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.acme.fruit.domain.Role;

public class UserRegisterDTO {

	@NotBlank(message = "Username cannot be blank.")
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
	public String username;

	@NotBlank(message = "Password cannot be blank.")
	@Size(min = 6, message = "Password must be at least 6 characters long.")
	public String password;

	public Set<Role> roles;

}
