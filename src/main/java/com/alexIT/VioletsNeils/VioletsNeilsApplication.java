package com.alexIT.VioletsNeils;

import com.alexIT.VioletsNeils.bot.NeilBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@SpringBootApplication
public class VioletsNeilsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(VioletsNeilsApplication.class, args);
        try (TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication()) {
            NeilBot neilBot = context.getBean(NeilBot.class);
            String token = context.getEnvironment().getProperty("token");
            application.registerBot(token, neilBot);
            log.info("Бот запущен!");
            Thread.currentThread().join();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    @Bean
    public static TelegramClient getTelegramClient(Environment env) {
        return new OkHttpTelegramClient(env.getProperty("token"));
    }
}
