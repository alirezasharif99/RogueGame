package rogue;
import java.util.ArrayList;
//import java.util.Map;
import java.awt.Point;


/**
 * A room within the dungeon - contains monsters, treasure,
 * doors out, etc.
 *  a player is in the room its position is (2,2) by default
 */
public class Room  {


  private int width;
  private int height;
  private int id;
  private Rogue myGame;
  private ArrayList<Item> items = new ArrayList<Item>();
 // private ArrayList<String> directions = new ArrayList<String>();
 // private ArrayList<Integer> locations = new ArrayList<Integer>();
  private ArrayList<Door> doors = new ArrayList<Door>();
  private Player player;
  private boolean playerInRoom = false;

  // Default constructor
  /**
  * first constructor.
  */
  public Room() {
  }

  /**
  * get rogue class that thw room is in.
  *@param game
  */
  public void setRogue(Rogue game) {
    myGame = game;

  }

  /**
  * get room width.
  *@return width
  */
  public int getWidth() {
    return width;
  }

 /**
 * set room width.
 *@param newWidth
 */
  public void setWidth(int newWidth) {
    width = newWidth;
  }

  /**
  * get room height.
  *@return height
  */
  public int getHeight() {

    return height;
 }

 /**
 * set room height.
 *@param newHeight
 */
  public void setHeight(int newHeight) {

    height = newHeight;
 }

  /**
  * get room id.
  *@return id
  */
  public int getId() {
    return id;

 }

 /**
 * set room id.
 *@param newId
 */
  public void setId(int newId) {

    id = newId;
 }

 /**
 * get room items.
 *@return items
 */

  public ArrayList<Item> getRoomItems() {
    return items;
 }

 /**
 * set room items.
 *@param newRoomItems
 */
  public void setRoomItems(ArrayList<Item> newRoomItems) {
    items = newRoomItems;
 }

 /**
 * get room player.
 *@return player
 */
  public Player getPlayer() {
    return player;

 }

 /**
 * set room player.
 *@param newPlayer
 */
  public void setPlayer(Player newPlayer) {
    player = newPlayer;
 }

/**
 * get a door by its direction .
 *@param direction
 *@return (int) location
 */
  public int getDoor(String direction) {

    for (Door theDoor : doors) {
      if (direction.equals(theDoor.getDir())) {
        return theDoor.getPos();
      }
    }
    return -1;

 }

 /**
 * get all the doors in the room.
 *@return doors
 */
 public ArrayList<Door> getDoors() {
  return doors;
 }


/*
direction is one of NSEW
location is a number between 0 and the length of the wall
*/

/**
* set a new door.
*@param toAdd
*/
  public void setDoor(Door toAdd) {

    doors.add(toAdd);

  }
 /**
 * set playerInRoom boolean value.
 *@param playerInTheRoom
 */
  public void playerInRoom(Boolean playerInTheRoom) {
    playerInRoom = playerInTheRoom;
  }

  /**
  * get playerInRoom value.
  *@return playerInRoom
  */
  public boolean isPlayerInRoom() {
    return playerInRoom;
  }

  /**
  * add an item to items list.
  *@param toAdd
  *@throws ImpossiblePositionException
  *@throws NoSuchItemException
  */
  public void addItem(Item toAdd) throws ImpossiblePositionException, NoSuchItemException {

    // check if item is in the items list

    if (!(myGame.containsItem(toAdd))) {
      throw new NoSuchItemException();
      }

    Point itemLoc = toAdd.getXyLocation();
    int x = (int) itemLoc.getX();
    int y = (int) itemLoc.getY();
    /// check to see item is not on a same location as another item
    Boolean full = false;
    for (Item item : items) {
      if ((itemLoc.equals(item.getXyLocation()))) {
        full = true;
        break;
        }
    }
    // check to see item is not on the walls
    if (x > 0 && y > 0 && x < width - 1 && y < height - 1 && (!full)) {

      items.add(toAdd);
    } else {
        throw new ImpossiblePositionException();
    }


  }

  /**
  * give a location to the player (where there is no item).
  */
  public void setPlayerLocation() {
      if (isPlayerInRoom()) {

      Boolean found = false;
      Boolean full = false;
      int i = 0;
      int j = 0;
      for (i = 2; i < getWidth() - 2; i++) {
        for (j = 1; j < getHeight() - 2; j++) {
          Point playerLoc = new Point(i, j);
          for (Item item : items) {

          if ((playerLoc.equals(item.getXyLocation()))) {
            full = true;
            break;
            }
          }
          if (!full) {
            if (!found) {

              player.setXyLocation(playerLoc);
              found = true;
            }
          }
       }
      }
    }
  }
  /**
  * returns true if the room is complete.
  *@return (boolean)
  *@throws NotEnoughDoorsException
  */

 public boolean verifyRoom() throws NotEnoughDoorsException {
    Boolean verified = true;
    for (Door door : doors) {
      if (door.getConnectedRooms().size() != 2) {
        verified = false;
        break;
      }
    }
    if (doors.size() == 0) {
      throw new NotEnoughDoorsException();
    }
    return verified;
  }
  /**
  * this method create an 2d array of the room view and fills the walls , floors and doors.
  * @return roomArray
  */
  private String[][] makeRoomPrint() {
    String[][] roomArray = new String[getWidth()][getHeight()];
    int i = 0;
    int j = 0;

    // fill the margin walls

    for (i = 0; i < getWidth(); i++) {
      roomArray[i][0] = "NS_WALL";
      roomArray[i][getHeight() - 1] = "NS_WALL";
    }

    for (j = 1; j < getHeight() - 1; j++) {
      roomArray[0][j] = "EW_WALL";
      roomArray[getWidth() - 1][j] = "EW_WALL";
    }

    for (i = 1; i < getWidth() - 1; i++) {
      for (j = 1; j < getHeight() - 1; j++) {
        roomArray[i][j] = "FLOOR";
      }
    }

    //get doors location
    int doorLoc = 0;
    for  (Door myDoor : doors) {

      doorLoc = myDoor.getPos();
      if (myDoor.getDir().equals("N")) {
        roomArray[doorLoc][0] = "DOOR";
      } else if (myDoor.getDir().equals("S")) {
        roomArray[doorLoc][getHeight() - 1] = "DOOR";
      } else if (myDoor.getDir().equals("W")) {
        roomArray[0][doorLoc] = "DOOR";
      } else if (myDoor.getDir().equals("E")) {
        roomArray[getWidth() - 1][doorLoc] = "DOOR";
      }

    }
    return roomArray;
  }

   /**
    * Produces a string that can be printed to produce an ascii rendering of the room and all of its contents.
    * @return (String) String representation of how the room looks
    */
   public String displayRoom() {
    int i = 0;
    int j = 0;
    String[][] roomArray = new String[getWidth()][getHeight()];
    roomArray = makeRoomPrint();
    //get Items locations
    for (Item item : getRoomItems()) {
      int x;
      int y;
      x = (int) item.getXyLocation().getX();
      y = (int) item.getXyLocation().getY();
      roomArray[x][y] = item.getType().toUpperCase();
    }
    // put player on page
    if (isPlayerInRoom()) {
      Point playerLoc = player.getXyLocation();
      roomArray[(int) playerLoc.getX()][(int) playerLoc.getY()] = "PLAYER";
    }

    String roomPrint = "";

    for (j = 0; j < getHeight(); j++) {

      for (i = 0; i < getWidth(); i++) {

        roomPrint = roomPrint + roomArray[i][j];
      }
      roomPrint = roomPrint + "\n";

    }
    return roomPrint;
   }
}
