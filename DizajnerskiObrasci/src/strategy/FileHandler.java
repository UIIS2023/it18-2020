package strategy;

import java.io.File;

import mvc.DrawingController;
import mvc.DrawingModel;

public class FileHandler implements FileStrategy {
	
	private FileStrategy fileStrategy;
	
	public FileHandler(FileStrategy fileStrategy) {
		this.fileStrategy = fileStrategy;
	}
	
	public void save(File file, DrawingModel model, DrawingController controller) {
		fileStrategy.save(file, model, controller);
	}
	
	public void load(File file, DrawingModel model, DrawingController controller) {
		fileStrategy.load(file, model, controller);
	}

}
