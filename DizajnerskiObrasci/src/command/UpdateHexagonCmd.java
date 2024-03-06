package command;

import geometry.HexagonAdapter;

public class UpdateHexagonCmd implements Command {
	
	private HexagonAdapter oldHexagon;
	private HexagonAdapter newHexagon;
	private HexagonAdapter original = new HexagonAdapter();
	
	public UpdateHexagonCmd(HexagonAdapter oldHexagon, HexagonAdapter newHexagon) {
		this.oldHexagon = oldHexagon;
		this.newHexagon = newHexagon;
	}

	@Override
	public void execute() {
		original = (HexagonAdapter)oldHexagon.clone();
		
		oldHexagon.setCenter(newHexagon.getCenter());
		try {
			oldHexagon.setRadius(newHexagon.getRadius());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oldHexagon.setColor(newHexagon.getColor());
		oldHexagon.setInnerColor(newHexagon.getInnerColor());
		
	}

	@Override
	public void unexecute() {
		oldHexagon.setCenter(original.getCenter());
		try {
			oldHexagon.setRadius(original.getRadius());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oldHexagon.setColor(original.getColor());
		oldHexagon.setInnerColor(original.getInnerColor());
		
	}

}
