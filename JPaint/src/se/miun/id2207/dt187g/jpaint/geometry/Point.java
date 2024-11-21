/**
* Represents a point in a 2D coordinate system with X and Y values.
* 
*This class can be used to get and set new coordinates of a point.
*
* @author Johan Boström 2207
* @version 1.0
*/

package se.miun.id2207.dt187g.jpaint.geometry;

public class Point {
    //declaring variables to be used in our 2D point system along x-axel and y-axel
    private double x;
    private double y;

    //Constructor which initializes the point with default values (0, 0)
    public Point () {
        this.x = 0; 
        this.y = 0;
    }

    //sets current Point object to the given x and y values.
    public Point (double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    //gets the value of x-coordinates
    public double getX () {
        return x;
    }

    //gets the value of y-coordinates
    public double getY () {
        return y;
    }

    //sets the new value for the x-coordinate
    public void setX(double x) {
        this.x = x;
    }
    
    //sets the new value for the y-coordinate
    public void setY(double y) {
        this.y = y;
    }

    // string to return the values of the current x- and y-coordinates
    @Override
    public String toString() {
        return ("[" + x + ", " + y + "]");
    }
}
