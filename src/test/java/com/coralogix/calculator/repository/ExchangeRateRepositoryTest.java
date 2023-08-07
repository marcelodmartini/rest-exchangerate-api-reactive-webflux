package com.coralogix.calculator.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.r2dbc.core.DatabaseClient;

import com.coralogix.calculator.model.ExchangeRate;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataR2dbcTest
public class ExchangeRateRepositoryTest {

    @Autowired
    private ExchangeRateRepository repository;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setUp() {
        String sql = "CREATE TABLE IF NOT EXISTS PUBLIC.exchange_rate (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +  // Note the AUTO_INCREMENT
                "origin_currency VARCHAR(255)," +
                "final_currency VARCHAR(255)," +
                "date VARCHAR(255)," +
                "rate_value DOUBLE)";
        databaseClient.execute(sql)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findFirstByOriginCurrencyAndFinalCurrency() {
        ExchangeRate exchangeRate = new ExchangeRate();
        // Do not set the ID manually, let the DB handle it
        exchangeRate.setOriginCurrency("USD");
        exchangeRate.setFinalCurrency("EUR");
        exchangeRate.setDate("2023-08-01");
        exchangeRate.setRateValue(0.85);

        Mono<ExchangeRate> saved = repository.save(exchangeRate);

        StepVerifier.create(saved)
                .expectNextMatches(savedExchangeRate -> 
                     savedExchangeRate.getOriginCurrency().equals("USD") 
                     && savedExchangeRate.getFinalCurrency().equals("EUR"))
                .verifyComplete();

        Mono<ExchangeRate> found = repository.findFirstByOriginCurrencyAndFinalCurrency("USD", "EUR");

        StepVerifier.create(found)
                .expectNextMatches(foundExchangeRate -> 
                        foundExchangeRate.getOriginCurrency().equals("USD") 
                        && foundExchangeRate.getFinalCurrency().equals("EUR"))
                .verifyComplete();
    }
    
	@Test
	void testR2dbcConnection() {
		ConnectionFactory connectionFactory = ConnectionFactories.get("r2dbc:h2:mem:///testdb;DB_CLOSE_DELAY=-1;USER=sa;PASSWORD=;");

		Mono<Connection> connectionMono = Mono.from(connectionFactory.create());

		StepVerifier.create(connectionMono)
				.assertNext(connection -> {
					System.out.println("Conexión exitosa a la base de datos: " + connection.getMetadata().getDatabaseProductName());
				})
				.verifyComplete();
	}
	
	@Test
	void testR2dbcConnectionShowTables() {
	    ConnectionFactory connectionFactory = ConnectionFactories.get("r2dbc:h2:mem:///testdb;DB_CLOSE_DELAY=-1;USER=sa;PASSWORD=;");

	    Flux<String> tablesFlux = Mono.from(connectionFactory.create())
	        .flatMapMany(connection ->
	            Flux.from(connection.createStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'")
	                .execute())
	            .flatMap(result -> result.map((row, rowMetadata) -> row.get(0, String.class)))
	        );

	    StepVerifier.create(tablesFlux)
	        .thenConsumeWhile(
	            tableName -> {
	                System.out.println("Table: " + tableName);
	                return true;
	            },
	            e -> System.out.println("Failed to list tables due to: " + e)
	        )
	        .verifyComplete();
	}

}
