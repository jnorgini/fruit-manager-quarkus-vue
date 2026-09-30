package org.acme;

import java.util.List;

import jakarta.transaction.Transactional;
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
public class GreetingResource {

	@GET
	public List<Fruit> listFruits() {
		return Fruit.listAll();
	}

	@GET
	@Path("/{id}")
	public Fruit getById(@PathParam("id") Long id) {
		return Fruit.findById(id);
	}

	@POST
	@Transactional
	public Response addFruit(Fruit novaFruta) {
		novaFruta.persist();
		List<Fruit> list = Fruit.listAll();
		return Response.status(Response.Status.CREATED).entity(list).build();
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public Response deleteFruit(@PathParam("id") Long id) {
		Fruit.deleteById(id);
		List<Fruit> list = Fruit.listAll();
		return Response.ok(list).build();
	}

	@PUT
	@Path("/{id}")
	@Transactional
	public Response editFruit(@PathParam("id") Long id, Fruit updatedFruit) {
		Fruit fruit = Fruit.findById(id);
		if (fruit != null) {
			fruit.name = updatedFruit.name;
			fruit.color = updatedFruit.color;
		}
		List<Fruit> list = Fruit.listAll();
		return Response.ok(list).build();
	}

}
