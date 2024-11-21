/**
*Class for managing the menu.
*Creates the menu subsequently adds the various menu for drawing and editing.
*The submenus and subMenuItems are added with their respective actionListeners and 
*keystroke Action listeners. 
*
*The actionlisteners throws exceptions if the user tries to skip adding values
*for name or author of the drawing. Canceling does nothing.
*
* @author Johan Boström 2207
* @version 1.0
* @since 2024-10-25
*/
package se.miun.id2207.dt187g.jpaint.gui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import se.miun.id2207.dt187g.jpaint.Drawing;
import se.miun.id2207.dt187g.jpaint.geometry.Circle;
import se.miun.id2207.dt187g.jpaint.geometry.Rectangle;
import se.miun.id2207.dt187g.jpaint.geometry.Shape;

public class MenuManager {
    private JPaintFrame frame;
    private DrawingPanel drawingPanel;
    private Menu menu;



    public MenuManager(JPaintFrame frame, DrawingPanel drawingPanel) throws DrawingException {
        this.frame = frame;
        this.drawingPanel = drawingPanel;
        this.menu = new Menu();
        createMenu();
    }
    //returns the menu
    public Menu getMenu() {
        return menu;
    }
	//calls the other constructors which creates the menus and adds them.
    private void createMenu() throws DrawingException {
        createFileMenu();
        createEditMenu();
        createFilterMenu(); // Empty for now
    }
	//Constructor which calls to the different constructor in the Menu class
	//in order to add menus and menuItems with ActionListener and keystroke ActionListeners.
    private void createFileMenu() throws DrawingException {
		String sFile = "File";
		menu.addJMenu(sFile);
		menu.getJMenu(0).setMnemonic(KeyEvent.VK_F);

		menu.addJMenuItem(sFile, "New...", createNewDrawingAction(),
				KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		menu.addJMenuItem(sFile, "Load from Database", createLoadAction(),
				KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
		menu.addJMenuItem(sFile, "Save to Database", createSaveAction(),
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		menu.addJMenuItem(sFile, "Info", showInfoAction());

		menu.getJMenu(0).addSeparator();
		menu.addJMenuItem(sFile, "Exit", al -> System.exit(0));

	}
	//creates the Editmenu and adds its content menus and itemMenus to it.
    private void createEditMenu() {
		String sEdit = "Edit";
		String sDrawing = "Drawing";
		menu.addJMenu(sEdit);
		menu.addSubJMenu(sEdit, sDrawing);
		menu.getJMenu(1).setMnemonic(KeyEvent.VK_E);

		menu.addJMenuItem(sEdit, "Undo", createUndoAction(),
				KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
		menu.addJMenuItem(sDrawing, "Name...", createChangeNameAction());
		menu.addJMenuItem(sDrawing, "Author...", createChangeAuthorAction());
		//causes a nullexception which is handled and thus not added.
		menu.addJMenuItem("This JMenu doesn't exist", "abc");

	}

	//Radiobutton where only one of the three buttons added can be chosen at a time.
	//Used to filter which shapes the drawing is showing. can be all, only circles or only rectangles.
    private void createFilterMenu() {
		//Button for showing all shapes.
		JRadioButton allButton = new JRadioButton("All");
		//Default is the all button. shows all shapes.
		allButton.setSelected(true);
		//button for choosing to only show Circles.
		JRadioButton circleButton = new JRadioButton("Circle");
		//button for choosing to only show rectangles.
		JRadioButton rectangleButton = new JRadioButton("Rectangle");
		//sets the defaults value to true which does not filter any shapes from the drawing.
		drawingPanel.setShapeFilter(shape -> true);
		
		//same as the default value, does not filter any shapes. 
		//Meaning all the drawn shapes will be drawn.
		allButton.addActionListener(event -> {
			drawingPanel.setShapeFilter(shape -> true);
		});
		//sets the filter to filter for only circle shapes.
		//Meaning will only show drawn circle shapes
		circleButton.addActionListener(event -> {
			drawingPanel.setShapeFilter(shape -> shape instanceof Circle);
		});
		//sets the filter to filter for only rectangle shapes.
		//Meaning will only show drawn rectangle shapes.
		rectangleButton.addActionListener(event -> {
			drawingPanel.setShapeFilter(shape -> shape instanceof Rectangle);
		});
		@SuppressWarnings("serial") List<JRadioButton> radioButtons = new ArrayList<JRadioButton>() {
			{
				add(allButton);
				add(circleButton);
				add(rectangleButton);
			}
		};
		JMenu jMenu = new JMenu("Filter");
		ButtonGroup group = new ButtonGroup();
		for (var rb : radioButtons) {
			jMenu.add(rb);
			group.add(rb);
		}
		menu.add(jMenu);
	}
    
	//ActionListener for creating a new drawing. throws an exception with 
	//corresponding error message if the name and author is not filled in. 
	//If name and author is filled in, creates new drawing and updates header accordingly.
    private ActionListener createNewDrawingAction() throws DrawingException {
		return al -> {
			//input dialog box for user input to set name of the drawing	
			String name = JOptionPane.showInputDialog(drawingPanel, 
			"Enter name of the drawing:",
			 "Input", JOptionPane.QUESTION_MESSAGE);
			//input dialog box for user input to set author of the drawing			
			String author = JOptionPane.showInputDialog(drawingPanel, 
			"Enter name of the author:",
			 "Input", JOptionPane.QUESTION_MESSAGE);
			
			//If the user press cancel resulting in a null value, do nothing.
			if (name == null || author == null) {
				return;
			}
			//Tries to create a new drawing, if an exception is thrown, error message is shown.
			try {
				Drawing newDrawing = new Drawing(name, author);
				//sets the new empty drawing as drawing.
				drawingPanel.setDrawing(newDrawing);
				//updates the frame with the new name and author
				frame.updateHeader();
				//repaint the drawingPanel to the new empty drawing.
				drawingPanel.repaint();
			} catch (DrawingException e) {
				JOptionPane.showMessageDialog(drawingPanel, 
				"Both name and author needs to be filled in",
				 "JPaint", JOptionPane.ERROR_MESSAGE);
			}
		};
	}
	//ActionListener for updating the name of the drawing. Throws an exception with corresponding 
	//error message if a new name is not filled in. 
	//If name is filled in, updates the name of the drawing and the header accordingly
    private ActionListener createChangeNameAction() {
		return al -> {
			System.out.println("Change name action triggered");
			//input dialog box for user input to change name of the drawing	
			String name = JOptionPane.showInputDialog(drawingPanel, 
			"Change name of the drawing:",
			"Input", JOptionPane.QUESTION_MESSAGE);
			
			//If the user press cancel resulting in a null value, do nothing.
			if (name == null) {
				return;
			}
			//Tries to set the name, if exception is thrown, show error message.
			try {
				//sets the new name of the drawing
				drawingPanel.getDrawing().setName(name);
				//updates the frame to the new name
				frame.updateHeader();
			} catch (DrawingException e) {
				//shows an messageDialog to the user with the error message.
				JOptionPane.showMessageDialog(drawingPanel, e.getMessage(), 
                "JPaint", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
	
		};
	}
	//ActionListener for updating the author of the drawing. Throws an exception 
	//with corresponding error message if a new author is not filled in. 
	//If author is filled in, updates the author of the drawing and the header accordingly
	private ActionListener createChangeAuthorAction() {
		return al -> {
			//input dialog box for user input to change name of the drawing	
			String author = JOptionPane.showInputDialog(drawingPanel, 
			"Change author of the drawing:",
			"Input", JOptionPane.QUESTION_MESSAGE);
			
			//If the user press cancel resulting in a null value, do nothing.
			if (author == null) {
				return;
			}

			//tries to set author to new value, throws exception.
			try {
				//sets the new author of the drawing
				drawingPanel.getDrawing().setAuthor(author);
				//updates the frame to the new author	
				frame.updateHeader();
			} catch (DrawingException e) {
				//shows an messageDialog to the user with the error message.
				JOptionPane.showMessageDialog(drawingPanel, e.getMessage(), 
                "JPaint", JOptionPane.ERROR_MESSAGE);
				//Prints the error 
				e.printStackTrace();
			}
			
		};
	}

	//removes the latest drawn shape when pressed. Will do so as long as there are 
	//shapes in the drawingList
	private ActionListener createUndoAction() {
		return al -> {
			//Initialize the drawing as the drawing in the drawingPanel.
			Drawing drawing = drawingPanel.getDrawing();
			//as long as there is a shape to remove, remove it.
			if (drawing.getSize() > 0){
				//removes the latest shape
				drawing.removeShape(drawing.getSize()-1);
				//repaints the drawing.
				drawingPanel.repaint();
			}
		};
	}
	
	//when the infoItem is pressed shows information about the painting.
	private ActionListener showInfoAction() {
		return al -> {
			//If there is no name shows Unnamed drawing, otherwise shows given name.
			String name;
			if(drawingPanel.getDrawing().getName() == null){
				name = "[Unnamed Drawing]";
			}
			else {
				name = drawingPanel.getDrawing().getName();
			}

			//formats the totalArea and totalCircumference to just display two decimals.
			String totalArea = String.format("%.2f", drawingPanel.getDrawing().getTotalArea());
        	String totalCircumference = String.format("%.2f", 
			drawingPanel.getDrawing().getTotalCircumference());

			//Shows name of drawing, number of shapes drawn, the total area and circumference 
			//of these shapes.
			JOptionPane.showMessageDialog(drawingPanel, 
			name + System.lineSeparator() + 
			"Number of Shapes: " + drawingPanel.getDrawing().getSize() + System.lineSeparator() +
			"Total Area: " + totalArea + System.lineSeparator() + 
			"Total Circumference: " + totalCircumference,
			"Info", JOptionPane.INFORMATION_MESSAGE);
		};
	}

	private ActionListener createLoadAction() {
		return al -> {
			//User input for the name of the drawing user wants to load.
			String name = JOptionPane.showInputDialog(drawingPanel, 
			"Type the name of the drawing you would like to load: ",
			"Name input", JOptionPane.QUESTION_MESSAGE);
			//If the user press cancel resulting in a null value, do nothing.
			if (name == null) return;
			//User input for the author of the drawing user wants to load.
			String author = JOptionPane.showInputDialog(drawingPanel, 
			"Type the author of the drawing you would like to load: ",
			"Author input", JOptionPane.QUESTION_MESSAGE);
			//If the user press cancel resulting in a null value, do nothing.
			if (author == null) return;

			try{
				//Initiates loadedDrawing to the drawing corresponding to the give name and author.
				Drawing loadedDrawing = new FileHandler().Load(name, author);
				//sets the drawing to the loaded drawing.
				drawingPanel.setDrawing(loadedDrawing);
				//updates the header to the new name and author.
				frame.updateHeader();
				//repaints with the loaded drawing.
				drawingPanel.repaint();
				//Shows information JOptionPane to the user informing the drawing has been loaded.
				JOptionPane.showMessageDialog(null, 
				"Your Drawing " + name + " by " + author + " has been loaded!", 
				"Drawing Loaded!", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e){
				//error message for debugging
				System.out.println("Error loading drawing " + e.getMessage());
				//error message for user
				JOptionPane.showMessageDialog(drawingPanel, 
        		"An error occurred while loading the drawing: " + e.getMessage(), 
        		"Error", JOptionPane.ERROR_MESSAGE);
            	e.getStackTrace();
			}
			
		};
	}


	/*From assignment 6
	private ActionListener createLoadAction() {
		return al -> {
			//initialize a JFileChooser.
			JFileChooser fileChooser = new JFileChooser();
			//sets the filter to only show .shape files.
            fileChooser.setFileFilter(new FileNameExtensionFilter("Shape Filter", 
			"shape"));
            //Opens a dialog window for the user to choose a drawing file to load.
			int result = fileChooser.showOpenDialog(null);

			//When the user chooses a file to load the drawing is updated to the loaded file.
            if (result == JFileChooser.APPROVE_OPTION) {
                //inizialize the selected file as the file chosen by the user.
				File selectedFile = fileChooser.getSelectedFile();
				//initialze the chosen file directory path as a String.
				String filePath = selectedFile.getAbsolutePath();
				//Creates a new FileHandler in order to call the load method with the selected file path
				//as the fileName
                Drawing loadedDrawing = new FileHandler().load(filePath);
                
				//Checks in case there is a null value for the loaded drawing.
                if (loadedDrawing != null) {
                    //sets the drawing in drawingPanel to the loaded drawing.
					drawingPanel.setDrawing(loadedDrawing);
                	//Updates header loaded drawing values.
					frame.updateHeader();
					//Repaints the drawingPanel with the loaded drawing.
					drawingPanel.repaint();
                } else {
					//In the case where the file the user chose is not able to load, 
					//send a error message.
                    JOptionPane.showMessageDialog(null, 
					"Unable to load drawing", "Error loading drawing", 
					JOptionPane.ERROR_MESSAGE);
                }
            }
		};
	}
	*/

	private ActionListener createSaveAction() {
		return al -> {
			Drawing drawing = drawingPanel.getDrawing();
			//makes sure there is an name for the painting before saving
			if (drawing.getName() == null) {
				//input dialog box for user input to change name of the drawing	
				String name = JOptionPane.showInputDialog(drawingPanel, 
				"The drawing needs a name in order to save: ",
				"Input", JOptionPane.QUESTION_MESSAGE);
			
				//If the user press cancel resulting in a null value, do nothing.
				if (name == null) return;
				//Tries to set the name, if exception is thrown, show error message.
				try {
					//sets the new name of the drawing
					drawing.setName(name);
					//updates the frame to the new name
					frame.updateHeader();
				} catch (DrawingException e) {
					//shows an messageDialog to the user with the error message.
					JOptionPane.showMessageDialog(drawingPanel, e.getMessage(), 
                	"JPaint", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
			//makes sure there is an author of the painting before saving.
			if(drawing.getAuthor() == null){
				//input dialog box for user input to change name of the drawing.
				String author = JOptionPane.showInputDialog(drawingPanel, 
				"The drawing needs an Author in order to save: ",
				"Input", JOptionPane.QUESTION_MESSAGE);
			
				//If the user press cancel resulting in a null value, do nothing.
				if (author == null) return;

				//tries to set author to new value, throws exception.
				try {
					//sets the new author of the drawing
					drawing.setAuthor(author);
					//updates the frame to the new author	
					frame.updateHeader();
				} catch (DrawingException e) {
					//shows an messageDialog to the user with the error message.
					JOptionPane.showMessageDialog(drawingPanel, e.getMessage(), 
                	"JPaint", JOptionPane.ERROR_MESSAGE);
					//Prints the error 
					e.printStackTrace();
				}
			}
			//calls the Save method in Filehandler class to save file.
			new FileHandler().Save(drawing);
		};
	}

	/*
	private ActionListener createSaveAction() {
		return al -> {
			//initialize the drawing draw.
			Drawing drawing = drawingPanel.getDrawing();
			//initialize a JFileChooser.
            JFileChooser fileChooser = new JFileChooser();
			//sets the filter to only show .shape files.
            fileChooser.setFileFilter(new FileNameExtensionFilter("Shape Filter",
			 "shape"));
            //Opens a dialog window for the user to choose a drawing file to save.
			int result = fileChooser.showSaveDialog(null);

			//When the user chooses a file to save the drawing to.
            if (result == JFileChooser.APPROVE_OPTION) {
				//inizialize the selected file as the file chosen by the user.
                File selectedFile = fileChooser.getSelectedFile();
				//initialze the chosen file directory path as a String.
                String filePath = selectedFile.getAbsolutePath();

                // Ensures the file has the ".shape" extension, otherwise adds it.
                if (!filePath.endsWith(".shape")) {
                    filePath += ".shape";
                }

				//Calls the FileHandler class save method to save the file.
                new FileHandler().save(drawing, filePath);
				//Shows a JOptionPane informing the user the file has been saved.
                JOptionPane.showMessageDialog(null, 
				"Your Drawing has been saved!", 
				"Drawing Saved!", JOptionPane.INFORMATION_MESSAGE);
			}
		};
	}
*/

    
}
