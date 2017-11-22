package io.paradoxical.finatra.swagger.tests

import io.paradoxical.finatra.swagger.{Resolvers, SwaggerController, WebjarsController}
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter
import io.swagger.models.auth.BasicAuthDefinition
import io.swagger.models.{Info, Swagger}
import io.swagger.util.Json

object SampleSwagger extends Swagger {
  Json.mapper().setPropertyNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy)
}

object SampleApp extends HttpServer {
  val info = new Info()
              .description("The Student / Course management API, this is a sample for swagger document generation")
              .version("1.0.1")
              .title("Student / Course Management API")
  SampleSwagger
    .info(info)
    .addSecurityDefinition("sampleBasic", {
      val d = new BasicAuthDefinition()
      d.setType("basic")
      d
    })


  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[CommonFilters]
      .add[WebjarsController]
      .add(new SwaggerController(swagger = SampleSwagger))
      .add[SampleController]
  }
}
