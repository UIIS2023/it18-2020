package geometry;

import java.awt.Color;
import java.awt.Graphics;

import hexagon.Hexagon;

public class HexagonAdapter extends SurfaceShape {
	
	private Hexagon hexagon = new Hexagon(0, 0, 0);
	
	public HexagonAdapter()
	{
		
	}
	
	public HexagonAdapter(Point center, int radius)
	{
		hexagon = new Hexagon(center.getX(), center.getY(), radius);
	}
	
	public HexagonAdapter (Point center, int radius, boolean selected, Color color, Color innerColor) {
		hexagon = new Hexagon(center.getX(), center.getY(), radius);
		hexagon.setSelected(selected);
		hexagon.setBorderColor(color);
		hexagon.setAreaColor(innerColor);
	}
	
	public HexagonAdapter (Point center, int radius, Color color, Color innerColor) {
		hexagon = new Hexagon(center.getX(), center.getY(), radius);
		hexagon.setBorderColor(color);
		hexagon.setAreaColor(innerColor);
	}

	@Override
	public void moveTo(int x, int y) {
		getCenter().moveTo(x, y);
		
	}

	@Override
	public void moveBy(int x, int y) {
		getCenter().moveBy(x, y);
		
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void fill(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof HexagonAdapter) {
			HexagonAdapter pomocni = (HexagonAdapter) obj;
			if (getCenter().equals(pomocni.getCenter()) && getRadius() == pomocni.getRadius()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean contains(int x, int y) {
		
		return hexagon.doesContain(x, y);
	}

	@Override
	public void draw(Graphics g) {
		hexagon.paint(g);
			
	}
	
	public Point getCenter() {
		Point center = new Point(hexagon.getX(), hexagon.getY());
		return center;
	}
	
	public void setCenter(Point center) {
		hexagon.setX(center.getX());
		hexagon.setY(center.getY());
	}
	
	public int getRadius() {
		return hexagon.getR();
	}

	public void setRadius(int radius) throws Exception {
		if(radius<0) {
			throw new Exception("Vrednost poluprecnika mora biti veci od 0");
		}
		hexagon.setR(radius);
	}
	
	public Color getColor() {
		return hexagon.getBorderColor();
	}
	
	public void setColor(Color color) {
		hexagon.setBorderColor(color);
	}
	
	public Color getInnerColor() {
		return hexagon.getAreaColor();
	}
	
	public void setInnerColor(Color innerColor) {
		hexagon.setAreaColor(innerColor);
	}
	
	public void setSelected(boolean selected) {
		hexagon.setSelected(selected);
		this.selected = selected;
	}
	
	public String toString() {
		return "Hexagon --> Center=" + getCenter().getX() + "," + getCenter().getY() + " Radius=" + getRadius() + 
				" Color=" + getColor().getRGB() + " InnerColor=" + getInnerColor().getRGB();
	}

	@Override
	public Shape clone() {
		HexagonAdapter hexagon = new HexagonAdapter();
		hexagon.setCenter(getCenter());
		try {
			hexagon.setRadius(getRadius());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hexagon.setColor(getColor());
		hexagon.setInnerColor(getInnerColor());
		
		return hexagon;
	}
}
