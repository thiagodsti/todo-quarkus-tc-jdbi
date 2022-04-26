package com.showcase.demo;

import io.agroal.api.AgroalDataSource;
import java.sql.SQLException;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.ws.rs.Produces;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

@ApplicationScoped
public record JdbiConfiguration(AgroalDataSource defaultDataSource) {

  public Jdbi jdbi()
      throws SQLException {
    Jdbi jdbi = Jdbi.create(defaultDataSource.getConnection());
    jdbiPlugins().forEach(jdbi::installPlugin);
    rowMappers().forEach(jdbi::registerRowMapper);
    return jdbi;
  }

  public List<JdbiPlugin> jdbiPlugins() {
    return List.of(new SqlObjectPlugin());
  }

  public List<RowMapper<?>> rowMappers() {
    return List.of(new TodoRowMapper());
  }

  @Produces
  @ApplicationScoped
  public TodoRepository todoRepository() throws SQLException {
    return jdbi().onDemand(TodoRepository.class);
  }
}
