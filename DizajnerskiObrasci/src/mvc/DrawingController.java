package mvc;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import command.AddShapeCmd;
import command.BringToBackCmd;
import command.BringToFrontCmd;
import command.Command;
import command.RemoveShapeCmd;
import command.ToBackCmd;
import command.ToFrontCmd;
import command.UpdateCircleCmd;
import command.UpdateDonutCmd;
import command.UpdateHexagonCmd;
import command.UpdateLineCmd;
import command.UpdatePointCmd;
import command.UpdateRectangleCmd;
import drawing.DlgCircle;
import drawing.DlgDonut;
import drawing.DlgHexagon;
import drawing.DlgLine;
import drawing.DlgPoint;
import drawing.DlgRectangle;
import geometry.Circle;
import geometry.Donut;
import geometry.HexagonAdapter;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;
import observer.Observable;
import observer.Observer;
import strategy.FileHandler;
import strategy.FileStrategy;
import strategy.SaveLoadSerializationFile;
import strategy.SaveLoadTextFile;

public class DrawingController implements Observable {
	
	private FrmDrawing frame;
	private DrawingModel model;
	
	private final int OPERATION_DRAWING = 1;
	private final int OPERATION_EDIT_DELETE = 0;
	
	private int activeOperation = OPERATION_DRAWING;
	boolean lineWaitingForEndPoint = false;
	private Point startPoint;
	
	private Color activeEdgeColor = Color.BLACK;
	private Color activeInnerColor = Color.WHITE;
	
	private List<Shape> currentSelectedShapes;
	private List<Command> undoCommands;
	private List<Command> redoCommands;
	private List<Observer> observers = new ArrayList<Observer>();
	private boolean modifyBtnEnabled = false;
	private boolean deleteBtnEnabled = false;
	private DefaultListModel<String> logModel;
	private List<String> textFileList = new ArrayList<String>();
	
	public DrawingController(DrawingModel model, FrmDrawing frame) {
		super();
		this.model = model;
		this.frame = frame;
		activeOperation = OPERATION_DRAWING;
		undoCommands = new ArrayList<Command>();
		redoCommands = new ArrayList<Command>();
		this.logModel = frame.logModel;
	}
	
	private void executeAddCmd(Shape shape) {
		AddShapeCmd addShapeCmd = new AddShapeCmd(model, shape);
		addShapeCmd.execute();
		undoCommands.add(addShapeCmd);
		redoCommands.clear();
		logModel.addElement("Add " + shape.toString());
	}
	
	public void select(Point point, int ctrlButton) {
		boolean checkContains = false;
		Shape currentShape = null;
		for (int i = model.getShapes().size()-1; i >= 0; i--) {
			if (model.getShapes().get(i).contains(point.getX(), point.getY()) && ctrlButton == MouseEvent.CTRL_DOWN_MASK && 
					model.getShapes().get(i).isSelected() == false) {
				model.getShapes().get(i).setSelected(true);
				logModel.addElement("Select " + model.getShapes().get(i).toString());
				checkContains = true;
				System.out.println(checkNumberSelectedShapes());
				notifyObservers();
				return;
			} else if(model.getShapes().get(i).contains(point.getX(), point.getY()) && ctrlButton != MouseEvent.CTRL_DOWN_MASK && 
					model.getShapes().get(i).isSelected() == false) {
				deselect();
				model.getShapes().get(i).setSelected(true);
				logModel.addElement("Select " + model.getShapes().get(i).toString());
				checkContains = true;
				System.out.println(checkNumberSelectedShapes());
				notifyObservers();
				return;
			}
			
			if(model.getShapes().get(i).contains(point.getX(), point.getY())) {
				currentShape = model.getShapes().get(i);
				break;
			}
		}
		if(checkContains==false && currentShape==null) 
			deselect();
		else {
			currentShape.setSelected(false);
			logModel.addElement("Deselect " + currentShape.toString());
		}
		System.out.println(checkNumberSelectedShapes());
		notifyObservers();
	}
	
	public int checkNumberSelectedShapes() {
		int i = 0;
		currentSelectedShapes = new ArrayList<Shape>();
		for(Shape s : model.getShapes()) {
			if(s.isSelected()) {
				i++;
				currentSelectedShapes.add(s);
			}
		}
		return i;
	}
	
	public void deselect() {
		if(checkNumberSelectedShapes()>1) {
			model.getShapes().forEach(shape -> {
				if(shape.isSelected()==true) {
					shape.setSelected(false);
				}
			});
			logModel.addElement("DeselectAllSelected");
			return;
		}
		
		model.getShapes().forEach(shape -> {
			if(shape.isSelected()==true) {
				shape.setSelected(false);
				logModel.addElement("Deselect " + shape.toString());
			}
		});
	}
	
