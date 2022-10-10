package com.polaroid.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class PolaroidApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PolaroidApplication.class, args);
		
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		//return super.configure(builder);
		return application.sources(PolaroidApplication.class);
	}

}
