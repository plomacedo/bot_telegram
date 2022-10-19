package br.com.ejprv;


import br.com.ejprv.clima.Weather;
import br.com.ejprv.moeda.CurrencyService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class ResponseHandler {

    public static String response(Update update, BotDetails botDetails) {
        String reply = "";
        var message = deAccent(update.getMessage().getText().toLowerCase());

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
    }

    private static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    private static String getGroupInfo() {
        return "Somos os alunos da turma 1SCJR do MBA de Desenvolvimento FullStack da FIAP: " +
                "\n\nRM346614 - Ebbert Costa dos Santos " +
                "\nRM346139 - Juliana Mota Carneiro" +
                "\nRM347401 - Pamela Lais Oliveira Macedo" +
                "\nRM346573 - Rafael Luiz Ross de Moura " +
                "\nRM346746 - Vitor Souza Alves";
    }

    private static String getData() {
        var formatter = new SimpleDateFormat("dd/MM/yyyy");
        return "A data atual é: " + formatter.format(new Date());
    }

    private static String getHora() {
        var formatter = new SimpleDateFormat("HH:mm:ss");
        return "A hora atual é: " + formatter.format(new Date());
    }

}
