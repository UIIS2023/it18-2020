package mvc;

import java.awt.EventQueue;

public class DrawingApp {
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DrawingModel model = new DrawingModel();
					FrmDrawing frame = new FrmDrawing();
					frame.getPnlDrawing().setModel(model);
					DrawingController controller = new DrawingController(model, frame);
					frame.setController(controller);
					controller.addObserver(frame);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
