/**
*starts the app by creating a new object of the JPaintFrame class
*
*GUI needs to be run with the SwingUtilities. 
*This ensure the program is running without issue and creates thread safety
*when using Swing
*
* @author Johan Boström 2207
* @version 1.0
*/

package se.miun.id2207.dt187g.jpaint;
//Imports the SwingUtilities class
import javax.swing.SwingUtilities;

import se.miun.id2207.dt187g.jpaint.gui.DrawingException;
//imports the JPaintFrame class from the gui folder
import se.miun.id2207.dt187g.jpaint.gui.JPaintFrame;

public class AppStart {

	public static void main(String[] args) {
		// Make sure GUI is created on the event dispatching thread
		//This is important in Swing in order to be able to execute the code properly
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Creates a new JPaintFrame and sets it to visible
				try {
                //Creates a new JPaintFrame and sets it to visible
                new JPaintFrame().setVisible(true);
                } catch (DrawingException e) {
                    //If there is a error throws a DrawingException.
                    e.printStackTrace();
                }
			}
		});
	}
}
