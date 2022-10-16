package br.com.ejprv.moeda;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RateCurrency {
    private double cotacaoCompra;
    private double cotacaoVenda;
    private String dataHoraCotacao;
    private String moeda;
}
