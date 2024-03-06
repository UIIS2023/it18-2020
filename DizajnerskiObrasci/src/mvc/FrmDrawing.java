//frame
package mvc;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
/*import javax.swing.border.TitledBorder;

import drawing.DlgCircle;
import drawing.DlgDonut;
import drawing.DlgLine;
import drawing.DlgPoint;
import drawing.DlgRectangle;*/
import geometry.Circle;
import geometry.Donut;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;
import observer.Observer;

//import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;

//import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;

import java.awt.Component;
//import javax.swing.border.LineBorder;
//import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/*import java.util.ArrayList;
import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;*/
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JScrollPane;
import javax.swing.JList;

public class FrmDrawing extends JFrame implements Observer {
	
	private PnlDrawing pnlDrawing = new PnlDrawing();
	private DrawingController controller;
	
	public PnlDrawing getPnlDrawing() {
		return pnlDrawing;
	}

	public void setController(DrawingController controller) {
		this.controller = controller;
	}

	public ButtonGroup btnsOperation = new ButtonGroup();
	public ButtonGroup btnsShapes = new ButtonGroup();
	public JToggleButton btnOperationDrawing = new JToggleButton("Drawing");
	public JToggleButton btnOperationEditOrDelete = new JToggleButton("Select");
	public JButton btnActionEdit = new JButton("Modify");
	public JButton btnActionDelete = new JButton("Delete");
	public JToggleButton btnShapePoint = new JToggleButton("Point");
	public JToggleButton btnShapeLine = new JToggleButton("Line");
	public JToggleButton btnShapeRectangle = new JToggleButton("Rectangle");
	public JToggleButton btnShapeCircle = new JToggleButton("Circle");
	public JToggleButton btnShapeDonut = new JToggleButton("Donut");
	public JButton btnColorEdge = new JButton("Edge color");
	public JButton btnColorInner = new JButton("Inner color");
	public JToggleButton btnShapeHexagon = new JToggleButton("Hexagon");
	

	public Color edgeColor = Color.BLACK, innerColor = Color.WHITE;
	
	
	
