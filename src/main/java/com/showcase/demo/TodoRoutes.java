package com.showcase.demo;

import io.quarkus.vertx.web.Body;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.Route.HttpMethod;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public record TodoRoutes(TodoRepository repository) {

    // neither path nor regex is set - match a path derived from the method name
    @Route(path = "/todos/:todoId", methods = Route.HttpMethod.GET, produces = MediaType.APPLICATION_JSON)
    Uni<Todo> getById(RoutingContext rc) {
        String todoId = rc.pathParam("todoId");
        Optional<Todo> opt = repository.findById(Long.parseLong(todoId));
        if (opt.isEmpty()) {
            rc.fail(404);
            return Uni.createFrom().nothing();
        }
        return Uni.createFrom().optional(opt);
    }

    @Route(path = "/todos", methods = HttpMethod.POST, consumes = MediaType.APPLICATION_JSON)
    void save(RoutingContext rc, @Body TodoSave todo) {
        var saved = repository.save(todo);
        rc.response().end("id " + saved.id());
    }

    @Route(path = "/todos/:id", methods = HttpMethod.DELETE)
    void remove(RoutingContext rc) {
        repository.deleteById(Long.parseLong(rc.pathParam("id")));
        rc.response().setStatusCode(204).end();
    }

    @Route(path = "/todos/:id", methods = HttpMethod.PUT)
    void update(RoutingContext rc, @Body TodoUpdate todo) {
        long id = Long.parseLong(rc.pathParam("id"));
        Optional<Todo> todoOpt = repository.findById(id);
        if (todoOpt.isEmpty()) {
            rc.fail(404);
            return;
        }
        repository.update(todo, id);
        rc.response().end();
    }
}