package geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Donut extends Circle {

	private int innerRadius;

	public Donut() {

	}

	public Donut(Point center, int radius, int innerRadius) {
		// this.setCenter(center); --center je definisan kao private
		// this.center=center;--center je definisan kao protected
		super(center, radius);
		this.innerRadius = innerRadius;
	}

	public Donut(Point center, int radius, int innerRadius, boolean selected) {
		this(center, radius, innerRadius);
		setSelected(selected);
	}
	
	public Donut(Point center, int radius, int innerRadius, boolean selected, Color color) {
		this(center, radius, innerRadius, selected);
		setColor(color);
	}

	public Donut(Point center, int radius, int innerRadius, boolean selected, Color color, Color innerColor) {
		this(center, radius, innerRadius, selected, color);
		setInnerColor(innerColor);
	}
	
	public Donut(Point center, int radius, int innerRadius, Color color, Color innerColor) {
		this(center, radius, innerRadius);
		setColor(color);
		setInnerColor(innerColor);
	}

	public double area() {
		return super.area() - innerRadius * innerRadius * Math.PI;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Donut) {
			Donut pomocni = (Donut) obj;
			if (this.center.equals(pomocni.center) && this.getRadius() == pomocni.getRadius()
					&& innerRadius == pomocni.innerRadius) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean contains(int x, int y) {
		return center.distance(x, y) >= innerRadius && super.contains(x, y);
	}

	public boolean contains(Point p) {
		return center.distance(p.getX(), p.getY()) >= innerRadius && super.contains(p.getX(), p.getY());
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D gd = (Graphics2D) g;
		Ellipse2D innerCircle = new Ellipse2D.Float(center.getX()-innerRadius, center.getY()-innerRadius, innerRadius * 2, innerRadius * 2);
		Ellipse2D outerCircle = new Ellipse2D.Float(center.getX()-getRadius(), center.getY()-getRadius(), getRadius() * 2, getRadius() * 2);
		
		Area shape = new Area(outerCircle);
		shape.subtract(new Area(innerCircle));
		g.setColor(getColor());
		gd.draw(shape);
		g.setColor(getInnerColor());
		gd.fill(shape);
		
		if (selected) {
			g.setColor(Color.BLUE);
			//g.drawRect(center.getX() - 2, center.getY() - 2, 4, 4);
			g.drawRect(center.getX() - innerRadius - 2, center.getY() - 2, 4, 4);
			g.drawRect(center.getX() + innerRadius - 2, center.getY() - 2, 4, 4);
			g.drawRect(center.getX() - 2, center.getY() - innerRadius - 2, 4, 4);
			g.drawRect(center.getX() - 2, center.getY() + innerRadius - 2, 4, 4);
			
			g.drawRect(center.getX() - getRadius() - 2, center.getY() - 2, 4, 4);
			g.drawRect(center.getX() + getRadius() - 2, center.getY() - 2, 4, 4);
			g.drawRect(center.getX() - 2, center.getY() - getRadius() - 2, 4, 4);
			g.drawRect(center.getX() - 2, center.getY() + getRadius() - 2, 4, 4);
			//this.fill(g);
		}
	}
	
	public void fill(Graphics g) {
		g.setColor(getInnerColor());
		super.fill(g);
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval(center.getX() - innerRadius, center.getY() - innerRadius, innerRadius * 2 - 2, innerRadius * 2 - 2);
	}

	@Override
	public int compareTo(Object o) {

		if (o instanceof Donut) {
			return (int) (this.area() - ((Donut) o).area());
		}
		return 0;
	}

	public int getInnerRadius() {
		return innerRadius;
	}

	public void setInnerRadius(int innerRadius) {
		this.innerRadius = innerRadius;
	}

	public String toString() {
		// Center=(x,y), radius= radius, innerRadius=innerRadius
		return "Donut --> Center=" + center.getX() + "," + center.getY() + " Radius=" + getRadius() + 
				" InnerRadius=" + innerRadius + 
				" Color=" + getColor().getRGB() + " InnerColor=" + getInnerColor().getRGB();
	}
	
	@Override
	public Shape clone() {
		Donut donut = new Donut();
		donut.getCenter().setX(getCenter().getX());
		donut.getCenter().setY(getCenter().getY());
		try {
			donut.setRadius(getRadius());
		} catch (Exception e) {
			e.printStackTrace();
		}
		donut.setInnerRadius(getInnerRadius());
		donut.setColor(getColor());
		donut.setInnerColor(getInnerColor());
		
		return donut;
	}

}
