package command;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import geometry.Shape;
import mvc.DrawingModel;

public class RemoveShapeCmd implements Command {
	
	private DrawingModel model;
	private List<Shape> shapesForDelete = new ArrayList<Shape>();
	
	
	public RemoveShapeCmd(DrawingModel model) {
		this.model = model;
	}

	@Override
	public void execute() {
		for(Shape s : model.getShapes()) {
			this.shapesForDelete.add(s);
				
		}
		model.removeSelected();
		
	}

	@Override
	public void unexecute() {
		model.getShapes().clear();
		model.getShapes().addAll(shapesForDelete);
		this.shapesForDelete.clear();	
		
	}

}
