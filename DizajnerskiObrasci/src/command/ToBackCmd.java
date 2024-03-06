package command;

import geometry.Shape;
import mvc.DrawingModel;

public class ToBackCmd implements Command {
	
	private DrawingModel model;
	private Shape currentSelectedShape;
	private int index;
	
	public ToBackCmd(DrawingModel model,Shape currentSelectedShape, int index) {
		this.model = model;
		this.currentSelectedShape = currentSelectedShape;
		this.index = index;
	}

	@Override
	public void execute() {
		Shape backShape = model.getShape(index-1);
		model.getShapes().remove(currentSelectedShape);
		model.getShapes().remove(backShape);
		model.getShapes().add(index-1, currentSelectedShape);
		model.getShapes().add(index, backShape);
		
	}

	@Override
	public void unexecute() {
		Shape frontShape = model.getShape(index);
		model.getShapes().remove(frontShape);
		model.getShapes().remove(currentSelectedShape);
		model.getShapes().add(index-1, frontShape);
		model.getShapes().add(index, currentSelectedShape);

	}

}
