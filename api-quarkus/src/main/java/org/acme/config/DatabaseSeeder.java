package org.acme.config;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.fruit.domain.Role;
import org.acme.fruit.domain.User;
import org.acme.fruit.repository.UserRepository;

import java.util.Set;

@ApplicationScoped
public class DatabaseSeeder {

	@Inject
	UserRepository userRepository;

	@Transactional
	public void init(@Observes StartupEvent event) {
		String adminUsername = "system_admin";

		if (userRepository.findByUsername(adminUsername) == null) {

			System.out.println("🚀 [DatabaseSeeder] Creating default Admin user in MySQL...");

			User adminUser = new User();
			adminUser.username = adminUsername;
			adminUser.password = BcryptUtil.bcryptHash("SecurePassword123");
			adminUser.roles = Set.of(Role.USER, Role.ADMIN);
			userRepository.persist(adminUser);

			System.out.println(
					"✅ [DatabaseSeeder] Admin created successfully! Username: system_admin | Password: SecurePassword123");
		} else {
			System.out.println("ℹ️ [DatabaseSeeder] Admin user already exists. Seeding skipped.");
		}
	}

}
