package eesti.frontend.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		/* ConfigurableApplicationContext context = */ SpringApplication.run(DemoApplication.class, args);
		
		// ItemsClient greetingClient = context.getBean(ItemsClient.class);
		 //Flux<Itemdetails> 	 greetingClient.getItems();
		// We need to block for the content here or the JVM might exit before the message is logged
	//	System.out.println(">> message = " + greetingClient.getMessage());
		
		
		//WebClient webClient = WebClient.create("http://localhost:8080");

/*		Mono<Itemdetails> createdEmployee = 
										 * webClient.post() .uri("/items") .header(HttpHeaders.CONTENT_TYPE,
										 * MediaType.APPLICATION_JSON_VALUE) .body(Mono.just(emp), Itemdetails.class)
										 * .retrieve() .bodyToMono(Itemdetails.class);
										 
		webClient.get()
	    .uri("/items")
	    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
	    .retrieve()
		.bodyToMono(Itemdetails.class);
		
		System.out.println(createdEmployee.block());
	*/
	}
	@Bean
	public WebClient.Builder getWebClientBuilder() {

		return WebClient.builder();
	}

}
