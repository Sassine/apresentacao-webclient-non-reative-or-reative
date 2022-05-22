package dev.sasine.apresentacao.webclient.cep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@SpringBootApplication
public class WebclientApplication {

	@Bean
	public WebClient consultaCep(WebClient.Builder builder) {
		return WebClient
				.builder()
				.baseUrl("https://viacep.com.br/ws/")
				.filter(ExchangeFilterFunction.ofResponseProcessor(this::exchangeFilterResponseProcessor))
				.filter(logRequest())
				.filter(logResponse())
				.build();
	}

	@Bean
	public WebClient webClient() {
		var http = HttpClient.create()
		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
		.doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10)).addHandlerLast(new WriteTimeoutHandler(10)));
		
		var connector = new ReactorClientHttpConnector(http.wiretap(true));

		return WebClient
				.builder()
				.baseUrl("http://localhost:8080")
				.clientConnector(connector)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(WebclientApplication.class, args);
	}
	
	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(cRequest -> {
			System.out.println("Request %s %s".formatted(cRequest.method(), cRequest.url()));
			return Mono.just(cRequest);
		});
	}

	private ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(cResponse -> {
			System.out.println("Response status code %s ".formatted(cResponse.statusCode()));
			return Mono.just(cResponse);
		});
	}
	
	private Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
		HttpStatus status = response.statusCode();
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			return response.bodyToMono(String.class).flatMap(body -> Mono.error(new RuntimeException(body)));
		}
		
		return Mono.just(response);
		
		/*****************************************************************************************************
		******** Se a sua versão java for compativel com esta implementação  ... utilize este metodo  ********
		******************************************************************************************************
		
		return switch(response.statusCode()) {
		case INTERNAL_SERVER_ERROR -> response.bodyToMono(String.class).flatMap(body -> Mono.error(new RuntimeException(body)));
		default -> Mono.just(response);
		};  
		
		
		******************************************************************************************************/
	}

}
