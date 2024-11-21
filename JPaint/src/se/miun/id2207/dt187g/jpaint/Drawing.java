/**
*Implements the Drawable interface.
*This class uses ArrayLists in order to add author, name of the drawing and the shapes used
*to make out the drawing. 
*It also calculates the total area and circumference for all the shapes as well as number of shapes
*the drawing consists out of. 
*
*Uses the .toString to each corresponding shape in order to give further detail about those shapes.
*
* @author Johan Boström 2207
* @version 1.0
*/

package se.miun.id2207.dt187g.jpaint;

import se.miun.id2207.dt187g.jpaint.geometry.*;
import se.miun.id2207.dt187g.jpaint.gui.*;

// Import the Graphics class for future use
import java.awt.Graphics;
//imports the util class to be able to use collections, generics and iterators for example.
import java.util.ArrayList;


public class Drawing implements Drawable {
    //initiates name, author and an ArrayList
    private String name;
    private String author;
    private ArrayList<Shape> drawingList;

    //Constructor initialized without any values that initialize a drawingList Array.
    public Drawing() {
        this.drawingList = new ArrayList<>();
    }
    
    //Constructor for the name and author of the object
    public Drawing(String name, String author) throws DrawingException{
        this();
        if ((name == null || name.length() == 0) || (author == null || author.length() == 0)) {
            throw new DrawingException("Name and author cannot be null");
        }
        this.name = name;
        this.author = author;
    }

    //sets the current name object to the given name.
    //throws an exception if there is a null or empty string.
    public void setName(String name) throws DrawingException {
        if (name == null) {
            throw new DrawingException("The name of the drawing cannot be null");
        } else if (name.length() == 0) {
            throw new DrawingException("The name of the drawing cannot be an empty");
        }
        this.name = name;
    }

    //sets the current author object to the given author.
    //throws an exception if there is a null or empty string.
    public void setAuthor(String author) throws DrawingException {
        if (author == null) {
            throw new DrawingException("The author of the drawing cannot be null");
        } else if (author.length() == 0) {
            throw new DrawingException("The author of the drawing cannot be an empty");
        }
        this.author = author;
    }

    //gets the value of the name
    public String getName() {
        return name;
    }

    //gets the value of the author
    public String getAuthor() {
        return author;
    }

    //Method which adds the shape to the ArrayList.
    //Checks if the shape doesn't have null value.
    public void addShape(Shape shape) {
        if (shape != null) {
            drawingList.add(shape);
        }
    }

    //Method which checks the size of the list on order to determine how many
    //shapes there are in the list.
    public int getSize() {
        return drawingList.size();
    }

    //gets the drawinList
    public ArrayList<Shape> getDrawingList(){
        return drawingList;
    }

    //uses a for loop to go through all the shapes and add them up to a total cicumference
    //because the method getArea in our rectangle and circle class always returns a value
    //this method doesn't need to check for it.
    public double getTotalCircumference() {
        double totalShapesCircumference = 0; 
        for (Shape shape : drawingList) {
            totalShapesCircumference += shape.getCircumference();
        }
        return totalShapesCircumference;
    }

    //uses a for loop to loop through all the shapes and adds them up to a total value of all the shapes
    //because the method getArea in our rectangle and circle class always returns a value
    //this method doesn't need to check for it.
    public double getTotalArea() {
        double totalShapesArea = 0; 
        for (Shape shape : drawingList) {
            totalShapesArea += shape.getArea();
        }
        return totalShapesArea;
    }


    //Method for drawing or in this case printing the author and the name of the painting
    //Also loops through the diffrent shapes the drawing consists of and using their .toStrings to
    //print more information about each individual shape.

    @Override
    public void draw() {
        //used for debugging
        for (Shape shape : drawingList) {
            System.out.println(shape.toString());
        }
    }
    
    //Draws the shapes in the drawingList
    @Override
    public void draw(Graphics g){
        //Draws all the shapes from the drawingList.
        for (Shape shape : drawingList) {
            shape.draw(g);
        }
    }

    //removes the shape on the argumented index from the drawingList
    public void removeShape(int index){
        if (index >= 0 && index < getSize()) {
            drawingList.remove(index);
        }
    }

    //
    public ArrayList<Shape> getShapes() {
        return drawingList;
    }


    //Method returning the name and author, the number of shapes, the total circumference and
    //the total area of the shapes.
    @Override
    public String toString() {
            return "Drawing[name=" + (name != null ? name : " ") +
            "; author=" + (author != null ? author : " ") +
            "; size=" + getSize() +
            "; circumference=" + getTotalCircumference() +
            "; area=" + getTotalArea() + "]";
    }
}
