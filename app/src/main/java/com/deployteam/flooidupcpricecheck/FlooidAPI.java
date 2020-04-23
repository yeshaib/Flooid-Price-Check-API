package com.deployteam.flooidupcpricecheck;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class FlooidAPI {

    static class flooidAPIitem extends AsyncTask<String, Void, String[]> {

        private Exception exception;
        private String[] ResString;
        private String itemx;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String[] doInBackground(String... ItemUPC) {
            Utils.logd(ItemUPC[0]);
            Utils.logd(ItemUPC[1]);
            String url = "http://"+ ItemUPC[1] +":8080/esi-itemservice/services/ESI/ItemService/rs-itemservice/lookupItem";

            String returnValue = "";
            String username = "user";
            String password = "user";
            String authstring = username + ":" + password;
            byte[] postDataAsByteArray;
            URL obj;
            HttpURLConnection postConnection;
            String itemDescription = "";
            String itemPrice = "";
            String itemID = "";
            String UnitofMeasure = "";
            Boolean responseOK = false;

            //XML Formatted Data
            String XMLdata = "<?xml version='1.0' encoding='UTF-8'?> " +
                    "<ItemLookup xmlns='http://www.nrf-arts.org/IXRetail/namespace/'>" +
                    "<ARTSHeader ActionCode='Lookup' MessageType='Request'>" +
                    "<MessageID>2356</MessageID>" +
                    "<DateTime TypeCode='Message'>2018-06-21T18:13:51.0Z</DateTime>" +
                    "</ARTSHeader>" +
                    "<ItemMaintenance>" +
                    "<BusinessUnit TypeCode='RetailStore'>4571</BusinessUnit>" +
                    "<Item>" +
                    "<ItemID Type='SKU'>" + ItemUPC[0] + "</ItemID>" +
                    "</Item>" +
                    "</ItemMaintenance>" +
                    "<InformationParameters>" +
                    "<InformationParameter>ALL</InformationParameter>" +
                    "</InformationParameters>" +
                    "</ItemLookup>";

            //Clear any previous description, price info or Errors
            //ClearOutputFields();

            //Create data to pass to ESI-Item service
            postDataAsByteArray = XMLdata.getBytes();

            try {
                obj = new URL(url);
                postConnection = (HttpURLConnection) obj.openConnection();
                postConnection.setRequestMethod("POST");

                //setting Authorization header
                String authStr = Base64.getEncoder().encodeToString(authstring.getBytes());
                postConnection.setRequestProperty("Authorization", "Basic " + authStr);
                postConnection.setRequestProperty("Content-Type", "application/xml");
                postConnection.setDoOutput(true);

                OutputStream os = postConnection.getOutputStream();
                os.write(postDataAsByteArray);
                os.flush();
                os.close();
                int responseCode = postConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) //success
                {
                    BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));

                    String response = new String();
                    while ((response = in.readLine()) != null) {
                        returnValue += response;
                    }
                    in.close();

                    //Get Document Builder
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();

                    //Build Document
                    Document document = builder.parse(new InputSource(new StringReader(returnValue)));

                    //Normalize the XML Structure; It's just too important !!
                    document.getDocumentElement().normalize();

                    //Here comes the root node
                    Element root = document.getDocumentElement();
                    //get prefix from root element as the values could be ns2: ns3: etc.
                    String rootname = root.getNodeName();
                    String prefix = rootname.substring(0, 4);

                    NodeList nList = document.getElementsByTagName(prefix + "ItemLookupResponse");

                    for (int temp = 0; temp < nList.getLength(); temp++) {
                        Node node = nList.item(temp);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) node;

                            String responsecode = eElement.getElementsByTagName(prefix + "Response").item(0).getAttributes().getNamedItem("ResponseCode").getNodeValue();
                            System.out.println(responseCode);
                            if (responsecode.equals("OK")) {
                                responseOK = true;
                            } else {
                                System.out.println("Error");
                                //ErrorTextBox.setText(eElement.getElementsByTagName(prefix + "Description").item(0).getTextContent());
                            }

                            if (responseOK) {
                                itemDescription = (eElement.getElementsByTagName(prefix + "Name").item(0).getTextContent());
                                System.out.println("Item Description : " + itemDescription);
                                itemPrice = (eElement.getElementsByTagName(prefix + "ItemPrice").item(0).getTextContent());
                                System.out.println("Item Price : " + itemPrice);
                                itemID = (eElement.getElementsByTagName(prefix + "ItemID").item(0).getTextContent());
                                System.out.println("Item Price : " + itemID);
                                UnitofMeasure = (eElement.getElementsByTagName(prefix + "UnitofMeasure").item(0).getTextContent());
                                System.out.println("Item Price : " + UnitofMeasure);
                            }
                        }
                    }

                    if (responseOK) {
                        if (itemDescription.length() > 0)
                            //ItemDescriptionTextBox.setText(itemDescription);
                            System.out.println(itemDescription);
                        if (itemPrice.length() > 0)
                            //return itemPrice;
                            //ItemPriceTextBox.setText(itemPrice);
                            System.out.println(itemPrice);
                    } else {
                        String error;
                        if (itemDescription.length() <= 0)
                            //if (ErrorTextBox.getCharCount() == 0)
                            error = "ITEM NOT FOUND!!!";

                        //ErrorTextBox.setText("ITEM NOT FOUND!!!");
                    }
                } else {
                    if (responseCode == 503)
                        //ErrorTextBox.setText("The remote server returned an error: (503) Server Unavailable.");
                        System.out.println("The remote server returned an error: (503) Server Unavailable.");
                    else
                        //ErrorTextBox.setText("Connection Failed!");
                        System.out.println("Connection Failed!");
                }
            } catch (MalformedURLException e1) {
                //e1.printStackTrace();
                //ErrorTextBox.setText(e1.getMessage());
                System.out.println(e1.getMessage());
            } catch (IOException e) {
                //e.printStackTrace();
                //ErrorTextBox.setText(e.getMessage());
                System.out.println(e.getMessage());
            } catch (ParserConfigurationException e) {
                //e.printStackTrace();
                //ErrorTextBox.setText(e.getMessage());
                System.out.println(e.getMessage());
            } catch (SAXException e) {
                //e.printStackTrace();
//			ErrorTextBox.setText(e.getMessage());
                System.out.println(e.getMessage());
            }

            ResString = new String[]{itemDescription, itemPrice, itemID, UnitofMeasure, XMLdata};

            return ResString;
        }
    }
}
