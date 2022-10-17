import java.text.SimpleDateFormat;
import java.util.Date;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot{

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
		return DataBot.BOT_USER_NAME;
	}

	public String getBotToken() {
		return DataBot.BOT_TOKEN;
	}

	private SendMessage reply(Update update) {
		var message = update.getMessage().getText().toLowerCase();
		var chatId = update.getMessage().getChatId().toString();

		var reply = "";


		if (message.startsWith("ola") || message.startsWith("olá") || message.startsWith("oi")) {
			reply = "Olá, Seja bem vindo(a) ao bot da turma 1SCJR!";
		} else if (message.startsWith("quem é você") || message.startsWith("quem e voce")) {
			reply = getGroupInfo();
		}else if ("data".equals(message)) {
			reply = getData();
		} else if (message.startsWith("hora")) {
			reply = getHora();
		} else if (message.startsWith("/help")) {
			reply = "Utilize um dos comandos:\nolá\ndata\nhora\nquem é você?";
		} else {
			reply = "Não entendi!\nDigite /help para ver os comandos disponíveis.";
		}

		return SendMessage.builder()
				.text(reply)
				.chatId(chatId)
				.build();
	}

	private String getGroupInfo() {
		return "Somos os alunos da turma 1SCJR do MBA de Desenvolvimento FullStack da FIAP: " +
				"\n\nRM346614 - Ebertt Costa dos Santos " +
				"\nRM346139 - Juliana Mota Carneiro" +
				"\nRM347401 - Pamela Lais Oliveira Macedo" +
				"\nRM346573 - Rafael Luiz Ross de Moura " +
				"\nRM346746 - Vitor Souza Alves";
	}

	private String getData() {
		var formatter = new SimpleDateFormat("dd/MM/yyyy");
		return "A data atual é: " + formatter.format(new Date());
	}

	private String getHora() {
		var formatter = new SimpleDateFormat("HH:mm:ss");
		return "A hora atual é: " + formatter.format(new Date());
	}


}


