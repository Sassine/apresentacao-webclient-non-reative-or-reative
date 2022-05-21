package dev.sasine.apresentacao.webclient.cep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class WebclientApplication {

	
	@Bean
	public WebClient consultaCep(WebClient.Builder builder) {
		return builder
				.baseUrl("https://viacep.com.br/ws/")
				.build();
	}
	
	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder
				.baseUrl("http://localhost:8080/")
				.build();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(WebclientApplication.class, args);
	}

}
