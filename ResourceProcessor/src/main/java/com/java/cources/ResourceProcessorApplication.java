package com.java.cources;

import com.java.cources.reader.S3StorageReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceProcessorApplication.class, args);

	}


}
