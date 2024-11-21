/**
*This class is the mainframe for the app.
*by determining the size of the app and adding the classes 
*which contains the different parts it acts as a container 
*
*Methods for capturing mouse movements and actions is
*used in order to give the user the ability to choose
*between colors and shapes to draw with.
*
* @author Johan Boström 2207
* @version 1.0
*/
package se.miun.id2207.dt187g.jpaint.gui;

import se.miun.id2207.dt187g.jpaint.Drawing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JPaintFrame extends JFrame {
	//declares the private variables for the class
	private Container c = this.getContentPane();
	private StatusBarPanel statusBarPanel;
	private DrawingPanel drawingPanel;
	private ColorPalettePanel colorPalettePanel;
	private JComboBox<String> shapesCombo;
	private String shapeRectangle;
	private String shapeCircle;
	private String drawingTitle;
	public final String APP_NAME = "JPaint";

	//Initiates the main frame by calling the method init
	//Which contains the panels and containers making the main frame
	public JPaintFrame() throws DrawingException {
		init();
    }

	private void init() throws DrawingException {
        //Sets the title of the frame
		this.setTitle(APP_NAME);
		//Sets the size of the Frame
		this.setSize(500,500);
		//gets the icon image from the images folder
		this.setIconImage(new ImageIcon(getClass().getResource("/images/dolphinIcon.png")).getImage());
		//Closes the window with the X in the corner, calls System.Exit(0) change in future assignments depending.
		//as this does not give the option to save what ever drawing or progress has been made.
		this.setDefaultCloseOperation(JPaintFrame.EXIT_ON_CLOSE);
		//set the location of the window to the center of the screen
		this.setLocationRelativeTo(null);

		//creates the top panel to be used for our colorPalettePanel and JComboBox
		JPanel topPanel = new JPanel();
		//sets the preffered height an skips the width as this will be determined by the parent mainFrame
		//or changed by the user
		topPanel.setPreferredSize(new Dimension(0, 50));
		//sets the layout to borderlayout for the topPanel
		topPanel.setLayout(new BorderLayout());
		
		//Initilize our colorPalettePanel with a new object
		colorPalettePanel = new ColorPalettePanel();
		//adds the colorPalettePanel to the topPanel
		topPanel.add(colorPalettePanel);

		//Initilize the shapeCircle with a circle string
		shapeCircle = "Circle";
		//initialize the shapeRectangle with a Rectangle string
		shapeRectangle = "Rectangle";
		//creates and initializes a JComboBox.
		shapesCombo = new JComboBox<String>();
		//adds the item shapeCircle to the shapeCombo
		shapesCombo.addItem(shapeCircle);
		//adds the item shapeRectangle to the shapeComobo
		shapesCombo.addItem(shapeRectangle);
		//sets the default starting value of the comboBox to the first array value
		//in this case shapeCircle
		shapesCombo.setSelectedIndex(0);
		//sets the width of the box to 100, the height will be detemined by the topPanel 
		shapesCombo.setSize(100, 0);
		//adds an ActionListener to the JComboBox in order to choose between the shapes
		//sets the shape to draw with. uses Lambda
		shapesCombo.addItemListener(e -> {
			String selectedShape = (String) shapesCombo.getSelectedItem();
			if (shapeRectangle.equals(selectedShape)) {
				drawingPanel.setActiveShape("Rectangle");
			} else if (shapeCircle.equals(selectedShape)) {
				drawingPanel.setActiveShape("Circle");
			}
		});
		//adds the shapesCombo to the topPanel
		topPanel.add(shapesCombo, BorderLayout.EAST);

		//initialize a new drawingpanel object
		drawingPanel = new DrawingPanel();
		//initialize and decleares a customMouseAdapter
		CustomMouseAdapter customMouseAdapter = new CustomMouseAdapter();
		//Adds the mouselistener to our drawingpanel object
		drawingPanel.addMouseListener(customMouseAdapter);
		//Adds the mousemotionlistener to our drawingpanel object
		drawingPanel.addMouseMotionListener(customMouseAdapter);

		//Initialize a new statusbarpanel object
		statusBarPanel = new StatusBarPanel();
		//Sets the height to 25
		statusBarPanel.setSize(0, 25);

		//uses the mouselistener in the colorPalettePanel to listen for events
		colorPalettePanel.setMouseListenerForColorPanels(new MouseAdapter() {
			//When the mouse is pressed it will capture the MouseEvent e
			@Override
			public void mousePressed(MouseEvent e) {
				//the color of the pressed panel is returned and sent as an argument to the 
				//updateSelectedColor in the statusBarPanel class.
				statusBarPanel.updateSelectedColor(((ColorPanel) e.getSource()).getBackground());
				drawingPanel.setDrawColor(((ColorPanel) e.getSource()).getBackground());
			}
		});

		MenuManager menuManager = new MenuManager(this, drawingPanel);

		//Sets the layout on the c Container to borderlayout
		c.setLayout(new BorderLayout());
		//adds the Menu
		setJMenuBar(menuManager.getMenu());
		//adds the statusBarPanel to the c conatiner
		c.add(statusBarPanel, BorderLayout.SOUTH);
		//adds the topPanel object to the c container
		c.add(topPanel, BorderLayout.NORTH);
		//adds the drawingPanel object to the c contaienr
		c.add(drawingPanel, BorderLayout.CENTER);

		drawingPanel.setActiveShape("Circle");
		drawingPanel.setDrawColor(Color.BLACK);
		drawingPanel.setDrawIsActive(false);

		statusBarPanel.setOnChangeListener(new OnChangeListener<StatusBarPanel>() {
            @Override
            public void onChange(StatusBarPanel object) {
                // Update the drawing panel color based on the status bar's selected color
                drawingPanel.setDrawColor(object.getSelectedColor());
            }
        });
		
	}


	class CustomMouseAdapter extends MouseAdapter {
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if (((Component) e.getSource()).getMousePosition() != null) {
				//set the drawing as true or active since the user is drawing.
				drawingPanel.setDrawIsActive(true);
				//updates the coordinates in the statusBarPanel to where the mouse is 
				//dragged on the drawing panel
				statusBarPanel.updateCoordinates(e.getX(), e.getY());
				//updates the endpoint to the x and y coordinates as user is moving the mouse
				drawingPanel.setEndPoint(e.getX(), e.getY());
				//repaints the drawingPanel to show what the user is painting.
				drawingPanel.repaint();

			} else {
				//if there is no value reset the coordinate to default 0,0
				statusBarPanel.updateCoordinates(0, 0);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			//updates the statusBarPanels cooridnates when the mouse exits or 
			//rather, leaves the main frame to default 0,0
			statusBarPanel.updateCoordinates(0, 0);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			//updates the coordinates in statusBarPanel when the user moves the mouse
			//on the drawingPanel
			statusBarPanel.updateCoordinates(e.getX(), e.getY());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			//sets the starting point of the shape when mouse is pressed.
			drawingPanel.setStartPoint(e.getX(), e.getY());
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			//when the mouse is released sets the end point.
			drawingPanel.setEndPoint(e.getX(), e.getY());
			//change drawing is active as user has finished drawing.
			drawingPanel.setDrawIsActive(false);
			//adds the shape that has been drawn.
			drawingPanel.addShape();
			//repaints the drawingpanel to show the drawn shape.
			drawingPanel.repaint();
		}

	}

	//constructor which checks if the drawing has a name or an author.
	//checks and updates if it has either or both and updates the title accordingly.
	//if it has neither then simply updates to JPaint.
	public void updateHeader() {
		this.setTitle(getDrawingTitle());
	}

	//constructor which calls the drawing object initialized by the DrawingPanel.
	//Then sets the name and author sent as arguments as the new name and author.
	//Finally updates the header for the new names.
	public void setDrawingTitle(String name, String author) throws DrawingException{
		drawingPanel.getDrawing().setName(name);
        drawingPanel.getDrawing().setAuthor(author);
		updateHeader();
	}

	//returns the drawingTitle
	public String getDrawingTitle() {
		//gets the name of the current drawing object.
		String name = drawingPanel.getDrawing().getName();
		//gets the author of the c urrent drawing object.
		String author = drawingPanel.getDrawing().getAuthor();
		//if the drawing has no name and no author simply updates header to JPaint
		if (name == null && author == null) {
			drawingTitle = APP_NAME;
		} 
		//if the drawing has a name but no author, updates header to Jpaint and the name
		else if (name != null && author == null) {
			drawingTitle = APP_NAME + " - " + name;
		} 
		//if the drawing has no name but an author, updates header to Jpaint and untitled drawing by author
		else if (name == null && author != null) {
			drawingTitle = APP_NAME + " - [untitled drawing] by " + author;
		} 
		//If the drawing has both a name and an author the header is updated to JPaint, drawing name by author.
		else {
			drawingTitle = APP_NAME + " - "+ name + " by " + author;
		}
		return drawingTitle;
	}


}