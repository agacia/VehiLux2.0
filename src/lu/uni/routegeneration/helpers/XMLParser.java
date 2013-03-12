package lu.uni.routegeneration.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import lu.uni.routegeneration.generation.Trip;
import lu.uni.routegeneration.generation.VType;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;


public class XMLParser {
	
	public static void readFile(String path, ContentHandler h) {
		try {
			File file = new File(path);
			if (file.exists()) {
				XMLReader parser = XMLReaderFactory.createXMLReader();
				parser.setContentHandler(h);
				parser.parse(new InputSource(path));
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
	}
	
	// Xml writing
	public static TransformerHandler xmlMain(StreamResult sr) throws Exception {
		SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
		TransformerHandler tfh = tf.newTransformerHandler();
		Transformer serTf = tfh.getTransformer();
		serTf.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		serTf.setOutputProperty(OutputKeys.INDENT, "yes");
		tfh.setResult(sr);
		tfh.startDocument();
		return tfh;
	}
	
	public static void writeFlow(String id, String sourceId, String destinationId, int begin, int end, String vehicleId, int numberOfVehicles, TransformerHandler tfh, AttributesImpl ai) {
		try {
			ai.clear();
			ai.addAttribute("", "", "id", "CDATA", "trip" + id);
			ai.addAttribute("", "", "from", "CDATA", "" + sourceId);
			ai.addAttribute("", "", "to", "CDATA", "" + destinationId);
			ai.addAttribute("", "", "begin", "CDATA", "" + begin);
			ai.addAttribute("", "", "end", "CDATA", "" + end);
			ai.addAttribute("", "", "type", "CDATA", "" + vehicleId);
			ai.addAttribute("", "", "number", "CDATA", "" + numberOfVehicles);
			tfh.startElement("", "", "flow", ai);
			tfh.endElement("", "", "flow");	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeRoute(String id, String type, double depart, String edges, TransformerHandler tfh, AttributesImpl ai) {
		ai.clear();
		ai.addAttribute("", "", "id", "CDATA", id);
		ai.addAttribute("", "", "type", "CDATA", type);
		ai.addAttribute("", "", "depart", "CDATA", "" + depart);
		ai.addAttribute("", "", "departLane", "CDATA", "free");
		ai.addAttribute("", "", "departPos", "CDATA", "random_free");
		ai.addAttribute("", "", "departLane", "CDATA", "free");
		try {
			tfh.startElement("", "", "vehicle", ai);
			ai.clear();
			ai.addAttribute("", "", "edges", "CDATA", edges);
			tfh.startElement("", "", "route", ai);
			tfh.endElement("", "", "route");
			tfh.endElement("", "", "vehicle");
		} 
		catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeFlows(String baseFolder, String baseName, String outputFolder, ArrayList<Trip> trips, ArrayList<VType> vtypes, int stopTime) {
		String path = baseFolder + outputFolder + baseName + ".flows.xml";
		String outputDirPath = baseFolder + outputFolder;
		try {
			File file = new File(outputDirPath);
			if (!file.exists()) {
				File dir = new File(outputDirPath);  
				dir.mkdir();
			}
			StreamResult sr = new StreamResult(path);
			TransformerHandler tfh = XMLParser.xmlMain(sr);
			AttributesImpl ai = new AttributesImpl();
			tfh.startElement("", "", "flows", ai);
			XMLParser.writeVTypes(vtypes, tfh, ai);
			for (Trip trip : trips) {
				XMLParser.writeFlow(trip.getId(), trip.getSourceId(), trip.getDestinationId(), (int)trip.getDepartTime(), stopTime, trip.getVehicleId(), 1, tfh, ai); 
			}
			tfh.endElement("", "", "flows");
			tfh.endDocument();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeRoutes(String baseFolder, String baseName, String outputFolder, ArrayList<Trip> trips, ArrayList<VType> vtypes) {
		String path = baseFolder + outputFolder + baseName + ".rou.xml";
		String outputDirPath = baseFolder + outputFolder;
		try {
			File file = new File(outputDirPath);
			if (!file.exists()) {
				File dir = new File(outputDirPath);  
				dir.mkdir();
			}
			StreamResult sr = new StreamResult(path);
			final TransformerHandler tfh = XMLParser.xmlMain(sr);
			final AttributesImpl ai = new AttributesImpl();
			tfh.startElement("", "", "routes", ai);
			XMLParser.writeVTypes(vtypes, tfh, ai);
			for (Trip trip : trips) {
				XMLParser.writeRoute(trip.getId(), trip.getVehicleId(), trip.getDepartTime(), trip.getRoute(), tfh, ai);
			}
			tfh.endElement("", "", "routes");
			tfh.endDocument();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void writeVTypes(List<VType> vtypes, TransformerHandler tfh, AttributesImpl ai) {
		try {
			// ------------- Compute the vtypes ------------------
			for (VType vt : vtypes) {
				ai.clear();
				ai.addAttribute("", "", "accel", "CDATA", vt.getAccel());
				ai.addAttribute("", "", "color", "CDATA", vt.getColor());
				ai.addAttribute("", "", "decel", "CDATA", vt.getDecel());
				ai.addAttribute("", "", "id", "CDATA", vt.getId());
				ai.addAttribute("", "", "length", "CDATA", vt.getLength());
				ai.addAttribute("", "", "minGap", "CDATA", vt.getMinGap());
				ai.addAttribute("", "", "maxSpeed", "CDATA", vt.getMaxSpeed());
				ai.addAttribute("", "", "sigma", "CDATA", vt.getSigma());
				tfh.startElement("", "", "vType", ai);
				tfh.endElement("", "", "vType");
			}
			// ---------------- the default truck vtype ----------------
			ai.clear();
			ai.addAttribute("", "", "accel", "CDATA", "1.05");
			ai.addAttribute("", "", "color", "CDATA", "0.1,0.1,0.1");
			ai.addAttribute("", "", "decel", "CDATA", "4");
			ai.addAttribute("", "", "id", "CDATA", "truck");
			ai.addAttribute("", "", "length", "CDATA", "15");
			ai.addAttribute("", "", "minGap", "CDATA", "2.5");
			ai.addAttribute("", "", "maxSpeed", "CDATA", "30");
			ai.addAttribute("", "", "sigma", "CDATA", "0");
			tfh.startElement("", "", "vType", ai);
			tfh.endElement("", "", "vType");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
