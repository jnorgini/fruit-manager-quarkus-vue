package org.acme.fruit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FruitResponseDTO {

	public Long id;
	public String name;
	public String color;

}
