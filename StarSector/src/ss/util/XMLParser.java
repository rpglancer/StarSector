package ss.util;

import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ss.engine.Tracon;
import ss.entity.Static;
import ss.lib.Calc;

public class XMLParser {
	private Document dom;
	
	public void parseXmlFile(){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse("src/data/sectors.xml");
		}catch(ParserConfigurationException pce){
			pce.printStackTrace();
		}catch(SAXException se){
			se.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public void parseDocument(){
		Vector<Static> statlist = new Vector<Static>();
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("Static");
		if(nl != null && nl.getLength() > 0){
			System.out.println("Static List Size: " + nl.getLength());
			for(int i = 0; i < nl.getLength(); i++){
				Element el = (Element)nl.item(i);
				statlist.add(getStatic(el));
			}
		}
		if(statlist != null && statlist.size() > 0){
			for(int i = 0; i < statlist.size(); i++){
				Tracon.addStatic(statlist.get(i));
			}
		}
	}
	
	
	private int getIntValue(Element e, String val){
		return Integer.parseInt(getTextValue(e, val));
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
	
	private Static getStatic(Element staticEl){
		String type = getTextValue(staticEl, "Type");
		String name = getTextValue(staticEl, "Name");
		int x = getIntValue(staticEl, "X");
		int y = getIntValue(staticEl, "Y");
		int z = getIntValue(staticEl, "Z");
		Static s = new Static(type, name, x, y, z);
		NodeList temp = staticEl.getElementsByTagName("DEPART");
		if(temp != null && temp.getLength() > 0){
			System.out.println("DEPART Found!");
			for(int i = 0; i < temp.getLength(); i++){
				Element el = (Element)temp.item(i);
				int h = getIntValue(el, "H");
				int m = getIntValue(el, "M");
				System.out.println("H: " + h + ", M: " + m);
				s.setDepartCoords(Calc.convertCourseToCoords(s.getLoc(), h, m, 10));
			}
		}
		temp = staticEl.getElementsByTagName("ARRIVE");
		if(temp != null && temp.getLength() > 0){
			for(int i = 0; i < temp.getLength(); i++){
				Element el = (Element)temp.item(i);
				int h = getIntValue(el, "H");
				int m = getIntValue(el, "M");
				s.setArriveCoords(Calc.convertCourseToCoords(s.getLoc(), h, m, 10));
			}
		}
		return s;
	}

}
