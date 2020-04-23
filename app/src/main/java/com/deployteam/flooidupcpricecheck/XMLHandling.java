package com.deployteam.flooidupcpricecheck;

import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XMLHandling {

    public static void XMLHandlingLogD (String LogMessage){
        Log.d("Log D", LogMessage);
        Log.v("Log V", LogMessage);

    }

    public static void CreateXMLFile (String xmlServerIP) {

    }

    public static void CreateXMLSettingsFile(String xmlServerIP) {

        try {

            //File xmlFullPath = new File(Environment.getFilesDir+"/Settings.xml" );
            File xmlFilePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Settings.xml");
            xmlFilePath.createNewFile();
            FileOutputStream fileos = new FileOutputStream(xmlFilePath);

            FileWriter writer = new FileWriter(xmlFilePath, true);

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();


            // root element
            Element root = document.createElement("ex-e-cu-ter");
            document.appendChild(root);

            // Attribute of child Element
            Element ServerSettings = document.createElement("ServerSettings");
            root.appendChild(ServerSettings);

            //ServerIP.setAttribute("name", "value");

            Element ServerIP = document.createElement("ServerIP");
            System.out.println(xmlServerIP);
            ServerIP.appendChild(document.createTextNode(xmlServerIP));
            ServerSettings.appendChild(ServerIP);

            // email element
            Element email = document.createElement("email");
            email.appendChild(document.createTextNode("admin@deployteam.com"));
            root.appendChild(email);

            // employee element
            //Element serverip = document.createElement("Server");
            //root.appendChild(serverip);



            // set an attribute to staff element
            //Attr attr = document.createAttribute("id");
            //attr.setValue("10");
            //employee.setAttributeNode(attr);

            //you can also use staff.setAttribute("id", "1") for this

            // firstname element
            //Element dbusername = document.createElement("dbusername");
            //firstName.appendChild(document.createTextNode("James"));
            //employee.appendChild(firstName);

            // lastname element
            //Element lastname = document.createElement("lastname");
            //lastname.appendChild(document.createTextNode("Harley"));
            //employee.appendChild(lastname);

            // email element
            //Element email = document.createElement("email");
            //email.appendChild(document.createTextNode("james@example.org"));
            //employee.appendChild(email);

            // department elements
            //Element department = document.createElement("department");
            //department.appendChild(document.createTextNode("Human Resources"));
            //employee.appendChild(department);

            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(String.valueOf(xmlFilePath)));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
//    public class ReadSettingsXMLFile {
//
//        // Return Strings
//        String currentServerIP = "";
//
//        //Read Price XML File
//        public static void readPriceXML(String settingsXMLFile) {
//            File fXmlFile = new File(settingsXMLFile);
//            DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder;
//            XPath xp = XPathFactory.newInstance().newXPath();
//            try {
//                dBuilder = Factory.newDocumentBuilder();
//                Document doc = dBuilder.parse(fXmlFile);
//                doc.getDocumentElement().normalize();
//
//                // Node Lists
//                NodeList nList = doc.getElementsByTagName("PRICE");
//                NodeList priceBandList = doc.getElementsByTagName("PRICES");
//                NodeList prodPriceList = doc.getElementsByTagName("PRODUCT_PRICE");
//
//                for(int x=0,size= nList.getLength(); x<size; x++) {
//                    System.out.println("For:");
//                    System.out.println("----------------------------");
//                    //System.out.println(priceBandList.item(x).getAttributes().getNamedItem("price_band").getNodeValue());
//
//                    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//
//                    Node nNode = nList.item(x);
//
//                    System.out.println("\nCurrent Element :" + nNode.getNodeName());
//
//                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//
//                        Element eElement = (Element) nNode;
//                        //String connName = ((Element)eElement.getAttribute("price_band"));
//                        //System.out.println(priceBandList.item(x).getAttributes().getNamedItem("PRICES").getNodeValue());
//
//                        System.out.println("Tax : " + eElement.getAttribute("tax_included"));
//                        System.out.println("CRUD : " + eElement.getElementsByTagName("CRUD").item(0).getTextContent());
//                        System.out.println("SKU : " + eElement.getElementsByTagName("SKU").item(0).getTextContent());
//                        System.out.println("From Date : " + eElement.getElementsByTagName("FROM_DATE").item(0).getTextContent());
//                        System.out.println("Price Band : " + eElement.getAttribute("price_band"));
//                        System.out.println("PRICES : " + eElement.getElementsByTagName("PRICES").item(0).getTextContent());
//                        System.out.println("PRODUCT_PRICE : " + eElement.getElementsByTagName("PRODUCT_PRICE").item(0).getTextContent());
//                        System.out.println("XPath : " + xp.compile("/@Price").evaluate(nList.item(x)));
//                        System.out.println("XP Prod Price : " + xp.compile("./@price_band").evaluate(nList.item(x)));
//                    }
//                }
//
//            } catch (ParserConfigurationException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            catch (SAXException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (XPathExpressionException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
//
//        public static String ReadXMLFile(String fileReadXMLFile) {
//            WriteToLog.writeLogLine("The File Name : " + fileReadXMLFile +".xml" );
//            DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder;
//
//            try {
//                dBuilder = Factory.newDocumentBuilder();
//                Document doc = dBuilder.parse(fileReadXMLFile);
//                doc.getDocumentElement().normalize();
//
//
//                // Node Lists
//                NodeList nList = doc.getElementsByTagName("ServerSettings");
//
//                for(int x=0,size= nList.getLength(); x<size; x++) {
//                    System.out.println("----------------------------");
//                    //System.out.println(priceBandList.item(x).getAttributes().getNamedItem("price_band").getNodeValue());
//
//                    System.out.println("Root element : " + doc.getDocumentElement().getNodeName());
//
//                    Node nNode = nList.item(x);
//
//                    System.out.println("\nCurrent Element : " + nNode.getNodeName());
//
//                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//
//                        Element eElement = (Element) nNode;
//                        //String connName = ((Element)eElement.getAttribute("price_band"));
//                        //System.out.println(nList.item(x).getAttributes().getNamedItem("TheServerIP").getNodeValue());
//                        System.out.println("ServerIP : " + eElement.getElementsByTagName("ServerIP").item(0).getTextContent());
//                        currentServerIP = (eElement.getElementsByTagName("ServerIP").item(0).getTextContent());
//
//
//                    }
//                }
//
//            } catch (ParserConfigurationException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            catch (SAXException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            return currentServerIP;
//
//        }
//
//
//    }
}