/**
* Class which represents a circle. Inherits properties from superclass Shape
*Two points are used to calculate the starting point and end point.
*This can then be used to calculate the radius, area, circumference
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
 
public class Circle extends Shape {
    //Constant variable for Pi
    private static final double Pi = 3.14159265;
    
    // Constructor for the circle using a center point and color
     public Circle(Point p, String color) {
        //Calls the superclass Shape constructor with a point and color
        super(p, color);
    }
     
    // Calls to the first Circle constructor to create a new Point with x and y and a color 
    public Circle(double x, double y, String color) {
        this(new Point(x, y), color);
    }

    //Creates a Circle directly by using two points and a color as arguments.
    public Circle(double x1, double y1, double x2, double y2, String color){
        this(new Point(x1, y1), color);
        this.addPoint(x2, y2);
    }
 
    /*Method to calculate the radius of the circle
    *Uses formula radius=squareroot of (x2-x1)^2 + (y2-y1)^2
    *calculates the distance between the starting point and the end point based on both x and y axel
    */
    public double getRadius() {
        //checks if our points list has two values for the x-y coordinates if yes then calculate the radius
        if (points.size() > 1) {
            //gets the absolute distance from the X points
            double distanceX = Math.abs(points.get(1).getX() - points.get(0).getX()); 
            //gets the absolute distance from the Y points
            double distanceY = Math.abs(points.get(1).getY() - points.get(0).getY()); 
            //returns the squareroot of the absolute distanceX ^2 added with the absolute distanceY ^2 to get the radius of the circle
            return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        }
        //if there is no second coordinate, the end point it will return value 0
        else {
            return 0;
        }
    }

    //Method to calculate the circumference of the circle with formula, 2 * pi * radius
    @Override
    public double getCircumference() {
        return (getRadius() > 0) ? 2 * Pi * getRadius() : 0;
    }
 
    // Method to calculate the area of the circle by using pi * radius^2
    @Override
    public double getArea() {
        return (getRadius() > 0) ? Pi * getRadius() * getRadius() : 0;
    }
     
    //Method for drawing or in this case printing this objects .toString
    @Override
    public void draw() {
        
    }

    //Method for drawing the Circles on the drawing.
    @Override
    public void draw(Graphics g) {
        //initialize Graphics 2D
        Graphics2D g2 = (Graphics2D) g;
        //sets the rendering in order to smooth the edges of the Circle.
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //decodes the color from hex 
        g2.setColor(Color.decode(getColor()));

        //Get the coordinates of the starting point and endpoint of the Circle.
        //change it into int.
        int x1 = (int) points.get(0).getX();
        int y1 = (int) points.get(0).getY();
        //gets the radius for the circle.
        int circleRadius = (int) getRadius();

        //Draws the Circle filled with the set color.
        g2.fillOval(x1 - circleRadius, y1 - circleRadius, 2 * circleRadius, 2 * circleRadius);

    }
 
    // Adding the second point for this object in the arrayList with value. This will determine the end point for the circle.
    // And will determine the radius.
    @Override
    public void addPoint(Point p) {
        // This adds the second point, the end point or sets it to a new value.
        if (points.size() == 1) {
            points.add(p);
        } else {
            points.set(1, p); 
        }
    }
      
    // Calls to the other addPoint method to add the x and the y value
    @Override
    public void addPoint(double x, double y) {
        addPoint(new Point(x, y));
    }
      
    // Checks if there is a second point, the endpoint added to the arraylist in true or false
    // Also checks if the second point is null in which case, also returns false.
    @Override
    public boolean hasEndpoint() {
        return points.size() > 1 && points.get(1) != null;
    }
 
    //method returning  the circles starting point, end point, the radius and the color.
    //Unless these values are 0 or null, where it returns N/A
    @Override
    public String toString() {
        return "Circle[start=" + (points.get(0) != null ? points.get(0) : "N/A") +
                " end=" + (points.get(0)!= null ? points.get(0) : "N/A") +
                " radius=" + (getRadius() > 0 ? getRadius() : "N/A") +
                " color=" + getColor() + "]";
    }

    //Method for returning a save format for the circles starting point, end point, the radius and the color.
    //Unless these values are 0 or null, where it returns N/A
    //Used to simplyfy the FileHandler class saving formating
    @Override
    public String toSaveFormat(){
        return String.format(Locale.US, "Circle %.2f %.2f %.2f %.2f %s", 
            points.get(0).getX(),
            points.get(0).getY(),
            points.get(1).getX(),
            points.get(1).getY(),
            getColor());
    }
}