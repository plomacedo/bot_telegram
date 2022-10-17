#EM CONSTRUÇÃO

# MBA Fullstack FIAP - Turma 1SCJR

+ RM346614 - Ebertt Costa dos Santos 
+ RM346139 - Juliana Mota Carneiro 
+ RM347401 - Pamela Lais Oliveira Macedo 
+ RM346573 - Rafael Luiz Ross de Moura 
+ RM346746 - Vitor Souza Alves 

### 🔧 Instalação
Para utilizar nosso bot, acesse **_[Turma 1SCJR - galerinha de java](https://t.me/Ejprv_bot)_**.
<br> Após a instalação do Git em sua máquina, clone o repositório https://github.com/plomacedo/bot_telegram</br>
<br> Tenha em mãos um Token Telegram válido, e o atribua no BOT_TOKEN. A classe DataBot possui as informações utilziadas para a criação do Bot através do BotFather.</br>

```

public class DataBot {
	public static final String BOT_TOKEN = "";
	public static final String BOT_USER_NAME = "Ejprv_bot";
}
```
### 🛠️ Desenvolvimento

```
try {
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(new Bot());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
```

```
 public class Bot extends TelegramLongPollingBot
```

```
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

```
```
	private SendMessage reply(Update update) {
		var message = update.getMessage().getText().toLowerCase();
		var chatId = update.getMessage().getChatId().toString();

		var reply = "";


		if (message.startsWith("ola") || message.startsWith("olá") || message.startsWith("oi")) {
			reply = "Olá, Seja bem vindo(a) ao bot da turma 1SCJR!";
		} else if (message.startsWith("quem é você") || message.startsWith("quem e voce")) {
			reply = getGroupInfo();
		}else if ("data".equals(message)||"dia".equals(message)) {
			reply = "A data de hoje é " + getData();
		} else if (message.startsWith("hora")) {
			reply = "A hora atual é " + getHora();
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
```
