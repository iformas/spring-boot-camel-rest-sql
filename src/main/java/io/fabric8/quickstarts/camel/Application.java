/*
 * Copyright 2005-2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package io.fabric8.quickstarts.camel;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ImportResource({ "classpath:spring/camel-context.xml" })
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	ServletRegistrationBean servletRegistrationBean() {
		ServletRegistrationBean servlet = new ServletRegistrationBean(new CamelHttpTransportServlet(),
				"/camel-rest-sql/*");
		servlet.setName("CamelServlet");
		return servlet;
	}

	@Component
	class RestApi extends RouteBuilder {

		@Override
		public void configure() {
			restConfiguration().contextPath("/cnad-api").apiContextPath("/api-doc")
					.apiProperty("api.title", "Camel REST API").apiProperty("api.version", "1.0")
					.apiProperty("cors", "true").apiContextRouteId("doc-api").component("servlet")
					.bindingMode(RestBindingMode.json).enableCORS(true).corsAllowCredentials(true)
					.corsHeaderProperty("Access-Control-Allow-Origin", "*")
					.corsHeaderProperty("Access-Control-Allow-Headers",
							"Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");

			rest("/prestaciones").description("Prestaciones REST service").get()
					.description("List all of registered prestaciones").produces("application/json").route()
					.routeId("all-prestaciones-api").to("direct:get-prestaciones").endRest().get("/{id}")
					.description("Details of a prestacion by id").produces("application/json").route()
					.routeId("one-prestacion-api").to("direct:get-prestacion").endRest().post()
					.description("Insert the data of a prestacion").type(Prestaciones.class)
					.consumes("application/json").produces("application/json").route().routeId("save-prestacion-api")
					.to("direct:post-prestacion").endRest().put("/{id}")
					.description("Update the data of a prestacion by id").type(Prestaciones.class)
					.consumes("application/json").produces("application/json").route().routeId("update-prestacion-api")
					.to("direct:put-prestacion").endRest().delete("/{id}")
					.description("Delete a pretacion register by id").route().routeId("delete-prestacion-api")
					.to("direct:delete-prestacion").endRest();

			from("direct:get-prestaciones").to("sql:select * from prestaciones?"
					+ "dataSource=dataSource&outputClass=io.fabric8.quickstarts.camel.Prestaciones");

			from("direct:get-prestacion").log("Get prestacion with ID ${header.id}").choice()
					.when(simple("${header.id} regex '^\\d+$'"))
					.to("sql:select * from prestaciones where id = :#${header.id}?"
							+ "dataSource=dataSource&outputClass=io.fabric8.quickstarts.camel.Prestaciones")
					.choice().when(simple("${body.isEmpty()}")).setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
					.setHeader(Exchange.HTTP_RESPONSE_TEXT, constant("Prestacion isn't registered.")).otherwise()
					.setBody(simple("${body.get(0)}")).endChoice().otherwise()
					.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400)).setHeader(Exchange.HTTP_RESPONSE_TEXT,
							constant("ID of the prestacion is necessary, it needs be numeric."))
					.endChoice();

			from("direct:post-prestacion").choice().when(simple("${body} == null"))
					.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
					.setHeader(Exchange.HTTP_RESPONSE_TEXT, constant(
							"Request format is incorrect, ask backend administrator to give prestacion data format."))
					.otherwise().log("Will be inserted prestacion ${body.toString()}")
					.setProperty("newRow", bodyAs(Prestaciones.class))
					.to("sql:select id as ID from prestaciones order by id desc limit 1?dataSource=dataSource").choice()
					.when(simple("${body.isEmpty()}")).setHeader("newId", constant(1)).otherwise()
					.setHeader("newId", simple("${body.get(0)['ID']}++")).end().log("New id ${header.newId}")
					.setBody(exchangeProperty("newRow"))
					.to("language:ognl:request.body.id = request.headers.newId?transform=false")
					.log("New prestacion that is going to be inserted ${body.toString()}")
					.to("sql:insert into prestaciones (id, patient, exam, dateIn, maxDaysToBeforeInform, informDate, examImageId, type, medicalRecord) values "
							+ "(:#${body.id} , :#${body.patient}, :#${body.exam}, :#${body.dateIn}, :#${body.maxDaysToBeforeInform}, :#${body.informDate}, :#${body.examImageId}, :#${body.type}, :#${body.medicalRecord})?"
							+ "dataSource=dataSource")
					.log("Inserted prestacion ${body.toString()}").endChoice();

			from("direct:put-prestacion").log("Will be updated the prestacion with ID ${header.id}").choice()
					.when(simple("${header.id} regex '^\\d+$'")).choice().when(simple("${body} != null"))
					.log("${body.toString()}")
					.to("sql:update prestaciones set patient = :#${body.patient}, exam = :#${body.exam}, dateIn = :#${body.dateIn}, maxDaysToBeforeInform = :#${body.maxDaysToBeforeInform}, informDate = :#${body.informDate}, examImageId = :#${body.examImageId}, type = :#${body.type}, medicalRecord = :#${body.medicalRecord} where id = :#${header.id}?"
							+ "dataSource=dataSource&outputClass=io.fabric8.quickstarts.camel.Prestaciones")
					.otherwise().setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
					.setHeader(Exchange.HTTP_RESPONSE_TEXT, constant(
							"Request format is incorrect, ask backend administrator to give prestacion data format."))
					.endChoice().otherwise().setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
					.setHeader(Exchange.HTTP_RESPONSE_TEXT,
							constant("ID of the prestacion is necessary, it's need be numeric."))
					.endChoice();

			from("direct:delete-prestacion").log("Will be deleted prestacion with id ${header.id}")
					.to("sql:delete from prestaciones where id = :#${header.id}?" + "dataSource=dataSource");
		}
	}

}
