package com.api.condutor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@org.junit.jupiter.api.Disabled("Disabled since DB is not available in CI/test environment without testcontainers")
class CondutorApplicationTests {

	@Test
	void contextLoads() {
	}

}
