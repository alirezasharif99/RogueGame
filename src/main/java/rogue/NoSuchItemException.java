package rogue;
/**
* exception  if an item does not exist on items list.
*@author Alireza Sharif
*/
public class NoSuchItemException extends Exception {
    /**
    * default constructor.
    */

    public NoSuchItemException() {
        super();
    }

    /**
    * second constructor.
    * @param message
    */
    public NoSuchItemException(String message) {
        super(message);
    }

}
