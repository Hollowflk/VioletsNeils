package com.alexIT.VioletsNeils;

import com.alexIT.VioletsNeils.bot.NeilBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@SpringBootApplication
public class VioletsNeilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(VioletsNeilsApplication.class, args);
		StandardEnvironment env = new StandardEnvironment();
		try (TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication()) {
			application.registerBot(env.getProperty("token"), new NeilBot(getTelegramClient(env)));
			log.info("Бот запущен!");
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
	}

	@Bean
	public static TelegramClient getTelegramClient(Environment env) {
		return new OkHttpTelegramClient(env.getProperty("token"));
	}
}
