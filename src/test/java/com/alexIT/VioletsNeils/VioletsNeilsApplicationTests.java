package com.alexIT.VioletsNeils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

class VioletsNeilsApplicationTests {

	@Test
	void test() {
		String duration = "02:00:00";
		String hour = duration.substring(1,2);
		System.out.println("Hour: " + hour);
		String minutes = duration.substring(3,5);
		System.out.println("Minutes: " + minutes);
		String durationString = String.format("PT%sH%sM", hour, minutes);
		Duration parseDuration = Duration.parse(durationString);

		System.out.println("Строка:" + durationString);
		System.out.println("Duration:" + parseDuration.getSeconds());
	}

}
