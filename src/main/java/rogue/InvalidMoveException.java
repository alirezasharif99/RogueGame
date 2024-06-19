package rogue;
/**
* exception  player is attempting an invalid move.
*@author Alireza Sharif
*/
public class InvalidMoveException extends Exception {
    /**
    * default constructor.
    */

    public InvalidMoveException() {
        super();
    }

    /**
    * second constructor.
    * @param message
    */
    public InvalidMoveException(String message) {
        super(message);
    }

}

