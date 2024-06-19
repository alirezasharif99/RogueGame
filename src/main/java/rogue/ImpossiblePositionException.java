package rogue;

/**
* exception if an item is placed on a wall or .
* a door, on top of an existing element (item or player) or outside the boundaries of the room.
*@author Alireza Sharif
*/

public class ImpossiblePositionException extends Exception {
    /**
    * default constructor.
    */
    public ImpossiblePositionException() {
        super();
    }

    /**
    * second constructor.
    * @param message
    */
    public ImpossiblePositionException(String message) {
        super(message);
    }
}
