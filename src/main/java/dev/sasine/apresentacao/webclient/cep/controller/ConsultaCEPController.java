/**
 * @author Sassine El-Asmar (github.com/Sassine)
 * 
 */
package dev.sasine.apresentacao.webclient.cep.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import dev.sasine.apresentacao.webclient.cep.dto.CepDTO;

@RestController
@RequestMapping("/cep")
public class ConsultaCEPController {

	@Autowired
	private WebClient consultaCep;

	@Autowired
	private WebClient webClient;
	
	@GetMapping
	public Map<String, String> getLinks() {
		return Map.of("Normal","/cep/17015311", "Reativo","/cep/reative/17015311");
	}

	@GetMapping("/reative/{cep}")
	public String getCepReative(@PathVariable String cep) {
		
		webClient
		.method(HttpMethod.GET)
		.uri("cep/%s".formatted(cep))
		.retrieve()
		.bodyToMono(String.class)
		.subscribe(i -> {
			System.out.println("A request retornou agora ;) ");
		});
		
		System.out.println("getFixedCepReative completa");
	
		return "Chamada realizada ;)";
	}

	@GetMapping("/{cep}")
	public CepDTO findCep(@PathVariable String cep) {
		
		System.out.println("find Cep foi chamado %s".formatted(cep));
		
		return consultaCep
				.get()
				.uri(uriBuilder -> uriBuilder.path("/{cep}/json/").build(cep))
				.retrieve()
				.bodyToMono(CepDTO.class)
				.block();
	}

}
