package pl.amw.gdynia.lab6;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;


public class LoadDataBase {

    public void addData (Currency currency){
        Statement status = null;
        Connection connect = null;

        try{
            connect = DriverManager.getConnection("jdbc:sqlite:DB.sqlite");
            status = connect.createStatement();

            String addSQL = "INSERT INTO Wallet (Date, FromCurr, ToCurr, Rate)"+ "Values ('" + currency.getDate() + "','" + currency.getFrom()+ "','" + currency.getTo() + "'," + currency.getRate() + ");";
            status.executeUpdate(addSQL);
            status.close();
            connect.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteData(){
        Statement status = null;
        Connection connect = null;

        try{
            connect = DriverManager.getConnection("jdbc:sqlite:DB.sqlite");
            status = connect.createStatement();
            String clearSQL = "delete" + "from Wallet" + "where FromCurrency = 'EUR';";
            status.executeUpdate(clearSQL);
            status.close();
            connect.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Currency readData(String date, String to) {
        Currency currency = new Currency();
        Statement status = null;
        Connection connect = null;

        String newDate;
        try {
            connect = DriverManager.getConnection("jdbc:sqlite:DB.sqlite");
            status = connect.createStatement();
            String selectSQL = "SELECT * from Wallet " + "where Date =='" + date + "' and ToCurrency == '" + to + "';";
            ResultSet result = status.executeQuery(selectSQL);
            if (result.isClosed()) {
                int day = Integer.parseInt(date.substring(8));
                int month = Integer.parseInt(date.substring(5, 7));
                int year = Integer.parseInt(date.substring(0, 4));
                if (day == 1) {
                    if (month == 1 || month == 2 || month == 4 || month == 6 || month == 8 || month == 9 || month == 11) {
                        day = 31;
                        month -= 1;
                        if (month == 1) {
                            year -= 1;
                        }
                    } else if (month == 3) {
                        if ((year - 2000) % 4 == 0) {
                            day = 29;
                            month -= 1;
                        } else {
                            day = 28;
                            month -= 1;
                        }
                    } else {
                        day = 30;
                        month -= 1;
                    }
                } else {
                    day -= 1;
                }
                if (day < 10 && month < 10) {
                    newDate = year + "-0" + month + "-0" + day;
                } else if (month < 10) {
                    newDate = year + "-0" + month + "-" + day;
                } else if (day < 10) {
                    newDate = year + "-" + month + "-0" + day;
                } else {
                    newDate = year + "-" + month + "-" + day;
                }
                currency = readData(newDate, to);
                currency.setDate(date);
            } else {
                while (result.next()) {
                    currency.setDate(result.getString("Date"));
                    currency.setFrom(result.getString("FromCurrency"));
                    currency.setTo(result.getString("ToCurrency"));
                    currency.setRate(result.getDouble("Rate"));
                }
                    result.close();
                    connect.close();
                    status.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return currency;
    }
}
