package ru.blekzet.pibot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.blekzet.pibot.service.parser.JoyreactorParser;

@SpringBootTest
class PiBotApplicationTests {
	@Value("${joyUrl}")
	private String joyreactorUrl;

	@Test
	void contextLoads() {
		JoyreactorParser joyreactorParser = new JoyreactorParser(joyreactorUrl);
		System.out.println(joyreactorParser.getRandomPictures().toString());
	}

}
