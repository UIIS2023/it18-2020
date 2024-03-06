package command;

import geometry.Rectangle;

public class UpdateRectangleCmd implements Command {
	
	private Rectangle oldRectangle;
	private Rectangle newRectangle;
	private Rectangle original = new Rectangle();
	
	public UpdateRectangleCmd(Rectangle oldRectangle, Rectangle newRectangle) {
		this.oldRectangle = oldRectangle;
		this.newRectangle = newRectangle;
	} 

	@Override
	public void execute() {
		original = (Rectangle)oldRectangle.clone();
		
		oldRectangle.getUpperLeftPoint().setX(newRectangle.getUpperLeftPoint().getX());
		oldRectangle.getUpperLeftPoint().setY(newRectangle.getUpperLeftPoint().getY());
		oldRectangle.setWidth(newRectangle.getWidth());
		oldRectangle.setHeight(newRectangle.getHeight());
		oldRectangle.setColor(newRectangle.getColor());
		oldRectangle.setInnerColor(newRectangle.getInnerColor());
		
	}

	@Override
	public void unexecute() {
		oldRectangle.getUpperLeftPoint().setX(original.getUpperLeftPoint().getX());
		oldRectangle.getUpperLeftPoint().setY(original.getUpperLeftPoint().getY());
		oldRectangle.setWidth(original.getWidth());
		oldRectangle.setHeight(original.getHeight());
		oldRectangle.setColor(original.getColor());
		oldRectangle.setInnerColor(original.getInnerColor());
		
	}
	

}
