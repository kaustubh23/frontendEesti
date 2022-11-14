package eesti.frontend.demo.ssf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import eesti.assignment.model.Itemdetails;
import reactor.core.publisher.Flux;

@Component
public class ItemsClient {

	private final WebClient client;

	@Autowired
	public ItemsClient() {
		this.client = WebClient.create("http://localhost:8080");
	}

	public Flux<Itemdetails> getItems() {

		return client.get().uri("/items").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve()
				.bodyToFlux(Itemdetails.class);
	}

	public Flux<Itemdetails> buyItem(List<Itemdetails> value) {
		return client.put().uri("/items/buy").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.bodyValue(value).retrieve().bodyToFlux(Itemdetails.class);

	}

}
