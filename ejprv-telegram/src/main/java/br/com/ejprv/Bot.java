package br.com.ejprv;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot{

	private final BotDetails botDetails;

	public Bot(BotDetails botDetails) {
		this.botDetails = botDetails;
	}

	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			var message = reply(update);
			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}

	}

	public String getBotUsername() {
		return this.botDetails.getBotUserName();
	}

	public String getBotToken() {
		return this.botDetails.getBotToken();
	}

	private SendMessage reply(Update update) {

		var chatId = update.getMessage().getChatId().toString();

		return SendMessage.builder()
				.text(ResponseHandler.response(update, botDetails))
				.chatId(chatId)
				.build();
	}
}


