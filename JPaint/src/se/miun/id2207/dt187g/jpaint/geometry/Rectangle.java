/**
* Class which represents a rectangle. Inherits properties from superclass Shape.
* Two points are used to calculate the starting point and end point.
* This can then be used to calculate the width, height, area, and circumference.
* 
 * Updated to adjust for the use of arraylist in Shape Superclass
*
* @author Johan Boström 2207
* @version 2.0
*/

package se.miun.id2207.dt187g.jpaint.geometry;
import java.awt.Color;
// Import the Graphics class for future use
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Locale; 

public class Rectangle extends Shape {
    //Constructor for the rectangle using a starting point and color
    public Rectangle(Point p, String color) {
        //Calls the superclass Shape constructor
        super(p, color);
    }
    
    //Calls to the first Rectangle constructor to create a new Point with x and y and a color 
    public Rectangle(double x, double y, String color) {
        this(new Point(x, y), color);
    }

    //Creates a Rectangle directly by using two points and a color as arguments.
    public Rectangle(double x1, double y1, double x2, double y2, String color){
        this(new Point(x1, y1), color);
        this.addPoint(x2, y2);
    }

    //Method to calulate the height, if there is no second value in the array the value 0 is returned.
    //By using Maths.abs uses the absolute distance between the two points, works with negative values.
    public double getHeight() {
        return (points.size() > 1) ? Math.abs(points.get(1).getY() - points.get(0).getY()) : 0;
    }

    //Method to calulate the Width, if there is no second value in the array the value 0 is returned.
    //By using Maths.abs uses the absolute distance between the two points, works with negative values.
    public double getWidth(){
        return (points.size() > 1) ? Math.abs(points.get(1).getX() - points.get(0).getX()) : 0;
    }
    

    //Method to calculate the circumference of the rectangle using formula, 2 * (width + height) returns 0 if not.
    @Override
    public double getCircumference() {
        return (getWidth() > 0 && getHeight() > 0) ? 2* (getWidth() + getHeight()) : 0;
    }

    //Method to calculate the area of the rectangle using width * height. Otherwise returns 0
    @Override
    public double getArea(){
        return (getWidth() > 0 && getHeight() > 0) ? getWidth() * getHeight() : 0;
    }
    
    // Method for drawing or in this case printing this object's .toString
    @Override
    public void draw() {
        System.out.println(this.toString());
    }
    
    //Method for drawing the rectangles on the drawing.
    @Override
    public void draw(Graphics g){
        //initialize Graphics 2D
        Graphics2D g2 = (Graphics2D) g;
        //sets the rendering in order to smooth the edges of the Rectangle.
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //sets the drawing color for the rectangle.
        //decodes the color from hex 
        g2.setColor(Color.decode(getColor()));
        
        // Get the coordinates of the starting point and endpoint of the rectangle
        int x1 = (int) points.get(0).getX();
        int y1 = (int) points.get(0).getY();
        int x2 = (int) points.get(1).getX();
        int y2 = (int) points.get(1).getY();
        // Calculate the width and height of the rectangle
        int width = Math.abs(x2 - x1);
        int height = Math.abs(y2 - y1);
        
        //Draws the rectangle filled with the set color.
        g2.fillRect(Math.min(x1, x2), Math.min(y1, y2), width, height);
    }

    //Adding the second point for this object in the array with value
    @Override
    public void addPoint(Point p) {
        // This adds the second point, the end point or sets it to a new value.
        if (points.size() == 1) {
            points.add(p);
        } else {
            points.set(1, p); 
        }
    }
     
    //Calls to the other addPoint method to add the x and the y value
    @Override
    public  void addPoint(double x, double y) {
        addPoint(new Point(x, y));
    }
     
    // Checks if there is a second point, the endpoint added to the arraylist in true or false
    // Also checks if the second point is null in which case, also returns false.
    @Override
    public boolean hasEndpoint() {
        return points.size() > 1 && points.get(1) != null;
    }

    //Method returning the rectangle's starting point, end point, width, height, and color
    // Unless these values are 0 or null, where it returns N/A
    @Override
    public String toString() {
        return "Rectangle[start=" + (points.get(0) != null ? points.get(0) : "N/A") +
        " end=" + (points.get(1) != null ? points.get(1) : "N/A") +
        " width=" + (getWidth() > 0 ? getWidth() : "N/A") +
        " height=" + (getHeight() > 0 ? getHeight() : "N/A") +
        " color=" + getColor() + "]";
    }

    //Method for returning a save format for the rectangle starting point, end point, width, height, and color
    //Unless these values are 0 or null, where it returns N/A
    //Used to simplyfy the FileHandler class saving formating
    @Override
    public String toSaveFormat(){
        return String.format(Locale.US, "Rectangle %.2f %.2f %.2f %.2f %s", 
        points.get(0).getX(),
        points.get(0).getY(),
        points.get(1).getX(),
        points.get(1).getY(),
        getColor());
    }
}