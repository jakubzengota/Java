package pl.amw.gdynia.lab6;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;

import java.time.LocalTime;
import java.time.LocalDate;

public class ExportXML extends LoadDataBase {
    Document  document;
    LocalDate today = LocalDate.now();

    String daytime= LocalTime.now().toString();
    String day = LocalDate.now().getDayOfWeek().toString();

    public int exportXMLTo(){
        Currency currency;
        String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        int responseCode;
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(new URL(url).openStream());
            responseCode = 200;
        }catch(Exception e){
            e.printStackTrace();
            responseCode = 404;
        }
        NodeList list = document.getElementsByTagName("Cube");
        Node nodeTime = list.item(1);
        Element time = (Element) nodeTime;
        if(time.getAttribute("time").equals(today.toString())){
            for(int i = 2; i < list.getLength();i++){
                Node node = list.item(i);
                Element element = (Element) node;
                currency = new Currency(today.toString(), "EUR", element.getAttribute("currency"), Double.parseDouble(element.getAttribute("rate")) );
                addData(currency);
            }
        }
        return responseCode;
    }
    public void readXMLHistorical(){
        deleteData();
        Currency currency;
        String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.xml";
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(new URL(url).openStream());
        }catch(Exception e){
            e.printStackTrace();
        }
        NodeList list = document.getElementsByTagName("Cube");
        String time = LocalDate.now().toString();
        for(int i = 1; i < list.getLength();i++){
            Node nodeTime = list.item(i);
            Element element = (Element) nodeTime;
            if(element.hasAttribute("time")){
                time = element.getAttribute("time");
            }else {
                currency = new Currency(time, "EUR", element.getAttribute("currency"), Double.parseDouble(element.getAttribute("rate")) );
                addData(currency);
            }
        }
    }
}