package com.java.cources;

import com.java.cources.parser.AudioFileParser;
import com.java.cources.reader.S3StorageReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResourceProcessorApplicationTests {



	@Autowired
	private AudioFileParser parser;

	@Test
	void contextLoads() {
		S3StorageReader reader = new S3StorageReader("http://localhost:9000", "minio_user", "minio_password");
		String metadata = parser.mapToSongJson(parser.parseMetadata(reader.readFile("test-bucket", "tjeFMCAo")));
		System.out.println(metadata);
	}

}
