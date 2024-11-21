/**
*Class for creating the drawing panel. 
*Will be used as the center panel of the app 
*where users are able to draw representing the paper.
*
*default value is white however, can be changed by send
*chosen color to the constructor.
*
* @author Johan Boström 2207
* @version 1.0
*/

package se.miun.id2207.dt187g.jpaint.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.function.Predicate;

import se.miun.id2207.dt187g.jpaint.Drawing;
import se.miun.id2207.dt187g.jpaint.geometry.*;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
	Drawing drawing;
	private Color drawColor;
	private boolean drawIsActive;
	private String activeShape;
	private int x1;
	private int x2;
	private int y1;
	private int y2;
	private Predicate<Shape> shapeFilter;

	//standard constructor to calling the other drawingpanel with a white background.
	public DrawingPanel() {
		this(Color.WHITE);
	}
	
	//This constructor sets the color of the background to
	//the color object sent to the constructor.
	public DrawingPanel(Color background) {
		//initialize drawing as a new drawing.
		this.drawing = new Drawing();
		//sets the objects background color to selected background color
		this.setBackground(background);
	}

	//returns the drawing
	public Drawing getDrawing() {
		return drawing;
	}
	//sets the drawing to the given drawing.
	public void setDrawing(Drawing drawing) {
		this.drawing = drawing;
	}
	
	//gets the chosen drawing color
	public Color getDrawColor(){
		return drawColor;
	}

	//sets the chosen drawing color
	public void setDrawColor(Color color){
		this.drawColor = color;
	}

	//gets boolean for if the drawing is being painted (right now).
	public boolean getDrawIsActive(){
		return drawIsActive;
	}

	//sets boolean for when someone is drawing the painting (right now).
	public void setDrawIsActive(boolean isActive){
		this.drawIsActive = isActive;
	}

	//returns the active shape, like rectangle or circle.
	public String getActiveShape (){
		return activeShape;
	}

	//sets chosen shape to draw with, like rectangle or circle.
	public void setActiveShape(String shape){
		this.activeShape = shape;
	}

	//sets the x and y value for the starting point of the chosen shape.
	public void setStartPoint(int x1, int y1){
		this.x1 = x1;
		this.y1 = y1;
	}

	//sets the x and y value for the end point for the chosen shape.
	public void setEndPoint(int x2, int y2){
		this.x2 = x2;
		this.y2 = y2;
	}

	//sets the shapefilter to the chosen shapes to filter for and repaints after.
	public void setShapeFilter(Predicate<Shape> shapeFilter)
	{
 		this.shapeFilter = shapeFilter;
 		this.repaint();
	}

	//adds the shape depending on if it is a circle or rectangle. Also adds it to the drawing drawingList.
	public void addShape() {
		switch (activeShape) {
		case "Rectangle":
			Point rectStart = new Point(x1 < x2 ? x1 : x2, y1 < y2 ? y1 : y2);
			Point rectEnd = new Point(x1 > x2 ? x1 : x2, y1 > y2 ? y1 : y2);
			Shape rect = new Rectangle(rectStart, getColorAsHexString(drawColor));
			rect.addPoint(rectEnd);
			drawing.addShape(rect);
			break;
		case "Circle":
			Point circleStart = new Point(x1, y1);
			Point circleEnd = new Point(x2, y1);
			Shape circle = new Circle(circleStart, getColorAsHexString(drawColor));
			circle.addPoint(circleEnd);
			drawing.addShape(circle);
			break;
		default:
			break;
		}
	}


	private void drawRect(Graphics2D g2) {
		java.awt.Shape rect = new java.awt.Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2),
				Math.abs(y1 - y2));
		g2.fill(rect);
	}


	private void drawCircle(Graphics2D g2) {
		java.awt.Shape circle = new Ellipse2D.Double(x1 - Math.abs(x1 - x2), y1 - Math.abs(x1 - x2),
				Math.abs(x1 - x2) * 2, Math.abs(x1 - x2) * 2);
		g2.fill(circle);
	}


	@Override
	public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	Graphics2D g2 = (Graphics2D) g;
		//Replaces the old drawing.draw(g) drawing method.
		//Draws the drawinglist from drawing with the applied shapefilter which allows the user
		//to apply a filter for showing only certain shapes or all shapes.
		//Uses a stream to filter the list of shapes and only draw the filtered shapes.
		drawing.getDrawingList().stream()
          .filter(shapeFilter)
          .forEach(shape -> shape.draw(g2));
		
    	if (drawIsActive) {
        	g2.setColor(drawColor);
        	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        	switch (activeShape) {
        	case "Rectangle":
            	drawRect(g2);
            	break;
        	case "Circle":
            	drawCircle(g2);
            	break;
        	default:
            	break;
        	}
    	}
	}

	//returns the color as a hex string.
	public static String getColorAsHexString(Color color) {
		
    	String red = Integer.toHexString(color.getRed());
    	String green = Integer.toHexString(color.getGreen());
    	String blue = Integer.toHexString(color.getBlue());
    	red = red.length() == 1 ? "0"+red : red;
    	green = green.length() == 1 ? "0"+green : green;
    	blue = blue.length() == 1 ? "0"+blue : blue;

    	return "#" + red + green + blue;
	}
}