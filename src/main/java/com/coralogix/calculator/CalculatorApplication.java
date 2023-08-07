package com.coralogix.calculator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/* @EnableR2dbcRepositories es una anotación en el marco de trabajo Spring Boot que se utiliza para activar la creación de repositorios 
 * R2DBC (Reactive Relational Database Connectivity). Específicamente, esta anotación le permite a Spring saber que debe buscar 
 * interfaces que extiendan ReactiveCrudRepository o R2dbcRepository y generar implementaciones de repositorio para ellas.
 * R2DBC es una iniciativa que tiene como objetivo proporcionar un acceso reactivo y no bloqueante a las bases de datos relacionales.
 *  A diferencia de JDBC, que bloquea la ejecución hasta que se completa la operación de la base de datos, R2DBC utiliza un modelo de 
 *  programación reactivo para garantizar que la aplicación pueda continuar ejecutando otras tareas mientras espera que se complete la 
 *  operación de la base de datos.*/
//@EnableR2dbcRepositories
@EnableWebFlux
@SpringBootApplication
public class CalculatorApplication {
	
	  @Bean
	  ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

	    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
	    initializer.setConnectionFactory(connectionFactory);
	    initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

	    return initializer;
	  }

	public static void main(String[] args) {
		SpringApplication.run(CalculatorApplication.class, args);
	}
}
