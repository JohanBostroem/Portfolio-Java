/**
* An exception for when a IOException is thrown.
*
*
*
*
*
* @author Johan Boström 2207
* @version 1.0
* @since 2024-11-09
*/

package se.miun.id2207.dt187g.jpaint.gui;

public class IOException extends Exception{
    public IOException() {
        super();
    }
    //sets the exception message to the given message.
    public IOException(String msg) {
        super(msg);
    }
}
