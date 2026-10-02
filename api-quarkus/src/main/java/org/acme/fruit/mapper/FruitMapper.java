package org.acme.fruit.mapper;

import java.util.List;

import org.acme.fruit.Fruit;
import org.acme.fruit.dto.FruitRequestDTO;
import org.acme.fruit.dto.FruitResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "jakarta")
public interface FruitMapper {

	FruitResponseDTO toResponse(Fruit fruit);

	List<FruitResponseDTO> toResponseList(List<Fruit> fruits);

	Fruit toEntity(FruitRequestDTO requestDTO);

	void updateEntityFromDto(FruitRequestDTO dto, @MappingTarget Fruit entity);
}
