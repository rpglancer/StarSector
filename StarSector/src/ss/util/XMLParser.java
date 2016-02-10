package ss.util;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
	private Document dom;
	
	public void parseXmlFile(){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse("../../data/sectors.xml");
		}catch(ParserConfigurationException pce){
			pce.printStackTrace();
		}catch(SAXException se){
			se.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	private void parseDocument(){
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("Sector");
		if(nl != null && nl.getLength() > 0){
			for(int i = 0; i < nl.getLength(); i++){
				Element el = (Element)nl.item(i);
			}
		}
	}
	
	private String getName(Element e){
		return getTextValue(e, "Name");
	}
	
	private String getTextValue(Element e, String val){
		String textVal = null;
		NodeList nl = e.getElementsByTagName(val);
		if(nl != null && nl.getLength() > 0){
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
	}

}