	public void mouseClicked(MouseEvent e) {
		Point mouseClick = new Point(e.getX(), e.getY());
		
		if (activeOperation == OPERATION_EDIT_DELETE) {
			select(mouseClick,e.getModifiersEx());
			frame.repaint();
			return;
		}
		
		if (frame.btnShapePoint.isSelected()) {
			DlgPoint dlgPoint = new DlgPoint();
			dlgPoint.setPoint(mouseClick);
			dlgPoint.setColors(frame.edgeColor);
			dlgPoint.getBtnEdgeColor().setBackground(activeEdgeColor);
			dlgPoint.setVisible(true);
			if(dlgPoint.getPoint() != null)
			{
				executeAddCmd(dlgPoint.getPoint());
				activeEdgeColor = dlgPoint.getBtnEdgeColor().getBackground();
			}
			
			
		} else if (frame.btnShapeLine.isSelected()) {
			if(lineWaitingForEndPoint) {
				
				DlgLine dlgLine = new DlgLine();
				Line line = new Line(startPoint,mouseClick);
				dlgLine.setLine(line);
				dlgLine.setColors(frame.edgeColor);
				dlgLine.getBtnColor().setBackground(activeEdgeColor);
				dlgLine.setVisible(true);
				if(dlgLine.getLine()!= null) 
				{
					executeAddCmd(dlgLine.getLine());
					activeEdgeColor = dlgLine.getBtnColor().getBackground();
				}
				frame.repaint();
				lineWaitingForEndPoint=false;
				return;
			}
			startPoint = mouseClick;
			lineWaitingForEndPoint=true;
			return;
			

		} else if (frame.btnShapeRectangle.isSelected()) {
			DlgRectangle dlgRectangle = new DlgRectangle();
			dlgRectangle.setPoint(mouseClick);
			dlgRectangle.setColors(frame.edgeColor, frame.innerColor);
			dlgRectangle.getBtnEdgeColor().setBackground(activeEdgeColor);
			dlgRectangle.getBtnInnerColor().setBackground(activeInnerColor);
			dlgRectangle.setVisible(true);
			if(dlgRectangle.getRectangle() != null) 
			{
				executeAddCmd(dlgRectangle.getRectangle());
				activeEdgeColor = dlgRectangle.getBtnEdgeColor().getBackground();
				activeInnerColor = dlgRectangle.getBtnInnerColor().getBackground();
			}
			
		} else if (frame.btnShapeCircle.isSelected()) {
			DlgCircle dlgCircle = new DlgCircle();
			dlgCircle.setPoint(mouseClick);
			dlgCircle.setColors(frame.innerColor, frame.edgeColor);
			dlgCircle.getBtnEdgeColor().setBackground(activeEdgeColor);
			dlgCircle.getBtnInnerColor().setBackground(activeInnerColor);
			dlgCircle.setVisible(true);
			
			if(dlgCircle.getCircle() != null) 
			{
				executeAddCmd(dlgCircle.getCircle());
				activeEdgeColor = dlgCircle.getBtnEdgeColor().getBackground();
				activeInnerColor = dlgCircle.getBtnInnerColor().getBackground();
			}
			
		} else if (frame.btnShapeDonut.isSelected()) {
			DlgDonut dlgDonut = new DlgDonut();
			dlgDonut.setPoint(mouseClick);
			dlgDonut.setColors(frame.edgeColor, frame.innerColor);
			dlgDonut.getBtnEdgeColor().setBackground(activeEdgeColor);
			dlgDonut.getBtnInnerColor().setBackground(activeInnerColor);
			dlgDonut.setVisible(true);
			
			if(dlgDonut.getDonut() != null) 
			{
				executeAddCmd(dlgDonut.getDonut());
				activeEdgeColor = dlgDonut.getBtnEdgeColor().getBackground();
				activeInnerColor = dlgDonut.getBtnInnerColor().getBackground();
			}
			
		} else if (frame.btnShapeHexagon.isSelected()) {
			DlgHexagon dlgHexagon = new DlgHexagon();
			dlgHexagon.setPoint(mouseClick);
			dlgHexagon.setColors(frame.innerColor, frame.edgeColor);
			dlgHexagon.getBtnEdgeColor().setBackground(activeEdgeColor);
			dlgHexagon.getBtnInnerColor().setBackground(activeInnerColor);
			dlgHexagon.setVisible(true);
			
			if(dlgHexagon.getHexagonAdapter() != null) 
			{
				executeAddCmd(dlgHexagon.getHexagonAdapter());
				activeEdgeColor = dlgHexagon.getBtnEdgeColor().getBackground();
				activeInnerColor = dlgHexagon.getBtnInnerColor().getBackground();
			}
		
		}
		frame.repaint();
	}
	
