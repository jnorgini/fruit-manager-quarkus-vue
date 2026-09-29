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

@Path("/frutas")
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

	@GET
	public List<Fruta> listarFrutas() {
		return Fruta.listAll();
	}

	@GET
	@Path("/{id}")
	public Fruta buscarPorId(@PathParam("id") Long id) {
		return Fruta.findById(id);
	}

	@POST
	@Transactional
	public Response adicionarFruta(Fruta novaFruta) {
		novaFruta.persist();
		List<Fruta> listaAtualizada = Fruta.listAll();
		return Response.status(Response.Status.CREATED).entity(listaAtualizada).build();
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public Response deletarFruta(@PathParam("id") Long id) {
		Fruta.deleteById(id);
		List<Fruta> listaAtualizada = Fruta.listAll();
		return Response.ok(listaAtualizada).build();
	}

	@PUT
	@Path("/{id}")
	@Transactional
	public Response atualizarFruta(@PathParam("id") Long id, Fruta frutaAtualizada) {
		Fruta frutaBanco = Fruta.findById(id);
		if (frutaBanco != null) {
			frutaBanco.nome = frutaAtualizada.nome;
			frutaBanco.cor = frutaAtualizada.cor;
		}
		List<Fruta> listaAtualizada = Fruta.listAll();
		return Response.ok(listaAtualizada).build();
	}
	
}
