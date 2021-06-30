package com.okta.developer.notes

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [DemoApplication::class, MockSecurityConfiguration::class])
class DemoApplicationTests {

	@Test
	fun contextLoads() {
	}

}
