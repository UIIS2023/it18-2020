package mvc;

import java.util.ArrayList;
import java.util.List;

import geometry.Point;
import geometry.Shape;

public class DrawingModel {
	
private List<Shape> shapes = new ArrayList<>(); //veza sa interfejsima
private int i;
	
	public void removeShape(Shape s) {
		shapes.remove(s);
	}
	
	public void addShape(Shape shape) {
		shapes.add(shape);
	}
	public Shape getShape(int index) {
		return shapes.get(index);
	}
	
	public void setShape(int index, Shape shape) {
		shapes.set(index, shape);
	}
	
	public void removeSelected() {
		shapes.removeIf(shape -> shape.isSelected());
	}
	
	/*public void deselect() {
		shapes.forEach(shape -> shape.setSelected(false));
	}
	
	public void select(Point point) {
		for (i = shapes.size()-1; i >= 0; i--) {
			if (shapes.get(i).contains(point.getX(), point.getY())) {
				shapes.get(i).setSelected(true);
				return;
			}
		}
	}*/
	
	public int getSelected() {
		for (i = shapes.size()-1; i >= 0; i--) {
			if (shapes.get(i).isSelected()) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean isEmpty() {
		return shapes.isEmpty();
	}
	
	public List<Shape> getShapes() {
		return shapes;
	}

}
