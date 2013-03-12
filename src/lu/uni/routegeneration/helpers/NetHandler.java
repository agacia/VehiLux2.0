package lu.uni.routegeneration.helpers;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import lu.uni.routegeneration.ui.Lane;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.jhlabs.map.proj.Projection;
import com.jhlabs.map.proj.ProjectionFactory;

public class NetHandler extends DefaultHandler {
	
	private String projParameter = "+proj=utm + zone=31 +ellps=WGS84 +datum=WGS84 +units=m +no_defs";
	
	private Projection proj;
	public Projection getProj() {
		return proj;
	}

	private Point2D.Double netOffset;
	public Point2D.Double getNetOffset() {
		return netOffset;
	}

	private boolean readEdges;
	private ArrayList<Lane> edges;
	
	public ArrayList<Lane> getEdges() {
		return edges;
	}

	public NetHandler() {
		this(false);
	}
	
	public NetHandler(boolean readEdges) {
		this.readEdges = readEdges;
		edges = new ArrayList<Lane>();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (qName.equals("location")) {
			projParameter = attributes.getValue("projParameter");
			proj = ProjectionFactory.fromPROJ4Specification(projParameter.split(" "));
			String offset = attributes.getValue("netOffset");
			String[] toffset = offset.split(",");
			netOffset = new Point2D.Double();
			netOffset.x = Double.parseDouble(toffset[0]);
			netOffset.y = Double.parseDouble(toffset[1]);
		}
		if (readEdges && qName.equals("lane")) {
			Lane e = new Lane();
			String shape = attributes.getValue("shape");
			for (String point : shape.split(" ")) {
				Point2D.Double p = new Point2D.Double();
				String[] xy = point.split(",");
				p.x = Double.parseDouble(xy[0]);
				p.y = Double.parseDouble(xy[1]);
				e.shape.add(p);
			}
			edges.add(e);
		}
	}
	};