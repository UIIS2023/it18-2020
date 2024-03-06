package command;

import java.util.ArrayList;
import java.util.List;

import geometry.Shape;
import mvc.DrawingModel;

public class BringToBackCmd implements Command {
	
	private DrawingModel model;
	private Shape currentSelectedShape;
	private int index;
	
	public BringToBackCmd(DrawingModel model,Shape currentSelectedShape, int index) {
		this.model = model;
		this.currentSelectedShape = currentSelectedShape;
		this.index = index;
	}

	@Override
	public void execute() {
		List<Shape> listOfBackShapes = new ArrayList<>();
		for (int i = 0; i < model.getShapes().size(); i++) {
			listOfBackShapes.add(model.getShapes().get(i));
	    }
	    
	    model.getShapes().removeAll(listOfBackShapes);
	    
	    model.getShapes().add(currentSelectedShape);	   
	    for (Shape s : listOfBackShapes) {
	        if (s != currentSelectedShape) {
	            model.getShapes().add(s);
	        }
	    }
	}

	@Override
	public void unexecute() {
		List<Shape> listOfFrontShapes = new ArrayList<>();
		for (int i = 0; i < model.getShapes().size(); i++) {
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

}
