package geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Drawing extends JPanel {

	public static void main(String[] args) {

		JFrame frame = new JFrame("Drawings");
		frame.setSize(800, 600);
		frame.setVisible(true);

		Drawing draw = new Drawing();
		frame.getContentPane().add(draw);
	}

	@Override
	public void paint(Graphics g) {
		Point p = new Point(50, 50);
		// p.draw(g);
		g.setColor(Color.RED);

		Line l1 = new Line(new Point(100, 100), new Point(200, 200));
		// l1.draw(g);

		Rectangle r1 = new Rectangle(l1.getEndPoint(), 100, 50);
		// r1.draw(g);

		Circle c1 = new Circle(new Point(500, 100), 80);
		// c1.draw(g);

		g.setColor(Color.GREEN);
		Donut d1 = new Donut(new Point(800, 100), 50, 25);
		// d1.draw(g);

		Rectangle k1 = new Rectangle(new Point(500, 500), 50, 50);
		// k1.draw(g);

		int innerR = (int) (k1.getHeight() * Math.sqrt(2) / 2);
		Donut d2 = new Donut(new Point(k1.getUpperLeftPoint().getX() + k1.getWidth() / 2,
				k1.getUpperLeftPoint().getY() + k1.getWidth() / 2), 80, innerR);
		// d2.draw(g);

		// Vezbe 8.
		// Zadatak 1.
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		shapes.add(p);
		shapes.add(l1);
		shapes.add(c1);
		shapes.add(d1);
		shapes.add(k1);
		Iterator<Shape> it = shapes.iterator();
		while (it.hasNext()) {
			it.next().moveBy(10, 0);
		}

		// Zadatak 2.
		shapes.get(3).draw(g);

		// prvi nacin
		/*
		 * int arrayListLength=0; while(it.hasNext()) { arrayListLength++; it.next(); }
		 * shapes.get(arrayListLength-1).draw(g);
		 */
		// drugi nacin
		shapes.get(shapes.size() - 1).draw(g);

		/*
		 * jedan od nacina, ako zelimo da nacrtamo obrisani element Shape tempShape =
		 * shapes.get(1); shapes.remove(1); tempShape.draw(g);
		 */
		// drugi nacin
		shapes.remove(1);
		// pomera se lista
		shapes.get(1).draw(g);

		shapes.get(3).draw(g);

		shapes.add(3, l1);

		it = shapes.iterator();
		while (it.hasNext()) {
			Shape pomocniS = it.next();
			if (pomocniS instanceof Circle || pomocniS instanceof Rectangle) {
				pomocniS.draw(g);
			}
		}

		// Zadatak 3.
		try {
			c1.setRadius(-10);
			System.out.println("Provera da li se vraca u try blok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Provera da li se nastavlja izvrsenje programa");

		// Zadatak 4.
		it = shapes.iterator();
		while (it.hasNext()) {
			Shape pomocniS = it.next();
			pomocniS.setSelected(true);
			pomocniS.draw(g);
		}

		// Zadatak 5.
		HashMap<String, Shape> hmShapes = new HashMap<String, Shape>();
		hmShapes.put("point", p);
		hmShapes.put("line", l1);
		System.out.println(hmShapes.get("line"));
	}

}
