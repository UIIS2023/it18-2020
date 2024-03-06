package command;

import geometry.Donut;

public class UpdateDonutCmd implements Command {
	
	private Donut oldDonut;
	private Donut newDonut;
	private Donut original = new Donut();
	
	public UpdateDonutCmd(Donut oldDonut, Donut newDonut) {
		this.oldDonut = oldDonut;
		this.newDonut = newDonut;
	} 

	@Override
	public void execute() {
		original = (Donut)oldDonut.clone();
		
		oldDonut.getCenter().setX(newDonut.getCenter().getX());
		oldDonut.getCenter().setY(newDonut.getCenter().getY());
		try {
			oldDonut.setRadius(newDonut.getRadius());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oldDonut.setInnerRadius(newDonut.getInnerRadius());
		oldDonut.setColor(newDonut.getColor());
		oldDonut.setInnerColor(newDonut.getInnerColor());
		
	}

	@Override
	public void unexecute() {
		oldDonut.getCenter().setX(original.getCenter().getX());
		oldDonut.getCenter().setY(original.getCenter().getY());
		try {
			oldDonut.setRadius(original.getRadius());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oldDonut.setInnerRadius(original.getInnerRadius());
		oldDonut.setColor(original.getColor());
		oldDonut.setInnerColor(original.getInnerColor());
	}

}
