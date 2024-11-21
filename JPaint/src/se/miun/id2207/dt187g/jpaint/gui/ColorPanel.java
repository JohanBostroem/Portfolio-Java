
/**
*class used for each color panel.
*Used to set the background to a chosen color 
*and also return the color of a panel.
*
*
*
* @author Johan Boström 2207
* @version 1.0
*/
package se.miun.id2207.dt187g.jpaint.gui;

import java.awt.Color;

import javax.swing.JPanel;

public class ColorPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	//Sets the background to the color of the object sent as an argument
	public ColorPanel(Color color) {
		this.setBackground(color);
	}
	
	//returns the background color for the object
	public Color getColor() {
		return this.getBackground(); 
	}
	
	//returns the color of the background as a hex value 
	public String getColorAsHexString() {
		var color = this.getBackground();
		String red = Integer.toHexString(color.getRed());
		String green = Integer.toHexString(color.getGreen());
		String blue = Integer.toHexString(color.getBlue());
		red = red.length() == 1 ? "0"+red : red;
		green = green.length() == 1 ? "0"+green : green;
		blue = blue.length() == 1 ? "0"+blue : blue;

        return "#" + red + green + blue;
	}
		
}
