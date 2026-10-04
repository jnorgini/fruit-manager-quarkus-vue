package org.acme.fruit.domain;

import org.acme.exception.ResourceNotFoundException;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Fruit extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	public String name;
	public String color;

	public static Fruit findByIdOrThrow(Long id) {
		Fruit fruit = Fruit.findById(id);
		if (fruit == null) {
			throw new ResourceNotFoundException("Fruta com o ID " + id + " não encontrada.");
		}
		return fruit;
	}

}
