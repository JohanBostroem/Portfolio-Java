/**
*class for the status bar used in the JPaintFrame
*The status bar shows the coordinates of the mouse to the left and 
*the chosen color to the right. It is updated when the user changes
*these values by moving the mouse or choosing new color.
*
* @author Johan Boström 2207
* @version 1.0
*/

package se.miun.id2207.dt187g.jpaint.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class StatusBarPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	//declaring private variables used in the class
	private JLabel coordinates;
	private JPanel selectedColor;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JLabel coordinatesLabel;
	private JLabel selectedColorLabel;
	private String coordinatesText = "0, 0";
	private OnChangeListener<StatusBarPanel> onChangeListener;

	public StatusBarPanel() {
		//initializes a new JLabel with the default coordinates 0,0
		coordinates = new JLabel(coordinatesText);
		//sets the background color to light gray of the statusBarPanel object
		this.setBackground(Color.LIGHT_GRAY);
		//sets the layout of the statusBarPanel object to borderlayout
		this.setLayout(new BorderLayout());
		//creates a dark grey border with thickness 2
		Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);
        //adds the border to the StatusBarPanel object
        this.setBorder(border);
		//initializes selectedColor as a JPanel
		selectedColor = new JPanel();
		//sets the selectedColors default value as black
		selectedColor.setBackground(Color.BLACK);
		//inizilaize the leftPanel
		leftPanel = new JPanel();
		//sets the background of the leftPanel color to light gray
		leftPanel.setBackground(Color.LIGHT_GRAY);
		//sets the leftpanel object to have a Borderlayout
		leftPanel.setLayout(new BorderLayout());
		//initilize the coordinatesLabel as a new JLabel with a text
		coordinatesLabel = new JLabel("Coordinates: ");
		//adds the coordinatesLabel to the leftPanel to the left
		leftPanel.add(coordinatesLabel, BorderLayout.WEST);
		//adds the coordinates to the leftPanel in the middle
		leftPanel.add(coordinates, BorderLayout.CENTER);
		//initialize rightPanel as a JPanel
		rightPanel = new JPanel();
		//sets the background color of the rightPanel to light grey
		rightPanel.setBackground(Color.LIGHT_GRAY);
		//sets the rightPanel object to have a Borderlayout
		rightPanel.setLayout(new BorderLayout());
		//initialze the selectedColorLabel as a JLabel with a text
		selectedColorLabel = new JLabel("Selected Color: ");
		//adds the selectedColorLabel to the rightPanel in the center
		rightPanel.add(selectedColorLabel, BorderLayout.CENTER);
		//adds the selectedColor to the rightPanel i to the right 
		rightPanel.add(selectedColor, BorderLayout.EAST);
		//adds the leftPanel to our StatusBarPanel Object to the left
		this.add(leftPanel, BorderLayout.WEST);
		//adds the rightPanel to our StatusBarPanel Object to the right
		this.add(rightPanel, BorderLayout.EAST);
	}

	//Updates the coordinateslabel with the x and y coordinates sent to the method
	//this is done whenever the mouse is moved. 
	public void updateCoordinates(int x, int y) {
		//formats the coordinates to string fromat
		coordinatesText = String.format("%d, %d", x, y);
		//sets the coordinates label to the new coordinates
        coordinates.setText(coordinatesText);
	}
	
	//adds a onChangeListener that listens to changes to the statusBarPanel.
	public void setOnChangeListener(OnChangeListener<StatusBarPanel> listener) {
        this.onChangeListener = listener;
    }


	// Method to update the selectedColor when there is a new color chosen by the user.
	//the onChangeListener also told there was a change in the color.
	public void updateSelectedColor(Color color) {
		this.selectedColor.setBackground(color);

		if (onChangeListener != null) {
            onChangeListener.onChange(this);
        }
	}

	//Method for returning the selected color
	public Color getSelectedColor() {
		return selectedColor.getBackground(); 
	}


}
