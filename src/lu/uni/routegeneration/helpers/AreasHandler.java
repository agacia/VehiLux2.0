package lu.uni.routegeneration.helpers;

import java.util.ArrayList;

import lu.uni.routegeneration.generation.Area;
import lu.uni.routegeneration.generation.ZoneType;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
* <areas residential_proba="5" commercial_proba="80" industrial_proba="15"> 
* <area id="1" type="COMMERCIAL" x="20418" y="14500" radius="1500" probability="10"/> 
* <area id="2" type="COMMERCIAL" x="22400" y="16700" radius="1500" probability="15"/> 
* </areas>
*/
public class AreasHandler extends DefaultHandler {
	
	private double residentialTypeProbabilityIn = 0.0;
	private double commercialTypeProbabilityIn = 0.0;
	private double industrialTypeProbabilityIn = 0.0;
	private double residentialTypeProbabilityOut = 0.0;
	private double commercialTypeProbabilityOut = 0.0;
	private double industrialTypeProbabilityOut = 0.0;
	
	public double getResidentialTypeProbabilityIn() {
		return residentialTypeProbabilityIn;
	}

	public double getCommercialTypeProbabilityIn() {
		return commercialTypeProbabilityIn;
	}

	public double getIndustrialTypeProbabilityIn() {
		return industrialTypeProbabilityIn;
	}
	
	public double getResidentialTypeProbabilityOut() {
		return residentialTypeProbabilityOut;
	}

	public double getCommercialTypeProbabilityOut() {
		return commercialTypeProbabilityOut;
	}

	public double getIndustrialTypeProbabilityOut() {
		return industrialTypeProbabilityOut;
	}

	private double residentialAreasSumProbability = 0.0;
	private double commercialAreasSumProbability = 0.0;
	private double industrialAreasSumProbability = 0.0;

	private ArrayList<Area> areas = new ArrayList<Area>();
	
	public double getResidentialAreasSumProbability() {
		return residentialAreasSumProbability;
	}

	public double getCommercialAreasSumProbability() {
		return commercialAreasSumProbability;
	}

	public double getIndustrialAreasSumProbability() {
		return industrialAreasSumProbability;
	}

	public ArrayList<Area> getAreas() {
		return areas;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (qName.equals("areas")) {
			double sum = 0.0;
			residentialTypeProbabilityIn += Double.parseDouble(attributes.getValue("residential_proba_in"));
			commercialTypeProbabilityIn += Double.parseDouble(attributes.getValue("commercial_proba_in"));
			industrialTypeProbabilityIn += Double.parseDouble(attributes.getValue("industrial_proba_in"));
			residentialTypeProbabilityOut += Double.parseDouble(attributes.getValue("residential_proba_out"));
			commercialTypeProbabilityOut += Double.parseDouble(attributes.getValue("commercial_proba_out"));
			industrialTypeProbabilityOut += Double.parseDouble(attributes.getValue("industrial_proba_out"));
//			sum += residentialTypeProbability;
//			sum += commercialTypeProbability;
//			sum += industrialTypeProbability;
//			if (sum != 0) {
//				residentialTypeProbability /= sum;
//				commercialTypeProbability /= sum;
//				industrialTypeProbability /= sum;
//			}
		}
		if (qName.equals("area")) {
			String id = attributes.getValue("id");
			double x = Double.parseDouble(attributes.getValue("x"));
			double y = Double.parseDouble(attributes.getValue("y"));
			double radius = Double.parseDouble(attributes.getValue("radius"));
			double probability = Double.parseDouble(attributes.getValue("probability"));
			
			String type = attributes.getValue("type");
			ZoneType zoneType = ZoneType.RESIDENTIAL;
			if (type.equals("RESIDENTIAL")) {
				zoneType = ZoneType.RESIDENTIAL;
				residentialAreasSumProbability += probability;
			}  
			else if (type.equals("COMMERCIAL")) {
				zoneType = ZoneType.COMMERCIAL;
				commercialAreasSumProbability += probability;
			}
			else if (type.equals("INDUSTRIAL")) {
				zoneType = ZoneType.INDUSTRIAL;
				industrialAreasSumProbability += probability;
			}
			Area area = new Area(id, x, y, radius, probability, zoneType);
			areas.add(area);
		}
	}

	@Override
	public void endDocument() throws SAXException {
//		for (Area area : areas) {
//			double normalisedProbability = 0;
//			if (area.getZoneType().equals(ZoneType.RESIDENTIAL)) {
//				normalisedProbability = area.getProbability() / residentialAreasSumProbability;
//			}  
//			else if (area.getZoneType().equals(ZoneType.COMMERCIAL)) {
//				normalisedProbability = area.getProbability() / commercialAreasSumProbability;
//			}
//			else if (area.getZoneType().equals(ZoneType.INDUSTRIAL)) {
//				normalisedProbability = area.getProbability() / industrialAreasSumProbability;
//			}
//			area.setProbability(normalisedProbability);
//		}
	}

};
