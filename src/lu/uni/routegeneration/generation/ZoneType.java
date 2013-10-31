package lu.uni.routegeneration.generation;

import java.awt.Color;



public enum ZoneType {
	
//	RESIDENTIAL(0, new Color(75, 170, 0, 90)), 
//	COMMERCIAL(0, new Color(44, 157, 222, 90)),
//	INDUSTRIAL(0, new Color(83, 49, 206, 90));
	
	RESIDENTIAL(0, 0, new Color(75, 170, 0)), 
	COMMERCIAL(0, 0, new Color(255, 127, 0)),
	INDUSTRIAL(0, 0, new Color(83, 49, 206)); 
	
	// grayscale
//	RESIDENTIAL(0, new Color(220, 220, 220)), 
//	COMMERCIAL(0, new Color(150, 150, 150)),
//	INDUSTRIAL(0, new Color(80, 80, 80)); 

	private double probabilityIn;
	private double probabilityOut;
	private Color color;
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	ZoneType(double probabilityIn, double probabilityOut, Color color) {
		this.probabilityIn = probabilityIn;
		this.probabilityIn = probabilityOut;
		this.color = color;
	}

	public double getProbabilityIn() {
		return probabilityIn;
	}
	
	public void setProbabilityIn(double probability) {
		this.probabilityIn = probability;
	}
	
	public double getProbabilityOut() {
		return probabilityOut;
	}
	
	public void setProbabilityOut(double probability) {
		this.probabilityOut = probability;
	}
};
