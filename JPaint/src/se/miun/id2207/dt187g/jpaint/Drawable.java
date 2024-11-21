/**
*Interface for drawable objects. 
* This ensures future classes that implements this interface create and use their own methods for drawing. 
*
* 
*To be used in the future for graphical assignments
*
* @author Johan Boström 2207
* @version 1.0
*/
package se.miun.id2207.dt187g.jpaint;

//Imports Graphics class to be used in future assignments
import java.awt.Graphics;

public interface Drawable {
    
    //Method for printing a message to the console
    void draw();
    
    //Method for drawing the shape of an object with the imported Graphics class
    void draw (Graphics g);
}