	private JPanel contentPane;
	private final JPanel panel_3 = new JPanel();
	public JButton btnToFront = new JButton("To Front");
	public JButton btnToBack = new JButton("To Back");
	public JButton btnBringToFront = new JButton("Bring To Front");
	public JButton btnBringToBack = new JButton("Bring To Back");
	private final JPanel panel_5 = new JPanel();
	public JButton btnUndo = new JButton("Undo");
	public JButton btnRedo = new JButton("Redo");
	private final JPanel panel = new JPanel();
	private final JList<String> list = new JList<String>();
	public DefaultListModel<String> logModel = new DefaultListModel<String>();
	public JButton btnSave = new JButton("Save");
	public JButton btnLoad = new JButton("Load");
	public JButton btnInteraction = new JButton("Load Next");

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public FrmDrawing() {
		setTitle("Ivana Mladenovic, IT 18/2020");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 800);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(1100, 800));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		pnlDrawing.addMouseListener(pnlDrawingClickListener());
		
		contentPane.add(pnlDrawing, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new GridLayout(4, 0, 0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(237, 252, 243));
		panel_2.setBorder(new MatteBorder(3, 3, 0, 3, (Color) new Color(0, 0, 0)));
		panel_1.add(panel_2);
		
		btnOperationDrawing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setOperationDrawing();
			}
		});
		btnOperationDrawing.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsOperation.add(btnOperationDrawing);
		btnOperationDrawing.setPreferredSize(new Dimension(90,25));
		
		btnOperationDrawing.setSelected(true);
		
		btnOperationEditOrDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setOperationEditDelete();
			}
		});
		btnOperationEditOrDelete.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsOperation.add(btnOperationEditOrDelete);
		btnOperationEditOrDelete.setPreferredSize(new Dimension(90,25));
		
		btnActionDelete.addActionListener(btnActionDeleteClickListener());
		btnActionDelete.setEnabled(false);
		
		btnActionEdit.addActionListener(btnActionEditClickListener());
		btnActionEdit.setEnabled(false);
		
		btnActionEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnActionEdit.setPreferredSize(new Dimension(90,25));
		btnActionDelete.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnActionDelete.setPreferredSize(new Dimension(90,25));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnActionEdit, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
						.addComponent(btnOperationDrawing, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
					.addGap(35)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnActionDelete, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
						.addComponent(btnOperationEditOrDelete, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
					.addGap(20))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(40)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOperationEditOrDelete, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnOperationDrawing, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(40)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnActionDelete, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnActionEdit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(29, Short.MAX_VALUE))
		);
		panel_2.setLayout(gl_panel_2);
		
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(237, 252, 243));
		panel_4.setBorder(new MatteBorder(0, 3, 0, 3, (Color) new Color(0, 0, 0)));
		panel_4.setSize(200, 1200);
		panel_1.add(panel_4);
		
		btnShapePoint.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsShapes.add(btnShapePoint);
		btnShapePoint.setPreferredSize(new Dimension(200,20));
		
		btnShapeLine.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsShapes.add(btnShapeLine);
		btnShapeLine.setPreferredSize(new Dimension(200,20));
		
		btnShapeRectangle.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsShapes.add(btnShapeRectangle);
		btnShapeRectangle.setPreferredSize(new Dimension(200,20));
		
		btnShapeCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsShapes.add(btnShapeCircle);
		btnShapeCircle.setPreferredSize(new Dimension(200,20));
		
		btnShapeDonut.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsShapes.add(btnShapeDonut);
		btnShapeDonut.setPreferredSize(new Dimension(200,20));
		
		btnShapeHexagon.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsShapes.add(btnShapeHexagon);
		btnShapeHexagon.setPreferredSize(new Dimension(200,20));
		
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panel_4.createSequentialGroup()
							.addGap(5)
							.addComponent(btnShapePoint, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel_4.createSequentialGroup()
							.addGap(5)
							.addComponent(btnShapeLine, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel_4.createSequentialGroup()
							.addGap(5)
							.addComponent(btnShapeRectangle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel_4.createSequentialGroup()
							.addGap(5)
							.addComponent(btnShapeCircle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(5)
							.addComponent(btnShapeHexagon, GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel_4.createSequentialGroup()
							.addGap(5)
							.addComponent(btnShapeDonut, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.BASELINE)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGap(5)
					.addComponent(btnShapePoint, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnShapeLine, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnShapeRectangle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnShapeCircle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnShapeDonut, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnShapeHexagon, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(20, Short.MAX_VALUE))
		);
		panel_4.setLayout(gl_panel_4);	
		panel_3.setBackground(new Color(237, 252, 243));
		panel_3.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(0, 0, 0)));
		
		panel_1.add(panel_3);
		btnToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformedMoveToFront();
			}
		});
		
		btnToFront.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnToFront.setPreferredSize(new Dimension(200,20));
		//btnToFront.setEnabled(false);
		
		btnToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformedMoveToBack();
			}
		});
		
		btnToBack.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnToBack.setPreferredSize(new Dimension(200,20));
		//btnToBack.setEnabled(false);
		
		btnBringToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformedBringToFront();
			}
		});
		
		btnBringToFront.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnBringToFront.setPreferredSize(new Dimension(200,20));
		//btnBringToFront.setEnabled(false);
		
		btnBringToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformedBringToBack();
			}
		});
		
		btnBringToBack.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnBringToBack.setPreferredSize(new Dimension(200,20));
		//btnBringToBack.setEnabled(false);
		
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panel_3.createSequentialGroup()
							.addGap(5)
							.addComponent(btnToFront, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel_3.createSequentialGroup()
							.addGap(5)
							.addComponent(btnToBack, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel_3.createSequentialGroup()
							.addGap(5)
							.addComponent(btnBringToFront, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel_3.createSequentialGroup()
							.addGap(5)
							.addComponent(btnBringToBack, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addContainerGap())
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.BASELINE)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(32)
					.addComponent(btnToFront, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnToBack, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnBringToFront, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnBringToBack, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addContainerGap(20, Short.MAX_VALUE))
		);
		panel_3.setLayout(gl_panel_3);
		panel_5.setBackground(new Color(237, 252, 243));
		panel_5.setBorder(new MatteBorder(0, 3, 3, 3, (Color) new Color(0, 0, 0)));
		
		panel_1.add(panel_5);
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformedUndo();
			}
		});
		
		btnUndo.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnUndo.setPreferredSize(new Dimension(90,25));
		
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformedRedo();
			}
		});
		
		btnRedo.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnRedo.setPreferredSize(new Dimension(90,25));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformedSave();
			}
		});
		
		btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSave.setPreferredSize(new Dimension(90,25));
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformedLoad();
			}
		});
		
		btnLoad.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLoad.setPreferredSize(new Dimension(90,25));
		
		btnInteraction.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnInteraction.setPreferredSize(new Dimension(90,25));
		btnInteraction.setEnabled(false);
		btnInteraction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformedInteraction();
			}
		});
		
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnUndo, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
						.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
					.addGap(35)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnRedo, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
						.addComponent(btnLoad, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
					.addGap(20))
				.addGroup(gl_panel_5.createSequentialGroup()
					.addGap(70)
					.addComponent(btnInteraction, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(84, Short.MAX_VALUE))
		);
		gl_panel_5.setVerticalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnUndo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnRedo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(10)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnLoad, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addComponent(btnInteraction)
					.addContainerGap(40, Short.MAX_VALUE))
		);
		
		panel_5.setLayout(gl_panel_5);
		panel.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(0, 0, 0)));
		panel.setBackground(new Color(237, 252, 243));
		
		contentPane.add(panel, BorderLayout.SOUTH);
		
		panel.setPreferredSize(new Dimension(100,120));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		scrollPane.setViewportView(list);
		scrollPane.setPreferredSize(new Dimension(800, 105));
		list.setModel(logModel);
	}
	
	private MouseAdapter pnlDrawingClickListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {	
				controller.mouseClicked(e);
			}
		};
	}
	
	private ActionListener btnActionEditClickListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformedEditClick(e);
			}
		};
	}
	
	private ActionListener btnActionDeleteClickListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformedDeleteClick(e);
			}
		};
	}
	

	private ActionListener btnColorEdgeClickListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformedEdgeClick(e);
			}
		};
	}
	
	private ActionListener btnColorInnerClickListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformedInnerClick(e);
			}
		};
	}

	@Override
	public void update(boolean deleteBtnEnabled, boolean modifyBtnEnabled) {
		btnActionDelete.setEnabled(deleteBtnEnabled);
		btnActionEdit.setEnabled(modifyBtnEnabled);
		
	}
	
	
}
