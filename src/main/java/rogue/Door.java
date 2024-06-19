package rogue;

import java.util.ArrayList;
/**
* Door class.
*@author Alireza Sharif
*/
public class Door {

    private String direction;
    private int connectedRoomId;
    private int position;
    private ArrayList<Room> connectedRooms = new ArrayList<Room>();

    /**
    Default constructor.
    */
    public Door() {

    }

    /**
    set door direction.
    @param dir
    */

    public void setDir(String dir) {

        direction = dir;
    }

    /**
    * get door direction.
    *@return direction
    */

    public String getDir() {
        return direction;
    }

    /**
    * set door position.
    *@param pos
    */
    public void setPos(int pos) {
        position = pos;
    }

    /**
    * get door position.
    *@return position
    */

    public int getPos() {
        return position;
    }

    /**
    * set connected door id.
    *@param id
    */

    public void setConDoor(int id) {
        connectedRoomId = id;
    }

    /**
    * get connected door id.
    *@return connectedRoomId
    */
    public int getConDoor() {
        return connectedRoomId;
    }

    /**
    * specify one of the two rooms that can be attached to a door.
    * @param r
    */
    public void connectRoom(Room r) {
        connectedRooms.add(r);
    }

    /**
    *get an Arraylist that contains both rooms connected by this door.
    * @return (ArrayList<Room>)
    */

    public ArrayList<Room> getConnectedRooms() {
        return connectedRooms;
    }

    /**
    *get the connected room by passing in the current room.
    *@param  currentRoom
    *@return (Room)
    */

    public Room getOtherRoom(Room currentRoom) {
        for (Room otherRoom : connectedRooms) {
            if (otherRoom.getId() != currentRoom.getId()) {
                return otherRoom;
            }
        }
        return null;
    }











}
