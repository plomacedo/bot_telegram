package br.com.ejprv.moeda;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.cglib.core.Local;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrencyService {

    public static String listCurrencies(String apiUrlList){
        JsonNode json = Unirest.get(apiUrlList).asJson().getBody();
        var currencies = json.getObject().getJSONArray("value");
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Moeda>>(){}.getType();
        List<Moeda> listMoedas = gson.fromJson(currencies.toString(),listType);
        String currenciesResult = "Digite: cotação [simbolo_moeda] \n\n";
        currenciesResult += "Ex.: cotação USD \n";
        for (Moeda moeda : listMoedas) {
            currenciesResult += CurrencyFlag.currencies(moeda.getSimbolo()) + moeda.getSimbolo() + " - " + moeda.getNomeFormatado() + "\n";
        }
        return currenciesResult;
    }

    public static String getRate(String currency, String apiUrlRate) {

        System.out.println(LocalDateTime.now().getDayOfWeek().getValue() );

        LocalDate day = LocalDate.now();
        if (day.getDayOfWeek().getValue() > 5) {
            day = LocalDate.now().minusDays(day.getDayOfWeek().getValue() == 6 ? 1l : 2l);
        }

        System.out.println(day);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        var rateUrl = apiUrlRate.replace(":moeda", currency).replace(":data", formatter.format(day));
        System.out.println(rateUrl);
            JsonNode json = Unirest.get(rateUrl).asJson().getBody();

        if (json == null) {
            return String.format("Nenhuma cotação encontrada para: %s!", currency);
        }
        var result = json.getObject().getJSONArray("value") ;
        if (result.length() > 0) {
            var rate = result.get(0);
            System.out.println(rate);
            Gson gson = new Gson();
            RateCurrency rateCurrency = gson.fromJson(rate.toString(), RateCurrency.class);
            System.out.println(rate);
            rateCurrency.setMoeda(currency);
            return rateCurrency.toString();

        } else {
            return String.format("Nenhuma cotação encontrada para: %s!", currency);
        }
    }
    public static String response(String message, String apiUrlList, String apiUrlRate) {
        String[] currencyReq = message.split(" ");
        if (currencyReq.length == 1) {
            return listCurrencies(apiUrlList);
        } else {
            return getRate(currencyReq[1].toUpperCase(), apiUrlRate);
        }
    }
}
