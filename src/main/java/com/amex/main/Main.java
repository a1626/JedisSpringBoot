package com.amex.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {

	final static Logger LOGGER = LogManager.getFormatterLogger();
	public static void main(String[] args) {
		ApplicationContext context=SpringApplication.run(Main.class, args);
		String[] beans=context.getBeanDefinitionNames();
		for (String bean : beans) {
			LOGGER.info(bean);
		}
	}
}
