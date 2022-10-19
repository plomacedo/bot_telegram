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
<br> Tenha em mãos um Token Telegram válido, e o atribua no BOT_TOKEN, dentro do arquivo de constantes env.properties.exemplo, assim como o seu BOT_USER_NAME.
Neste mesmo arquivo, consta a url da API utilizada para o bot de tempo (WEATHER_API_URL),  a constante que deverá armazenar o token gerado para a API (WEATHER_API_TOKEN), assim como as URLs do banco central que retorna todas as moedas disponíveis no Banco Central (LIST_URRENCY_URL) e retorno da cotação do dia (CURRENCY_URL)

Para a execução do projeto, renomeie o arquivo apenas para env.properties


### 🛠️ Desenvolvimento

Para o gerenciamento de dependências, utilizamos Maven, onde adicionamos as dependências do telegram e do spring boot no arquivo pom.xml

```
 <dependencies>
  	<dependency>
  		<groupId>org.telegram</groupId>
		<artifactId>telegrambots</artifactId>
		<version>5.3.0</version>
  	</dependency>
  </dependencies>
```

Para inicializar a aplicação, no @bean do spring boot, o Environment irá buscar os dados no env.properties através da instância do BotDetails, e iniciar a chamada da api
```
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
```
No BotDetails, ele irá carregar as informações contidas no arquivo de propriedades env.properties
```
 public BotDetails(Environment environment) {
        this.botToken = environment.getProperty("telegram.token");
        this.botUserName = environment.getProperty("telegram.username");
        this.weatherApiUrl = environment.getProperty("telegram.weather.url");
        this.weatherApiToken = environment.getProperty("telegram.weather.token");
        this.currencyListUrl = environment.getProperty("telegram.currency.list_url");
        this.currencyRateUrl = environment.getProperty("telegram.currency.rate_url");
    }
    
```

Em nossa classe Bot, extendemos a classe TelegramLongPollingBot que é responsável por pegar periodicamente dados do telegram de forma automática. 
```
 public class Bot extends TelegramLongPollingBot
```

Esta API implementa 3 métodos: onUpdateReceived, getBotUsername e getBotToken.

O método onUpdateReceived é responsável por escutar as requisições das mensagens recebidas pelo bot. Através desse método, o Bot verifica se é recebido uma mensagem de texto, e caso positivo, criamos a mensagem através do objeto SendMessage reply.

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
```
Os métodos getBotUsername e getBotToken são responsáveis por pegar o token e o nome do bot criados para o nosso projeto que estão armazenados como constantes na classe DataBot
```
public String getBotUsername() {
		return this.botDetails.getBotUserName();
	}

	public String getBotToken() {
		return this.botDetails.getBotToken();
	}

```
Montamos a resposta a ser enviada para o usuário através do objeto SendMessage reply
```
	private SendMessage reply(Update update) {

		var chatId = update.getMessage().getChatId().toString();

		return SendMessage.builder()
				.text(ResponseHandler.response(update, botDetails))
				.chatId(chatId)
				.build();
	}
```

A classe ResponseHandler irá receber mensagem e irá fazer os comparativos para determinar qual o tipo de resposta o usuário irá receber.

```
if (message.startsWith("ola")) {
            String firstName;
            if (update.getMessage().getFrom() != null) {
                firstName = " " + update.getMessage().getFrom().getFirstName();
            } else {
                firstName = "";
            }
            reply = Greetings.initialGreeting(firstName);
        } else if (message.startsWith("cotacao")) {
            reply = CurrencyService.response(message, botDetails.getCurrencyListUrl(), botDetails.getCurrencyRateUrl());
        }
        else if (message.startsWith("clima")) {
            reply = Weather.response(message, botDetails.getWeatherApiUrl(), botDetails.getWeatherApiToken());
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
        return reply;
```

A classe Greetings, irá retornar um cumprimento para o usuário utilizando o primeiro nome do user, e identifica o período do dia (Bom dia, Boa Tarde e Boa Noite)
```
    public static String initialGreeting(String nome) {
            return  String.format("Olá%s, %s! \nSeja bem vindo(a) ao bot da turma 1SCJR!", nome, periodo());
    }

    private static String periodo() {
        LocalTime now = LocalTime.now();
        if ((!now.isBefore(MORNING)) && now.isBefore(AFTER_NOON)) {
            return "bom dia";
        } else if ((!now.isBefore(AFTER_NOON)) && now.isBefore(EVENING)) {
            return "boa tarde";
        } else {
            return "boa noite";
        }
    }
}
```

