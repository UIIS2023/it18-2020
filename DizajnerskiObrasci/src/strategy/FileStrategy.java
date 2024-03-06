package strategy;

import java.io.File;

import mvc.DrawingController;
import mvc.DrawingModel;

public interface FileStrategy {
	
	void save(File file, DrawingModel model, DrawingController controller);
	
	void load(File file, DrawingModel model, DrawingController controller);

}