	public void actionPerformedEditClick(ActionEvent e) {
		int index = model.getSelected();
		if (index == -1) return;
		
		Shape shape = model.getShape(index);
		
		if (shape instanceof Point) {
			DlgPoint dlgPoint = new DlgPoint();
			dlgPoint.setPoint((Point)shape);
			dlgPoint.getBtnEdgeColor().setBackground(shape.getColor());
			dlgPoint.setVisible(true);
			
			if(dlgPoint.getPoint() != null) {
				dlgPoint.getPoint().setSelected(true);
				UpdatePointCmd updatePointCmd = new UpdatePointCmd((Point)shape, dlgPoint.getPoint());
				updatePointCmd.execute();
				undoCommands.add(updatePointCmd);
				redoCommands.clear();
				activeEdgeColor = dlgPoint.getBtnEdgeColor().getBackground();
				logModel.addElement("Edit " + dlgPoint.getPoint().toString());
			}
		} else if (shape instanceof Line) {
			DlgLine dlgLine = new DlgLine();
			dlgLine.setLine((Line)shape);
			dlgLine.getBtnColor().setBackground(shape.getColor());
			dlgLine.setVisible(true);
			
			if(dlgLine.getLine() != null) {
				dlgLine.getLine().setSelected(true);
				UpdateLineCmd updateLineCmd = new UpdateLineCmd((Line)shape, dlgLine.getLine());
				updateLineCmd.execute();
				undoCommands.add(updateLineCmd);
				redoCommands.clear();
				activeEdgeColor = dlgLine.getBtnColor().getBackground();
				logModel.addElement("Edit " + dlgLine.getLine().toString());
			}
		} else if (shape instanceof Rectangle) {
			Rectangle rectangle = (Rectangle)shape;
			DlgRectangle dlgRectangle = new DlgRectangle();
			dlgRectangle.setRectangle(rectangle);
			dlgRectangle.getBtnEdgeColor().setBackground(rectangle.getColor());
			dlgRectangle.getBtnInnerColor().setBackground(rectangle.getInnerColor());
			dlgRectangle.setVisible(true);
			
			if(dlgRectangle.getRectangle() != null) {
				dlgRectangle.getRectangle().setSelected(true);
				UpdateRectangleCmd updateRectangleCmd = new UpdateRectangleCmd(rectangle, dlgRectangle.getRectangle());
				updateRectangleCmd.execute();
				undoCommands.add(updateRectangleCmd);
				redoCommands.clear();
				activeEdgeColor = dlgRectangle.getBtnEdgeColor().getBackground();
				activeInnerColor = dlgRectangle.getBtnInnerColor().getBackground();
				logModel.addElement("Edit " + dlgRectangle.getRectangle().toString());
			}
		
		}else if (shape instanceof Donut) {
			Donut donut = (Donut)shape;
			DlgDonut dlgDonut = new DlgDonut();
			dlgDonut.setDonut(donut);
			dlgDonut.getBtnEdgeColor().setBackground(donut.getColor());
			dlgDonut.getBtnInnerColor().setBackground(donut.getInnerColor());
			dlgDonut.setVisible(true);
			
			if(dlgDonut.getDonut() != null) {
				dlgDonut.getDonut().setSelected(true);
				UpdateDonutCmd updateDonutCmd = new UpdateDonutCmd(donut, dlgDonut.getDonut());
				updateDonutCmd.execute();
				undoCommands.add(updateDonutCmd);
				redoCommands.clear();
				activeEdgeColor = dlgDonut.getBtnEdgeColor().getBackground();
				activeInnerColor = dlgDonut.getBtnInnerColor().getBackground();
				logModel.addElement("Edit " + dlgDonut.getDonut().toString());
			}
		} else if (shape instanceof Circle) {
			Circle circle = (Circle)shape;
			DlgCircle dlgCircle = new DlgCircle();
			dlgCircle.setCircle(circle);
			dlgCircle.getBtnEdgeColor().setBackground(circle.getColor());
			dlgCircle.getBtnInnerColor().setBackground(circle.getInnerColor());
			dlgCircle.setVisible(true);
			
			if(dlgCircle.getCircle() != null) {
				dlgCircle.getCircle().setSelected(true);
				UpdateCircleCmd updateCircleCmd = new UpdateCircleCmd(circle, dlgCircle.getCircle());
				updateCircleCmd.execute();
				undoCommands.add(updateCircleCmd);
				redoCommands.clear();
				activeEdgeColor = dlgCircle.getBtnEdgeColor().getBackground();
				activeInnerColor = dlgCircle.getBtnInnerColor().getBackground();
				logModel.addElement("Edit " + dlgCircle.getCircle().toString());
			}
		} else if (shape instanceof HexagonAdapter) {
			HexagonAdapter hexagonAdapter = (HexagonAdapter)shape;
			DlgHexagon dlgHexagon = new DlgHexagon();
			dlgHexagon.setHexagonAdapter(hexagonAdapter);
			dlgHexagon.getBtnEdgeColor().setBackground(hexagonAdapter.getColor());
			dlgHexagon.getBtnInnerColor().setBackground(hexagonAdapter.getInnerColor());
			dlgHexagon.setVisible(true);
			
			if(dlgHexagon.getHexagonAdapter() != null) {
				dlgHexagon.getHexagonAdapter().setSelected(true);
				UpdateHexagonCmd updateHexagonCmd = new UpdateHexagonCmd(hexagonAdapter, dlgHexagon.getHexagonAdapter());
				updateHexagonCmd.execute();
				undoCommands.add(updateHexagonCmd);
				redoCommands.clear();
				activeEdgeColor = dlgHexagon.getBtnEdgeColor().getBackground();
				activeInnerColor = dlgHexagon.getBtnInnerColor().getBackground();
				logModel.addElement("Edit " + dlgHexagon.getHexagonAdapter().toString());
			}
		}
		frame.repaint();
	}
	
