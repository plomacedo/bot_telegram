package br.com.ejprv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@Slf4j
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner (Environment environment) {

		return args -> {
			try {
				TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
				telegramBotsApi.registerBot(new Bot(new BotDetails(environment)));
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		};
	}

}
