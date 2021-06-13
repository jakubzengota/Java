package pl.amw.gdynia.lab6;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDate;


@Controller
public class CurrencyController extends LoadDataBase {
    public CurrencyController()throws Exception{

    }

    protected static Currency currencyToEuro(Currency currency){
        double rate = 1/ currency.getRate();
        Currency swap = new Currency(currency.getDate(), currency.getTo(), currency.getFrom(),rate);
        return swap;
    }
    protected static Currency currencyToCurrency(Currency fromCurrency, Currency toCurrency){
        Currency swap;
        double rate = toCurrency.getRate()/ fromCurrency.getRate();
        swap = new Currency(fromCurrency.getDate(), fromCurrency.getTo(), toCurrency.getTo(), rate);
        return swap;
    }

    LocalDate today = LocalDate.now();
    String string2Date = today.toString();

    @GetMapping("/service/currency/rate/{currencyFrom}/{currencyTo}")
    @ResponseBody
    public Currency endpointtoday(@PathVariable String currencyFrom, @PathVariable String currencyTo){

        Currency currency = new Currency();
        if(currencyFrom.equals("EUR")){
            currency = readData(string2Date,currencyTo);
        }else if(currencyTo.equals("EUR")){
            currency = readData(string2Date,currencyFrom);
            currency = currencyToEuro(currency);
        }else {
            Currency exFrom = readData(string2Date,currencyFrom);
            Currency exTo = readData(string2Date,currencyTo);
            currency = currencyToCurrency(exFrom,exTo);
        }
        return currency;
    }

    @GetMapping("/service/currency/rate/{currencyFrom}/{currencyTo}/{date}")
    @ResponseBody
    public Currency endpointhistorical(@PathVariable String currencyFrom, @PathVariable String currencyTo, @PathVariable String date){
        Currency currency = new Currency();
        if(currencyFrom.equals("EUR")){
            currency = readData(date,currencyTo);
        }else if(currencyTo.equals("EUR")){
            currency = readData(date,currencyFrom);
            currency = currencyToEuro(currency);
        }else {
            Currency exFrom = readData(date,currencyFrom);
            Currency exTo = readData(date,currencyTo);
            currency = currencyToCurrency(exFrom,exTo);
        }
        return currency;
    }
}
