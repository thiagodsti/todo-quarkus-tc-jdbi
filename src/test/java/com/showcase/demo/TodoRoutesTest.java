package com.showcase.demo;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TodoRoutesTest {

  @InjectMock
  TodoRepository todoRepository;

  @Test
  public void saveTodo_andReturnLocation() {
    var todo = TodoSave.builder().title("test").description("test description").build();

    when(todoRepository.save(eq(todo))).thenReturn(Todo.builder().id(1L).build());

    given().post("/todos").then().statusCode(201).header("Location", "/todos/1");
  }
}
