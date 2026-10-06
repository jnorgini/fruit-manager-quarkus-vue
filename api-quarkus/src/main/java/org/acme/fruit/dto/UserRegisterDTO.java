package org.acme.fruit.dto;

import java.util.Set;

import org.acme.fruit.domain.Role;

public class UserRegisterDTO {

	public String username;
	public String password;
	public Set<Role> roles;

}
