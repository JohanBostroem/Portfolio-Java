/**
* An exception for when a DrawingException is thrown.
*
*
*
*
*
* @author Johan Boström 2207
* @version 1.0
* @since 2024-10-25
*/

package se.miun.id2207.dt187g.jpaint.gui;

public class DrawingException extends Exception{
    public DrawingException() {
        super();
    }
    //sets the exception message to the given message.
    public DrawingException(String msg) {
        super(msg);
    }
}
