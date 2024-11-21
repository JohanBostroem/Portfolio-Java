/**
*This class defines the layout of the palette panel to which
*all the colors are added including a mouse listener to each panel.
*
*This makes it possible for the user to choose between colors
*to draw with in the main frame.
*
* @author Johan Boström 2207
* @version 1.0
*/
package se.miun.id2207.dt187g.jpaint.gui;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.Color;

import javax.swing.JPanel;

public class ColorPalettePanel extends JPanel {

	//declares our colorPanels ArrayList
	private ArrayList<ColorPanel> colorPanels;
	//declares colors array to be used in ColorPalettePanel
	private final Color[] colors = {Color.RED, Color.BLACK, Color.GREEN, Color.YELLOW, 
		Color.ORANGE, Color.BLUE, Color.PINK};

	//Constructor for initializing the colorPalettePanel
	public ColorPalettePanel() {
		//sets the layout to GridLayout with one row
		this.setLayout(new GridLayout(1,0));
		//initialize colorPanels as an ArrayList
		colorPanels = new ArrayList<>();
		//for loop adding each color in the colors Array as new ColorPanel
		for (Color color : colors){
			//creates new colorPanel object with the Color color
			ColorPanel colorPanel = new ColorPanel(color);
			//adds the new colorPanel object to arraylist colorPanels
			colorPanels.add(colorPanel);
			//adds the new colorPanel object to the ColorPalettePanel
			this.add(colorPanel);
		}
	}
	
	//constructor which can take a predetermined list of ColorPanel objects
	//and adds them as panel
	public ColorPalettePanel(ArrayList<ColorPanel> colorPanels) {
		//sets colorPanels object to the colorPanels sent as arg.
		this.colorPanels = colorPanels;
		//sets the layout to GridLayout with one row
		this.setLayout(new GridLayout(1,0));
		//iterates through the panels in colorPanels and adds them to the colorPanels array
		//and the ColorPalettePanel
		for (ColorPanel panel : colorPanels){
			addColorPanel(panel);
		}
	}

	//The ColorPanel obejects sent as arguments are added to the colorPanels
	public void addColorPanel(ColorPanel cp) {
		//adds the colorPanel to colorPanels array.
		colorPanels.add(cp);
		//adds colorpanel to ColorPalettePanel object.
		this.add(cp);
	}

	//Iterates through the Colorpanel array to add a mouse listener for each panel in the colorPanels.
	//attaches a MouseListener to each panel of color.
	public void setMouseListenerForColorPanels(MouseListener listener) {
		for(ColorPanel panel : colorPanels){
			panel.addMouseListener(listener);
		}
	}

}
