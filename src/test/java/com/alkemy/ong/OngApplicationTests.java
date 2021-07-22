package com.alkemy.ong;

import com.alkemy.ong.service.Interface.IEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class OngApplicationTests {
	@Autowired
	private IEmailService emailService;

	@Test
	void contextLoads() throws IOException {
	}

}
