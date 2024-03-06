package strategy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import mvc.DrawingController;
import mvc.DrawingModel;

public class SaveLoadTextFile implements FileStrategy {

	@Override
	public void save(File file, DrawingModel model, DrawingController controller) {
		try {
			FileWriter fw = new FileWriter(file.getAbsolutePath()+".txt");
			for(Object logText : controller.getLogModel().toArray()) {
				fw.write(logText.toString()+"\n");
				
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void load(File file, DrawingModel model, DrawingController controller) {
		Path filePath = Paths.get(file.getAbsolutePath());
		try {
			List<String> textFileString = Files.readAllLines(filePath);
			System.out.println(textFileString.size());
			controller.setTextFileList(textFileString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
