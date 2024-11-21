/**
* This is an abstract superclass used for various shapes like rectangles or circles.
*
*Subclasses needs to implement the specified abstrac methods. This in order to 
*calculate circumference, area, adding points and checking if there is a end point added.
*
* implements the Drawable interface.
*
* updated to adjust for an ArrayList
*
* @author Johan Boström 2207
* @version 2.0
*/

package se.miun.id2207.dt187g.jpaint.geometry;
//imports the classes from Jpaint like the Drawable.
import se.miun.id2207.dt187g.jpaint.*;

import java.awt.Graphics;
// Import the Graphics class for future use
//import java.awt.Graphics;
//imports the util class to be able to use collections, generics and iterators for example.
import java.util.ArrayList;

public abstract class Shape implements Drawable{
    
    //used to give a color to the shape
    private String color;
    //an ArrayList in order to save the coordinates for our shapes.
    protected ArrayList<Point> points;

    //Constructor
    public Shape (Point p, String color) {
        //current objects color is initiated to the color
        this.color = color;
        //Initiates an ArrayList to store the points or rather the x- and y-coordinates.
        this.points = new ArrayList<>();
        //current objects point is set to the Point p value
        this.points.add(p);
    }

    //getter to return the color
    public String getColor() {
        return color;
    }
    
    //setter for current color
    public void setColor(String color) {
        this.color = color;
    }

    //method for adding each point to the points list
    public void addPoint(Point p) {
        this.points.add(p);
    }
            
    // A method to add a new point with x- and y-coordinates of a shape. 
    public void addPoint(double x, double y){
        this.points.add(new Point(x, y));
    }

    //method for getting the points list which stores all the coordinates for the points.
    public ArrayList<Point> getPointsList(){
        return points;
    }

    // abstract method to calculate the circumference of a shape. Must implement in subclass
    public abstract double getCircumference();

    // abstract method to calculate the area of a shape. Must implement in subclass
    public abstract double getArea();
        
    // abstract method to check if a shape has an end point. true if it does, false if it doesnt. Must implement in subclass
    public abstract boolean hasEndpoint();

    //abstract method to draw the shape . Must implement in subclass
    public abstract void draw(Graphics g);

    public abstract String toSaveFormat(); 
    }

    