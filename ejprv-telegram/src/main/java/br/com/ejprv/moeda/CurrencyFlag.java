package br.com.ejprv.moeda;

public class CurrencyFlag {

    public static String currencies(String currency) {
        switch (currency) {
            case "AUD":
                return "\uD83C\uDDE6\uD83C\uDDFA";
            case "CAD":
                return "\uD83C\uDDE8\uD83C\uDDE6";
            case "CHF":
                return "\uD83C\uDDE8\uD83C\uDDED";
            case "DKK":
                return "\uD83C\uDDE9\uD83C\uDDF0";
            case "EUR":
                return "\uD83D\uDCB6";
            case "GBP":
                return "\uD83C\uDDEC\uD83C\uDDE7";
            case "JPY":
                return "\uD83C\uDDEF\uD83C\uDDF5";
            case "NOK":
                return "\uD83C\uDDF3\uD83C\uDDF4";
            case "SEK":
                return "\uD83C\uDDF8\uD83C\uDDEA";
            case "USD":
                return "\uD83C\uDDFA\uD83C\uDDF8";
            default:
                return "";
        }
    }
}
