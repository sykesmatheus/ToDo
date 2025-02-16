package com.matheus.ToDo;

import com.matheus.ToDo.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class TodoApplicationTests {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	void testCreateSucess() {
		var todo = new Todo("Todo Teste", "teste", false, 1);
		webTestClient
				.post()
				.uri("/todos")
				.bodyValue(todo)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$.length()").isEqualTo(1)
				.jsonPath("$[0].nome").isEqualTo(todo.getNome())
				.jsonPath("$[0].descricao").isEqualTo(todo.getDescricao())
				.jsonPath("$[0].realizado").isEqualTo(todo.isRealizado())
				.jsonPath("$[0].prioridade").isEqualTo(todo.getPrioridade());
	}
	@Test
	void testCreateFailure(){
		webTestClient
				.post()
				.uri("/todos")
				.bodyValue( new Todo("", "", false, 0))
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void testUpdateSuccess() {
		Todo oldTodo = new Todo("Antigo", "Antigo", false, 3);
		EntityExchangeResult<List<Todo>> postResult = webTestClient.post()
				.uri("/todos")
				.bodyValue(oldTodo)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Todo.class)
				.returnResult();

		List<Todo> createdTodos = postResult.getResponseBody();
		Todo createdTodo = createdTodos.get(0);
		Long createdTodoId = createdTodo.getId();
		Todo newTodo = new Todo("Atualizar", "Atualizar", true, 1);
		newTodo.setId(createdTodoId);
		String uri = "/todos/" + createdTodoId;
		webTestClient
				.put()
				.uri(uri)
				.bodyValue(newTodo)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Todo.class)
				.hasSize(createdTodos.size())
				.consumeWith(result -> {
					Todo updatedTodo = result.getResponseBody().get(0);
					assertEquals(newTodo.getNome(), updatedTodo.getNome());
					assertEquals(newTodo.getDescricao(), updatedTodo.getDescricao());
					assertEquals(newTodo.isRealizado(), updatedTodo.isRealizado());
					assertEquals(newTodo.getPrioridade(), updatedTodo.getPrioridade());
				});
	}
	@Test
	void testUpdateFailure() {
		Todo oldTodo = new Todo("Antigo", "Antigo", false, 3);
		EntityExchangeResult<List<Todo>> postResult = webTestClient.post()
				.uri("/todos")
				.bodyValue(oldTodo)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Todo.class)
				.returnResult();

		List<Todo> createdTodos = postResult.getResponseBody();
		Todo createdTodo = createdTodos.get(0);
		Long failureId = createdTodo.getId() + 10L;
		Todo newTodo = new Todo("Atualizar", "Atualizar", true, 1);
		newTodo.setId(failureId);
		webTestClient
				.put()
				.uri("/todos/" + failureId)
				.bodyValue(newTodo)
				.exchange()
				.expectStatus().isNotFound();
		webTestClient
				.put()
				.uri("/todos/" + createdTodo.getId())
				.bodyValue( new Todo("", "", false, 0))
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void testDeleteSuccess() {
		Todo todo = new Todo("DeleteTeste", "Teste de delecao", false, 3);
		EntityExchangeResult<List<Todo>> postResult = webTestClient.post()
				.uri("/todos")
				.bodyValue(todo)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Todo.class)
				.returnResult();
		List<Todo> createdTodos = postResult.getResponseBody();
		Todo createdTodo = createdTodos.get(0);
		Long createdTodoId = createdTodo.getId();
		String uri = "/todos/" + createdTodoId;
		webTestClient
				.delete()
				.uri(uri)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Todo.class);

	}

	@Test
	void testDeleteFailure() {
		Long failureId = 10l;
		String uri = "/todos/" + failureId;
		webTestClient
				.delete()
				.uri(uri)
				.exchange()
				.expectStatus().isNotFound();


	}
}
