/**
*Class in order to handle saving and loading of files.
*Uses Bufferedwriter and reader in order to read and write to files.
*
*Creates a new drawing when loading a file and writes all the data in the file
*to that new drawing before returning the drawing.
*
*Creates a new file and writes the data on a line each in the file.
*
*
* @author Johan Boström 2207
* @version 1.2
*/


package se.miun.id2207.dt187g.jpaint.gui;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

/*
From assignment 6
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
*/

import se.miun.id2207.dt187g.jpaint.Drawing;
import se.miun.id2207.dt187g.jpaint.geometry.Circle;
import se.miun.id2207.dt187g.jpaint.geometry.Rectangle;
import se.miun.id2207.dt187g.jpaint.geometry.Shape;

public class FileHandler {

    public void Save(Drawing drawing){
        int drawingId;
        String drawingName = drawing.getName();
        String drawingAuthor = drawing.getAuthor();

        //initiates a string url with the database to connect to.
        String url = "jdbc:sqlite:src/database/drawingdb.db";
        //Sql expression for creating a Document table.
        String sqlDocument = "CREATE TABLE IF NOT EXISTS document(\n" +
            "documentId INTEGER PRIMARY KEY AUTOINCREMENT,\n" + 
            "name TEXT NOT NULL,\n" + 
            "author TEXT NOT NULL\n" + 
            ");";
        //Sql expression for creating a Shapes table.
        String sqlShapes = "CREATE TABLE IF NOT EXISTS shapes(\n" +
            "drawingId INTEGER NOT NULL,\n" + 
            "shape TEXT NOT NULL,\n" + 
            "FOREIGN KEY (drawingId) REFERENCES document(documentId)\n" +
            ");";

        //Sql expression for inserting name and author into document table.
        String sqlInsertDocument = "INSERT INTO document(name, author) VALUES(?, ?)";
        //Sql expression for inserting drawingId and shape into shapes table.
        String sqlInsertShapes = "INSERT INTO shapes(drawingId, shape) VALUES(?, ?)";
        //SQL expression for deleting each shape belonging to a certain drawing using fk.
        String sqlUpdateDelete = "DELETE FROM shapes WHERE drawingId = ?";
        //sql expression for finding the documentId of a drawing.
        String sqlExisting = "SELECT documentId FROM document WHERE name = ? AND author = ?";

        //Tries to connect to the database url
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            //Autocommit inactivated in order to handle transactions manually.
            conn.setAutoCommit(false);
            //allows the use of foreign keys
            stmt.execute("PRAGMA foreign_keys = ON;");

            //Creates both tables for document and shapes
            stmt.execute(sqlDocument);
            stmt.execute(sqlShapes);
            
            //Initiates the prepared sql statements.
            try (PreparedStatement pstmtInsertDocument = conn.prepareStatement(sqlInsertDocument);
                PreparedStatement pstmtInsertShapes = conn.prepareStatement(sqlInsertShapes);
                PreparedStatement pstmtUpdateDelete = conn.prepareStatement(sqlUpdateDelete);
                PreparedStatement pstmtExisting = conn.prepareStatement(sqlExisting);) {
                    
                    //sets the name and author to in order to check f there is a drawing saved
                    //with the same name and author previously.
                    pstmtExisting.setString(1, drawingName);
                    pstmtExisting.setString(2, drawingAuthor);
                    //executes the sql query looking for if there is a previous document
                    //with the same name and author.
                    ResultSet rsId = pstmtExisting.executeQuery();

                    //if there already is a drawing, delete all the shapes saved to the drawing.
                    if(rsId.next()){
                        //sets the drawingId to the documentId of the drawing
                        drawingId = rsId.getInt("documentId");
                        //deletes all the shapes with the key drawingId (documentId)
                        pstmtUpdateDelete.setInt(1, drawingId);
                        pstmtUpdateDelete.executeUpdate();
                    //if there is no previous drawing 
                    //save the name and author of the drawing creating a new drawing.
                    } else {
                        pstmtInsertDocument.setString(1, drawingName);
                        pstmtInsertDocument.setString(2, drawingAuthor);
                        pstmtInsertDocument.executeUpdate();
                        //gets the resultset of the generated key
                        ResultSet rsKeyId = pstmtInsertDocument.getGeneratedKeys();
                        //updates the drawingId to the generated documentId (index 1) for the drawing.
                        drawingId = rsKeyId.getInt(1);
                        rsKeyId.close();
                    }
                    //closes the ResultSet rsId connection.
                    rsId.close();

                    //adds each shape in the drawing to the shapes list connected to the
                    //saved name and author by using keys.
                    //each shape is saved in the following format: shape x1 y1 x2 y2 color
                    for (Shape shape : drawing.getDrawingList()) {
                        //adds the drawingId as the key.
                        pstmtInsertShapes.setInt(1, drawingId);
                        //adds the shape
                        pstmtInsertShapes.setString(2, shape.toSaveFormat());
                        //executes an update for the InserShapes.
                        pstmtInsertShapes.executeUpdate();
                    }
                    //commit to the transaction and performs it.
                    conn.commit();
                    //Shows a JOptionPane informing the user the file has been saved.
			        JOptionPane.showMessageDialog(null, 
			        "Your Drawing has been saved!", 
			        "Drawing Saved!", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException e) {
                //In case of error, rolls back the transaction.
                if (conn != null) {
                    try {
                        System.err.print("Transaction has been rolled back due to unforseen error: ");
                        System.err.println(e.getMessage());
                        conn.rollback();
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } finally {
                // returns the autocommit to true meaning it is no longer "manual".
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            }
        //Catches SQLExeptions and prints the error message.
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
        }
    }


    public Drawing Load (String name, String author){
        int drawingId;
        Drawing drawing = new Drawing();

        //attempts to set the name of the drawing, throws exception 
        try{
            drawing.setName(name);
            drawing.setAuthor(author);
        }catch(DrawingException e){
            //error message for debugging.
            System.out.println("could not find name or author " + e.getMessage());
			//error message for user.
			JOptionPane.showMessageDialog(null, 
        	"No drawing with that name and author could be found. " + e.getMessage(), 
        	"Error", JOptionPane.ERROR_MESSAGE);
            e.setStackTrace(null);
            throw new RuntimeException("Could not set name or author", e);
        }

        //initiates a string url with the database to connect to.
        String url = "jdbc:sqlite:src/database/drawingdb.db";
        //Sql expression for creating a Document table.
        String sqlDocument = "CREATE TABLE IF NOT EXISTS document (\n" +
            "documentId INTEGER PRIMARY KEY AUTOINCREMENT, \n" + 
            "name TEXT NOT NULL, \n" + 
            "author TEXT NOT NULL\n" + 
            ");";
        //Sql expression for creating a Shapes table.
        String sqlShapes = "CREATE TABLE IF NOT EXISTS shapes (\n" +
            "drawingId INTEGER NOT NULL, \n" + 
            "shape TEXT NOT NULL, \n" + 
            "FOREIGN KEY (drawingId) REFERENCES document(documentId) \n" +
            ");";

        //sql expression for finding the documentId of a drawing.
        String sqlExisting = "SELECT documentId FROM document WHERE name = ? AND author = ?";
        //sql expression for finding the shapes corresponding to the documentId
        String sqlFindingShapes = "SELECT shape FROM shapes WHERE drawingId = ?";

        
        //Tries to connect to the database url
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            //Autocommit inactivated in order to handle transactions manually.
            conn.setAutoCommit(false);
            //allows the use of foreign keys
            stmt.execute("PRAGMA foreign_keys = ON;");

            //Connects to the document and shapes table.
            stmt.execute(sqlDocument);
            stmt.execute(sqlShapes);
        
            //initiates the prepared sql statements
            try (PreparedStatement pstmtExisting = conn.prepareStatement(sqlExisting);
                PreparedStatement pstmtFindingShapes = conn.prepareStatement(sqlFindingShapes);) {
                            
                    //sets the name and author to in order to check f there is a drawing saved
                    //with the same name and author previously.
                    pstmtExisting.setString(1, name);
                    pstmtExisting.setString(2, author);
                    //executes the sql query looking for if there is a previous document
                    //with the same name and author.
                    ResultSet rsId = pstmtExisting.executeQuery();
        
                    //if there already is a drawing, delete all the shapes saved to the drawing.
                    if(rsId.next()){
                        //sets the drawingId to the documentId of the drawing
                        drawingId = rsId.getInt("documentId");
                    } else {
                        JOptionPane.showMessageDialog(null, 
        	            "No drawing with that name and author could be found. ", 
        	            "Drawing not found", JOptionPane.INFORMATION_MESSAGE);
                        return null;
                    }
                    //closes the ResultSet rsId connection.
                    rsId.close();
                    //sets the drawingId which is the key as the search parameter for the sql expression.
                    pstmtFindingShapes.setInt(1, drawingId);
                    ResultSet rsShape = pstmtFindingShapes.executeQuery();
                    
                    //adds the various values for each shape and adds it to the drawing.
                    while (rsShape.next()){
                        String shape = rsShape.getString("shape");
                        shape = shape.trim();
                        String[] inData = shape.split(" ");
                        String shapeType = inData[0];
                        double x1 = Double.parseDouble(inData[1]);
                        double y1 = Double.parseDouble(inData[2]);
                        double x2 = Double.parseDouble(inData[3]);
                        double y2 = Double.parseDouble(inData[4]);
                        String drawColor = inData[5];
                        //checks the type and adds the corresponding shape to the drawing.
                        switch (shapeType) {
                            case "Rectangle":
                                drawing.addShape(new Rectangle (x1, y1, x2, y2, drawColor));
                                break;
                            case "Circle":
                                drawing.addShape(new Circle (x1, y1, x2, y2, drawColor));
                                break;
                        }
                    }
                    
                    // commit to the transaction and performs it.
                    conn.commit();
                    System.out.println("Transaction confirmed.");
        
                } catch (SQLException e) {
                //In case of error, rolls back the transaction.
                if (conn != null) {
                    try {
                        System.err.print("Transaction has been rolled back due to unforseen error: ");
                        System.err.println(e.getMessage());
                        conn.rollback();
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } finally {
                // returns the autocommit to true meaning it is no longer "manual".
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            }
        //Catches SQLExeptions and prints the error message.
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
        }
        
        //returns the loaded drawing.
        return drawing;
    }



    //code from assignment 6
    /*public void save(Drawing drawing, String fileName) {
        //initialize the filePath to the fileName.
        Path filePath = Paths.get(fileName);

        //Tries to write the drawing to the given fileName.
        try (BufferedWriter fileWriter = Files.newBufferedWriter(filePath)) {
            // Write the drawing name and author, or "[not specified]" not decided
            fileWriter.write(drawing.getName() != null ? drawing.getName() : "[not specified]");
            fileWriter.newLine();
            fileWriter.write(drawing.getAuthor() != null ? drawing.getAuthor() : "[not specified]");
            fileWriter.newLine();
        
            //writes each shape in the drawinglist to the file on a new line.
            for(Shape shape : drawing.getDrawingList()){
                fileWriter.write(shape.toString());
                fileWriter.newLine();
            }
        //Catches IO exceptions.
        } catch (IOException e) {
            //Shows an error message pane
            JOptionPane.showMessageDialog(null, 
            "Error accessing the file", 
            "IO Error", JOptionPane.ERROR_MESSAGE);
            //Prints the stack for debugging
            e.printStackTrace();
        }
    }
    
    public Drawing load (String fileName) {
        //creates a new drawing.
        Drawing drawing = new Drawing();

        //tries to read the given file. Otherwise throws exception.
        try (BufferedReader fileIn = Files.newBufferedReader(Paths.get(fileName))) {
            //sets the name of the drawing 
            String name = fileIn.readLine();
            drawing.setName(name);
            //sets the name of the author
            String author = fileIn.readLine();
            drawing.setAuthor(author);
            
            //For each shape in the saved file adds the shape to the drawing.
            String line;
            //as long as there is another line which means there is another shape. 
            //adds the shape to the drawing.
            while ((line = fileIn.readLine()) != null){
                String[] inData = line.split(" ");
                String shapeType = inData[0];
                double x1 = Double.parseDouble(inData[1]);
                double y1 = Double.parseDouble(inData[2]);
                double x2 = Double.parseDouble(inData[3]);
                double y2 = Double.parseDouble(inData[4]);
                String drawColor = inData[5];
                //checks the type and adds the corresponding shape to the drawing.
                switch (shapeType) {
                    case "Rectangle":
                        drawing.addShape(new Rectangle (x1, x2, y1, y2, drawColor));
                        break;
                    case "Circle":
                        drawing.addShape(new Circle (x1, x2, y1, y2, drawColor));
                        break;
                }
            }
        }
        //throws IO exception.
        catch(IOException e){
            //Shows an error message for when a IO error occurs
            System.err.println("IO error loading file " + e.getMessage());
            //Prints the stack for debugging
            e.printStackTrace();
        }
        //throws DrawingException
        catch(DrawingException e){
            //Shows an error message for when a DrawingException error occurs
            System.err.println("Error drawing the file" + e.getMessage());
        }
        //returns the drawing with all the shapes "loaded" onto it.
        return drawing;
    }
        */
}