	public void actionPerformedMoveToFront() {
		if(checkNumberSelectedShapes()== 1) {
			Shape currentSelectedShape = currentSelectedShapes.get(0);
			int index = model.getShapes().indexOf(currentSelectedShape);
			if(index == model.getShapes().size()-1) {
				JOptionPane.showMessageDialog(null, "Your shape is already front!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				ToFrontCmd toFrontCmd = new ToFrontCmd(model, currentSelectedShape, index);
				toFrontCmd.execute();
				undoCommands.add(toFrontCmd);
				redoCommands.clear();
				frame.repaint();
				logModel.addElement("MoveToFront " + currentSelectedShape.toString());
			}
		}else {
			JOptionPane.showMessageDialog(null, "Please select one shape!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void actionPerformedMoveToBack() {
		if(checkNumberSelectedShapes()== 1) {
			Shape currentSelectedShape = currentSelectedShapes.get(0);
			int index = model.getShapes().indexOf(currentSelectedShape);
			if(index == 0) {
				JOptionPane.showMessageDialog(null, "Your shape is already back!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				ToBackCmd toBackCmd = new ToBackCmd(model, currentSelectedShape, index);
				toBackCmd.execute();
				undoCommands.add(toBackCmd);
				redoCommands.clear();
				frame.repaint();
				logModel.addElement("MoveToBack " + currentSelectedShape.toString());
			}
		}else {
			JOptionPane.showMessageDialog(null, "Please select one shape!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void actionPerformedBringToFront() {
		if(checkNumberSelectedShapes()== 1) {
			Shape currentSelectedShape = currentSelectedShapes.get(0);
			int index = model.getShapes().indexOf(currentSelectedShape);
			if(index == model.getShapes().size()-1) {
				JOptionPane.showMessageDialog(null, "Your shape is already front!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				BringToFrontCmd bringToFrontCmd = new BringToFrontCmd(model, currentSelectedShape, index);
				bringToFrontCmd.execute();
				undoCommands.add(bringToFrontCmd);
				redoCommands.clear();
				frame.repaint();
				logModel.addElement("BringToFront " + currentSelectedShape.toString());
			}
		}else {
			JOptionPane.showMessageDialog(null, "Please select one shape!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void actionPerformedBringToBack() {
		if(checkNumberSelectedShapes()== 1) {
			Shape currentSelectedShape = currentSelectedShapes.get(0);
			int index = model.getShapes().indexOf(currentSelectedShape);
			if(index == 0) {
				JOptionPane.showMessageDialog(null, "Your shape is already back!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				BringToBackCmd bringToBackCmd = new BringToBackCmd(model, currentSelectedShape, index);
				bringToBackCmd.execute();
				undoCommands.add(bringToBackCmd);
				redoCommands.clear();
				frame.repaint();
				logModel.addElement("BringToBack " + currentSelectedShape.toString());
			}
		}else {
			JOptionPane.showMessageDialog(null, "Please select one shape!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void actionPerformedUndo() {
		if(undoCommands.size() > 0) {
			int lastUndoCommandIndex = undoCommands.size()-1;
			Command undoCommand = undoCommands.get(lastUndoCommandIndex);
			undoCommand.unexecute();
			redoCommands.add(undoCommand);
			undoCommands.remove(lastUndoCommandIndex);
			frame.repaint();
			logModel.addElement("Undo " + undoCommand.getClass().toString().substring(14));
			notifyObservers();
		} else {
			JOptionPane.showMessageDialog(null, "Undo commands is empty!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void actionPerformedRedo() {
		if(redoCommands.size() > 0) {
			int lastRedoCommandIndex = redoCommands.size()-1;
			Command redoCommand = redoCommands.get(lastRedoCommandIndex);
			redoCommand.execute();
			undoCommands.add(redoCommand);
			redoCommands.remove(lastRedoCommandIndex);
			frame.repaint();
			logModel.addElement("Redo " + redoCommand.getClass().toString().substring(14));
			notifyObservers();
		} else {
			JOptionPane.showMessageDialog(null, "Redo commands is empty!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void actionPerformedSave() {
		JFileChooser fileChooser = new JFileChooser("C:\\Users\\ivana.IVANA\\Desktop\\provera");
		fileChooser.setDialogTitle("Save Shapes");
		int returnValue = fileChooser.showSaveDialog(frame);
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			FileStrategy saveSerialization = new FileHandler(new SaveLoadSerializationFile());
			FileStrategy saveText = new FileHandler(new SaveLoadTextFile());
			
			saveSerialization.save(fileChooser.getSelectedFile(), model, this);
			saveText.save(fileChooser.getSelectedFile(), model, this);
		}
		
	}
	
	public void actionPerformedLoad() {
		JFileChooser fileChooser = new JFileChooser("C:\\Users\\ivana.IVANA\\Desktop\\provera");
		fileChooser.setDialogTitle("Load Shapes");
		int returnValue = fileChooser.showOpenDialog(frame);
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			FileStrategy loadSerialization = new FileHandler(new SaveLoadSerializationFile());
			FileStrategy loadText = new FileHandler(new SaveLoadTextFile());
			
			if(fileChooser.getSelectedFile().getAbsolutePath().endsWith(".txt")) {
				loadText.load(fileChooser.getSelectedFile(), model, this);
				if(textFileList.size()>0) {
					frame.btnInteraction.setEnabled(true);
					setOperationLoadNext(false);
				}
			} else {
				loadSerialization.load(fileChooser.getSelectedFile(), model, this);
			}
			
		}
		frame.repaint();
		notifyObservers();
	}
	
	public void actionPerformedInteraction() {
		String[] elements;
		String[] coordinate1;
		String[] coordinate2;
		String color, innerColor, width, height, radius, innerRadius;
		String s = textFileList.get(0);
		//System.out.println(s);
		elements = s.split(" ");
		if (elements[0].equals("Add")) {
			if (elements[1].equals("Point")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				color = elements[4].split("=")[1];
				Point point = new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1]),
						new Color(Integer.parseInt(color)));
				executeAddCmd(point);

			} else if (elements[1].equals("Line")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				coordinate2 = elements[4].split("=")[1].split(",");
				color = elements[5].split("=")[1];
				Line line = new Line(new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						new Point(Integer.parseInt(coordinate2[0]), Integer.parseInt(coordinate2[1])),
						new Color(Integer.parseInt(color)));
				executeAddCmd(line);
			} else if (elements[1].equals("Rectangle")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				width = elements[4].split("=")[1];
				height = elements[5].split("=")[1];
				color = elements[6].split("=")[1];
				innerColor = elements[7].split("=")[1];
				Rectangle rectangle = new Rectangle(
						new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(width), Integer.parseInt(height), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				executeAddCmd(rectangle);
			} else if (elements[1].equals("Circle")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				radius = elements[4].split("=")[1];
				color = elements[5].split("=")[1];
				innerColor = elements[6].split("=")[1];
				Circle circle = new Circle(
						new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(radius), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				executeAddCmd(circle);
			} else if (elements[1].equals("Donut")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				radius = elements[4].split("=")[1];
				innerRadius = elements[5].split("=")[1];
				color = elements[6].split("=")[1];
				innerColor = elements[7].split("=")[1];
				Donut donut = new Donut(new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(radius), Integer.parseInt(innerRadius), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				executeAddCmd(donut);
			} else if (elements[1].equals("Hexagon")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				radius = elements[4].split("=")[1];
				color = elements[5].split("=")[1];
				innerColor = elements[6].split("=")[1];
				HexagonAdapter hexagon = new HexagonAdapter(
						new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(radius), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				executeAddCmd(hexagon);
			}
		} else if (elements[0].equals("Select")) {
			if (elements[1].equals("Point")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				color = elements[4].split("=")[1];
				Point point = new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1]),
						new Color(Integer.parseInt(color)));
				model.getShapes().get(model.getShapes().indexOf(point)).setSelected(true);
				logModel.addElement("Select " + point.toString());
			} else if (elements[1].equals("Line")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				coordinate2 = elements[4].split("=")[1].split(",");
				color = elements[5].split("=")[1];
				Line line = new Line(new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						new Point(Integer.parseInt(coordinate2[0]), Integer.parseInt(coordinate2[1])),
						new Color(Integer.parseInt(color)));
				model.getShapes().get(model.getShapes().indexOf(line)).setSelected(true);
				logModel.addElement("Select " + line.toString());
			} else if (elements[1].equals("Rectangle")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				width = elements[4].split("=")[1];
				height = elements[5].split("=")[1];
				color = elements[6].split("=")[1];
				innerColor = elements[7].split("=")[1];
				Rectangle rectangle = new Rectangle(
						new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(width), Integer.parseInt(height), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				model.getShapes().get(model.getShapes().indexOf(rectangle)).setSelected(true);
				logModel.addElement("Select " + rectangle.toString());
			} else if (elements[1].equals("Circle")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				radius = elements[4].split("=")[1];
				color = elements[5].split("=")[1];
				innerColor = elements[6].split("=")[1];
				Circle circle = new Circle(
						new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(radius), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				model.getShapes().get(model.getShapes().indexOf(circle)).setSelected(true);
				logModel.addElement("Select " + circle.toString());
			} else if (elements[1].equals("Donut")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				radius = elements[4].split("=")[1];
				innerRadius = elements[5].split("=")[1];
				color = elements[6].split("=")[1];
				innerColor = elements[7].split("=")[1];
				Donut donut = new Donut(new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(radius), Integer.parseInt(innerRadius), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				model.getShapes().get(model.getShapes().indexOf(donut)).setSelected(true);
				logModel.addElement("Select " + donut.toString());
			} else if (elements[1].equals("Hexagon")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				radius = elements[4].split("=")[1];
				color = elements[5].split("=")[1];
				innerColor = elements[6].split("=")[1];
				HexagonAdapter hexagon = new HexagonAdapter(
						new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(radius), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				model.getShapes().get(model.getShapes().indexOf(hexagon)).setSelected(true);
				logModel.addElement("Select " + hexagon.toString());
			}
		} else if (elements[0].equals("Deselect")) {
			if (elements[1].equals("Point")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				color = elements[4].split("=")[1];
				Point point = new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1]),
						new Color(Integer.parseInt(color)));
				model.getShapes().get(model.getShapes().indexOf(point)).setSelected(false);
				logModel.addElement("Deselect " + point.toString());
			} else if (elements[1].equals("Line")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				coordinate2 = elements[4].split("=")[1].split(",");
				color = elements[5].split("=")[1];
				Line line = new Line(new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						new Point(Integer.parseInt(coordinate2[0]), Integer.parseInt(coordinate2[1])),
						new Color(Integer.parseInt(color)));
				model.getShapes().get(model.getShapes().indexOf(line)).setSelected(false);
				logModel.addElement("Deselect " + line.toString());
			} else if (elements[1].equals("Rectangle")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				width = elements[4].split("=")[1];
				height = elements[5].split("=")[1];
				color = elements[6].split("=")[1];
				innerColor = elements[7].split("=")[1];
				Rectangle rectangle = new Rectangle(
						new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(width), Integer.parseInt(height), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				model.getShapes().get(model.getShapes().indexOf(rectangle)).setSelected(false);
				logModel.addElement("Deselect " + rectangle.toString());
			} else if (elements[1].equals("Circle")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				radius = elements[4].split("=")[1];
				color = elements[5].split("=")[1];
				innerColor = elements[6].split("=")[1];
				Circle circle = new Circle(
						new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(radius), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				model.getShapes().get(model.getShapes().indexOf(circle)).setSelected(false);
				logModel.addElement("Deselect " + circle.toString());
			} else if (elements[1].equals("Donut")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				radius = elements[4].split("=")[1];
				innerRadius = elements[5].split("=")[1];
				color = elements[6].split("=")[1];
				innerColor = elements[7].split("=")[1];
				Donut donut = new Donut(new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(radius), Integer.parseInt(innerRadius), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				model.getShapes().get(model.getShapes().indexOf(donut)).setSelected(false);
				logModel.addElement("Deselect " + donut.toString());
			} else if (elements[1].equals("Hexagon")) {
				coordinate1 = elements[3].split("=")[1].split(",");
				radius = elements[4].split("=")[1];
				color = elements[5].split("=")[1];
				innerColor = elements[6].split("=")[1];
				HexagonAdapter hexagon = new HexagonAdapter(
						new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(radius), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				model.getShapes().get(model.getShapes().indexOf(hexagon)).setSelected(false);
				logModel.addElement("Deselect " + hexagon.toString());
			}
		} else if (elements[0].equals("DeselectAllSelected")) {
			model.getShapes().forEach(shape -> {
				if(shape.isSelected()==true) {
					shape.setSelected(false);
				}
			});
			logModel.addElement("DeselectAllSelected");
		} else if (elements[0].equals("Edit")) {
			int index = model.getSelected();
			Shape shape = model.getShape(index);
			
			if(shape instanceof Point) {
				coordinate1 = elements[3].split("=")[1].split(",");
				color = elements[4].split("=")[1];
				Point point = new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1]),
						new Color(Integer.parseInt(color)));
				UpdatePointCmd updatePointCmd = new UpdatePointCmd((Point)shape, point);
				updatePointCmd.execute();
				undoCommands.add(updatePointCmd);
				logModel.addElement("Edit " + point.toString());
			} else if(shape instanceof Line) {
				coordinate1 = elements[3].split("=")[1].split(",");
				coordinate2 = elements[4].split("=")[1].split(",");
				color = elements[5].split("=")[1];
				Line line = new Line(new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						new Point(Integer.parseInt(coordinate2[0]), Integer.parseInt(coordinate2[1])),
						new Color(Integer.parseInt(color)));
				UpdateLineCmd updateLineCmd = new UpdateLineCmd((Line)shape, line);
				updateLineCmd.execute();
				undoCommands.add(updateLineCmd);
				logModel.addElement("Edit " + line.toString());
			} else if(shape instanceof Rectangle) {
				coordinate1 = elements[3].split("=")[1].split(",");
				width = elements[4].split("=")[1];
				height = elements[5].split("=")[1];
				color = elements[6].split("=")[1];
				innerColor = elements[7].split("=")[1];
				Rectangle rectangle = new Rectangle(
						new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(width), Integer.parseInt(height), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				UpdateRectangleCmd updateRectangleCmd = new UpdateRectangleCmd((Rectangle)shape, rectangle);
				updateRectangleCmd.execute();
				undoCommands.add(updateRectangleCmd);
				logModel.addElement("Edit " + rectangle.toString());
			} else if (shape instanceof Donut) {
				coordinate1 = elements[3].split("=")[1].split(",");
				radius = elements[4].split("=")[1];
				innerRadius = elements[5].split("=")[1];
				color = elements[6].split("=")[1];
				innerColor = elements[7].split("=")[1];
				Donut donut = new Donut(new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(radius), Integer.parseInt(innerRadius), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				UpdateDonutCmd updateDonutCmd = new UpdateDonutCmd((Donut)shape, donut);
				updateDonutCmd.execute();
				undoCommands.add(updateDonutCmd);
				logModel.addElement("Edit " + donut.toString());
			} else if (shape instanceof Circle) {
				coordinate1 = elements[3].split("=")[1].split(",");
				radius = elements[4].split("=")[1];
				color = elements[5].split("=")[1];
				innerColor = elements[6].split("=")[1];
				Circle circle = new Circle(
						new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(radius), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				UpdateCircleCmd updateCircleCmd = new UpdateCircleCmd((Circle)shape, circle);
				updateCircleCmd.execute();
				undoCommands.add(updateCircleCmd);
				logModel.addElement("Edit " + circle.toString());
			} else if (shape instanceof HexagonAdapter) {
				coordinate1 = elements[3].split("=")[1].split(",");
				radius = elements[4].split("=")[1];
				color = elements[5].split("=")[1];
				innerColor = elements[6].split("=")[1];
				HexagonAdapter hexagon = new HexagonAdapter(
						new Point(Integer.parseInt(coordinate1[0]), Integer.parseInt(coordinate1[1])),
						Integer.parseInt(radius), new Color(Integer.parseInt(color)),
						new Color(Integer.parseInt(innerColor)));
				UpdateHexagonCmd updateHexagonCmd = new UpdateHexagonCmd((HexagonAdapter)shape, hexagon);
				updateHexagonCmd.execute();
				undoCommands.add(updateHexagonCmd);
				logModel.addElement("Edit " + hexagon.toString());
			}
		} else if (elements[0].equals("Delete")) {
			RemoveShapeCmd removeShapeCmd = new RemoveShapeCmd(model);
			logModel.addElement("Delete " + checkNumberSelectedShapes() + " selected shapes");
			removeShapeCmd.execute();
			undoCommands.add(removeShapeCmd);
		} else if (elements[0].equals("MoveToFront")) {
			actionPerformedMoveToFront();
		} else if (elements[0].equals("MoveToBack")) {
			actionPerformedMoveToBack();
		} else if (elements[0].equals("BringToFront")) {
			actionPerformedBringToFront();
		} else if (elements[0].equals("BringToBack")) {
			actionPerformedBringToBack();
		} else if (elements[0].equals("Undo")) {
			actionPerformedUndo();
		} else if (elements[0].equals("Redo")) {
			actionPerformedRedo();
		}
		
		if(textFileList.size() == 1) {
			frame.btnInteraction.setEnabled(false);
			setOperationLoadNext(true);
		}
		
		textFileList.remove(0);
		frame.repaint();
		//notifyObservers();
	}
	
	public void actionPerformedDeleteClick(ActionEvent e) {
		if (model.isEmpty()) return;
		if (checkNumberSelectedShapes() > 0 && JOptionPane.showConfirmDialog(null, "Do you really want to delete selected shape?", "Yes", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
			RemoveShapeCmd removeShapeCmd = new RemoveShapeCmd(model);
			logModel.addElement("Delete " + checkNumberSelectedShapes() + " selected shapes");
			removeShapeCmd.execute();
			undoCommands.add(removeShapeCmd);
			System.out.println(checkNumberSelectedShapes());
			notifyObservers();
		}
		frame.repaint();
	}
	
	public void actionPerformedEdgeClick(ActionEvent e) {
		frame.edgeColor = JColorChooser.showDialog(null, "Choose edge color", frame.edgeColor);
		if (frame.edgeColor == null) frame.edgeColor = Color.BLACK;
	}
	
	public void actionPerformedInnerClick(ActionEvent e) {
		frame.innerColor = JColorChooser.showDialog(null, "Choose inner color", frame.innerColor);
		if (frame.innerColor == null) frame.innerColor = Color.WHITE;
	}
	
	public void setOperationDrawing() {
		activeOperation = OPERATION_DRAWING;

		deselect();
		notifyObservers();
	    frame.repaint();
		
		//frame.btnActionEdit.setEnabled(false);
		//frame.btnActionDelete.setEnabled(false);
		
		frame.btnShapePoint.setEnabled(true);
		frame.btnShapeLine.setEnabled(true);
		frame.btnShapeRectangle.setEnabled(true);
		frame.btnShapeCircle.setEnabled(true);
		frame.btnShapeDonut.setEnabled(true);
		frame.btnShapeHexagon.setEnabled(true);
		
		frame.btnColorEdge.setEnabled(true);
		frame.btnColorInner.setEnabled(true);
	}
	
	public void setOperationEditDelete() {
		activeOperation = OPERATION_EDIT_DELETE;
		
		//frame.btnActionEdit.setEnabled(true);
		//frame.btnActionDelete.setEnabled(true);
		
		frame.btnShapePoint.setEnabled(false);
		frame.btnShapeLine.setEnabled(false);
		frame.btnShapeRectangle.setEnabled(false);
		frame.btnShapeCircle.setEnabled(false);
		frame.btnShapeDonut.setEnabled(false);
		frame.btnShapeHexagon.setEnabled(false);
		
		frame.btnColorEdge.setEnabled(false);
		frame.btnColorInner.setEnabled(false);
	}
	
	public void setOperationLoadNext(boolean enable) {
		
		frame.btnOperationDrawing.setEnabled(enable);
		frame.btnOperationEditOrDelete.setEnabled(enable);
		
		frame.btnShapePoint.setEnabled(enable);
		frame.btnShapeLine.setEnabled(enable);
		frame.btnShapeRectangle.setEnabled(enable);
		frame.btnShapeCircle.setEnabled(enable);
		frame.btnShapeDonut.setEnabled(enable);
		frame.btnShapeHexagon.setEnabled(enable);
		
		frame.btnUndo.setEnabled(enable);
		frame.btnRedo.setEnabled(enable);
		
		notifyObservers();
	}

	@Override
	public void addObserver(Observer observer) {
		observers.add(observer);		
	}

	@Override
	public void removeObserver(Observer observer) {
		observers.remove(observer);
		
	}

	@Override
	public void notifyObservers() {
		if(checkNumberSelectedShapes() == 0) {
			deleteBtnEnabled = false;
			modifyBtnEnabled = false;
		} else if (checkNumberSelectedShapes() == 1) {
			deleteBtnEnabled = true;
			modifyBtnEnabled = true;
		} else if(checkNumberSelectedShapes() > 1) {
			deleteBtnEnabled = true;
			modifyBtnEnabled = false;
		}
		
		Iterator<Observer> it = observers.iterator();
		while(it.hasNext()) {
			it.next().update(deleteBtnEnabled, modifyBtnEnabled);
		}
		
	}

	public DefaultListModel<String> getLogModel() {
		return logModel;
	}

	public void setLogModel(DefaultListModel<String> logModel) {
		this.logModel = logModel;
	}

	public List<String> getTextFileList() {
		return textFileList;
	}

	public void setTextFileList(List<String> textFileList) {
		this.textFileList = textFileList;
	}
	
	
	
	
	

}
