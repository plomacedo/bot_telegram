# MBA Fullstack FIAP - Turma 1SCJR

+ RM346614 - Ebertt Costa dos Santos 
+ RM346139 - Juliana Mota Carneiro 
+ RM347401 - Pamela Lais Oliveira Macedo 
+ RM346573 - Rafael Luiz Ross de Moura 
+ RM346746 - Vitor Souza Alves 

### 🔧 Instalação
<br> Após a instalação do Git em sua máquina, clone o repositório https://github.com/plomacedo/bot_telegram</br>
<br> Tenha em mãos um Token Telegram válido, e o atribua no BOT_TOKEN, dentro do arquivo de constantes env.properties.exemplo, assim como o seu BOT_USER_NAME.
Neste mesmo arquivo, consta a url da API utilizada para o bot de tempo (WEATHER_API_URL),  a constante que deverá armazenar o token gerado para a API (WEATHER_API_TOKEN), assim como as URLs do banco central que retorna todas as moedas disponíveis no Banco Central (LIST_URRENCY_URL) e retorno da cotação do dia (CURRENCY_URL)

Para a execução do projeto, renomeie o arquivo apenas para env.properties


### 🤖 Interagindo com o Bot: 
     1. Olá
     2. Clima
     3. Quem e voce
     4. Data
     5. Hora
     6. /help
     
**Clima em** São Paulo, SP:
* 🌡️ **Temperatura:** 19ºC.
* 📆 **Dia:** 15/10/2022.
* 🕛 **Hora:** 16h53.
* 🏙️ **Clima:** Chuvas esparsas.
* 💧 **Umidade:** 89%.
* 💨 **Vento:** null (se tiver uma resposta, poderia ser por exemplo: 💨 **Vento:** 5.14 km/h)
* 🌄 **Amanhecer:** 05:33 am. 
* 🌇 **Pôr do Sol:** 06:17 pm.

Digite: cotação simbolo_moeda
Ex: **cotação USD** 

+ USDAUD - Dólar Australiano 
+ CAD - Dólar Canadense 
+ CHF - Franco Suiço
+ DKK - Coroa Dinamarquesa
+ EUR - Euro
+ GBP - Libra Esterlina
+ JPY - Iene
+ NOK - Coroa Norueguesa
+ SEK - Coroa Sueca
+ USD - Dólar Estadunidense

🤑 Cotação do **DÓLAR CANADENSE**: 
* **Comercial:** $ 3.3702 
* **Turismo:** $ 3.3716
* **Data:** 2022-10-05 13:10:18

### 🛠️ Desenvolvimento

Para o gerenciamento de dependências, utilizamos Maven, onde adicionamos as dependências do telegram e do spring boot no arquivo pom.xml

```xml
 <dependencies>
  	<dependency>
  		<groupId>org.telegram</groupId>
		<artifactId>telegrambots</artifactId>
		<version>5.3.0</version>
  	</dependency>
  </dependencies>
```

Para inicializar a aplicação, no @bean do spring boot, o Environment irá buscar os dados no env.properties através da instância do BotDetails, e iniciar a chamada da api
```java
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
```java
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
```java
 public class Bot extends TelegramLongPollingBot
```

Esta API implementa 3 métodos: onUpdateReceived, getBotUsername e getBotToken.

O método onUpdateReceived é responsável por escutar as requisições das mensagens recebidas pelo bot. Através desse método, o Bot verifica se é recebido uma mensagem de texto, e caso positivo, criamos a mensagem através do objeto SendMessage reply.

```java
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
```java
public String getBotUsername() {
		return this.botDetails.getBotUserName();
	}

	public String getBotToken() {
		return this.botDetails.getBotToken();
	}

```
Montamos a resposta a ser enviada para o usuário através do objeto SendMessage reply
```java
	private SendMessage reply(Update update) {

		var chatId = update.getMessage().getChatId().toString();

		return SendMessage.builder()
				.text(ResponseHandler.response(update, botDetails))
				.chatId(chatId)
				.build();
	}
```

A classe ResponseHandler irá receber mensagem e irá fazer os comparativos para determinar qual o tipo de resposta o usuário irá receber.

```java
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
```java
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
Na classe Weather, através do método get, passando a url e a cidade desejada, é retornado os dados de  temperatura, data, hora, condições climáticas, período do dia, cidade, umidade, vrlocidade do vento, horário em que o sol nasce e horario que o sol se põe da cidade inserida:

```java
   var url = apiUrl.replace(":token", apiToken).replace(":city", String.join(" ", city));
    JsonNode json = Unirest.get(url).asJson().getBody();
```
```java
  return String.format("*Clima em %s:\n" +
                "* \uD83C\uDF21️ *Temperatura:* %s ºC\n" +
                "* \uD83D\uDCC6 *Dia:* %s .\n" +
                "* \uD83D\uDD5B *Hora:* %s \n" +
                "* \uD83C\uDFD9️ *Clima:* %s \n" +
                "* \uD83D\uDCA7 *Umidade:* %s \n" +
                "* \uD83D\uDCA8 *Vento:* %s \n" +
                "* \uD83C\uDF04 *Amanhecer:* %s \n" +
                "* \uD83C\uDF07 *Pôr do Sol:* %s ", city_name, temp, date, time, description, humidity, windSpeedy == null ? "-" : windSpeedy, sunrise, sunset);
    }
}
```
