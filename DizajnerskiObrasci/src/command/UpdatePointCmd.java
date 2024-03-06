package command;

import geometry.Point;

public class UpdatePointCmd implements Command {

	private Point oldPoint;
	private Point newPoint;
	private Point original = new Point();
	
	public UpdatePointCmd(Point oldPoint, Point newPoint) {
		this.oldPoint = oldPoint;
		this.newPoint = newPoint;
	}

	@Override
	public void execute() {
		original = (Point)oldPoint.clone();
		
		oldPoint.setX(newPoint.getX());
		oldPoint.setY(newPoint.getY());
		oldPoint.setColor(newPoint.getColor());
		
	}

	@Override
	public void unexecute() {
		oldPoint.setX(original.getX());
		oldPoint.setY(original.getY());
		oldPoint.setColor(original.getColor());
		
	}
}
