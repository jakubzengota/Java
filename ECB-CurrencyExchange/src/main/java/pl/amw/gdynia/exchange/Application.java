package pl.amw.gdynia.lab6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application extends ExportXML {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}