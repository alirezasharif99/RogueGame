package rogue;
/**
* exception  if a room does not have at least one door.
*@author Alireza Sharif
*/
public class NotEnoughDoorsException extends Exception {
    /**
    *first constructor.
    */
    public NotEnoughDoorsException() {
        super();
    }

    /**
    * second constructor.
    * @param message
    */
    public NotEnoughDoorsException(String message) {
        super(message);
    }

}
