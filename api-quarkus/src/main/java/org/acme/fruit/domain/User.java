package org.acme.fruit.domain;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Column(unique = true, nullable = false)
	public String username;

	@Column(nullable = false)
	public String password;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	public Set<Role> roles;
}
