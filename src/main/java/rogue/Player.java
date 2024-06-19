package rogue;
//import java.util.ArrayList;
import java.awt.Point;
/**
 * The player character.
 */
public class Player {

    private String name;
    private Point point;
    private Room room;

    // Default constructor
    /**
    *class first constructor.
    */
    public Player() {

    }

    /**
    * class constructor to set the player name.
    *@param newName
    */
    public Player(String newName) {
        this.name = newName;
    }

    /**
    * Get name.
    *@return name
    */

    public String getName() {

        return name;
    }

    /**
    * Set name.
    *@param newName
    */
    public void setName(String newName) {
        name = newName;
    }
    /**
    * Get location.
    *@return point
    */
    public Point getXyLocation() {
        return point;

    }

    /**
    * Set location.
    *@param newXyLocation
    */
    public void setXyLocation(Point newXyLocation) {
        point = newXyLocation;
    }

    /**
    * Get item's room.
    *@return room
    */
    public Room getCurrentRoom() {
        return room;

    }

    /**
    * Set room.
    *@param newRoom
    */

    public void setCurrentRoom(Room newRoom) {
        room = newRoom;
    }
}
