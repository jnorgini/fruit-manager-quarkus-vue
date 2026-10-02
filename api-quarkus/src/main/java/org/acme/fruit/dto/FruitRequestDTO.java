package org.acme.fruit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FruitRequestDTO {

	@NotBlank(message = "The fruit name cannot be blank.")
	@Size(min = 2, max = 50, message = "The name must be between 2 and 50 characters.")
	public String name;

	@NotBlank(message = "The fruit color cannot be blank.")
	public String color;
	
}
