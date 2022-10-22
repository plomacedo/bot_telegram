package br.com.ejprv.moeda;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
@ToString
public class RateCurrency {
    private double cotacaoCompra;
    private double cotacaoVenda;
    private String dataHoraCotacao;
    private String moeda;

    @Override
    public String toString() {

        String[] dateValues = dataHoraCotacao.substring(0, 10).split("-");
        String day = dateValues[2];
        String month = dateValues[1];
        String year = dateValues[0];
        return String.format("Cotação do %s : \n" +
                "* Compra: R$ %s \n" +
                "* Venda: R$ %s \n" +
                "* Data: %s/%s/%s ", moeda, cotacaoCompra, cotacaoVenda, day, month, year);
    }
}
