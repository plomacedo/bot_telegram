package br.com.ejprv.moeda;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
        currenciesResult += "Ex.: cotação USD";
        for (Moeda moeda : listMoedas) {
            currenciesResult += moeda.getSimbolo() + " - " + moeda.getNomeFormatado() + "\n";
        }
        return currenciesResult;
    }

    public static String getRate(String currency, String apiUrlRate) {

        System.out.println(LocalDateTime.now().getDayOfWeek() );
        SimpleDateFormat formato = new SimpleDateFormat("MM-dd-yyyy");

        var rateUrl = apiUrlRate.replace(":moeda", currency).replace(":data", formato.format(new Date()));
        System.out.println(rateUrl);
        JsonNode json = Unirest.get(rateUrl).asJson().getBody();
        var result = json.getObject().getJSONArray("value");
        var rate = result.get(0);
        System.out.println(rate);
        Gson gson = new Gson();
        RateCurrency rateCurrency = gson.fromJson(rate.toString(), RateCurrency.class);
        rateCurrency.setMoeda("");
        return rateCurrency.toString();
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
