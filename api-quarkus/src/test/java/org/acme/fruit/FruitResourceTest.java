package org.acme.fruit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import org.acme.fruit.dto.FruitRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;

@QuarkusTest
public class FruitResourceTest {

	@BeforeEach
	@Transactional
	public void clearDatabase() {
		Fruit.deleteAll();
	}

	@Test
	public void shouldListFruitsSuccessfully() {
		createFruitInDatabase("Banana", "Amarela");

		given()
		.when()
			.get("/fruits")
		.then()
			.statusCode(200)
			.body("name", hasItem("Banana"));
	}

	@Test
	public void shouldFindFruitByIdSuccessfully() {
		Long insertedId = createFruitInDatabase("Morango", "Vermelho");

		given()
			.pathParam("id", insertedId)
		.when()
			.get("/fruits/{id}")
		.then()
			.statusCode(200)
			.body("id", is(insertedId.intValue()))
			.body("name", is("Morango"));
	}

	@Test
	public void shouldReturn404WhenFruitDoesNotExist() {
		given()
			.pathParam("id", 999)
		.when()
			.get("/fruits/{id}")
		.then()
			.statusCode(404)
			.body("status", is(404))
			.body("error", is("Not Found"))
			.body("message", is("Fruta com o ID 999 não encontrada."));
	}

	@Test
	public void shouldCreateFruitSuccessfully() {
		FruitRequestDTO request = new FruitRequestDTO();
		request.name = "Melancia";
		request.color = "Verde";
		
		given()
			.contentType(ContentType.JSON)
			.body(request)
		.when()
			.post("/fruits")
		.then()
			.statusCode(201)
			.body("name", hasItem("Melancia"));
	}

	@Test
	public void shouldNotCreateFruitWhenNameIsEmpty() {
		FruitRequestDTO invalidRequest = new FruitRequestDTO();
		invalidRequest.name = ""; 
		invalidRequest.color = "Azul";

		given()
			.contentType(ContentType.JSON)
			.body(invalidRequest)
		.when()
			.post("/fruits")
		.then()
			.statusCode(400)
			.body("status", is(400))
			.body("error", is("Bad Request"))
			.body("message", is("The fruit name cannot be blank."));
	}

	@Test
	public void shouldNotCreateFruitWhenNameIsTooShort() {
		FruitRequestDTO invalidRequest = new FruitRequestDTO();
		invalidRequest.name = "A"; 
		invalidRequest.color = "Verde";

		given()
			.contentType(ContentType.JSON)
			.body(invalidRequest)
		.when()
			.post("/fruits")
		.then()
			.statusCode(400)
			.body("status", is(400))
			.body("message", is("The name must be between 2 and 50 characters."));
	}

	@Test
	public void shouldUpdateFruitSuccessfully() {
		Long insertedId = createFruitInDatabase("Limao", "Verde");

		FruitRequestDTO updateRequest = new FruitRequestDTO();
		updateRequest.name = "Limao Siciliano";
		updateRequest.color = "Amarelo";

		given()
			.contentType(ContentType.JSON)
			.pathParam("id", insertedId)
			.body(updateRequest)
		.when()
			.put("/fruits/{id}")
		.then()
			.statusCode(200)
			.body("name", hasItem("Limao Siciliano"))
			.body("name", not(hasItem("Limao"))); 
	}

	@Test
	public void shouldDeleteFruitSuccessfully() {
		Long insertedId = createFruitInDatabase("Uva", "Roxa");
		
		given()
			.pathParam("id", insertedId)
		.when()
			.delete("/fruits/{id}")
		.then()
			.statusCode(200);
			
		given()
			.pathParam("id", insertedId)
		.when()
			.get("/fruits/{id}")
		.then()
			.statusCode(404);
	}

	@Transactional
	public Long createFruitInDatabase(String name, String color) {
		Fruit fruit = new Fruit();
		fruit.name = name;
		fruit.color = color;
		fruit.persist();
		return fruit.id;
	}
	
}
