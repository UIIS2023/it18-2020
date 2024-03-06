package command;

import java.util.ArrayList;
import java.util.List;

import geometry.Shape;
import mvc.DrawingModel;

public class BringToFrontCmd implements Command {
	
	private DrawingModel model;
	private Shape currentSelectedShape;
	private int index;
	
	public BringToFrontCmd(DrawingModel model,Shape currentSelectedShape, int index) {
		this.model = model;
		this.currentSelectedShape = currentSelectedShape;
		this.index = index;
	}

	@Override
	public void execute() {		
	    List<Shape> listOfFrontShapes = new ArrayList<>();
	    for (int i = index; i < model.getShapes().size(); i++) {
	    	listOfFrontShapes.add(model.getShapes().get(i));
	    }
	    
	    model.getShapes().removeAll(listOfFrontShapes);
	    
	    for (Shape s : listOfFrontShapes) {
	        if (s != currentSelectedShape) {
	            model.getShapes().add(s);
	        }
	    }
	    model.getShapes().add(currentSelectedShape);	
	}

	@Override
	public void unexecute() {
		List<Shape> listOfBackShapes = new ArrayList<>();
	    for (int i = index; i < model.getShapes().size(); i++) {
	    	listOfBackShapes.add(model.getShapes().get(i));
	    }
	    
	    model.getShapes().removeAll(listOfBackShapes);
	    
	    for (Shape s : listOfBackShapes) {
	        if (s == currentSelectedShape) {
	            model.getShapes().add(index, currentSelectedShape);
	        } else {
	        	model.getShapes().add(s);
	        }
	    }	
	}

}
