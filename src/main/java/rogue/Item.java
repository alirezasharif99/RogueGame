package rogue;
import java.awt.Point;

/**
 * A basic Item class; basic functionality for both consumables and equipment.
 */
public class Item  {

    private int id;
    private String name;
    private String type;
    private Point point;
    private Character character;
    private String description;
    private Room room;

    //Constructors
    /**
    * class constructor.
    */
    public Item() {

    }

    /**
    * Constructor to get the class fields.
    *@param newId item's id indicating its room
    *@param newName indicates the item name
    *@param newType indicates the item type
    *@param xyLocation indicates the location of item
    */
    public Item(int newId, String newName, String newType, Point xyLocation) {
        this.id = newId;
        this.name = newName;
        this.type = newType;
        this.point = xyLocation;
    }

    // Getters and setters

    /**
    * Get id.
    *@return id
    */
    public int getId() {
        return id;
    }

    /**
    * Set id.
    *@param newId
    */
    public void setId(int newId) {
        this.id = newId;
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
        this.name = newName;

    }

    /**
    * Get type.
    *@return type
    */

    public String getType() {
        return type;

    }

    /**
    * Set type.
    *@param newType
    */
    public void setType(String newType) {
        this.type = newType;
    }

    /**
    * Get character.
    *@return character
    */
    public Character getDisplayCharacter() {
        return character;
    }

    /**
    * Set character.
    *@param newDisplayCharacter
    */

    public void setDisplayCharacter(Character newDisplayCharacter) {
        character = newDisplayCharacter;

    }

    /**
    * Get description.
    *@return description
    */
    public String getDescription() {
        return description;

    }

    /**
    * Set description.
    *@param newDescription
    */
    public void setDescription(String newDescription) {
     description = newDescription;
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
    *@param newCurrentRoom
    */
    public void setCurrentRoom(Room newCurrentRoom) {
        room = newCurrentRoom;
    }
}
