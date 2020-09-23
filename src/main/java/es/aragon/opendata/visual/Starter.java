package es.aragon.opendata.visual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableAutoConfiguration
@ImportResource("classpath:/META-INF/application-context.xml")
public class Starter {

	Starter(){
		//DEFAULT CONSTRUCTOR
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Starter.class, args);
	}
}
