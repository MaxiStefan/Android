//type... variables -> not specified number of parameters, array of objects
//doInBackground-> SEPARATE thread, in order not to block another thread

package com.example.proiect1;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Network extends AsyncTask<URL,Void, InputStream> {
    public FXRate cv;
    InputStream ist = null;
    static String buffer = new String();

    @Override
    protected InputStream doInBackground(URL... urls) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) urls[0].openConnection();
            conn.setRequestMethod("GET"); //let the connection know that we want to get something from that address
            ist = conn.getInputStream();

            Parsing(ist); //parsare


            /*
            InputStreamReader isr=new InputStreamReader(ist);
            BufferedReader reader=new BufferedReader(isr);
            String line="";
            while((line=reader.readLine())!=null)
            {
                buffer+=line;
            }
            */
        } catch (Exception ex) {
            Log.e("doInBackground", ex.getMessage());
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return ist;
    }

    public List<Currency> parseXML(InputStream inputStream){
        List<Currency> currencies=new ArrayList<>();
        try{
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(inputStream,null);
            int eventName=parser.getEventType();
            while(eventName!=XmlPullParser.END_DOCUMENT){
                String currencyName;
                Double currencyValue;
                String currentTag;
                switch(eventName){
                    case XmlPullParser.START_DOCUMENT:{
                        break;
                    }
                    case XmlPullParser.START_TAG: {
                        currentTag=parser.getName();
                        if(currentTag.equalsIgnoreCase("Rate")){
                            currencyName=parser.getAttributeValue(null,"currency");
                            currencyValue=Double.parseDouble(parser.nextText());
                            Currency currency = new Currency(currencyName, currencyValue);
                            currencies.add(currency);
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        break;
                    }
                }
                eventName=parser.next();
            }
        }catch(XmlPullParserException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }catch(NumberFormatException  e){
            e.printStackTrace();
        }
        return currencies;
    }

    public String extractFXRates()
    {
        try{
            URL url=new URL("https://www.bnr.ro/nbrfxrates.xml");
            execute(url); //launches the thread and will call the doInBackground

            String result=null;
            if(ist!=null)
            {
                result=buffer;
            }
            else
            {
                Thread.sleep(5000);
                result=buffer;
            }

            return result;
        }
        catch(Exception ex)
        {
            Log.e("extractFXRates",ex.getMessage());
            return null;
        }
    }


    public static Node getNodeByName(String nodeName, Node parentNode)
            throws Exception {

        if (parentNode.getNodeName().equals(nodeName)) {
            return parentNode;
        }

        NodeList list = parentNode.getChildNodes();
        for (int i = 0; i < list.getLength(); i++)
        {
            Node node = getNodeByName(nodeName, list.item(i));
            if (node != null) {
                return node;
            }
        }
        return null;

    }

    public static String getAttributeValue(Node node, String attrName) {
        try {
            return ((Element)node).getAttribute(attrName);
        }
        catch (Exception e) {
            return "";
        }
    }

    public void Parsing(InputStream isr)
    {
        try{
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance(); // genereaza obiecte dom din fis xml
            DocumentBuilder db=dbf.newDocumentBuilder(); // instanta dom din fisier xml
            Document domDoc=db.parse(isr); //parsare
            domDoc.getDocumentElement().normalize(); //structurare continut fisier

            cv=new FXRate();
            Node cube= getNodeByName("Cube", domDoc.getDocumentElement()); //parent node second param
            if(cube!=null){
                String data=getAttributeValue(cube,"date");
                cv.setDate(data);
                NodeList childList=cube.getChildNodes(); //includes all the rate nodes
                //navigate through the list
                for(int i=0; i<childList.getLength();i++){
                    Node node=childList.item(i);
                    String attribute=getAttributeValue(node,"currency");
                    if(attribute.equals("EUR"))
                        cv.setEUR(node.getTextContent()); // extracts the value from the node
                    if(attribute.equals("GBP"))
                        cv.setGBP(node.getTextContent());
                    if(attribute.equals("USD"))
                        cv.setUSD(node.getTextContent());
                    if(attribute.equals("XAU"))
                        cv.setXAU(node.getTextContent());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}