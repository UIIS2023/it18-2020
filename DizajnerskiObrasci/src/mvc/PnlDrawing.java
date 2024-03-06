//view
package mvc;

import javax.swing.JPanel;

import geometry.Point;
import geometry.Shape;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PnlDrawing extends JPanel {
	
	private DrawingModel model = new DrawingModel();

	public void setModel(DrawingModel model) {
		this.model = model;
	}
	public int x = 0;
	public PnlDrawing() {
		setBackground(Color.WHITE);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Iterator<Shape> it = model.getShapes().iterator();
		while(it.hasNext())
			it.next().draw(g);
	}
	
}

