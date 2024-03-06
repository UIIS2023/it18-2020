package geometry;

import java.awt.Color;
import java.awt.Graphics;

public class Line extends Shape{

	private Point startPoint = new Point();
	private Point endPoint = new Point();

	public Line() {
	}

	public Line(Point startPoint, Point endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	public Line(Point startPoint, Point endPoint, boolean selected) {
		this(startPoint, endPoint);// prva naredba u bloku (telu konstruktora)
		
		setSelected(selected);
		//menja se prilikom dodavanja Shape
		//this.selected = selected;
	}
	
	public Line(Point startPoint, Point endPoint, Color color) {
		this(startPoint, endPoint);
		this.setColor(color);
	}
	
	public Line(Point startPoint, Point endPoint, boolean selected, Color color) {
		this(startPoint, endPoint, selected);
		this.setColor(color);
	}

	public double length() {
		return this.startPoint.distance(this.endPoint.getX(), this.endPoint.getY());
	}
	
	public boolean equals(Object obj) {

		if (obj instanceof Line) {

			Line pomocna = (Line) obj;
			if (startPoint.equals(pomocna.startPoint) && endPoint.equals(pomocna.endPoint))
				return true;
			else
				return false;
		} else
			return false;

	}
	
	public boolean contains(int x, int y) {
		return (this.startPoint.distance(x, y)+this.endPoint.distance(x, y)) - length() <= 2;
	}
	
	

	@Override
	public void draw(Graphics g) {
		g.setColor(getColor());
		g.drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
		
		if(selected) {
			g.setColor(Color.BLUE);
			g.drawRect(startPoint.getX()-2, startPoint.getY()-2, 4, 4);
			g.drawRect(endPoint.getX()-2, endPoint.getY()-2, 4, 4);
			
			g.drawRect(this.middleOfLine().getX()-3, this.middleOfLine().getY()-3, 6, 6);
		}
	}
	
	public Point middleOfLine() {
		int middleByX = (startPoint.getX()+endPoint.getX())/2;
		int middleByY = (startPoint.getY()+endPoint.getY())/2;
		Point p = new Point(middleByX,middleByY);
		return p;
	}

	@Override
	public void moveTo(int x, int y) {
		//nije moguce implementirati
		
	}

	@Override
	public void moveBy(int x, int y) {
		startPoint.moveBy(x, y);
		endPoint.moveBy(x, y);
	}
	
	@Override
	public int compareTo(Object o) {
		
		if(o instanceof Line) {
			return (int)(this.length()-((Line)o).length());
		}
		return 0;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}
	
	public String toString() {
		return "Line --> StartPoint=" + startPoint.getX() + "," + startPoint.getY() + " EndPoint="
				+ endPoint.getX() + "," + endPoint.getY() + " Color=" + getColor().getRGB();
	}

	@Override
	public Shape clone() {
		Line line = new Line();
		line.getStartPoint().setX(this.startPoint.getX());
		line.getStartPoint().setY(this.startPoint.getY());
		line.getEndPoint().setX(this.endPoint.getX());
		line.getEndPoint().setY(this.endPoint.getY());
		line.setColor(getColor());
		
		return line;
	}

}
