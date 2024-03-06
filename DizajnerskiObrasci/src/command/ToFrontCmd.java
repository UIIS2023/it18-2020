package command;

import geometry.Shape;
import mvc.DrawingModel;

public class ToFrontCmd implements Command {
	
	private DrawingModel model;
	private Shape currentSelectedShape;
	private int index;
	
	public ToFrontCmd(DrawingModel model,Shape currentSelectedShape, int index) {
		this.model = model;
		this.currentSelectedShape = currentSelectedShape;
		this.index = index;
	}

	@Override
	public void execute() {
		Shape frontShape = model.getShape(index+1);
		model.getShapes().remove(frontShape);
		model.getShapes().remove(currentSelectedShape);
		model.getShapes().add(index, frontShape);
		model.getShapes().add(index+1, currentSelectedShape);
		
	}

	@Override
	public void unexecute() {
		Shape backShape = model.getShape(index);
		model.getShapes().remove(currentSelectedShape);
		model.getShapes().remove(backShape);
		model.getShapes().add(index, currentSelectedShape);
		model.getShapes().add(index+1, backShape);
		
	}

}
