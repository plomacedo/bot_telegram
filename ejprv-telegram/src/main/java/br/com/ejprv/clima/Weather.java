package br.com.ejprv.clima;

import br.com.ejprv.moeda.RateCurrency;
import com.google.gson.Gson;
import kong.unirest.GenericType;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;


import java.time.LocalDate;
import java.util.List;
@ToString
@Getter
@Setter

public class Weather {

    private Number temp;
    private String date;
    private String time;
    private String conditionCode;
    private String description;
    private String currently;
    private String city;
    private Number humidity;
    private Number cloudiness;
    private Number rain;
    private String windSpeedy;
    private Number windDirection;
    private String sunrise;
    private String sunset;
    private String condition_slug;
    private String city_name;

    public static String response(String message, String apiUrl, String apiToken) {
        var messageReq = message.split(" ");
        if (!messageReq[0].equalsIgnoreCase("clima")) {
            return "Valor incorreto! \nDigite: clima [Nome da Cidade]";
        }

        var city = ArrayUtils.remove(messageReq, 0);
        var url = apiUrl.replace(":token", apiToken).replace(":city", String.join(" ", city));

        JsonNode json = Unirest.get(url).asJson().getBody();
        var result = json.getObject();
        if (!Boolean.parseBoolean(result.getString("valid_key"))) {
            return "Nenhum dado encontrado para cidade digitada!";
        }
        var jsonWeather = result.getJSONObject("results");

        Gson gson = new Gson();
        Weather weatherResult = gson.fromJson(jsonWeather.toString(), Weather.class);
        System.out.println(weatherResult);
        return weatherResult.toString();
    }


    @Override
    public String toString() {

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
