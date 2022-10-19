package br.com.ejprv;

import lombok.Getter;
import org.springframework.core.env.Environment;

@Getter
public class BotDetails {

    private final String botToken;
    private final String botUserName;
    private final String weatherApiUrl;
    private final String weatherApiToken;

    private final String currencyListUrl;

    private final String currencyRateUrl;

    public BotDetails(Environment environment) {
        this.botToken = environment.getProperty("telegram.token");
        this.botUserName = environment.getProperty("telegram.username");
        this.weatherApiUrl = environment.getProperty("telegram.weather.url");
        this.weatherApiToken = environment.getProperty("telegram.weather.token");
        this.currencyListUrl = environment.getProperty("telegram.currency.list_url");
        this.currencyRateUrl = environment.getProperty("telegram.currency.rate_url");
    }
}
