package org.acme.fruit;

import java.util.List;

import org.acme.fruit.dto.FruitRequestDTO;
import org.acme.fruit.dto.FruitResponseDTO;
import org.acme.fruit.mapper.FruitMapper;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitResource {

	@Inject
	FruitMapper mapper;

	@GET
	public List<FruitResponseDTO> listFruits() {
		return mapper.toResponseList(Fruit.listAll());
	}

	@GET
	@Path("/{id}")
	public Response getById(@PathParam("id") Long id) {
		Fruit fruit = Fruit.findByIdOrThrow(id);
		return Response.ok(mapper.toResponse(fruit)).build();
	}

	@POST
	@Transactional
	public Response addFruit(@Valid FruitRequestDTO dto) {
		Fruit novaFruta = mapper.toEntity(dto);
		novaFruta.persist();

		return Response.status(Response.Status.CREATED).entity(mapper.toResponseList(Fruit.listAll())).build();
	}

	@PUT
	@Path("/{id}")
	@Transactional
	public Response editFruit(@PathParam("id") Long id, @Valid FruitRequestDTO dto) {
		Fruit fruit = Fruit.findByIdOrThrow(id);
		mapper.updateEntityFromDto(dto, fruit);

		return Response.ok(mapper.toResponseList(Fruit.listAll())).build();
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public Response deleteFruit(@PathParam("id") Long id) {
		Fruit fruit = Fruit.findByIdOrThrow(id);
		fruit.delete();

		return Response.ok(mapper.toResponseList(Fruit.listAll())).build();
	}

}
