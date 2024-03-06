package strategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

import javax.swing.JOptionPane;

import geometry.Shape;
import mvc.DrawingController;
import mvc.DrawingModel;

public class SaveLoadSerializationFile implements FileStrategy {

	@Override
	public void save(File file, DrawingModel model, DrawingController controller) {
		try (FileOutputStream fos = new FileOutputStream(file);
		     ObjectOutputStream oos = new ObjectOutputStream(fos)) {
		     oos.writeObject(model.getShapes());
		     oos.flush();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Try again to save shapes!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	@Override
	public void load(File file, DrawingModel model, DrawingController controller) {
		try (FileInputStream fis = new FileInputStream(file);
			 ObjectInputStream ois = new ObjectInputStream(fis)) {
			 model.getShapes().addAll((Collection<Shape>) ois.readObject());
			 ois.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Try again to load shapes!", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Try again to load shapes!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}

}
